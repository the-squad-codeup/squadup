let items = [0,1,2,3,4];
let animating = false;

window.setInterval(function(){
    startAnimations();
}, 800);

function startAnimations() {
    if(!animating){
        animating = true;
        $('.item--'+items[0]).css({ zIndex: -5 });
        $('.item--'+items[1]).css({ zIndex: -4 });
        $('.item--'+items[2]).css({ zIndex: -3 });
        $('.item--'+items[3]).css({ zIndex: -2 });
        $('.item--'+items[4]).css({ zIndex: -1 });

        $('.item--'+items[0]).animate({
            left: '70vw',
        }, 1000);
        $('.item--'+items[1]).animate({
            left: '55vw',
        }, 1000);
        $('.item--'+items[2]).animate({
            left: '40vw'
        }, 1000);
        $('.item--'+items[3]).animate({
            left: '25vw'
        }, 1000);
        $('.item--'+items[4]).animate({
            left: '10vw'
        }, 1000);
        console.log('animations-complete');
        setTimeout(function(){
            items.unshift(items.pop());
            setTimeout(function(){
                animating = false;
            }, 300);
        }, 1500);
    }

}



// items.push(items.shift());
// videos.push(videos.shift());

