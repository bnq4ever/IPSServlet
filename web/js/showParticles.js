var particles = {};
var MAC = "40:F3:08:3B:4F:AA";
var _MAC = "88:32:9B:B6:AB:56";

var colors = [
    "#0759a5",
    "#ed7b01",
    "#aab300",
    "#723289"
];

var ratioX;
var ratioY;

$(document).ready(function() {
    if (navigator.userAgent.match(/Mobi/)) {
        ratioX = 1;
        ratioY = 1;
    } else {
        var img_container = document.getElementById('map');
        ratioX = parseInt(img_container.style.width) / 685;
        ratioY = parseInt(img_container.style.height) / 1122;
    }
    setInterval(updateParticles, 100);
    setInterval(getParticles, 100);

    function updateParticles() {
        var particleCanvas = document.getElementById("particleArea");
        var ctx = particleCanvas.getContext("2d");
        ctx.clearRect(0, 0, particleCanvas.width, particleCanvas.height);
        
        if(!showParticles)
            return;
        
//        var img_container = document.getElementById('map');
//
//        var ratioX = parseInt(img_container.style.width) / 1200;
//        var ratioY = parseInt(img_container.style.height) / 1122;
        
        
        for (var id in particles) {
            var weight = particles[id].weight * 4;
            if (weight > 2) {
                weight = 2;
            }
//            ctx.fillStyle = colors[Math.floor((Math.random() * 4))];
            ctx.fillStyle = "#000000";
            ctx.beginPath();
            ctx.arc(particles[id].x*ratioX, particles[id].y*ratioY, weight*ratioX, 100, Math.PI * 2, true);
            ctx.closePath();
            ctx.fill();
        }
    }


    function getParticles() {
        
        if(!showParticles) {
            particles = {};
            return;
        }
        
        $.ajax({
            url: "Mapper",
            type: "get", //send it through get method
            data:{command: "GET_PARTICLES",
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