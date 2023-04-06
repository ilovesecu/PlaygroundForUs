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
        }
        this.validate={
            id:false,
            nickname:false,
            email:false,
            password:false
        }
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
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 검증 로직 함수화 
     * @작성일 : 2023-04-06 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    async procInputValid($target, url, $validateDom, validateTxt, validateObj){
        const val = target.value;
        const response = await axios.get(`/member/rest/duple_nick/${val}`);
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

}