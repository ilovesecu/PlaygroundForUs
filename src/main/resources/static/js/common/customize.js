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
    let resultHtml = bindTemplate(data);

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

/********************************************************************************************** 
 * @Method 설명 : 왼쪽, 오른쪽 버튼있는 모달창 (포지션 : 중앙) 
 * @작성일 : 2023-04-13 
 * @작성자 : 정승주
 * @변경이력 : 
 **********************************************************************************************/
window.modal = function (param, leftAction=()=>{}, rightAction=()=>{}){
    if($("#modalLayer").length !== 0)return ;
    const data = {
        title: param.title,
        content : param.content,
        leftBtn : param.leftBtn,
        rightBtn : param.rightBtn,
    }
    const parseHtml = param.html ?? false;

    //스타트 콜백 있으면 실행
    const startCallback = param.startCallback;
    if(startCallback) startCallback();

    //TEMPLATE LOAD
    const loader = new TemplateLoader();
    const modal = loader.modal;

    //HANDLEBARS BIND
    const bindTemplate = Handlebars.compile(modal); //bindTempalte는 메서드임!
    let resultHtml = bindTemplate(data);
    resultHtml = htmlParseProc(resultHtml, parseHtml);
    $("body").append(resultHtml);

    //TEMPLATE SHOW
    const $modal = document.querySelector(".modal");
    $modal.style.display = 'block';
    setTimeout(()=>{
        $modal.classList.add('show');
    },0)

    //Click Event Set
    doubleBtnActionSettings($modal,leftAction,rightAction);
    //클로즈 콜백은 여기에
    $modal.querySelector(".btn-close").addEventListener('click',e=>{
        $modal.classList.remove('show');
        setTimeout(()=>{
            $("#modalLayer").remove();
            //클로즈 콜백 있으면 실행
            if(param.closeCallback) param.closeCallback();
        },151);
    });
}

/**********************************************************************************************
 * @Method 설명 : 이미지1개 모달 (HTML을 넘겨도 되지만 깔끔하게 미리 만들어두고 src만 넘기는걸로..)
 * @작성일 : 2023-04-13
 * @작성자 : 정승주
 * @변경이력 :
 **********************************************************************************************/
window.simpleImageModal = function (param) {
    if($("#modalLayer").length !== 0)return ;
    const data = {
        content : param.content,
        actionName: param.actionName,
        imgSrc : param.imgSrc,
    }
    const parseHtml = param.html ?? false;
    const action = param.action;

    const templateLoader = new TemplateLoader();
    const simpleImageLoader = templateLoader.simpleImageModal;

    const bindTemplate = Handlebars.compile(simpleImageLoader);
    let resultHtml = bindTemplate(data);
    resultHtml = htmlParseProc(resultHtml, parseHtml);
    $("body").append(resultHtml);

    //TEMPLATE SHOW
    const $modal = document.querySelector(".modal");
    setTimeout(()=>{
        $modal.classList.add('show');
    },0);

    singleBtnActionSettings($modal, action);

}

/**********************************************************************************************
 * @Method 설명 : 버튼 한개 달린 창 클릭 이벤트 리스터 셋팅
 * @작성일 : 2023-04-13
 * @작성자 : 정승주
 * @변경이력 :
 **********************************************************************************************/
function singleBtnActionSettings($root, action){
    $root.addEventListener('click',e=>{
        const $target = e.target;
        if($target.dataset.pos === 'action'){
            if(action) action();
        }
    });
}



/********************************************************************************************** 
 * @Method 설명 :  왼쪽 오른쪽 있는 창 클릭 이벤트 리스너 셋팅
 * @작성일 : 2023-04-13 
 * @작성자 : 정승주
 * @변경이력 : 
 **********************************************************************************************/
function doubleBtnActionSettings($root, leftAction, rightAction){
    $root.addEventListener('click', e=>{
        const $target = e.target;
        if($target.dataset.pos === 'left'){
            if(leftAction) leftAction();
        }else if($target.dataset.pos==='right'){
            if(rightAction) rightAction();
        }
    });
}

/********************************************************************************************** 
 * @Method 설명 : 본문을 HTML로 파싱
 * @작성일 : 2023-04-13 
 * @작성자 : 정승주
 * @변경이력 : 
 **********************************************************************************************/
function htmlParseProc(content, parseHtml){
    if(parseHtml){
        content = resultHtml.replaceAll(/&lt;/g, '<').replaceAll(/&gt;/g, '>').replaceAll("&quot;", "\"").replaceAll("&#x3D;","=");
    }
    return content;
}

