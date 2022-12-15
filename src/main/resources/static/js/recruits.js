$(function() {
    console.log("Inside recruits.js");
    // function printUserCards(user){
    //     $("#card").html('');
    // }
    async function getAllRecruits(){
        let results = await fetch("http://localhost:8080/recruits/all");
        let data = await results.json();
        console.log(data);
    }

    getAllRecruits();

});