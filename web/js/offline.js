var xClick;
var yClick;

var startX;
var startY;
var endX;
var endY;

//Magentic path start point
function addStartPoint(x, y) {
    clearPathPoints();
    var div = document.createElement("DIV");
    var divID = document.createAttribute("id");
    divID.value = "startPoint";
    div.setAttributeNode(divID);
    div.style.backgroundImage = "url('imgs/startPoint.png')";
    div.style.backgroundSize = "20px 20px";
    div.style.backgroundRepeat = "no-repeat";

    div.style.position = "absolute";
    div.style.top = y - 10 + "px";
    div.style.left = x - 10 + "px";
    div.style.zIndex = "1";
    div.style.height = "20px";
    div.style.width = "20px";
    document.getElementById("map").appendChild(div);
}

//Magentic path end point
function addEndPoint(x, y) {
    var div = document.createElement("DIV");
    var divID = document.createAttribute("id");
    divID.value = "endPoint";
    div.setAttributeNode(divID);
    div.style.backgroundImage = "url('imgs/endPoint.png')";
    div.style.backgroundSize = "20px 20px";
    div.style.backgroundRepeat = "no-repeat";

    div.style.position = "absolute";
    div.style.top = y - 10 + "px";
    div.style.left = x - 10 + "px";
    div.style.zIndex = "1";
    div.style.height = "20px";
    div.style.width = "20px";
    document.getElementById("map").appendChild(div);
    drawPath();
}

// draw a line connecting elements
function drawPath() { 
    var startPoint = document.getElementById("startPoint");
    var endPoint = document.getElementById("endPoint");
    
    var width = 20;
    var height = 20;
    
    var startX = parseInt(startPoint.style.left) + width/2;
    var startY = parseInt(startPoint.style.top) + height/2;
    
    var endX = parseInt(endPoint.style.left) + width/2;
    var endY = parseInt(endPoint.style.top) + height/2;
    
    var thickness = 1;
    // distance
    var length = Math.sqrt(((endX - startX) * (endX - startX)) + ((endY - startY) * (endY - startY)));
    // center
    var cx = ((startX + endX) / 2) - (length / 2);
    var cy = ((startY + endY) / 2) - (thickness / 2);
    // angle
    var angle = Math.atan2((startY - endY), (startX - endX)) * (180 / Math.PI);
    // make hr
    //var htmlLine = "<div style='padding:0px; margin:0px; height:" + thickness + "px; background-color:black; line-height:1px; position:absolute; left:" + cx + "px; top:" + cy + "px; width:" + length + "px; -moz-transform:rotate(" + angle + "deg); -webkit-transform:rotate(" + angle + "deg); -o-transform:rotate(" + angle + "deg); -ms-transform:rotate(" + angle + "deg); transform:rotate(" + angle + "deg);' />";
    var div = document.createElement("DIV");
    
    var divID = document.createAttribute("id");
    divID.value = "path";
    div.setAttributeNode(divID);
    
    div.style.backgroundImage = "url('imgs/line.png')";
    div.style.backgroundRepeat = "repeat-x";
    div.style.padding = "0px";
    div.style.margin = "0px";
    div.style.height = thickness + "px";
    div.style.width = length + "px";
    div.style.lineHeight = "1px";
    div.style.position = "absolute";
    div.style.left = cx + "px";
    div.style.top = cy + "px";
    div.style.zIndex = "0";
    
    div.style.webkitTransform = 'rotate('+angle+'deg)'; 
    div.style.mozTransform    = 'rotate('+angle+'deg)'; 
    div.style.msTransform     = 'rotate('+angle+'deg)'; 
    div.style.oTransform      = 'rotate('+angle+'deg)'; 
    div.style.transform       = 'rotate('+angle+'deg)'; 
    
    document.getElementById("map").appendChild(div);
}

function clearPathPoints() {
    $("#startPoint").remove();
    $("#endPoint").remove();
    $("#path").remove();
}


//Bluetooth & Wifi point
function addPoint(x, y) {
    $("#point").remove();
    var div = document.createElement("DIV");
    var divID = document.createAttribute("id");
    divID.value = "point";
    div.setAttributeNode(divID);
    div.style.backgroundImage = "url('imgs/pointArea.png')";
    div.style.backgroundSize = "150px 150px";
    div.style.backgroundRepeat = "no-repeat";

    div.style.position = "absolute";
    div.style.top = y -75 + "px";
    div.style.left = x -75 + "px";
    div.style.zIndex = "1";
    div.style.height = "150px";
    div.style.width = "150px";
    document.getElementById("map").appendChild(div);
}
function addMagneticPoints(startX, startY, endX, endY, nbrOfSamples) {
    for (var i = 0; i < nbrOfSamples; i++) {        
        var div = document.createElement("DIV");
//        var divClass = document.createAttribute("class");
//        divClass.value = "magneticPoint";
//        div.setAttributeNode(divClass);
        div.style.backgroundImage = "url('imgs/magneticPoint.png')";
        div.style.backgroundSize = "10px 10px";
        div.style.backgroundRepeat = "no-repeat";

        div.style.position = "absolute";
        div.style.top = (startY + ((endY - startY)/nbrOfSamples) * i) - 5 + "px";
        div.style.left = (startX + ((endX - startX)/nbrOfSamples) * i) - 5 + "px";
        div.style.zIndex = "1";
        div.style.height = "10px";
        div.style.width = "10px";
        
        document.getElementById("map").appendChild(div);
    }
}

function clearPoint() {
    $("#point").remove();
}

$(document).ready(function () {
    $("#map").on("click", "#map_img", function (event) {
        
        var xClick = event.clientX + window.pageXOffset;//10;// - 219;//- 219
        var yClick = event.clientY + window.pageYOffset;//10;// - 65;//- 365
        
        //Androidanrop med klickkoordinater
        JSInterface.getClickCoordinates(xClick, yClick);
        
    });
    
});