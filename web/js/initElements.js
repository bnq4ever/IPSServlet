$(document).ready(function() {
    window.onload = init();
    window.onresize = resize;
    
    function init() {

        var screenWidth = $(window).width();
        var screenHeight = $(window).height();
        
        var width = screenWidth * 0.4;
        var height = width * 1.638;

//        var width = screenWidth * 0.8;
//        var height = width * 0.935;
        
        var img_container = document.getElementById('map');
        img_container.style.width = width + "px";
        img_container.style.height = height + "px";
        img_container.style.marginLeft = "auto"; 
        img_container.style.marginRight = "auto"; 
        img_container.style.marginTop = "20px";
        
        var particleCanvas = document.getElementById("particleArea");
        particleCanvas.width = width;
        particleCanvas.height = height;

        var img = document.getElementById('map_img');
        img.style.width = width + "px";
        img.style.height = height + "px";
        
        var radiomapCanvas = document.getElementById("radiomapArea");
        radiomapCanvas.width = width;
        radiomapCanvas.height = height;
        
        var candidateCanvas = document.getElementById("candidateArea");
        candidateCanvas.width = width;
        candidateCanvas.height = height;
        

    }
    
    function resize() {
        
        var screenWidth = $(window).width();
        var screenHeight = $(document).height();
        
        var width = screenWidth * 0.4;
        var height = width * 1.638;

//        var width = screenWidth * 0.8;
//        var height = width * 0.935;
        
        var img_container = document.getElementById('map');
        img_container.style.width =  width + "px";
        img_container.style.height = height + "px";
        
        var img = document.getElementById('map_img');
        img.style.width = width + "px";
        img.style.height = height + "px";
        
        var particleCanvas = document.getElementById("particleArea");
        particleCanvas.width  = width;
        particleCanvas.height = height;
        
        var radiomapCanvas = document.getElementById("radiomapArea");
        radiomapCanvas.width  = width;
        radiomapCanvas.height = height;
        
        var candidateCanvas = document.getElementById("candidateArea");
        candidateCanvas.width = width;
        candidateCanvas.height = height;
        
    }
});