$(document).ready(function () {

    $(".menu-filter-offline").hide();
    $(".menu-filter-online").show();

    $("#radiomapArea").hide();    
    $("#mappingArea").show();
    $("#particleArea").show();
    
    document.getElementById("btn-online").style.color = "lightgray";
        
    $(".menu-phase").on('click', '#btn-online', function () {
        alert("online");
        
        offline = false;
        online = true;
        
        document.getElementById("btn-online").style.color = "lightgray";
        document.getElementById("btn-offline").style.color = "black";

        $(".menu-filter-offline").hide();
        $(".menu-filter-online").show();
    
        $("#radiomapArea").fadeOut();
        $("#mappingArea").fadeIn();
        $("#particleArea").fadeIn();
    });
    
    $(".menu-phase").on('click', '#btn-offline', function () {
        alert("offline");
        
        online = false;
        offline = true;
        
        document.getElementById("btn-offline").style.color = "lightgray";
        document.getElementById("btn-online").style.color = "black";
        
        $(".menu-filter-online").hide();
        $(".menu-filter-offline").show();

        $("#mappingArea").fadeOut();
        $("#particleArea").fadeOut();
        $("#radiomapArea").fadeIn();
    });
    
    $("#filter-offline-magneticPoints :checkbox").change(function() {
        
        if($(this).is(':checked'))
            showMagneticPoints = true;
        else
            showMagneticPoints = false;
        
    });
    
    $("#filter-offline-referenceAreas :checkbox").change(function() {
        
        if($(this).is(':checked'))
            showReferenceAreas = true;
        else
            showReferenceAreas = false;
        
    });
    
    $("#filter-online-devices :checkbox").change(function() {
        
        if($(this).is(':checked'))
            showDevices = true;
        else
            showDevices = false;
        
    });
    
    $("#filter-online-particles :checkbox").change(function() {
        
        if($(this).is(':checked'))
            showParticles = true;
        else
            showParticles = false;
        
    });
    
    $("#filter-online-candidates :checkbox").change(function() {
        
        if($(this).is(':checked'))
            showCandidates = true;
        else
            showCandidates = false;
        
    });
});