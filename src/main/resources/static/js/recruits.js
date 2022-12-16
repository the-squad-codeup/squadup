$(function() {
    console.log("Inside recruits.js");
    async function printUserCards(recruits) {
        recruits = await recruits;
        console.log(recruits);
        $("#card").html('');
        for (let recruit of recruits) {
            $(`#card`).append(`
                <div class="card mx-auto" data-recruit-id="${recruit.id}">
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

   addEventListener('click', function (e){
        e.preventDefault();

    })

    async function getAllRecruits(){
        let results = await fetch("http://localhost:8080/recruits/all");
        let data = await results.json();
        console.log(data);
        return data;
    }

    printUserCards(getAllRecruits());

});

