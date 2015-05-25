
$(document).ready(function() {
    
    var magneticPoints = [];
    var referencePoints = [];

    setInterval(run, 1000);
    
    function run() {
        if(showRadiomap) {
            getRadiomap();
        }
    }

    function getRadiomap() {
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
                showRadiomap();
            },
            error: function (xhr) {
                alert("error");
            }
        });
    }

    function showRadiomap() {
        $(".point").remove();

        var canvas = document.getElementById("radiomapArea");
        var ctx = canvas.getContext("2d");
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        if (referencePoints.length === 0)
            return;


        var img_container = document.getElementById('map');

        var ratioX = parseInt(img_container.style.width) / 1200;
        var ratioY = parseInt(img_container.style.height) / 1122;

        if(showReferenceAreas){
            for (key in referencePoints) {
                createReferencePoint(referencePoints[key].x*ratioX, referencePoints[key].y*ratioY, 100*ratioX);
            }
        }
        
        if(showMagneticPoints) {
            for (key in magneticPoints) {
                createMagneticPoint(magneticPoints[key].x*ratioX, magneticPoints[key].y*ratioY, 5*ratioX);
            }
        }
    }

    function createReferencePoint(x, y, size) {
        var canvas = document.getElementById("radiomapArea");
        var ctx = canvas.getContext("2d");


        ctx.fillStyle = "rgba(240, 10, 70, .5)";
        ctx.beginPath();
        ctx.arc(x, y, size, 100, Math.PI * 2, true);
        ctx.closePath();
        ctx.fill();
    }

    function createMagneticPoint(x, y, size) {

        var canvas = document.getElementById("radiomapArea");
        var ctx = canvas.getContext("2d");

        ctx.fillStyle = "#000000";
        ctx.beginPath();
        ctx.arc(x, y, size, 100, Math.PI * 2, true);
        ctx.closePath();
        ctx.fill();
    }


    var Point = function (x, y) {
        this.x = x;
        this.y = y;
    };
    
});