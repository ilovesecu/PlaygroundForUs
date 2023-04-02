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

    private final MemberMapper mapper;

    @GetMapping(value = "/registerForm")
    public String registerForm(){
        mapper.testSel();
        return "pages/member/register";
    }

}
