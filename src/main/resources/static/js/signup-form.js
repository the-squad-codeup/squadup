import { Utils } from "./utils.js";

$(function() {
    // Local variables and requirements
    let passwordFulfillsRequirements = false;
    let lengthMet = false;
    let uppercaseMet = false;
    let lowercaseMet = false;
    let numberMet = false;
    let specialMet = false;
    let disabledAttribute = $("#signupPasswordSubmit").attr("disabled");

    // Check object for methods checking parameters
    const Check = {
        // Checks if input is a number
        isNumber: input => {
            return !(isNaN(Number(input)));
        },
        // Checks if length is at least eight characters
        atLeastEight: input => {
            if(input.length >= 8) {
                return true;
            } else {
                return false;
            }
        },
        // Checks if input contains a lowercase letter
        containsLowercase: input => {
            for(let i = 0; i < input.length; i++) {
                if(input.charAt(i).toLowerCase() === input.charAt(i) && !(Check.isNumber(input.charAt(i))) && !(Check.containsSpecialCharacter(input.charAt(i)))) {
                    return true;
                }
            }
            return false;
        },
        // Checks if input contains an uppercase letter
        containsUppercase: input => {
            for(let i = 0; i < input.length; i++) {
                if(input.charAt(i).toUpperCase() === input.charAt(i) && !(Check.isNumber(input.charAt(i))) && !(Check.containsSpecialCharacter(input.charAt(i)))) {
                    return true;
                }
            }
            return false;
        },
        // Checks if input contains a number
        containsNumber: input => {
            for(let i = 0; i < input.length; i++) {
                if(Check.isNumber(input.charAt(i))) {
                    return true;
                }
            }
            return false;
        },
        // Checks if input contains a special character
        containsSpecialCharacter: input => {
            let specials = "!@#$%^&*()-=_+{}[]|;:'\",./?><"
            for(let i = 0; i < input.length; i++) {
                for(let j = 0; j < specials.length; j++) {
                    if(input.charAt(i) === specials.charAt(j)) {
                        return true;
                    }
                }
            }
            return false;
        },
        // Checks if input meets requirements
        requirementsNotMet: input => {
            if(Check.atLeastEight(input) && Check.containsLowercase(input) && Check.containsUppercase(input) && Check.containsNumber(input) && Check.containsSpecialCharacter(input)){
                return false;
            } else {
                return true;
            }
        },
        // Checks if confirm password field matches password field
        confirmMatchesPassword: () => {
            return $("#signupConfirmPassword").val() === $("#signupPassword").val();
        },
        async userValid() {
            let users = await fetch(`${Utils.url()}user/all`).then(res => res.json());
            let usernameValid = this.usernameValid(users);
            let emailValid = this.emailValid(users);
            if(usernameValid && emailValid) {
                return true;
            }
            return false;
        },
        usernameValid(users) {
            let isValid = true;
            for(let user of users) {
                if(user.username.toLowerCase() === $("#signupUsername").val().toLowerCase()) {
                    isValid = false;
                }
            }
            usernameValidation(isValid);
            return isValid;
        },
        emailValid(users) {
            let isValid = true;
            for(let user of users) {
                if(user.email.toLowerCase() === $("#signupEmailAddress").val().toLowerCase()) {
                    isValid = false;
                }
            }
            emailValidation(isValid);
            return isValid;
        }
    }

    // Output object for output methods
    const Output = {
        Text: {
            revert: selector => {
                selector.css("color", "#6c757d");
            },
            makeRed: selector => {
                selector.css("color", "#F00");
            }
        },
        Border: {
            revert: selector => {
                selector.css("border", "1px solid #ced4da");
            },
            makeRed: selector => {
                selector.css("border", "2px solid #F00");
            },
            changeOnRequirements: selector => {
                if(Check.requirementsNotMet(selector.val())){
                    Output.Border.makeRed(selector);
                } else {
                    Output.Border.revert(selector);
                }
            },
            changePasswordMatch: selector => {
                if(Check.confirmMatchesPassword()) {
                    Output.Border.revert(selector);
                    $("#passwordsMustMatch").removeClass("d-block").addClass("d-none");
                } else {
                    Output.Border.makeRed(selector);
                    $("#passwordsMustMatch").removeClass("d-none").addClass("d-block");
                }
            }
        },
        changeIndividualRequirements: value => {
            lengthMet = Check.atLeastEight(value);
            lengthMet ? Output.Text.revert($("#signupPasswordHelpLength")) : Output.Text.makeRed($("#signupPasswordHelpLength"));
            lowercaseMet = Check.containsLowercase(value);
            lowercaseMet ? Output.Text.revert($("#signupPasswordHelpLowercaseLetter")) : Output.Text.makeRed($("#signupPasswordHelpLowercaseLetter"));
            uppercaseMet = Check.containsUppercase(value);
            uppercaseMet ? Output.Text.revert($("#signupPasswordHelpUppercaseLetter")) : Output.Text.makeRed($("#signupPasswordHelpUppercaseLetter"));
            numberMet = Check.containsNumber(value);
            numberMet ? Output.Text.revert($("#signupPasswordHelpNumber")) : Output.Text.makeRed($("#signupPasswordHelpNumber"));
            specialMet = Check.containsSpecialCharacter(value);
            specialMet ? Output.Text.revert($("#signupPasswordHelpSpecialCharacters")) : Output.Text.makeRed($("#signupPasswordHelpSpecialCharacters"));
        },

        //Does this appear now!?
        submitButtonEnableDisable: () => {
            disabledAttribute = $("#signupPasswordSubmit").attr("disabled");
            if(!(Check.requirementsNotMet($("#signupPassword").val())) && Check.confirmMatchesPassword()) {
                passwordFulfillsRequirements = true;
            }
            if(passwordFulfillsRequirements) {
                $("#signupPasswordSubmit").removeAttr("disabled");
            } else if(typeof disabledAttribute === undefined || typeof disabledAttribute === false) {
                $("#signupPasswordSubmit").attr("disabled", "");
            }
        }
    }

    // Checking and changing password field and text based on requirements
    $("#signupPassword")
        .focus(() => {
            Output.Border.changeOnRequirements($("#signupPassword"));
            Output.changeIndividualRequirements($("#signupPassword").val());
        })
        .keyup(() => {
            Output.Border.changeOnRequirements($("#signupPassword"));
            Output.changeIndividualRequirements($("#signupPassword").val());
            Output.submitButtonEnableDisable();
        })
    ;

    // Checking and changing confirm password field and text based on requirements
    $("#signupConfirmPassword")
        .focus(() => {
            Output.Border.changePasswordMatch($("#signupConfirmPassword"));
        })
        .keyup(() => {
            Output.Border.changePasswordMatch($("#signupConfirmPassword"));
            Output.submitButtonEnableDisable();
        })
    ;

    function emailValidation(isValid) {
        let emailInput = $("#signupEmailAddress");
        if(isValid) {
            emailInput.css("background", "");
            emailInput.parent().find("#email-validation-prepend").remove();
        } else {
            emailInput.css("background", "#fc3503");
            emailInput.parent().find("#email-validation-prepend").remove();
            emailInput.parent().prepend(`
                <p id="email-validation-prepend" class="validation-prepend">
                    <span>Email must be unique. Please choose another or sign in</span>
                </p>
            `);
        }
    }

    function usernameValidation(isValid) {
        let usernameInput = $("#signupUsername");
        if(isValid) {
            usernameInput.css("background", "");
            usernameInput.parent().find("#username-validation-prepend").remove();
        } else {
            usernameInput.css("background", "#fc3503");
            usernameInput.parent().find("#username-validation-prepend").remove();
            usernameInput.parent().prepend(`
                <p id="username-validation-prepend" class="validation-prepend">
                    <span>Username must be unique. Please choose another or sign in</span>
                </p>
            `);
        }
    }

    // Toggles signup sheet
    // $("#notAUserToggle")
    //     .click(() => {
    //         $(".rainbow").css("animation", "none");
    //         $(".colorReset1").css("animation", "none");
    //         $(".rgb").css("animation", "none");
    //         $("#loginPasswordSubmit").css("animation", "none");
    //         // $(".colorReset1").css("animation", "glow-text 20s linear 0s infinite");
    //         // $("#rainbowreset1").css("animation", "colorRotate 20s linear 0s infinite")
    //         // $(".glow-text").css("animation", "glow-text 20s linear 0s infinite")
    //         // $(".rgb").css("animation", "rgb 20s linear 0s infinite")
    //         // $("#r1").css("animation", "colorRotate 20s linear 0s infinite")
    //         // $(".rgb-background").css("animation", "rgb-background 20s linear 0s infinite")
    //     });

        $("#notAUserToggle").mousedown(function() {

            $(".colorReset1").css("animation", "none");
            $(".colorReset2").css("animation", "none");
            $(".colorReset2").removeClass("rainbow")

            $(".rgb").css("animation", "none");
            $("#loginPasswordSubmit").css("animation", "none");
        });

        $("#notAUserToggle").mouseup(function() {
            $("#signupContainer").slideToggle();

            $(".colorReset1").css("animation", "glow-text 20s linear 0s infinite");
            $(".colorReset2").css("animation", "colorRotate 20s linear 0s infinite");

            $(".rgb").css("animation", "rgb 20s linear 0s infinite");
            $("#loginPasswordSubmit").css("animation", "rgb-background 20s linear 0s infinite");
        });


    $("#forgot-password-link").on("click", function() {
        window.location.href = "/pwreset";
    });

    $("#signupPasswordSubmit").on("click", async function(e) {
        e.preventDefault();
        if(await Check.userValid()) {
            $("#signup-form").submit();
        } else {
            document.body.scrollTop = document.documentElement.scrollTop = 0;
        }
    });

    $(window).ready( function() {
        $("#username").focus();
    });

});