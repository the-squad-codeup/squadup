import {Utils} from "./utils.js"
$(function () {
    console.log("Inside social-hq.js");
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////// Recruits Section ///////////////////////////////////////////////////





    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////// Squads and Comrades Section /////////////////////////////////////////////
async function getSquads(){
    return await fetch(`${Utils.url()}user/squads`).then(res => res.json());
}

async function getComrades(){
    return await fetch(`${Utils.url()}comrades/all`).then(res => res.json())
}

async function printSquads(){
    let squads = await getSquads();
    console.log(squads)
    $("#squads-content").empty().append(`
        <div id="add-squad-wrapper" class="solo-squad">
            <h5>Add Squad</h5>
            <img class="solo-squad-img rgb" src="https://cdn.filestackcontent.com/ZbMRmZDQQC65sr7daKvq">
        </div>
    `);
    for(let squad of squads){
        $("#squads-content").append(`
            <div data-squad-id="${squad.id}" class="solo-squad">
                <h5>${squad.name}</h5>
                <img class="solo-squad-img rgb" src="${squad.squadPicture.url}">
            </div>
        `)
    }
}


async function pringComrades(){
    let comrades = await getComrades();
    console.log(comrades)
    for(let comrade of comrades){
        $("#comrades-content").append(`
            <div data-comrade-id="${comrade.id}" class="solo-com">
                <h5>${comrade.userTwo.username}</h5>
                <img class="solo-com-img rgb" src="${comrade.userTwo.profilePicture.url}">
            </div>
        `);
    }
}


printSquads();
pringComrades();
});