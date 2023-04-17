
document.addEventListener('DOMContentLoaded',e=>{
    new Login();
})

class Login{
    constructor() {
        this.doms = {
            $idInput: document.querySelector("#idInput")
            ,$passwdInput : document.querySelector("#passwdInput")
            ,$loginBtn : document.querySelector("#loginBtn")
            ,$errMsg : document.querySelector("#errMsg")
            ,$loginWarn : document.querySelector("#login_warn")
        }
        this.CONSTANT_MESSAGE = {
            BLANK_ID : '<span><strong>아이디</strong>를 정확히 입력해주세요.</span>',
            BLANK_PW : '<span><strong>비밀번호</strong>를 정확히 입력해주세요.</span>',
            INVALID_VALUE : '<span><strong>잘못된</strong> 아이디 또는 비밀번호 입니다.</span>',
            EMPTY : '',
        }
        this.CONSTANT_ALERT_CLASS = {
            WARN : 'alert-warning',
            ERR : 'alert-danger',
        }
        this.eventBinding();
    }

    eventBinding(){
        this.doms.$loginBtn.addEventListener('click',e=>{
            const idVal = this.doms.$idInput.value;
            const passwdVal = this.doms.$passwdInput.value;
            
            //예외처리
            if(this.doExceptChk(idVal, passwdVal)){
                this.doLogin(idVal, passwdVal);   //로그인 처리
            }
        });
    }

    /**********************************************************************************************
     * @Method 설명 : 로그인 진행 전 예외처리
     * @작성일 : 2023-04-17
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    doExceptChk(id, passwd){
        //1. ID 빈칸 체크
        if(id === null || id === undefined || id === ""){
            this.doAnimationForErrMsg(this.CONSTANT_ALERT_CLASS.ERR,this.CONSTANT_ALERT_CLASS.WARN,this.CONSTANT_MESSAGE.BLANK_ID);
            return false;
        }
        //2. PW 빈칸 체크
        if(passwd === null || passwd === undefined || passwd === ""){
            this.doAnimationForErrMsg(this.CONSTANT_ALERT_CLASS.ERR,this.CONSTANT_ALERT_CLASS.WARN,this.CONSTANT_MESSAGE.BLANK_PW);
            return false;
        }
        return true;
    }

    /**********************************************************************************************
     * @Method 설명 : 로그인 진행 함수
     * @작성일 : 2023-04-14
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
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
        if(responseData.data.startsWith("loginError")){
            //loginError-0-Invalid Username or Password
            const errArr = responseData.data.split("-");
            const code = Number(errArr[1]);
            if(code === 0){
                this.doAnimationForErrMsg(this.CONSTANT_ALERT_CLASS.WARN, this.CONSTANT_ALERT_CLASS.ERR, this.CONSTANT_MESSAGE.INVALID_VALUE);
                return ;
            }
            this.doAnimationForErrMsg(this.CONSTANT_ALERT_CLASS.WARN, this.CONSTANT_ALERT_CLASS.ERR, errArr[2]);
        } else if(responseData.data !== null){ //서버에서 redirect 할 URL을 내려준다. (302가 아니라 200으로 내려줌.)
            location.replace(responseData.data);
        }

    }


    /**********************************************************************************************
     * @Method 설명 : setTimout 애니메이션
     * @작성일 : 2023-04-17
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    doAnimationForErrMsg(removeClass, addClass, message){
        if(this.doms.$errMsg.classList.contains('show')){ //이미 보여지고 있다면
            return false;
        }
        this.doms.$errMsg.style.display = 'block';
        this.doms.$errMsg.classList.remove(removeClass);
        this.doms.$errMsg.classList.add(addClass);
        setTimeout(()=>{
            this.doms.$errMsg.classList.toggle('show')
            this.doms.$loginWarn.insertAdjacentHTML('afterend', message);
        },0);
        setTimeout(()=>{
            this.doms.$errMsg.classList.toggle('show');
        },1000);
        setTimeout(()=>{
            this.doms.$errMsg.style.display = 'none';
            this.doms.$errMsg.removeChild(this.doms.$errMsg.children[1]);
        },1200);
    }
}

