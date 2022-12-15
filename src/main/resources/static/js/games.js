$(function () {
    console.log("Inside games.js");

    const Games = {
        initialize() {
            console.log("inside Games.initialize()");
            Events.initialize();
        }
    }

    const Events = {
        initialize() {
            console.log("Inside Events.initialize()");
            $(document)
                .on("click", "#game-search-button", function() {
                    console.log("button has been clicked. Value in input: ");
                    console.log($("#game-search-input").val());
                })
                .on("keyup", function(e) {
                    if($("#game-search-input").is(":focus") && e.key === "Enter") {
                        $("#game-search-button").trigger("click");
                    }
                })
            ;
        }
    }

    Games.initialize();
});