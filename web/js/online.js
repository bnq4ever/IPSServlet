var connectedDevices = {};
var Command = new Command();

$(document).ready(function() {

    window.onload = init;
//    window.onresize = resize;
    

    setInterval(getConnectedDevices, 100);
    setInterval(updateMapDevices, 100);

    $('#mappingArea').on('click', '.device', function () {
        var device = connectedDevices[this.id];
        alert(device.name + "\n" + 
                "x: " + device.x + " y: " + device.y + "\n" +
                "id: " + device.id);
    });

    function init() {
        setMap();
        //initMatrix();
    }

    function updateMapDevices() {
        removeDisconnected();

        for (var id in connectedDevices) {            
            var div = document.getElementById(id);
            if (div === null) {
                generateDiv(id, connectedDevices[id].name, connectedDevices[id].x, connectedDevices[id].y);
            } else {
                moveTo(id);
            }
        }
    }

    function removeDisconnected() {
        $('#mappingArea').children('.device').each(function() {
            if (typeof connectedDevices[this.id] === 'undefined') {
                $(this).fadeOut("fast", function() {
                    $(this).remove();
                });
            }
        });
    }

    function moveTo(id) {
        var div = document.getElementById(id);
        var x = connectedDevices[id].x;
        var y = connectedDevices[id].y;
        div.style.left = x - 10 + "px";
        div.style.top = y - 10 + "px";
    }

    function generateDiv(deviceID, name, x, y) {
        var div = document.createElement("DIV");
        div.style.backgroundImage = "url('imgs/circle.png')";
        div.style.backgroundSize = "25px 25px";
        div.style.backgroundRepeat = "no-repeat";
        var divID = document.createAttribute("id");
        divID.value = deviceID;
        div.setAttributeNode(divID);

        var divClass = document.createAttribute("class");
        divClass.value = "device";
        div.setAttributeNode(divClass);
        div.style.position = "absolute";
        div.style.top = Math.floor(y) + "px";
        div.style.left = Math.floor(x) + "px";
        div.style.zIndex = "1";
        div.style.height = "25px";
        div.style.width = "25px";
        //div.style.backgroundcolor = getRandomColor();
        $(div).hide();
        document.getElementById("mappingArea").appendChild(div);
        $(div).clearQueue().fadeIn("fast");
    }
    
    function getRandomColor() {
        var letters = '0123456789ABCDEF'.split('');
        var color = "#";
        for (var i = 0; i < 6; i++) {
            color += letters[Math.floor(Math.random()*16)];
        }
        return color;
    }

    function setMap() {
        var img = document.getElementById('map');
        img.style.zIndex = -1;
        img.style.position = "relative";
//        img.style.top = "200px";
        img.style.left = "0px";
//        img.style.margin = "auto auto";
        img.style.width = "1200px";
    }

//    function initMatrix() {
//        var img = document.getElementById('map_img');
//        var c = document.createElement("canvas");
//        c.width = img.width;
//        c.height = img.height;
//        var ctx = c.getContext("2d");
//        ctx.drawImage(img, 0, 0);
//        var imgData = ctx.getImageData(0, 0, c.width, c.height);
//        var x = 487;
//        var y = 15;
//        for (var x = 0; x < c.width; x++) {
//            for (var y = 0; y < c.height; y++) {
//                if (imgData.data[(y*img.width + x)*4 + 0] === 255 && imgData.data[(y*img.width + x)*4 + 1] === 255 && imgData.data[(y*img.width + x)*4 + 2] === 255) {
//                    imgData.data[(y*img.width + x)*4 + 0] = 0;
//                    imgData.data[(y*img.width + x)*4 + 1] = 0;
//                    imgData.data[(y*img.width + x)*4 + 2] = 0;
//                    imgData.data[(y*img.width + x)*4 + 3] = 255;
//                } else {
//                    imgData.data[(y*img.width + x)*4 + 0] = 255;
//                    imgData.data[(y*img.width + x)*4 + 1] = 255;
//                    imgData.data[(y*img.width + x)*4 + 2] = 255;
//                    imgData.data[(y*img.width + x)*4 + 3] = 0;
//                }
//            }
//        }
//        var c = document.getElementById("myCanvas");
//        var ctx = c.getContext("2d");
//        ctx.putImageData(imgData, 0, 0);
//    }

    function getConnectedDevices() {
        $.ajax({
            url: "Mapper",
            type: "get", //send it through get method
            data:{command: "GET_CONNECTED_DEVICES"},
            success: function(response) {
                var tmp = {};
                var json = $.parseJSON(response);
                var jsonArray = json['devices'];
                for (var key in jsonArray) {
                    var id = jsonArray[key].id;
                    var name = jsonArray[key].name;
                    var x = jsonArray[key].x;
                    var y = jsonArray[key].y;
                    tmp[id] = new Device(id, name, x, y);
                }
                connectedDevices = tmp;
            },
            error: function(xhr) {
              alert("error");
            }
        });
    }

//    function resize() {
//        var width = $(window).height();
//        var height = $(window).width();
//        System.out.println(width + " " + height);
//
//    }

    var Device = function(id, name, x, y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
    };
});