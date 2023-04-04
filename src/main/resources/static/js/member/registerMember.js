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
            if(result.data === 1){ //중복체크 OK

            }else{ //중복체크 NO
                this.inputValid(e.target, 'is-invalid');
            }
        });
    }

    inputValid($dom, className='is-invalid', subTxt=""){
        $dom.classList.add(className);
    }

}