var Flat =
/******/ (function(modules) { // webpackBootstrap
/******/ 	// The module cache
/******/ 	var installedModules = {};

/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {

/******/ 		// Check if module is in cache
/******/ 		if(installedModules[moduleId])
/******/ 			return installedModules[moduleId].exports;

/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = installedModules[moduleId] = {
/******/ 			exports: {},
/******/ 			id: moduleId,
/******/ 			loaded: false
/******/ 		};

/******/ 		// Execute the module function
/******/ 		modules[moduleId].call(module.exports, module, module.exports, __webpack_require__);

/******/ 		// Flag the module as loaded
/******/ 		module.loaded = true;

/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}


/******/ 	// expose the modules object (__webpack_modules__)
/******/ 	__webpack_require__.m = modules;

/******/ 	// expose the module cache
/******/ 	__webpack_require__.c = installedModules;

/******/ 	// __webpack_public_path__
/******/ 	__webpack_require__.p = "";

/******/ 	// Load entry module and return exports
/******/ 	return __webpack_require__(0);
/******/ })
/************************************************************************/
/******/ ([
/* 0 */
/***/ function(module, exports, __webpack_require__) {

	
	var Component = __webpack_require__(1);
	var FlatModel = __webpack_require__(2);

	function testCanvas() {
	    init();
	}

	var linkWidth = 112 / 3;
	var linkHeight = 350 / 3;

	var necklaceSpec = {
	    imgURL: "/resources/img/demo-chain.png",
	    position: new THREE.Vector2(300, 50), // in untransformed grid, need to figure out less hacky way for this
	    width: 600,
	    height: 800,
	    lowerAnchor: null //new THREE.Vector2(0, 100)
	};

	var componentSpecs = [
	    {
	        imgURL: "/resources/img/charm-link.png",
	        position: new THREE.Vector2(-50, 120),
	        width: 5,
	        height: 5,
	        lowerAnchor: new THREE.Vector2(0, 0)
	    },
	    {
	        imgURL: "/resources/img/charm-link.png",
	        position: new THREE.Vector2(60, 120),
	        width: 5,
	        height: 5,
	        lowerAnchor: new THREE.Vector2(0, 0)
	    },
	    {
	        imgURL: "/resources/img/charm-link.png",
	        position: new THREE.Vector2(-50, 0),
	        width: linkWidth,
	        height: linkHeight,
	        upperAnchor: new THREE.Vector2(0, 46),
	        lowerAnchor: new THREE.Vector2(0, -46)
	    },
	    {
	        imgURL: "/resources/img/charm-link.png",
	        position: new THREE.Vector2(0, 0),
	        width: linkWidth,
	        height: linkHeight,
	        upperAnchor: new THREE.Vector2(0, 46),
	        lowerAnchor: new THREE.Vector2(0, -46)
	    },
	    {
	        imgURL: "/resources/img/charm-link.png",
	        position: new THREE.Vector2(50, 0),
	        width: linkWidth,
	        height: linkHeight,
	        upperAnchor: new THREE.Vector2(0, 46),
	        lowerAnchor: new THREE.Vector2(0, -46)
	    }
	];

	var model;
	var canvas = document.getElementById('canvas');
	var ctx = canvas.getContext('2d');
	var canvasWidth = canvas.width;
	var canvasHeight = canvas.height;

	function init(){
	    model = new FlatModel(necklaceSpec, componentSpecs);
	    model.load().then(function(blah){
	        console.log("finished loading model");
	        draw();
	    });
	}

	// assume y increasing going up, origin at center of 300x300 canvas
	function getMousePos(canvas, evt) {
	    var rect = canvas.getBoundingClientRect();
	    return {
	        x: evt.clientX - rect.left - (canvasWidth / 2),
	        y: (canvasHeight / 2 ) - (evt.clientY - rect.top)
	    };
	}

	var oldMousePos;
	var selectedComponent = null;

	canvas.addEventListener('mousedown', function(evt) {
	    var mousePos = getMousePos(canvas, evt);

	    var closestDist = Infinity;
	    model.eachComponent(function(component) {
	        component.selected = false;

	        var result = component.hitCheck(mousePos);
	        if (result.hit) {
	            if (!selectedComponent || result.dist < closestDist) {
	                selectedComponent = component;
	                closestDist = result.dist;
	            }
	        }
	    });

	    if (selectedComponent) {
	        // TODO: create list of viable (unoccupied and opposite polarity) anchors

	        var upperLink = selectedComponent.anchors.upper.attachedComponent;
	        if (upperLink) {
	            upperLink.anchors.lower.attachedComponent = null;
	            selectedComponent.anchors.upper.attachedComponent = null;
	        }

	        selectedComponent.setStatus('selected');
	    }
	});

	canvas.addEventListener('mouseup', function(evt) {
	    if (selectedComponent) {
	        selectedComponent.setStatus('normal');

	        var anchorResult = model.anchorScan(selectedComponent)
	        if (anchorResult.snap) {
	            selectedComponent.anchors.upper.attachedComponent = anchorResult.freshAttachment;
	            anchorResult.lowerAnchor.attachedComponent = selectedComponent;

	            selectedComponent.translateChain(anchorResult.params.x, anchorResult.params.y)
	        }
	    }

	    selectedComponent = null;

	});

	canvas.addEventListener('mousemove', function(evt) {
	    var mousePos = getMousePos(canvas, evt);
	    if (selectedComponent) {
	        var dx = mousePos.x - oldMousePos.x;
	        var dy = mousePos.y - oldMousePos.y;

	        selectedComponent.translateChain(dx, dy);

	        // highlight snappable anchors during drag
	        var anchorResult = model.anchorScan(selectedComponent)
	        if (anchorResult.snap) {
	            anchorResult.lowerAnchor.hovered = true;
	            anchorResult.upperAnchor.hovered = true;
	        }
	    }
	    oldMousePos = mousePos;
	}, false)

	var anchorOverlap;
	function draw() {
	    ctx.clearRect(0,0,canvasWidth,canvasHeight);

	    model.necklace.renderToContext(ctx);
	    model.necklace.drawAnchors(ctx);

	    ctx.save();
	    ctx.scale(1, -1);
	    ctx.translate(canvasWidth / 2, -canvasHeight / 2);

	    model.eachComponent(function(component) {
	        // component.syncTransition();
	        component.renderToContext(ctx);
	    });

	    model.eachComponent(function(component) {
	        component.drawAnchors(ctx);
	    });
	    ctx.restore();

	    window.requestAnimationFrame(draw);
	}

	testCanvas();


/***/ },
/* 1 */
/***/ function(module, exports) {

	
	var charmColors = {
	    normal: '#F26868',
	    selected: 'red',
	    hanging: 'yellow'
	}

	var anchorColors = {
	    normal: 'black',
	    locked: 'green',
	    overlap: 'yellow'
	};

	function drawCircle(ctx, x, y, radius, style) {
	    ctx.fillStyle = style;
	    ctx.beginPath();
	    ctx.arc(x, y, radius, 0, 2 * Math.PI, true);
	    ctx.fill();
	}

	function drawCenteredImage(ctx, x, y, width, height, img) {
	    var hx = width / 2;
	    var hy = height / 2;
	    ctx.drawImage(img, x - hx, y - hy, width, height);
	}

	function drawCenteredRectangle(ctx, x, y, width, height, style) {
	    ctx.save();
	    ctx.fillStyle = style;

	    var hx = width / 2;
	    var hy = height / 2;
	    ctx.fillRect(x - hx, y - hy, width, height);

	    ctx.restore();
	}


	function Component(spec) {
	    this.imgURL = spec.imgURL;
	    this.position = spec.position;
	    this.width = spec.width;
	    this.height = spec.height;
	    this.anchors = {};
	    if (spec.upperAnchor) {
	        this.anchors.upper = {
	            debugStyle: anchorColors['normal'],
	            offset: spec.upperAnchor,
	            attachedComponent: null
	        }
	    }

	    if (spec.lowerAnchor) {
	        this.anchors.lower = {
	            debugStyle: anchorColors['normal'],
	            offset: spec.lowerAnchor,
	            attachedComponent: null
	        }
	    }

	    this.setStatus('normal');
	}

	Component.prototype.setStatus = function(newStatus) {
	    this.style = charmColors[newStatus];
	}

	Component.prototype.translateChain = function(x, y) {
	    this.position.x += x;
	    this.position.y += y;

	    var hangingPiece = this.anchors.lower.attachedComponent;
	    if (hangingPiece) {
	        hangingPiece.translateChain(x, y);
	    }
	}

	Component.prototype.load = function() {
	    var that = this;
	    return new Promise(function(resolve, reject){
	        var img = new Image()
	        img.onload = function(){
	            that.rawWidth = img.naturalWidth; // || img.width;
	            that.rawHeight = img.naturalHeight; //  || img.height;
	            
	            resolve(that.imgURL)
	        }
	        img.onerror = function(){
	            reject(that.imgURL)
	        }

	        img.src = that.imgURL
	        that.img = img;
	    });
	}

	Component.prototype.hitCheck = function(checkPos) {
	    var hx = this.width / 2;
	    var hy = this.height / 2;
	    var minX = this.position.x - hx;
	    var maxX = this.position.x + hx;
	    var minY = this.position.y - hy;
	    var maxY = this.position.y + hy

	    var miss = { hit: false };
	    if (checkPos.x < minX) return miss;
	    if (checkPos.x > maxX) return miss;
	    if (checkPos.y < minY) return miss;
	    if (checkPos.y > maxY) return miss;

	    var center = this.position.clone();
	    var displacement = new THREE.Vector2(checkPos.x, checkPos.y).sub(center);

	    return {
	        hit: true,
	        dist: displacement.length()
	    };
	}

	Component.prototype.drawAnchors = function(ctx){
	    var anchorRenderRadius = 3;
	    var posX = this.position.x;
	    var posY = this.position.y;

	    var that = this;
	    Object.keys(this.anchors).map(function(anchorKey) {
	        ctx.save();

	        var anchor = that.anchors[anchorKey];
	        var x = posX + anchor.offset.x;
	        var y = posY + anchor.offset.y;

	        var style = anchor.hovered ? anchorColors['overlap'] : anchor.debugStyle;
	        anchor.hovered = false;
	        
	        // var anchorStyle = anchor.attachedComponent === null ? 'black' : 'green';
	        drawCircle(ctx, x, y, anchorRenderRadius, style);

	        ctx.restore();
	    });
	}

	Component.prototype.renderToContext = function(ctx){
	    //drawCenteredRectangle(ctx, this.position.x, this.position.y, this.width+2, this.height+2, 'black');
	    //drawCenteredRectangle(ctx, this.position.x, this.position.y, this.width, this.height, this.style);

	    drawCenteredImage(ctx, this.position.x, this.position.y, this.width, this.height, this.img);
	}

	Component.prototype.moveTo = function(x, y) {
	    this.position.set(x, y);
	}

	Component.prototype.transitionTo = function(x, y, duration) {
	    this.transitionActive = true;

	    this.transitionStart = Date.now();
	    this.duration = duration;

	    this.startPos = this.position.clone();
	    this.endPos = new THREE.Vector2(x, y);
	}

	Component.prototype.getGlobalAnchorPos = function(index) {
	    return {
	        x: this.anchors[index].position.x + this.position.x,
	        y: this.anchors[index].position.y + this.position.y
	    }
	};

	Component.prototype.compareAnchors = function(comparison) {
	    var lenA = this.anchors.length;
	    var lenB = comparison.anchors.length;
	    var pointA = new THREE.Vector2();
	    var pointB = new THREE.Vector2();
	    var cutoffRadius = 10;
	    for (var i = 0; i < lenA; i++) {
	        for (var j = i; j < lenB; j++) {
	            pointA.copy(this.anchors[i].position).add(this.position);
	            pointB.copy(comparison.anchors[j].position).add(comparison.position);
	            var dist = pointA.distanceTo(pointB);
	            if (dist < cutoffRadius) {
	                return {
	                    hit: true,
	                    dist: dist,
	                    indexA: i,
	                    indexB: j
	                }
	            }
	        }
	    }

	    return {
	        hit: false
	    };
	}

	Component.prototype.syncTransition = function() {
	    if (!this.transitionActive) {
	        return;
	    }

	    var progress = (Date.now() - this.transitionStart) / this.duration;
	    if (progress >= 1) {
	        this.transitionActive = false;
	        return;
	    }

	    this.position.lerpVectors(this.startPos, this.endPos, progress);
	}

	module.exports = Component;



/***/ },
/* 2 */
/***/ function(module, exports, __webpack_require__) {

	
	var Component = __webpack_require__(1);

	function FlatModel(necklaceSpec, componentSpecList) {
	    this.necklace = new Component(necklaceSpec);
	    this.componentList = componentSpecList.map(function(spec) {
	        return new Component(spec);
	    });
	}

	FlatModel.prototype.load = function() {
	    var loadingPromises = this.componentList.map(function(component){
	        return component.load();
	    });
	    loadingPromises.push( this.necklace.load() );

	    return Promise.all(loadingPromises);
	};

	FlatModel.prototype.anchorScan = function(queryComponent) {
	    var cutoffRadius = 30;
	    var result = {
	        snap: false
	    }

	    // assume top anchor must be open, so seek all open bottom anchors
	    this.eachComponent(function(targetComponent) {
	        if (targetComponent === queryComponent) {
	            return;
	        }

	        var targetSite = targetComponent.anchors.lower;
	        if (targetSite.attachedComponent) {
	            return;
	        }

	        var tx = targetComponent.position.x + targetSite.offset.x
	        var ty = targetComponent.position.y + targetSite.offset.y

	        var sourceSite = queryComponent.anchors.upper;
	        var qx = queryComponent.position.x + sourceSite.offset.x
	        var qy = queryComponent.position.y + sourceSite.offset.y

	        var dx = tx - qx;
	        var dy = ty - qy;

	        var dist = Math.sqrt(dx * dx + dy * dy);
	        if (dist < cutoffRadius) {
	            result.snap = true;
	            result.upperAnchor = sourceSite;
	            result.lowerAnchor = targetSite;
	            result.freshAttachment = targetComponent;
	            result.params = {
	                x: dx,
	                y: dy
	            }
	        }
	    });
	    return result;
	}

	FlatModel.prototype.checkAnchors = function() {
	    var len = this.componentList.length;
	    var overlap = null;
	    for (var i = 0; i < len; i++) {
	        for (var j = i; j < len; j++) {
	            if (i == j) continue;

	            var objA = this.componentList[i];
	            var objB = this.componentList[j];
	            
	            var results = objA.compareAnchors(objB);
	            if (results.hit) {
	                if (!overlap || results.dist < overlap.dist) {
	                    overlap = {
	                        dist: results.dist,
	                        first: objA,
	                        second: objB,
	                        firstIndex: results.indexA,
	                        secondIndex: results.indexB
	                    };
	                }
	            }
	        }
	    }
	    return overlap;
	}

	FlatModel.prototype.eachComponent = function(callback) {
	    return this.componentList.map(callback);
	}

	module.exports = FlatModel;


/***/ }
/******/ ]);