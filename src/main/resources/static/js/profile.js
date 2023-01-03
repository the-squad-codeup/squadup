import { Utils } from "./utils.js";
$(function (){
    // $.get("")
    async function getUserGames() {
        let userGames = await fetch(`${Utils.url()}game/user`).then(res => res.json());
        console.log(userGames)
        for(let i = 0; i < userGames.length; i++){
            $('.my-games').append(`
                <div class="game">
                    IM APPENDED!
                </div>
            `)
        }
    }
    getUserGames()

})