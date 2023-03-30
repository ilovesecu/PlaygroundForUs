document.addEventListener('DOMContentLoaded',()=>{
    new CommonBoardWriter();
})
class CommonBoardWriter{
    constructor() {
        this.doms = {
            $tagInput:document.querySelector("#tagInput"),
            $tagInputBtn:document.querySelector("#tagInputBtn"),
            $tagStore:document.querySelector("#tagStore"),
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

            this.data.tag.push(value);
            this.appendTag(value);

            $input.value = '';
        });
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 태그 VIEW 삽입
     * @작성일 : 2023-03-30 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    appendTag(value){
        const randIndex = Math.floor(Math.random() * this.CONSTANT.BADGE_COLOR_CLASS.length);
        const color = this.CONSTANT.BADGE_COLOR_CLASS[randIndex];
        const $span = `<span class="badge rounded-pill ${color} admin_pop_scroll">${value}</span>`;
        this.doms.$tagStore.innerHTML+= $span;
    }
    
    

}