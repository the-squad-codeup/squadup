
    function notEmptyFields() {
        document.addEventListener('keyup', function() {
            var blankEmail = document.contactUs.email.value;
            var blankBody = document.contactUs.body.value;
            if ((blankEmail !== "") && (blankBody !== "")) {
                document.getElementById('emailSend').removeAttribute("disabled");
            }
        });
    }
    notEmptyFields();

    function reDisable(){
        document.addEventListener('keyup', function (){
            var blankEmail = document.contactUs.email.value;
            var blankBody = document.contactUs.body.value;
            if ((blankEmail === "") || (blankBody === "")){
                document.getElementById('emailSend').disabled = true;
            }
        });
    }
    reDisable();


