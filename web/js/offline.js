var xClick;
var yClick;

var startX;
var startY;
var endX;
var endY;



function addEnd() {
    buildPath();
}

function addStartPoint(x, y) {
    var div = document.createElement("DIV");
    var divID = document.createAttribute("id");
    divID.value = "startPoint";
    div.setAttributeNode(divID);
    div.style.backgroundImage = "url('imgs/startPoint.png')";
    div.style.backgroundSize = "20px 20px";
    div.style.backgroundRepeat = "no-repeat";

    div.style.position = "absolute";
    div.style.top = y + "px";
    div.style.left = x + "px";
    div.style.zIndex = "1";
    div.style.height = "20px";
    div.style.width = "20px";
    document.getElementById("map").appendChild(div);
}

function addEndPoint(x, y) {
    var div = document.createElement("DIV");
    var divID = document.createAttribute("id");
    divID.value = "endPoint";
    div.setAttributeNode(divID);
    div.style.backgroundImage = "url('imgs/endPoint.png')";
    div.style.backgroundSize = "20px 20px";
    div.style.backgroundRepeat = "no-repeat";

    div.style.position = "absolute";
    div.style.top = y + "px";
    div.style.left = x + "px";
    div.style.zIndex = "1";
    div.style.height = "20px";
    div.style.width = "20px";
    document.getElementById("map").appendChild(div);
    drawLine();
}

function addPoint(x, y) {
    $("#point").remove();
    var div = document.createElement("DIV");
    var divID = document.createAttribute("id");
    divID.value = "point";
    div.setAttributeNode(divID);
    div.style.backgroundImage = "url('imgs/point.png')";
    div.style.backgroundSize = "20px 20px";
    div.style.backgroundRepeat = "no-repeat";

    div.style.position = "absolute";
    div.style.top = y + "px";
    div.style.left = x + "px";
    div.style.zIndex = "1";
    div.style.height = "20px";
    div.style.width = "20px";
    document.getElementById("map").appendChild(div);
}

function drawLine() { // draw a line connecting elements
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


$(document).ready(function () {
    
    $("#map").on("click", "#map_img", function (event) {
        var xClick = event.clientX + window.pageXOffset - 10;// - 219;//- 219
        var yClick = event.clientY + window.pageYOffset - 10;// - 65;//- 365
        
        JSInterface.getClickCoordinates(xClick, yClick);
    });

    function buildPath() {

    }
    function paintStuffOnMap() {

    }

});