package ilovepc.playgroundforus.member.controller;

import ilovepc.playgroundforus.member.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/member")
public class MemberController {

    @GetMapping(value = "/registerForm")
    public String registerForm(){
        return "pages/member/register";
    }

    @GetMapping(value = "/loginForm")
    public String loginForm(){
        return "pages/member/login";
    }
}
