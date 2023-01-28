// Overlays for time zones
export const Overlays = {
    hideOverlays() {
        document.getElementById('HAST').style.display = "none";
        document.getElementById('AK').style.display = "none";
        document.getElementById('PT').style.display = "none";
        document.getElementById('MT').style.display = "none";
        document.getElementById('AZ').style.display = "none";
        document.getElementById('CT').style.display = "none";
        document.getElementById('ET').style.display = "none";
        document.getElementById('AST').style.display = "none";
    },
    toggleOverlay(){
        this.hideOverlays();
        var x = document.getElementById('location').value
        if( x === 'Hawaii-Aleutian Time Zone') {
            document.getElementById('HAST').style.display = "block";
        } else if ( x === 'Alaskan Time Zone') {
            document.getElementById('AK').style.display = "block";
        } else if ( x === 'Pacific Time Zone') {
            document.getElementById('PT').style.display = "block";
        } else if ( x === 'Mountain Time Zone') {
            document.getElementById('MT').style.display = "block";
        } else if ( x === 'Mountain Arizona Time Zone') {
            document.getElementById('AZ').style.display = "block";
        } else if ( x === 'Central Time Zone') {
            document.getElementById('CT').style.display = "block";
        } else if ( x === 'Eastern Time Zone') {
            document.getElementById('ET').style.display = "block";
        } else if ( x === 'Atlantic Time Zone') {
            document.getElementById('AST').style.display = "block";
        }
    }
}

