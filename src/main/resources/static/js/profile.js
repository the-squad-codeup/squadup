import { Utils } from "./utils.js";
$(function (){
    // $.get("")
    async function getUserGames() {
        let userGames = await fetch(`${Utils.url()}game/user`).then(res => res.json());
        console.log(userGames)
    }
    getUserGames()

    async function getGame(){
        let game = await fetch(`${Utils.url()}game/user`).then(res => res.json());
        function renderUserGames(data){
            console.log("inside renderUserGames");
            for(let i = 0; i < data.list.length -1; i++){
                console.log(data)
                let game = data.list[i]
                $('.gamesContainer').append(`
                        <div class="m-4 col-2" >
                            <div class="card">
                                IM APPENDED!
                            </div>
                        </div>
                    `)
            }
        }
    }
    getGame()
})