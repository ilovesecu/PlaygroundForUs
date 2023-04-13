import Password from "./password.js";

document.addEventListener('DOMContentLoaded', ()=>{
    new RegisterMember();
})

class RegisterMember{
    constructor() {
        this.doms={
            $idInput : document.querySelector("#idInput"),
            $nickNameInput : document.querySelector("#nickNameInput"),
            $emailInput : document.querySelector("#emailInput"),
            $passwordInput : document.querySelector("#passwordInput"),
            $passwordConfirmInput: document.querySelector("#passwordConfirmInput"),
            $aboutMeTextArea:document.querySelector("#aboutMeTextArea"),
            $passwordGuideWrapper:document.querySelector("#passwordGuideWrapper"),
            $submitBtn : document.querySelector("#submitBtn"),
        }
        this.validate={
            id:false,
            nickname:false,
            email:false,
            password:false,
            passwordConfirm:false, //비밀번호 확인
        }
        
        //정규식 모음
        this.regExp = {
            //email: new RegExp('[a-z0-9]+@[a-z]+\.[a-z]{2,3}'), //이메일
            email: new RegExp("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$"),
            //하나 이상의 문자, 하나의 숫자 및 하나의 특수 문자 정규식
            blank : new RegExp(/\s/), //공백
            chars : new RegExp(/[a-zA-Z]/),//a~z, A~Z 사이의 모든 문자)
            num: new RegExp(/[0-9]/), //숫자만
            eng : new RegExp(/^[a-zA-Z]*$/), //영문만
            engNum : new RegExp(/^[A-Za-z0-9]+$/), //영문+숫자만
            engKorNum : new RegExp("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*"),
        }

        const passwordProps = {
            $passwdInput : this.doms.$passwordInput,
            $passwdGuideWrapper : this.doms.$passwordGuideWrapper,
        }
        this.password = new Password(passwordProps);
        this.eventBinding();
    }

    eventBinding(){
        /********************************************************************************************** 
         * @Method 설명 : 아이디 중복체크
         * @작성일 : 2023-04-04 
         * @작성자 : 정승주
         * @변경이력 : 
         **********************************************************************************************/
        this.doms.$idInput.addEventListener('focusout', async e=>{
            const target = e.target;
            const val = target.value;
            this.validate.id = false;

            if(!this.regExp.engNum.test(val)){
                this.setInputValidDirect(target, null, false, '아이디는 영문자와 숫자조합만 가능합니다.');
                return ;
            }
            this.procInputValid(target,'/member/rest/duple_id', '$idInputValid', '아이디','id');
        });
        
        /********************************************************************************************** 
         * @Method 설명 : 닉네임 중복체크
         * @작성일 : 2023-04-05 
         * @작성자 : 정승주
         * @변경이력 : 
         **********************************************************************************************/
        this.doms.$nickNameInput.addEventListener('focusout',async e=>{
            const target = e.target;
            const val = target.value;
            this.validate.nickname = false;
            const specialRule = /[`~!@#$%^&*|\\\'\";:\/?]/gi; //특수문자 정규식

            if(specialRule.test(val)){
                this.setInputValidDirect(target, null, false, '닉네임은 영문자,숫자,한글 조합만 가능합니다.');
                return ;
            }
            this.procInputValid(target,'/member/rest/duple_nick','$nickNameInputValid','닉네임', 'nickname');
        });
        
        /********************************************************************************************** 
         * @Method 설명 : 이메일 중복체크
         * @작성일 : 2023-04-06 
         * @작성자 : 정승주
         * @변경이력 : 
         **********************************************************************************************/
        this.doms.$emailInput.addEventListener('focusout', async e=>{
            const $target=e.target;
            this.validate.email = false;
            if(!this.emailRegExpValid($target.value)){
                this.setInputValidDirect($target,null,false,'올바른 형식의 이메일주소를 입력해주세요');
                return ;
            }
            this.procInputValid($target, '/member/rest/duple_email','$emailInputValid','이메일', 'email');
        });

        /**********************************************************************************************
         * @Method 설명 : 패스워드 검증
         * @작성일 : 2023-04-08
         * @작성자 : 정승주
         * @변경이력 :
         **********************************************************************************************/
        this.doms.$passwordInput.addEventListener('keyup', e=>{
            const $target = e.target;
            let $passwordInputValid = this.doms.$passwordInputValid ?? null;
            if(!$passwordInputValid){
                $passwordInputValid = this.doms.$passwordInput.parentElement.querySelector("div.valid");
                this.doms.$passwordInputValid = $passwordInputValid;
            }
            const validResult = this.password.validate($target,$passwordInputValid,null);
            this.setInputValidDirect($target, $passwordInputValid,validResult.valid,validResult.validTxt);
            this.validate.password=validResult;
            //패스워드 확인 포커스 왔다갔다리
            this.doms.$passwordConfirmInput.dispatchEvent(new Event('focusout'));
        });

        /**********************************************************************************************
         * @Method 설명 : 패스워드 확인 (패스워드 == 패스워드확인 인지 검사)
         * @작성일 : 2023-04-10
         * @작성자 : 정승주
         * @변경이력 :
         **********************************************************************************************/
        this.doms.$passwordConfirmInput.addEventListener('focusout', e=>{
            const $target = e.target;
            const $password = this.doms.$passwordInput;
            const confirmVal = $target.value;
            const passwordVal = $password.value;
            this.validate.passwordConfirm = false;

            let comment = '';
            let validFlag = false;
            if(passwordVal===null || passwordVal===undefined || passwordVal===''){
                comment = '패스워드를 확인해주세요.';
                validFlag = false;
            }else if(confirmVal===passwordVal){
                comment = '패스워드가 일치합니다.';
                validFlag = true;
                this.validate.passwordConfirm = true;
            }else{
                comment = '패스워드가 일치하지 않습니다.';
                validFlag = false;
            }
            this.setInputValidDirect($target,null,validFlag,comment);
        });


        /**********************************************************************************************
         * @Method 설명 : 가입하기 버튼 클릭 (회원가입 실행)
         * @작성일 : 2023-04-10
         * @작성자 : 정승주
         * @변경이력 :
         **********************************************************************************************/
        this.doms.$submitBtn.addEventListener('click',async e=>{
            for(let key of Object.keys(this.validate)){
                const value = this.validate[key];
                if(!value){
                    const item = this.validateKeyConvertKorean(key);
                    alert({title:'경고', content:`${item} 항목을 확인해주세요.`,actionName:'닫기', });
                    return ;
                }
            }
            const URI = `/member/rest`;
            const params = {
                userId : this.doms.$idInput?.value,
                "pgfuProfile.nickname" : this.doms.$nickNameInput?.value,
                "pgfuAuthentication.email" : this.doms.$emailInput?.value,
                "pgfuAuthPassword.password" : this.doms.$passwordInput?.value,
                "pgfuProfile.introduction" : this.doms.$aboutMeTextArea?.value,
            }
            const formDatas = Object.entries(params).map(v => v.join('=')).join('&');
            const responseResult = await axios.post(URI, formDatas);
            const data = responseResult.data;
            if(data.success === true){
                if(data.message === "fail"){
                    alert({title:'실패', content:`회원가입에 실패하였습니다.\n${data.reason}`, actionName:'닫기'});
                }else if(data.message === "success" && data.data !== null){
                    window.simpleImageModal({
                        content : `🎉${data.data.usernamenick}님! 🎊회원가입을 진심으로 축하합니다🎊`,
                        imgSrc : '/img/welcome.jpg',
                        actionName : '로그인하러 가기',
                        html : false,
                        action: ()=>{
                            $("#modalLayer").remove();
                            location.replace('https://developer.mozilla.org/en-US/docs/Web/API/Location/reload');
                        },
                    });
                }
            }

        });
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 검증 로직 함수화 
     * @작성일 : 2023-04-06 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    async procInputValid($target, url, $validateDom, validateTxt, validateObj){
        const val = $target.value;
        const response = await axios.get(`${url}/${val}`);
        const result = response.data;
        $validateDom = this.doms[$validateDom] ?? null;
        if(!$validateDom){
            $validateDom = $target.parentElement.querySelector("div.valid");
            this.doms[$validateDom] = $validateDom;
        }
        const param = this.setInputValidParam(result.data, $target, $validateDom, validateTxt, validateObj);
        this.setInputValidTxt(param);
    }

    /**********************************************************************************************
     * @Method 설명 : 검증텍스트를 위한 파라미터 셋팅
     * @작성일 : 2023-04-06
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    setInputValidParam(result, $dom, $validDom, targetText, validObj){
        const param = {
            $dom : $dom,
            $validDom : $validDom,
        }
        if(result === 1){ //중복체크 OK
            param.domClassName='is-valid',
            param.validClassName = 'valid-text';
            param.validText = `사용가능한 ${targetText} 입니다.`;
            this.validate[validObj] = true;
        }else{ //중복체크 NO
            param.domClassName='is-invalid',
            param.validClassName = 'invalid-text';
            param.validText = `이미 존재하는 ${targetText} 입니다.`;
            this.validate[validObj] = false;
        }
        return param;
    }

    /**********************************************************************************************
     * @Method 설명 : 검증 텍스트 셋팅
     * @작성일 : 2023-04-06
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    setInputValidTxt({$dom, $validDom, domClassName, validClassName, validText}){
        $dom.classList.remove('is-valid','is-invalid');
        $dom.classList.add(domClassName);
        if($validDom){
            $validDom.classList.remove('valid-text','invalid-text');
            $validDom.classList.add(validClassName);
            $validDom.textContent = validText;
        }
    }

    /**********************************************************************************************
     * @Method 설명 : 검증 텍스트 셋팅2 -> 직접적으로 호출할 수 있도록 간소화 버전
     * @작성일 : 2023-04-06
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    setInputValidDirect($dom, $validDom, isValid, text){
        $dom.classList.remove('is-valid','is-invalid');
        if(!$validDom) $validDom = $dom.parentElement.querySelector("div.valid");
        $validDom.classList.remove('valid-text','invalid-text');
        if(isValid){
            $dom.classList.add('is-valid');
            $validDom.classList.add('valid-text');
        } else{
            $dom.classList.add('is-invalid');
            $validDom.classList.add('invalid-text');
        }
        $validDom.textContent = text;
    }


    /**********************************************************************************************
     * @Method 설명 : 이메일 정규식 검증
     * @작성일 : 2023-04-06
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    emailRegExpValid(email){
        if(email){
            return this.regExp.email.test(email);
        }
        return false;
    }

    /**********************************************************************************************
     * @Method 설명 : validate 항목 key -> korean으로 변경
     * @작성일 : 2023-04-11
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    validateKeyConvertKorean(key){
        let result = "";
        switch (key){
            case "id":
                result = '아이디';
                break;
            case "nickname":
                result = '닉네임';
                break;
            case "email":
                result = '이메일';
                break;
            case "password":
                result = '패스워드';
                break;
            case "passwordConfirm":
                result = '패스워드 확인';
                break;
            default:
                result = "정의되지 않은 항목";
                break;
        }
        return result;
    }

}