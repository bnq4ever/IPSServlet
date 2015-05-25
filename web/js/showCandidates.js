var particles = {};

var _MAC = "40:F3:08:3B:4F:AA";
var MAC = "88:32:9B:B6:AB:56";

$(document).ready(function() {

    //window.onload = init;
    //window.onresize = resize;
    
    setInterval(updateCandidates, 200);
    setInterval(getCandidates, 50);

    function updateCandidates() {
        //$("#particleArea").empty();
        var canvas = $('canvas')[0];
        var ctx = canvas.getContext("2d");
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        
        for (var id in particles) {
            var weight = particles[id].weight*4;
            if(weight > 2) {
                weight = 2;
            }
            ctx.fillStyle = colors[Math.floor((Math.random() * 4))];
            ctx.beginPath();
            ctx.arc(particles[id].x, particles[id].y, weight, 100, Math.PI*2, true); 
            ctx.closePath();
            ctx.fill();
        }
        
    }


    function getCandidates() {
        $.ajax({
            url: "Mapper",
            type: "get", //send it through get method
            data:{command: "GET_CANDIDATES",
                  id: MAC},
            success: function(response) {
                var tmp = {};
                var json = $.parseJSON(response);
                var jsonArray = json['particles'];
                for (var key in jsonArray) {
                    var x = jsonArray[key].x;
                    var y = jsonArray[key].y;
                    var weight = jsonArray[key].weight;
                    var direction = jsonArray[key].direction;
                    var speed = jsonArray[key].speed;
//                    alert(x + " " + y + " " + weight + " " + direction + " " + speed);

                    tmp[key] = new Particle(x, y, weight, direction, speed);
                }
                particles = tmp;
            },
            error: function(xhr) {
              //alert("error");
            }
        });
        //updateParticles();
    }
    
    var Particle = function(x, y, weight, direction, speed) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.direction = direction;
        this.speed = speed;
    };
});