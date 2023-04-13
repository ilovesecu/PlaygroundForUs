
document.addEventListener('DOMContentLoaded',e=>{
    new Login();
})

class Login{
    constructor() {
        this.doms = {
            $idInput: document.querySelector("#idInput")
            ,$passwdInput : document.querySelector("#passwdInput")
            ,$loginBtn : document.querySelector("#loginBtn")
            ,
        }
        this.eventBinding();
    }

    eventBinding(){
        this.doms.$loginBtn.addEventListener('click',e=>{
            const $idVal = this.doms.$idInput.value;
            const $passwdVal = this.doms.$passwdInput.value;
            
            //예외처리
            console.log($idVal, $passwdVal);
        });
    }

    async doLogin(id, passwd){
        //로그인 진행
        const responseData = await axios.post('/');
    }

}