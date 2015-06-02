var candidates = {};

var MAC = "40:F3:08:3B:4F:AA";
var _MAC = "88:32:9B:B6:AB:56";

$(document).ready(function () {

    setInterval(getCandidates, 100);
    setInterval(updateCandidates, 100);

    function updateCandidates() {
        var candidateCanvas = document.getElementById("candidateArea");
        var ctx = candidateCanvas.getContext("2d");
        ctx.clearRect(0, 0, candidateCanvas.width, candidateCanvas.height);
        if(!showCandidates)
            return;
        
        var img_container = document.getElementById('map');

        var ratioX = parseInt(img_container.style.width) / 685;
        var ratioY = parseInt(img_container.style.height) / 1122;
        
        for (var id in candidates) {
            ctx.fillStyle = "#ffffff";
            ctx.beginPath();
            ctx.arc(candidates[id].x*ratioX, candidates[id].y*ratioY, 5*ratioX, 100, Math.PI * 2, true);
            ctx.closePath();
            ctx.fill();
        }

    }


    function getCandidates() {
        
        if(!showCandidates)
            return;
        
        $.ajax({
            url: "Mapper",
            type: "get", //send it through get method
            data: {command: "GET_BEST_CANDIDATES",
                id: MAC},
            success: function (response) {
                var tmp = {};
                var json = $.parseJSON(response);
                var jsonArray = json['candidates'];
                for (var key in jsonArray) {
                    var x = jsonArray[key].x;
                    var y = jsonArray[key].y;

                    tmp[key] = new Candidate(x, y);
                }
                candidates = tmp;
            },
            error: function (xhr) {
                //alert("error");
            }
        });
                
        updateCandidates();
    }

    var Candidate = function (x, y) {
        this.x = x;
        this.y = y;
    };
});