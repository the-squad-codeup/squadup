$(function() {

    let url = `${window.location.protocol}//${window.location.host}/background-image`;
    $('head').append(`
                <style>
                    body:before{
                        content: "";
                        display: block;
                        position: fixed;
                        left: 0;
                        top: 0;
                        width: 100%;
                        height: 100%;
                        z-index: -100;
                        background: url('${url}') no-repeat center center;
                        -webkit-background-size: cover;
                        -moz-background-size: cover;
                        -o-background-size: cover;
                        background-size: cover;
                    }
                </style>
            `);
});