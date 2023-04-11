import TemplateLoader from "./templateLoader.js";

/**********************************************************************************************
 * @Method 설명 : alert
 * @작성일 : 2023-04-10 
 * @작성자 : 정승주
 * @변경이력 : 
 **********************************************************************************************/
window.alert = function (param) {
    if($("#alertLayer").length !== 0)return ;

    const data = {
        title: param.title,
        content : param.content,
        actionName : param.actionName,
    }
    //스타트 콜백 있으면 실행
    const startCallback = param.startCallback;
    if(startCallback) startCallback();

    //TEMPLATE LOAD
    const loader = new TemplateLoader();
    const commonAlert = loader.commonAlert; //getter

    //TEMPLATE BIND
    const bindTemplate = Handlebars.compile(commonAlert); //bindTempalte는 메서드임!
    const resultHtml = bindTemplate(data);
    $("body").append(resultHtml);

    //TEMPLATE SHOW
    const $modal = document.querySelector(".modal");
    $modal.style.display='block';
    $modal.classList.add('show');

    document.querySelector("#closeAlertBtn").addEventListener('click',e=>{
        $modal.classList.remove('show');
        setTimeout(()=>{
            $("#alertLayer").remove();
            //클로즈 콜백 있으면 실행
            if(param.closeCallback) param.closeCallback(); 
        },151);
    });
    

}

