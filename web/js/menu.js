$(document).ready(function () {

    //INITIALIZATION
    $(".menu-filter-offline").hide();
    $(".menu-filter-online").show();

    $("#radiomapArea").hide();    
    $("#mappingArea").show();
    $("#particleArea").show();
    
    $("#filter-online-devices :checkbox").prop('checked', true);
        
    document.getElementById("btn-online").style.color = "lightgray";
    
    
    //ONLINE
    $(".menu-phase").on('click', '#btn-online', function () {
        offline = false;
        online = true;
        
        $("#filter-online-devices :checkbox").prop('checked', true);
        showDevices = true;
    
        document.getElementById("btn-online").style.color = "lightgray";
        document.getElementById("btn-offline").style.color = "black";

        $(".menu-filter-offline").hide();
        $(".menu-filter-online").show();
    
        $("#radiomapArea").fadeOut();
        $("#mappingArea").fadeIn();
        $("#particleArea").fadeIn();
    });
    
    
    //OFFLINE    
    $(".menu-phase").on('click', '#btn-offline', function () {
        online = false;
        offline = true;
        
        showMagneticPoints = true;
        $("#filter-offline-magneticPoints :checkbox").prop('checked', true);
        
        document.getElementById("btn-offline").style.color = "lightgray";
        document.getElementById("btn-online").style.color = "black";
        
        $(".menu-filter-online").hide();
        $(".menu-filter-offline").show();

        $("#mappingArea").fadeOut();
        $("#particleArea").fadeOut();
        $("#radiomapArea").fadeIn();
    });


    //ONLINE FILTERS
    $("#filter-online-devices :checkbox").change(function () {
        showDevices = $(this).is(':checked');
    });

    $("#filter-online-particles :checkbox").change(function () {
        showParticles = $(this).is(':checked');
    });

    $("#filter-online-candidates :checkbox").change(function () {
        showCandidates = $(this).is(':checked');
    });
    
    
    //OFFLINE FILTERS
    $("#filter-offline-magneticPoints :checkbox").change(function () {
        showMagneticPoints = $(this).is(':checked');
    });

    $("#filter-offline-referenceAreas :checkbox").change(function () {
        showReferenceAreas = $(this).is(':checked');
    });
   
});