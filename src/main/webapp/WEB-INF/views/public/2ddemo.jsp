<%--
  Created by IntelliJ IDEA.
  User: tim
  Date: 5/10/16
  Time: 2:45 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>2D Demo</title>
</head>
<body>
  <div class="group">
      <div class="canvasWrapper">
          <canvas width="600" height="600" id="canvas"></canvas>
          <div id="overlayContainer"></div>
      </div>
      <div class="drawerWrapper">
          <div id="drawerContainer"></div>
      </div>
  </div>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/three.js/r73/three.min.js" type="text/javascript"></script>
  <script type="text/javascript" src="/resources/js/box2d_v2.3.1_min.js"></script>
  <script src="/resources/js/MTO.js" type="text/javascript"></script>
  <script type="text/javascript">
    function hashCode(str) {
        var hash = 0, i, chr, len;
        if (str.length === 0) return hash;
        for (i = 0, len = str.length; i < len; i++) {
            chr   = str.charCodeAt(i);
            hash  = ((hash << 5) - hash) + chr;
            hash |= 0; // Convert to 32bit integer
        }
        return hash;
    };

    var rawRefObjects = [
        //{
            //"type": "charm",
            //"key": "dev-debug-link",
            //"version": 1,
            //"imgURL": "https://storage.googleapis.com/troveup-dev-private/mto-images/directed-charm-link.png",
            //"width": 1.866666666666667,
            //"height": 5.833333333333333,
            //"anchors": [0, 2.3, 0, -2.3]
        //},
        //{
            //"type": "charm",
            //"key": "dev-simple-link",
            //"version": 2,
            //"imgURL": "https://storage.googleapis.com/troveup-dev-private/mto-images/charm-link.png",
            //"width": 1.866666666666667,
            //"height": 5.833333333333333,
            //"anchors": [0, 2.3, 0, -2.3]
        //},
        //{
            //"type": "charm",
            //"key": "dev-splitter-link",
            //"version": 2,
            //"imgURL": "https://storage.googleapis.com/troveup-dev-private/mto-images/charm-link.png",
            //"width": 1.866666666666667,
            //"height": 5.833333333333333,
            //"anchors": [0, 2.3, 0, -2.3, 1, 0, -1, 0 ]
        //},
        {
            "type": "charm",
            "key": "dev-disk-pendant",
            "version": 1,
            "imgURL": "https://storage.googleapis.com/troveup-dev-private/mto-images/disk-pendant.png",
            // 100
            // 142 x 183: 0.7759562841530055
            "width": 3,
            "height": 3.866197183098591,
            "anchors": [ 0, 1.9 ]
        },
            // reduction factor: 33.33333333333333
        {
            "type": "charm",
            "key": "dev-squash-blossom",
            "version": 1,
            "imgURL": "https://storage.googleapis.com/troveup-dev-private/mto-images/squash-blossom.png",
            // 280
            // 281 x 302: 0.9304635761589404
            "width": 8.4,
            "height": 9.027758007117438,
            "anchors": [ 0, 4.2 ]
        },
        {
            "type": "charm",
            "key": "dev-bar-pendant",
            "version": 1,
            "imgURL": "https://storage.googleapis.com/troveup-dev-private/mto-images/bar-pendant.png",
            // 20
            // 26 x 250: 0.104
            "width": 0.6,
            "height": 5.769230769230769,
            "anchors": [ 0, 2.6, 0, -2.6 ]
        },
        {
            "type": "charm",
            "key": "dev-horizontal-bar",
            "version": 1,
            "imgURL": "https://storage.googleapis.com/troveup-dev-private/mto-images/horizontal-bar.png",
            // 40 units tall
            // 129 x 25: 5.16
            "width": 6.192,
            "height": 1.2,
            "anchors": [ 0, 0.2, 0, -0.6, 3, 0, -3, 0 ]
        },
        {
            "key": "dev-single-anchor",
            "imgURL": "https://storage.googleapis.com/troveup-dev-private/mto-images/demo-chain.png",
            "type": "chain",
            "version": 2,
            "width": 60,
            "height": 80,
            "anchors": [ 0.75, -15.5 ]
        },
        {
            "key": "dev-double-anchor",
            "imgURL": "https://storage.googleapis.com/troveup-dev-private/mto-images/demo-chain.png",
            "type": "chain",
            "version": 2,
            "width": 60,
            "height": 80,
            "anchors": [ 2.75, -15.5, -2.5, -15.5 ]
        }
    ];

    var autoCloudReferences = rawRefObjects.map(function(raw) {
        var serialized = JSON.stringify(raw);
        var hash = hashCode(serialized);

        return {
            bucket: 'troveup-dev-private',
            type: 'local-reference',
            refType: raw.type,
            key: raw.key,
            hash: hash,
            content: serialized
        };
    });

    MTO.main({
        referenceList: autoCloudReferences,
        canvas: document.getElementById('canvas'),
        overlayContainer: document.getElementById('overlayContainer'),
        drawerContainer: document.getElementById('drawerContainer')
    });
console.log("main has executeD");
</script>
<p>Don't switch chains while charms are attached</p>
<p>Don't delete charms that are connected to other charms</p>
</body>
</html>
