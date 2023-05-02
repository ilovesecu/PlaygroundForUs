document.addEventListener('DOMContentLoaded', ()=>{
   new Profile();
});

class Profile{
    constructor() {
        this.doms = {
            editProfile : document.querySelector("#editProfile"),
            profileView : document.querySelector("#profileView"),
            profileEditView: document.querySelector("#profileEditView"),
        }
        this.eventBinding();
    }

    eventBinding(){
        //프로필 수정 폼 전환 애니메이션
        this.doms.editProfile.addEventListener('click',e=>{
            this.doms.profileView.classList.add('view');
            setTimeout(()=>{
                this.doms.profileView.style.display='none';
            },500);
            this.doms.profileEditView.style.display='flex';
            this.doms.profileEditView.classList.add('editView');
            
        });
    }

}