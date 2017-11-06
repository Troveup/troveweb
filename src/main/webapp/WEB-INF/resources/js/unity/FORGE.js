
(function(){
    namespace("FORGE", init, loadModel, exportModel, getData, getSetting, addMeshData, syncValuesToThreeMesh, meshHandle);

    var modelData;
    var modelFilename;

    var activeFilename;
    var storageRoot;
    var exportURL = "/savenewmodel";

    var midExport = false;
    var customizerExecutablePrefix;

    var tInit;
    var tEvents;

    var settings = {};

    var scene;
    var camera;
    var renderer;

    var mainMesh;
    var trackballControls;

    function getSetting(label) {
        return settings[label];
    }

    function updateSetting(label, value) {
        settings[label] = value;
    }

    function render() {
        requestAnimationFrame( render );
        trackballControls.update();
        renderer.render( scene, camera );
    }

    var pointLights;

    // call this to setup the unity environment and include the appropriate scripts
    function init(configHash) {
        FORGE.Log.resetClock();

        if (FORGE.Page.isMobile()) {
            // TODO: probably need some custom handling for this case
        }

        settings = $.extend({
            storageRoot: "https://storage.googleapis.com/troveup-qa-cdn/models/",
            activeFilename: "open_bracelet.json",
            canvas: document.getElementById('canvas'),
            materialSelectID: "webmenu",
            initialParamsSelectID: "initialParameters",
            defaultMaterial: "1 98", // Gold - 18k Gold
            width: 550,
            height: 400,
            isFTUE: false
        }, configHash);

        scene = new THREE.Scene();
        camera = new THREE.PerspectiveCamera( 75, settings.width / settings.height, 0.1, 1000 );
        renderer = new THREE.WebGLRenderer({
            canvas: settings.canvas,
            antialias: true,
            preserveDrawingBuffer : true, // required to support .toDataURL()
            alpha: true
        });

        var hemiLight = new THREE.HemisphereLight( 0xffffff, 0x777777, 1);
        scene.add(hemiLight);

        handleResize();
        window.addEventListener('resize', handleResize, false);

        FORGE.Control.readInitialWeights();
        loadModel();
    }

    function getData(section, id) {
        var response = modelData[section];
        if (!id) {
            return response;
        }
        return response[id];
    }
    
    function triggerButtonExport(evt) {
        if (midExport) {
            FORGE.Log.error("Blocking trigger press because currently mid export");
            return;
        }
        midExport = true;

        var $button = $(evt.currentTarget);
        var buttonID = $button.attr("id").substr("button_".length);
        var addToBag = buttonID == "addToBag";
        exportModel(addToBag);
    }

    function meshHandle() {
        return mainMesh;
    }

    function loadModel(filename, customStorageRoot) {
        FORGE.Log.clockEvent("Starting Load", "");

        settings.storageRoot = customStorageRoot || settings.storageRoot;
        settings.activeFilename = filename || settings.activeFilename;
        var modelURL = settings.storageRoot + settings.activeFilename;

        FORGE.Log.info("Fetching from "+modelURL);
        $.getJSON(modelURL, function(jsonData) {
            //// FIXME: this is a hack to grab the first listed mesh from the "main" render group
            var singleMeshList = [];
            for (var i = 0; i < jsonData.meshes.length; i++) {
                var id = jsonData.meshes[i].id.toLowerCase();
                if (id.indexOf("main") > -1) {
                    singleMeshList.push(jsonData.meshes[i]);
                    break;
                }
            }

            mainMeshID = singleMeshList[0].id;
            modelData = {};
            modelData.meshes = FORGE.Util.arrayToObj(singleMeshList);
            modelData.controls = jsonData.controls;
            modelData.operators = FORGE.Util.arrayToObj(jsonData.operators);
            modelData.metadata = jsonData.metadata;

            trackballControls = new THREE.TrackballControls( camera, renderer.domElement );
            trackballControls.noPan = true;
            trackballControls.dynamicDampingFactor = 0.1;
            trackballControls.rotateSpeed = 3.0;
            trackballControls.zoomSpeed = 0.6;

            var loader = new THREE.JSONLoader();
            var parseResult = loader.parse(singleMeshList[0]);
            itemRadius = parseResult.geometry.boundingSphere.radius;

            trackballControls.minDistance = itemRadius * 1.5;
            trackballControls.maxDistance = itemRadius * 4.5;
            camera.position.z = (trackballControls.minDistance + trackballControls.maxDistance) / 2;

            var startPos = new THREE.Vector3(-10, 10, 10).normalize().multiplyScalar(itemRadius * 2);
            camera.position.copy(startPos);
            camera.lookAt(new THREE.Vector3(0,0,0));

            mainMesh = new THREE.Mesh(parseResult.geometry);
            mainMesh.geometry.computeVertexNormals();
            scene.add(mainMesh);

            FORGE.Control.main(modelData);
            FORGE.Control.applyInitialWeights();
            FORGE.Material.init();

            $('.spinner.controls').hide();
            $('.spinner.shape').hide();
            render();
        }).done(function(){
            FORGE.Page.setupMobile();
            FORGE.Log.clockEvent("Finished Load", "Successfully loaded model data");
        }).fail(function(){
            FORGE.Log.error("Failed to load model JSON from URL");
        });
    }

    function addBagData(exportData) {
        var materialInfo = $("#"+settings.materialSelectID).val().split(" ");
        exportData.append("modelMaterial", materialInfo[0]);
        exportData.append("modelMaterialFinish", materialInfo[1]);

        var category = getData("metadata", "category");
        var size = category+"_"+$("#size").val();
        exportData.append("size", size);

        //The parent model from which this model originated, expecting this exact identifier
        var modelParentID = document.getElementById("modelParentID").textContent;
        exportData.append("parentModelId", modelParentID);

        var name = $('#view').html();
        var description = $('#view2').html();

        exportData.append("modelName", name);
        exportData.append("modelDescription", description);
        exportData.append("isPrivate", true); // FIXME: not sure what this does
    }

    function addMeshData(exportData) {
        exportData.append("modelExtension", ".obj");
        var appendedIDs = {};
        $.each(modelData.meshes, function(meshID, meshObject) {

            var sliderIDs = FORGE.Control.getSliderControlIDs();
            $.each(sliderIDs, function(i, sliderID) {
                var sliderValue = FORGE.Control.getSliderValue(sliderID);
                exportData.append("modelWeight-"+sliderID, sliderValue);
                appendedIDs[sliderID] = true;
            });

            var sliderIDs = FORGE.Control.getOperators();
            $.each(sliderIDs, function(i, operatorID) {
                if (appendedIDs[operatorID]) {
                    return;
                }
                var operatorValue = FORGE.Control.getOperator(operatorID);
                exportData.append("modelWeight-"+operatorID, operatorValue);
            })

        });
    }
    
    var epsilon = .001;
    function isApproximately(testValue, comparisonValue) {
        var diff = testValue - comparisonValue;
        return diff > 0 ? diff < epsilon : diff > -epsilon;
    }

    function syncValuesToThreeMesh(operatorParams) {
        if (!operatorParams) return; // is this a bug in corner case?

        // read mesh definition into threejs mesh
        var meshDefinition = modelData.meshes[mainMeshID];
        var meshVertices = mainMesh.geometry.vertices;

        var i;
        $.each(meshVertices, function(j, meshVertex) {
            i = j * 3;
            meshVertex.set( meshDefinition.vertices[i], meshDefinition.vertices[i+1], meshDefinition.vertices[i+2] );
        });

        $.each(operatorParams, function(operatorID, operatorValue) {
            if (isApproximately(operatorValue, 0)) {
                return;
            }

            operatorID = operatorID.replace(" ","_"); // FIXME: remove this hack once we clean the database

            var morph = modelData.operators[operatorID];
            if (!morph) {
                FORGE.Log.error("No morph with operatorID = ["+operatorID+"]");
                return;
            }

            var len = morph.parameters.modifiedCount;
            if (len < 1) {
                FORGE.Log.info("Empty morph target...");
                return;
            }

            for (var k = 0; k < len;  k++) {
                i = k * 3;
                var displacement = new THREE.Vector3(
                    morph.parameters.displacements[i] * operatorValue,
                    morph.parameters.displacements[i+1] * operatorValue,
                    morph.parameters.displacements[i+2] * operatorValue);

                var vertexIndex = morph.parameters.indices[k];
                var oldVertex = mainMesh.geometry.vertices[vertexIndex]
                mainMesh.geometry.vertices[vertexIndex] = oldVertex.add(displacement);
            }
        });

        // need to signal to threejs that mesh has been modified
        mainMesh.geometry.computeVertexNormals();
        mainMesh.geometry.verticesNeedUpdate = true;
    }

    function exportModel(addToBag) {
        FORGE.Log.clockEvent("Starting Export", "");

        var modelExportData = new FormData();
        addBagData(modelExportData);
        addMeshData(modelExportData);

        modelExportData.append("shouldAddToCart", addToBag); // add to bag directly, or save to trove for later
        modelExportData.append("imageExtension", ".jpg");

        var screenshotLabel = "image-0";
        var screenData = FORGE.Camera.requestScreenshot(screenshotLabel);
        modelExportData.append(screenshotLabel, screenData);

        jQuery.ajax({
            url: exportURL,
            data: modelExportData,
            cache: false,
            contentType: false,
            processData: false,
            headers: {
                'Access-Control-Allow-Origin': '*',
                "X-CSRF-TOKEN": $("#csrfToken").val()
            },
            type: 'POST',
            success: function(data){
                midExport = false;
            },
            failure: function(data) {
                midExport = false;
                //console.log("FORGE Error: Failed to export model");
                //console.log(data);
            }
        });
    }

    function handleResize() {
        $canvasContainer = $(settings.canvasContainerSelector);
        if (!$canvasContainer.length) {
            setDimensions(settings.width, settings.height); // just user whatever is in the seetings
            return;
        }

        var x = $canvasContainer.width();
        var y = $canvasContainer.height();
        setDimensions(x, y);
    }

    function setDimensions(x, y) {
        renderer.setSize( x, y );
        camera.aspect = x / y ;
        camera.updateProjectionMatrix();

        settings.width = x;
        settings.height = y;
    }

    function stopResize() {
        window.removeEventListener('resize', handleResize);
    }

})();

