let items = [0,1,2,3,4,5,6];
let animating = false;

window.setInterval(function(){
    startAnimations();
}, 800);

function startAnimations() {
    if(!animating){
        animating = true;
        $('.item--'+items[0]).css({ zIndex: -22 });
        $('.item--'+items[1]).css({ zIndex: -21 });
        $('.item--'+items[2]).css({ zIndex: -20 });
        $('.item--'+items[3]).css({ zIndex: -19 });
        $('.item--'+items[4]).css({ zIndex: -18 });
        $('.item--'+items[5]).css({ zIndex: -17 });
        $('.item--'+items[6]).css({ zIndex: -16 });

        $('.item--'+items[0]).animate({
            left: '100%',
        }, 0);
        $('.item--'+items[1]).animate({
            left: '80%',
        }, 1000);
        $('.item--'+items[2]).animate({
            left: '60%'
        }, 1000);
        $('.item--'+items[3]).animate({
            left: '40%'
        }, 1000);
        $('.item--'+items[4]).animate({
            left: '20%'
        }, 1000);
        $('.item--'+items[5]).animate({
            left: '0%'
        }, 1000);
        $('.item--'+items[6]).animate({
            left: '-20%'
        }, 1000);
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

