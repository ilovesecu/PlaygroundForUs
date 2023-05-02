package ilovepc.playgroundforus.member.controller;

import ilovepc.playgroundforus.auth.PrincipalDetails;
import ilovepc.playgroundforus.member.repository.MemberMapper;
import ilovepc.playgroundforus.member.vo.PgfuMemberUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    //프로필 보기 + 수정 폼
    @GetMapping(value = "/profileForm")
    public String profileForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        PgfuMemberUser member = principalDetails.getPgfuMemberUser();
        model.addAttribute("member",member);
        return "pages/member/profile";
    }
}
