export default class TemplateLoader{
    constructor() {
    }

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

}