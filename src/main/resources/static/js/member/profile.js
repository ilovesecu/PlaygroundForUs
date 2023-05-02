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
        this.doms.editProfile.addEventListener('click',e=>{
            this.doms.profileView.classList.add('test_obj');
            this.doms.profileEditView.style.display='';
            this.doms.profileEditView.classList.add('test_obj2');
            setTimeout(()=>{
                this.doms.profileView.style.display='none';
            },500);

        });
    }

}