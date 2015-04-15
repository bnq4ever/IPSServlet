var xClick;
var yClick;

var start;
var end;

$(document).ready(function() {
    
    $("#map").on("click", "#map_img", function (event) {
        var canvas = document.getElementById("map_img");
        var xClick = event.clientX + window.pageXOffset - 219;//- 219
        var yClick = event.clientY + window.pageYOffset - 65;//- 365
        
        JSInterface.getClickCoordinates(xClick, yClick);
    });
    
    function addStart() {
        
    }
    
    function addEnd() {
        buildPath();
    }
    
    function buildPath() {
        
    }

});