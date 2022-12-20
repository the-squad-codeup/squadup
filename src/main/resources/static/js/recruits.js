$(function() {
    console.log("Inside recruits.js");
    async function printUserCards(recruits) {
        recruits = await recruits;
        console.log(recruits);
        $("#card").html('');
        for (let recruit of recruits) {
            $(`#card`).append(`
                <div class="card col-4" data-recruit-id="${recruit.id}">
                    <img class="card-img-top" src="https://i.imgur.com/0Z0Z0Z0.jpg" alt="Card image">
                        <div class="card-body">
                            <h4 class="card-title">${recruit.userTwo.username}</h4>
                            <p class="card-text">${recruit.userTwo.username}'s Bio: ${recruit.userTwo.preferences.bio}</p>
                            <a href="#"  class="btn btn-primary squadup-link">SquadUp</a>
                            <a href="#" class="btn btn-primary squaddown-link">SquadDown</a>
                        </div>
                </div>
            `);
        }
    }


    document.getElementById("card").addEventListener('click', function (e){
        e.preventDefault();
        if (e.target && e.target.classList.contains("squadup-link")){
         let id = e.target.parentElement.parentElement.getAttribute("data-recruit-id");
            $.ajax({
                url: `/api/recruits/${user}`,
                method: "PUT",
                data: JSON.stringify({
                    userOne: {
                        id: 1
                    },
                    userTwo: {
                        id: 2
                    },
                    userOneAccepted: true,
                    userTwoAccepted: false
                }),
                dataType: "json",
                contentType: "application/json",
                success: function(data) {
                    console.log(data);
                }
            })

        }

        if (e.target && e.target.classList.contains("squaddown-link")){
            let link = e.target.parentElement.parentElement.getAttribute("data-recruit-id")
            console.log(link);
        }

    })

    async function getAllRecruits(){
        let results = await fetch("http://localhost:8080/recruits/all");
        let data = await results.json();
        console.log(data);
        return data;
    }

    printUserCards(getAllRecruits());
});

