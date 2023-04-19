document.addEventListener('DOMContentLoaded',()=>{
    new CommonBoardWriter();
})
class CommonBoardWriter{
    constructor() {

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
        }
        this.eventBinding();
    }

    eventBinding(){
        //Tag 추가
        this.doms.$tagInputBtn.addEventListener('click',e=>{
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
            const title = this.doms.$boardTitle.value; //title
            const selctVal = this.doms.$categorySelector.options[this.doms.$categorySelector.selectedIndex].value;//category
            const content = document.querySelector(".note-editable").innerHTML;//content
            console.log(this.data.tag);
            const param = {
                boardTitle: title,
                boardContent: content,
                pgfuBoardCategory:{
                    categoryId : selctVal
                },
                pgfuBoardTags : this.data.tag
            }
            const response = await axios.post('/hub/commonboard/rest/post',param)

            //files
        });
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
    

}