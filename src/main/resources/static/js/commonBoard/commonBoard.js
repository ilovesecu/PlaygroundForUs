document.addEventListener('DOMContentLoaded',()=>{
    new CommonBoard();
})

class CommonBoard{
    constructor() {
        this.addBroadCast();
    }
    eventBinding(){

    }
    addBroadCast(){
        setTimeout(()=>{
            bc.onmessage = (event) => {
                console.log(event);
            };
        },500);
    }
}