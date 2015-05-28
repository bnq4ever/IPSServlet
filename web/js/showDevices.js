var connectedDevices = {};

$(document).ready(function() {

    setInterval(getConnectedDevices, 100);
    setInterval(updateMapDevices, 100);

    $('#mappingArea').on('click', '.device', function () {
        var device = connectedDevices[this.id];
        alert(device.name + "\n" + 
                "x: " + device.x + " y: " + device.y + "\n" +
                "id: " + device.id);
    });

    function updateMapDevices() {
        removeDisconnected();

        if(!showDevices)
            return;
        
        var img_container = document.getElementById('map');

        var ratioX = parseInt(img_container.style.width) / 685;
        var ratioY = parseInt(img_container.style.height) / 1122;
        
//        var img = document.getElementById('map_img');
//
//        var ratioX = parseInt(img_container.style.width) / 1200;
//        var ratioY = parseInt(img_container.style.height) / 1122;
        
        for (var id in connectedDevices) {            
            var div = document.getElementById(id);
            if (div === null) {
                generateDiv(id, connectedDevices[id].name, connectedDevices[id].x*ratioX, connectedDevices[id].y*ratioY, connectedDevices[id].color);
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
        
        var img_container = document.getElementById('map');

        var ratioX = parseInt(img_container.style.width) / 685;
        var ratioY = parseInt(img_container.style.height) / 1122;
        
//        var img = document.getElementById('map_img');
//
//        var ratioX = parseInt(img_container.style.width) / 1200;
//        var ratioY = parseInt(img_container.style.height) / 1122;

        var size = 40*ratioX;
        
        var div = document.getElementById(id);
        var x = connectedDevices[id].x*ratioX;
        var y = connectedDevices[id].y*ratioY;
        div.style.left = x - size/2 + "px";
        div.style.top = y - size/2 - 2 + "px";
    }

    function generateDiv(deviceID, name, x, y, color) {
        
        var img_container = document.getElementById('map');

        var ratioX = parseInt(img_container.style.width) / 685;
        var ratioY = parseInt(img_container.style.height) / 1122;
        
//        var img = document.getElementById('map_img');
//
//        var ratioX = parseInt(img_container.style.width) / 1200;
//        var ratioY = parseInt(img_container.style.height) / 1122;
        var size = 40*ratioX;
        
        var div = document.createElement("DIV");
        div.style.backgroundImage = "url('imgs/person.png')";
        div.style.backgroundColor = color;
        div.style.backgroundSize = size + "px " + size + 2 + "px";
        div.style.backgroundRepeat = "no-repeat";
        div.style.borderRadius = "20px";
        var divID = document.createAttribute("id");
        divID.value = deviceID;
        div.setAttributeNode(divID);

        var divClass = document.createAttribute("class");
        divClass.value = "device";
        div.setAttributeNode(divClass);
        div.style.position = "absolute";
        div.style.top = Math.floor(y) - size/2 - 2 + "px";
        div.style.left = Math.floor(x) - size/2 + "px";
        div.style.zIndex = "1";
        div.style.height = size + "px";
        div.style.width = size + "px";
        //div.style.backgroundcolor = getRandomColor();
        $(div).hide();
        document.getElementById("mappingArea").appendChild(div);
        $(div).clearQueue().fadeIn("fast");
    }

//    function setMap() {
//        var img = document.getElementById('map');
//        img.style.zIndex = -1;
//        img.style.position = "relative";
////        img.style.top = "200px";
//        img.style.left = "0px";
////        img.style.margin = "auto auto";
//        img.style.width = "1200px";
//    }

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

/* will not work
    function pushDevices(response) {
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
                updateMapDevices();
    }
*/
    function getConnectedDevices() {
        if(!showDevices || !online) {
            connectedDevices = {};
            return;
        }
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
                    var color = jsonArray[key].color;
                    tmp[id] = new Device(id, name, x, y, color);
                }
                connectedDevices = tmp;
            },
            error: function(xhr) {
              alert("error");
            }
        });
    }

    var Device = function(id, name, x, y, color) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.color = color;
    };
    
});