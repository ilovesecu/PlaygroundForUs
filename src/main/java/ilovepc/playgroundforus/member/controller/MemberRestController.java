package ilovepc.playgroundforus.member.controller;

import ilovepc.playgroundforus.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/member/rest")
public class MemberRestController {
    private final MemberService memberService;

    /********************************************************************************************** 
     * @Method 설명 : member id 중복체크
     * @작성일 : 2023-04-03 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    @GetMapping(value = "/{userId}")
    public int getId(@PathVariable(value = "id")String userId){
        return 0;
    }

}
