package ilovepc.playgroundforus.member.controller;

import ilovepc.playgroundforus.member.repository.MemberMapper;
import jakarta.servlet.http.HttpServletRequest;
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
    public String loginForm(HttpServletRequest req){
        //로그인 버튼을 누름 → 로그인 화면 → 로그인 성공 → 로그인 버튼을 누르는 시점의 페이지로 이동
        String referrer = req.getHeader("Referer");
        req.getSession().setAttribute("prev",referrer);
        return "pages/member/login";
    }
}
