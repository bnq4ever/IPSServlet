var particles = {};
var MAC = "88:32:9B:B6:AB:56";

$(document).ready(function() {

    //window.onload = init;
    //window.onresize = resize;
    
//    setInterval(updateParticles, 500);
    setInterval(getParticles, 500);

    function updateParticles() {
        $("#particleArea").empty();
        
        for (var id in particles) {            
            generateDiv(particles[id].x, particles[id].y, particles[id].weight);
        }
        
    }

    function generateDiv(x, y, weight) {
        var size = weight;
        var div = document.createElement("DIV");
        div.style.backgroundImage = "url('imgs/particle.png')";
        div.style.backgroundSize = size + "px";
        div.style.backgroundRepeat = "no-repeat";
        var divID = document.createAttribute("id");
        divID.value = "particle";
        div.setAttributeNode(divID);

        var divClass = document.createAttribute("class");
        divClass.value = "particle";
        div.setAttributeNode(divClass);
        div.style.position = "absolute";
        div.style.left = x - size/2 + "px";
        div.style.top = y - size/2 + "px";
        div.style.zIndex = "1";
        div.style.height = size + "px";
        div.style.width = size + "px";
        document.getElementById("particleArea").appendChild(div);
    }

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
                    updateParticles();
                }
                particles = tmp;
            },
            error: function(xhr) {
              //alert("error");
            }
        });
    }
    
    var Particle = function(x, y, weight, direction, speed) {
        this.x = x;
        this.y = y;
        this.weight = weight;
        this.direction = direction;
        this.speed = speed;
    };
});