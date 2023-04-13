export default class TemplateLoader{
    constructor() {
    }
    
    //알러트용으로 쓰는 모달
    get commonAlert(){
        return `
           <div id="alertLayer">
                <div class="pop__bg"></div>
                <div class="modal fade" id="{{ alertId }}" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
                  <div class="modal-dialog">
                    <div class="modal-content">
                      <div class="modal-header">
                        <h5 class="modal-title" id="exampleModalLabel">{{ title }}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                      </div>
                      <div class="modal-body">
                        {{ content }}
                      </div>
                      <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" id="closeAlertBtn">{{ actionName }}</button>
                      </div>
                    </div>
                  </div>
                </div>       
            </div>
        `;
    }
    
    //이미지 1개 들어감!
    get simpleImageModal(){
        return `
        <div id="modalLayer">
            <div class="pop__bg"></div>
            <div class="modal fade" id="{{ modalId }}" style="display: block;">
              <div class="modal-dialog">
                <div class="modal-content">
                  <!-- Modal body -->
                  <div class="modal-body text-center">
                    <img src="{{ imgSrc }}" width="100%"/>
                    {{ content  }}
                    </div>
                    <div class="modal-footer">
                       <div class="d-grid gap-2 col-6 mx-auto">
                          <button class="btn btn-primary" type="button" data-pos="action">{{ actionName }}</button>
                        </div>
                    </div>
                </div>
              </div>
            </div>
        </div>
        `;
    }

    //버튼 2개달린 모달 
    get modal(){
        return `
        <div id="modalLayer">
            <div class="pop__bg"></div>
            <div class="modal fade" id="{{ modalId }}" tabindex="-1" aria-modal="true" role="dialog">
              <div class="modal-dialog modal-dialog-centered modal-dialog-scrollable">
                <div class="modal-content">
                  <div class="modal-header">
                    <h5 class="modal-title" id="">{{ title }}</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close" data-pos="close"></button>
                  </div>
                  <div class="modal-body">
                    {{ content }}
                  </div>
                  <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal" data-pos="right">{{ rightBtn }}</button>
                    <button type="button" class="btn btn-primary" data-pos="left">{{ leftBtn }}</button>
                  </div>
                </div>
              </div>
            </div>
        </div>
        `;
    }

}