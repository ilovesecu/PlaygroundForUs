package ilovepc.playgroundforus.member;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/member")
public class MemberController {

    @GetMapping(value = "/registerForm")
    public String registerForm(){
        return "pages/member/register";
    }

}
