export default class Password{
    constructor(props) {
        this.$passwdInput = props.$passwdInput;
        this.$passwdGuideWrapper = props.$passwdGuideWrapper;
        if(this.$passwdGuideWrapper){
            this.setValidGuideDom();
        }

        //정규식 모음
        this.regExp = {
            email: new RegExp('[a-z0-9]+@[a-z]+\.[a-z]{2,3}'), //이메일
            //최소 8 자, 하나 이상의 문자, 하나의 숫자 및 하나의 특수 문자 정규식
            blank : new RegExp(/\s/), //공백
            chars : new RegExp(/[a-zA-Z]/),//a~z, A~Z 사이의 모든 문자)
            num: new RegExp(/[0-9]/), //숫자
        }

    }

    /**********************************************************************************************
     * @Method 설명 : 패스워드 검증
     * @작성일 : 2023-04-07
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    validate($target, $$passwdGuideWrapper){
        if(!this.$passwdGuideWrapper){
            this.$passwdGuideWrapper = $$passwdGuideWrapper;
            this.setValidGuideDom();
        }
        const val = $target.value;
        const specialRule = /[`~!@#$%^&*|\\\'\";:\/?]/gi;
        let valid=true;
        //공백검증
        if(this.regExp.blank.test(val)){ //공백 포함
            valid = false;
        }
        if(val.length < 5){ //글자 수 5 미만
            this.$fivePasswd.classList.remove('valid-passwd');
            valid = false;
        }else{
            this.$fivePasswd.classList.add('valid-passwd');
        }
        if(!specialRule.test(val)){ //특수문자 미포함
            this.$specialPasswd.classList.remove('valid-passwd');
            valid = false;
        }else{ //특수문자 포함
            this.$specialPasswd.classList.add('valid-passwd');
        }
        if(!this.regExp.num.test(val)){//숫자미포함
            this.$numPasswd.classList.remove('valid-passwd');
            valid = false;
        }else{ //숫자포함
            this.$numPasswd.classList.add('valid-passwd');
        }
        if(!this.regExp.chars.test(val)){ //문자미포함
            this.$charPasswd.classList.remove('valid-passwd');
            valid = false;
        }else{ //문자포함
            this.$charPasswd.classList.add('valid-passwd');
        }

        if(valid)this.validate.password = true;
        else this.validate.password = false;
        return valid;
    }

    /********************************************************************************************** 
     * @Method 설명 : 패스워드 검증 안내글 DOM 설정 (최소 5글자 이상, 하나 이상의 문자 포함... 등등)
     * @작성일 : 2023-04-08 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    setValidGuideDom(){
        if(this.$passwdGuideWrapper){
            this.$fivePasswd = this.$passwdGuideWrapper.querySelector(".five-passwd");
            this.$charPasswd=this.$passwdGuideWrapper.querySelector(".char-passwd");
            this.$numPasswd=this.$passwdGuideWrapper.querySelector(".num-passwd");
            this.$specialPasswd=this.$passwdGuideWrapper.querySelector(".speical-passwd");
        }
    }

}