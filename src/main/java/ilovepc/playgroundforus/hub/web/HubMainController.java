package ilovepc.playgroundforus.hub.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/hub")
public class HubMainController {
    @GetMapping(value = "/commonboard")
    public String index(){
        return "pages/hub/commonBoard";
    }

    @GetMapping(value = "/writePage")
    public String writePage(){
        return "pages/hub/commonBoardWritePage";
    }
}
