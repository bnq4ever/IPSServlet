var particles = {};
var MAC = "40:F3:08:3B:4F:AA";
var _MAC = "88:32:9B:B6:AB:56";


$(document).ready(function() {

    //window.onload = init;
    //window.onresize = resize;
    
    setInterval(updateParticles, 100);
    setInterval(getParticles, 100);

    function updateParticles() {
        //$("#particleArea").empty();
        var canvas = $('canvas')[0];
        var ctx = canvas.getContext("2d");
        ctx.clearRect(0, 0, canvas.width, canvas.height);
        
        for (var id in particles) {
//            var weight = particles[id].weight*4;
//            if(weight > 4) {
//                weight = 4;
//            }
            ctx.fillStyle = "#000000";
            ctx.beginPath();
            ctx.arc(particles[id].x, particles[id].y, 1, 10, Math.PI*2, true); 
            ctx.closePath();
            ctx.fill();
            //generateDiv(particles[id].x, particles[id].y, particles[id].weight);
        }
        
    }

//   function generateDiv(x, y, weight) {
//        var size = 2;
//        var div = document.createElement("DIV");
//        div.style.backgroundImage = "url('imgs/particle.gif')";
//        div.style.backgroundSize = size + "px";
//        div.style.backgroundRepeat = "no-repeat";
//        var divID = document.createAttribute("id");
//        divID.value = "particle";
//        div.setAttributeNode(divID);
//
//        var divClass = document.createAttribute("class");
//        divClass.value = "particle";
//        div.setAttributeNode(divClass);
//        div.style.position = "absolute";
//        div.style.left = x - size/2 + "px";
//        div.style.top = y - size/2 + "px";
//        div.style.zIndex = "1";
//        div.style.height = size + "px";
//        div.style.width = size + "px";
//        document.getElementById("particleArea").appendChild(div);
//    }

    function getParticles() {
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