// $(function() {
//
//
// });

// $( window ).on( "ready", hideOverlays() {
//     console.log( "overlays hidden" );
// });

function hideOverlays() {
    document.getElementById('HAST').style.display = "none";
    document.getElementById('AK').style.display = "none";
    document.getElementById('PT').style.display = "none";
    document.getElementById('MT').style.display = "none";
    document.getElementById('AZ').style.display = "none";
    document.getElementById('CT').style.display = "none";
    document.getElementById('ET').style.display = "none";
    document.getElementById('AST').style.display = "none";
}

function toggleOverlay(){
    hideOverlays();
    var x = document.getElementById('timezones').value
    if( x == 1) {
        document.getElementById('HAST').style.display = "block";
    } else if ( x == 2) {
        document.getElementById('AK').style.display = "block";
    } else if ( x == 3) {
        document.getElementById('PT').style.display = "block";
    } else if ( x == 4) {
        document.getElementById('MT').style.display = "block";
    } else if ( x == 5) {
        document.getElementById('AZ').style.display = "block";
    } else if ( x == 6) {
        document.getElementById('CT').style.display = "block";
    } else if ( x == 7) {
        document.getElementById('ET').style.display = "block";
    } else if ( x == 8) {
        document.getElementById('AST').style.display = "block";
    }
};