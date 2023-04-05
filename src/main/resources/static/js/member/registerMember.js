document.addEventListener('DOMContentLoaded', ()=>{
    new RegisterMember();
})

class RegisterMember{
    constructor() {
        this.doms={
            $idInput : document.querySelector("#idInput"),
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
            const val = e.target.value;
            const response = await axios.get(`/member/rest/duple/${val}`);
            const result = response.data;

            let $idInputValid = this.doms.$idInputValid ?? null;
            if(!$idInputValid){
                $idInputValid = this.doms.$idInput.parentElement.querySelector("div.valid");
                this.doms.$idInputValid = $idInputValid;
            }
            const param = {
                $dom : e.target,
                $validDom : $idInputValid,
            }

            if(result.data === 1){ //중복체크 OK
                param.domClassName='is-valid',
                param.validClassName = 'valid-text';
                param.validText = '사용가능한 아이디입니다.';
            }else{ //중복체크 NO
                param.domClassName='is-invalid',
                param.validClassName = 'invalid-text';
                param.validText = '이미 존재하는 아이디입니다.';
            }
            this.inputValid(param);
        });
    }

    inputValid({$dom, $validDom, domClassName, validClassName, validText}){
        $dom.classList.remove('is-valid','is-invalid');
        $dom.classList.add(domClassName);
        if($validDom){
            $validDom.classList.remove('valid-text','invalid-text');
            $validDom.classList.add(validClassName);
            $validDom.textContent = validText;
        }
    }

}