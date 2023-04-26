document.addEventListener('DOMContentLoaded',()=>{
    new CommonBoardWriter();
})

class CommonBoardWriter{
    constructor() {
        this.summernoteInit();
        this.doms = {
            $tagInput:document.querySelector("#tagInput"),
            $tagInputBtn:document.querySelector("#tagInputBtn"),
            $tagStore:document.querySelector("#tagStore"),
            $savePostBtn : document.querySelector("#savePostBtn"),
            $boardTitle : document.querySelector("#boardTitle"),
            $categorySelector : document.querySelector("#categorySelector"),
            $summernote : document.querySelector("#summernote"),
        }
        this.data = {
            tag:[],
        }
        this.CONSTANT = {
            BADGE_COLOR_CLASS:['bg-primary','bg-secondary','bg-success','bg-danger','bg-warning text-dark','bg-info text-dark','bg-light text-dark','bg-dark'],
            FILE_SERVER_TYPE_EDITOR: 'hubCbSm',
            FILE_SERVER_TYPE_ATTATCH: 'hubCb',
        }
        this.eventBinding();
    }

    eventBinding(){
        //Tag 추가
        this.doms.$tagInputBtn.addEventListener('click',e=>{
            e.stopPropagation();
            const $input = this.doms.$tagInput;
            const value = $input.value;
            if(value === undefined || value === null || value ==="")return ;
            this.appendTag(value);
            $input.value = '';
        });
        //Tag 추가할 때 엔터키 사용 가능하게 하기
        this.doms.$tagInput.addEventListener('keydown', e=>{
           if(e.keyCode === 13){ //Enter
                this.doms.$tagInputBtn.dispatchEvent(new Event('click'));
           }
        });

        //글저장 버튼
        this.doms.$savePostBtn.addEventListener('click', async e=>{
            const categoryIndex = this.doms.$categorySelector.selectedIndex; //선택된 카테고리 SELECT INDEX

            const title = this.doms.$boardTitle.value.trim(); //title
            const selctVal = this.doms.$categorySelector.options[categoryIndex].value;//category
            const content = document.querySelector(".note-editable").innerHTML;//content
            debugger
            //저장 전 예외처리 검사
            if(!this.saveExceptionChk(title, categoryIndex, content))return ;

            const param = {
                boardTitle: title,
                boardContent: content,
                pgfuBoardCategory:{
                    categoryId : selctVal
                },
                pgfuBoardTags : this.data.tag
            }
            const response = await axios.post('/hub/commonboard/rest/post',param)
        });

        //태그 스토어 이벤트 버블링을 활용
        this.doms.$tagStore.addEventListener('click',e=>{
            const $target = e.target;
            if($target.classList.contains("badge")){ //태그 클릭 시 삭제
                //해당 태그가 몇 번째 태그인지 판단
                const $siblings = $target.parentNode.childNodes;
                let index = -1;
                for(let i=0; i<$siblings[i].length; i++){
                    if($siblings[i] === $target){
                        index = i;
                        break;
                    }
                }
                this.delTag($target,index);
            }
        });
    }

    /**********************************************************************************************
     * @Method 설명 : 저장 전 예외처리
     * @작성일 : 2023-04-26
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    saveExceptionChk(title, categoryIndex, content){
        let errorMessage = "";
        let closeCallback = () => {};
        let exceptValue = false;
        const noBlankTitle = title.replaceAll(/\s/g,'');    //공백제거한 제목
        const noHtmlTagContent = content.replaceAll(/<[^>]*>?/g, ''); //HTML 제거 정규식을 적용한 content

        if(noBlankTitle === null || noBlankTitle === undefined || noBlankTitle === ""){
            errorMessage = "제목을 입력해주세요.";
            closeCallback = ()=>{this.doms.$boardTitle.focus();}
            exceptValue = true;
        } else if(categoryIndex === 0){ //카테고리 미선택
            errorMessage = "카테고리를 선택해주세요..";
            closeCallback = ()=>{this.doms.$categorySelector.focus();}
            exceptValue = true;
        } else if(noHtmlTagContent === null || noHtmlTagContent === undefined || noHtmlTagContent === ""){
            errorMessage = "내용을 입력해주세요..";
            closeCallback = ()=>{$('.note-editable').trigger('focus');}
            exceptValue = true;
        }

        if(exceptValue){ //예외발생 시
            this.blankExcept(errorMessage, closeCallback);
            return false;
        }
        return true;
    }

    /**********************************************************************************************
     * @Method 설명 : blank 예외처리 공통부분 함수
     * @작성일 : 2023-04-26
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    blankExcept(content="빈 칸을 입력해주세요", closeCallback=()=>{}){
        window.alert({title:'경고', content:`${content}`, actionName:'확인', closeCallback:closeCallback});
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 태그 VIEW 삽입
     * @작성일 : 2023-03-30 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    appendTag(value){
        if(this.dupleChkTag(value)){ //중복체크 후 넣는다.
            const randIndex = Math.floor(Math.random() * this.CONSTANT.BADGE_COLOR_CLASS.length);
            const color = this.CONSTANT.BADGE_COLOR_CLASS[randIndex];
            const $span = `<span class="badge rounded-pill ${color} admin_pop_scroll">${value}</span>`;
            this.doms.$tagStore.innerHTML+= $span; //VIEW ADD
            this.data.tag.push({tagValue:value}); //DATA ADD
        }
    }

    /**********************************************************************************************
     * @Method 설명 : 태그 중복체크
     * @작성일 : 2023-04-19
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    dupleChkTag(value){
        for(let obj of this.data.tag){
            if(obj.tagValue === value){
                return false;
            }
        }
        return true;
    }

    /**********************************************************************************************
     * @Method 설명 : 태그 삭제
     * @작성일 : 2023-04-19
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    delTag($target,index = -1){
        const tagValue = $target.textContent;
        const data = this.data.tag[index];
        if(data !== null && data !== undefined && data.tagValue === tagValue){
            this.data.tag.splice(index,1);
            this.doms.$tagStore.removeChild($target);
            return ;
        }

        for(let i=0; i<this.data.tag.length; i++){
            const obj = this.data.tag[i];
            const objVal = obj?.tagValue ?? null;
            if(objVal !== null){
                if(objVal === tagValue){
                    this.data.tag.splice(i,1);
                    this.doms.$tagStore.removeChild($target);
                    break;
                }
            }
        }
    }


    /**********************************************************************************************
     * @Method 설명 : SUMMER NOTE INIT
     * @작성일 : 2023-04-26
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    summernoteInit(){
        $('#summernote').summernote({
            placeholder: '내용을 입력해주세요.',
            tabsize: 2,
            minHeight: 100,
            maxHeight: 500,
            height: 330,
            focus:false,
            callbacks: {
                onImageUpload: function(files, editor, welEditable) {
                    for (let i = files.length - 1; i >= 0; i--) {
                        //editorImageUpload(files[i], editor, welEditable);
                    }
                    editorImageUpload(files, this, welEditable);
                },
                onPaste: function (e) {
                    var clipboardData = e.originalEvent.clipboardData;
                    if (clipboardData && clipboardData.items && clipboardData.items.length) {
                        var item = clipboardData.items[0];
                        if (item.kind === 'file' && item.type.indexOf('image/') !== -1) {
                            e.preventDefault();
                        }
                    }
                }
            }
        });

        const editorImageUpload = async (files, editor, welEditable) => {
            console.log(files);
            const formData = new FormData();
            for (let i = files.length - 1; i >= 0; i--) {
                formData.append('files',files[i]);
            }
            formData.append('userNo',1); //유저번호
            formData.append('temp',1);  //임시파일로 넘긴다.

            const URL = `/storage/${this.CONSTANT.FILE_SERVER_TYPE_EDITOR}/editor`; //DOMAIN 분리때는 이것도 분리되어야할듯.
            const response = await axios.post(URL, formData);
            console.log(response);

            if(response.status === 200 || response.status === 201){
                const data = response.data;
                const fileDetails = data.fileDetails;
                for(let i=0; i<fileDetails.length; i++){
                    const code = fileDetails[i].code;
                    if(code === 100101){
                        const encFileName = fileDetails[i].encFileName;
                        const fileName = fileDetails[i].fileName;
                        const temp = fileDetails[i].temp;
                        const filePath = `/storage/${this.CONSTANT.FILE_SERVER_TYPE_EDITOR}/image/${encFileName}?temp=${temp}` //DOMAIN 분리때는 이것도 분리되어야할듯.
                        $('#summernote').summernote('insertImage', filePath);
                    }
                }
            }

        }//end of editorImageUpload function
    }
}