var connectedDevices = {};
var ratioX;
var ratioY;

$(document).ready(function() {
    
    setInterval(getConnectedDevices, 200);
    setInterval(updateMapDevices, 200);

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
        if (navigator.userAgent.match(/Mobi/)) {
            ratioX = 1;
            ratioY = 1;
        } else {
            var img_container = document.getElementById('map');
            ratioX = parseInt(img_container.style.width) / 685;
            ratioY = parseInt(img_container.style.height) / 1122;
        }
        
        var size = 40*ratioX;
        
        var div = document.getElementById(id);
        var x = connectedDevices[id].x*ratioX;
        var y = connectedDevices[id].y*ratioY;

        div.style.left = x - size/2 + "px";
        div.style.top = y - size/2 - 2 + "px";
        
        var name = (div.firstElementChild||div.firstChild);
        name.style.position = "relative";
        name.style.bottom = -size + "px";
        name.style.left = -name.offsetWidth/2 + size/2 + "px";
        name.style.display = "inline";
        
    }

    function generateDiv(deviceID, name, x, y, color) {
        
        if (navigator.userAgent.match(/Mobi/)) {
            ratioX = 1;
            ratioY = 1;
        } else {
            var img_container = document.getElementById('map');
            ratioX = parseInt(img_container.style.width) / 685;
            ratioY = parseInt(img_container.style.height) / 1122;
        }
        
        var size = 40*ratioX;
        
        var div = document.createElement("DIV");
        div.style.backgroundImage = "url('imgs/person.png')";
        div.style.backgroundColor = color;
        div.style.backgroundSize = size + "px " + size + "px";
        div.style.backgroundRepeat = "no-repeat";
        div.style.borderRadius = "40px";
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
        
        
        var nameDiv = document.createElement("DIV");
        var nameClass = document.createAttribute("class");
        nameClass.value = "device-name";
        nameDiv.setAttributeNode(nameClass);
        nameDiv.innerHTML = name;
        
        div.appendChild(nameDiv);
        
        //div.style.backgroundcolor = getRandomColor();
        $(div).hide();
        document.getElementById("mappingArea").appendChild(div);
        $(div).clearQueue().fadeIn("fast");
    }

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
//                alert(response);
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
              //alert("error");
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