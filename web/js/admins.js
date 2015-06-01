var magneticPoints = [];
var referencePoints = [];

setInterval(getAllPoints, 250);
setInterval(showPoints, 250);


function getAllPoints() {
    $.ajax({
        url: "Builder",
        type: "get", //send it through get method
        data: {command: "GET_ALL_POINTS"},
        success: function (response) {
            var tmpRP = [];
            var tmpMP = [];
            var json = $.parseJSON(response);
            var jsonArray = json['referencePoints'];
            //alert(response);
            for (var key in jsonArray) {
                tmpRP.push(new Point(jsonArray[key].x, jsonArray[key].y));
                var magnetics = jsonArray[key].magnetics;

                for (var magKey in magnetics) {
                    tmpMP.push(new Point(magnetics[magKey].x, magnetics[magKey].y));
                }

            }
            referencePoints = tmpRP;
            magneticPoints = tmpMP;
        },
        error: function (xhr) {
            alert("error");
        }
    });
}

function showPoints() {
    $(".point").remove();
    if (referencePoints.length === 0)
        return;
    for (key in referencePoints) {
        createReferencePoint(referencePoints[key].x, referencePoints[key].y);
    }

    for (key in magneticPoints) {
        createMagneticPoint(magneticPoints[key].x, magneticPoints[key].y);
    }
}


function createReferencePoint(x, y) {
    var div = document.createElement("DIV");
    var divClass = document.createAttribute("class");
    divClass.value = "point";
    div.setAttributeNode(divClass);
    div.style.backgroundImage = "url('imgs/pointArea.png')";
    div.style.backgroundSize = "200px 200px";
    div.style.backgroundRepeat = "no-repeat";

    div.style.position = "absolute";
    div.style.top = y - 100 + "px";
    div.style.left = x - 100 + "px";
    div.style.zIndex = "1";
    div.style.height = "200px";
    div.style.width = "200px";
    document.getElementById("map").appendChild(div);
}

function createMagneticPoint(x, y) {
    var div = document.createElement("DIV");
    var divClass = document.createAttribute("class");
    divClass.value = "point";
    div.setAttributeNode(divClass);
    div.style.backgroundImage = "url('imgs/magneticPoint.png')";
    div.style.backgroundSize = "10px 10px";
    div.style.backgroundRepeat = "no-repeat";

    div.style.position = "absolute";
    div.style.top = y - 5 + "px";
    div.style.left = x - 5 + "px";
    div.style.zIndex = "1";
    div.style.height = "10px";
    div.style.width = "10px";

    document.getElementById("map").appendChild(div);
}


var Point = function (x, y) {
    this.x = x;
    this.y = y;
};