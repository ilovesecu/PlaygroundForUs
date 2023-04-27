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
                const param = JSON.parse(event.data);
                const {cmd, data} = param;
                if(cmd==="just_refresh"){
                  location.reload();
                }
            };
        },500);
    }
}