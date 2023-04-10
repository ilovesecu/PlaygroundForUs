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
            email: new RegExp('[a-z0-9]+@[a-z]+\.[a-z]{2,3}'), //이메일
            //최소 8 자, 하나 이상의 문자, 하나의 숫자 및 하나의 특수 문자 정규식
            blank : new RegExp(/\s/), //공백
            chars : new RegExp(/[a-zA-Z]/),//a~z, A~Z 사이의 모든 문자)
            num: new RegExp(/[0-9]/), //숫자
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
            const response = await axios.get(`/member/rest/duple_id/${val}`);
            const result = response.data;

            let $idInputValid = this.doms.$idInputValid ?? null;
            if(!$idInputValid){
                $idInputValid = this.doms.$idInput.parentElement.querySelector("div.valid");
                this.doms.$idInputValid = $idInputValid;
            }
            const param = this.setInputValidParam(result.data, target, $idInputValid, '아이디', 'id');
            this.setInputValidTxt(param);
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
            const response = await axios.get(`/member/rest/duple_nick/${val}`);
            const result = response.data;

            let $nickNameInputValid = this.doms.$nickNameInputValid ?? null;
            if(!$nickNameInputValid){
                $nickNameInputValid = this.doms.$nickNameInput.parentElement.querySelector("div.valid");
                this.doms.$nickNameInputValid = $nickNameInputValid;
            }
            const param = this.setInputValidParam(result.data, target, $nickNameInputValid, '닉네임', 'nickname');
            this.setInputValidTxt(param);
        });
        
        /********************************************************************************************** 
         * @Method 설명 : 이메일 중복체크
         * @작성일 : 2023-04-06 
         * @작성자 : 정승주
         * @변경이력 : 
         **********************************************************************************************/
        this.doms.$emailInput.addEventListener('focusout', async e=>{
            const $target=e.target;
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

            if(confirmVal===passwordVal){
                this.setInputValidDirect($target,null,true,'패스워드가 일치합니다.');
                this.validate.passwordConfirm = true;
            }else{
                this.setInputValidDirect($target,null,false,'패스워드가 일치하지 않습니다.');
                this.validate.passwordConfirm = false;
            }
        });


        /**********************************************************************************************
         * @Method 설명 : 가입하기 버튼 클릭 (회원가입 실행)
         * @작성일 : 2023-04-10
         * @작성자 : 정승주
         * @변경이력 :
         **********************************************************************************************/
        this.doms.$submitBtn.addEventListener('click',e=>{
            for(let key of Object.keys(this.validate)){
                const value = this.validate[key];

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

}