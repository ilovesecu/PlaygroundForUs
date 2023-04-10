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
    validate($target, $passwordInputValid, $$passwdGuideWrapper){
        if(!this.$passwdGuideWrapper){
            this.$passwdGuideWrapper = $$passwdGuideWrapper;
            this.setValidGuideDom();
        }
        const val = $target.value; //사용자가 입력한 패스워드
        const specialRule = /[`~!@#$%^&*|\\\'\";:\/?]/gi; //특수문자 정규식
        const resultObj = {};
        resultObj.valid=true;
        resultObj.validTxt = '사용가능한 패스워드 입니다.';
        //공백검증
        if(this.regExp.blank.test(val)){ //공백 포함
            resultObj.valid = false;
            resultObj.validTxt = '패스워드에 공백을 사용할 수 없습니다.';
            //this.setInputValidDirect($target,$passwordInputValid,false,'');
        }
        if(val.length < 5){ //글자 수 5 미만
            this.$fivePasswd.classList.remove('valid-passwd');
            resultObj.validTxt = '다섯 글자 이상 입력해주세요.';
            resultObj.valid = false;
        }else{
            this.$fivePasswd.classList.add('valid-passwd');
        }
        if(!specialRule.test(val)){ //특수문자 미포함
            this.$specialPasswd.classList.remove('valid-passwd');
            resultObj.validTxt = '최소 1개의 특수문자가 포함되어야 합니다.';
            resultObj.valid = false;
        }else{ //특수문자 포함
            this.$specialPasswd.classList.add('valid-passwd');
        }
        if(!this.regExp.num.test(val)){//숫자미포함
            this.$numPasswd.classList.remove('valid-passwd');
            resultObj.validTxt = '최소 1개의 숫자가 포함되어야 합니다.';
            resultObj.valid = false;
        }else{ //숫자포함
            this.$numPasswd.classList.add('valid-passwd');
        }
        if(!this.regExp.chars.test(val)){ //문자미포함
            this.$charPasswd.classList.remove('valid-passwd');
            resultObj.validTxt = '최소 1개의 문자가 포함되어야 합니다.';
            resultObj.valid = false;
        }else{ //문자포함
            this.$charPasswd.classList.add('valid-passwd');
        }

        return resultObj;
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