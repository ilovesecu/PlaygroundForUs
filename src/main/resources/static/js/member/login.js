
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
            const idVal = this.doms.$idInput.value;
            const passwdVal = this.doms.$passwdInput.value;
            
            //예외처리
            console.log(idVal, passwdVal);
            if(idVal === null || idVal === undefined || idVal === "")

            this.doLogin(idVal, passwdVal);
        });
    }

    async doLogin(id, passwd){
        //로그인 진행
        const param = {
            userId : id
            ,userPassword: passwd
        };
        console.log(Object.entries(param));
        console.log(Object.entries(param).map(v => v.join('=')).join("&"));
        const paramFormData = Object.entries(param).map(v => v.join('=')).join("&");
        /*const formData = new FormData();
        formData.append("userId",id);
        formData.append("userPassword",passwd);*/
        const responseData = await axios({
            method: "post",
            url: `/auth/loginProc`,
            data: paramFormData,
        });
        if(responseData.data !== null){
            location.replace(responseData.data);
        }
    }

}

