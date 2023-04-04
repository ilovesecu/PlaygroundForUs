package ilovepc.playgroundforus.member.controller;

import ilovepc.playgroundforus.base.response.DataResponseDto;
import ilovepc.playgroundforus.member.service.MemberService;
import ilovepc.playgroundforus.member.vo.PgfuMemberUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/member/rest")
public class MemberRestController {
    private final MemberService memberService;

    /********************************************************************************************** 
     * @Method 설명 : member id로 member가져오기
     * @작성일 : 2023-04-03 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    @GetMapping(value = "/{userId}")
    public DataResponseDto<PgfuMemberUser> getUserWithId(@PathVariable(value = "userId")String userId){
        return memberService.getUserWithId(userId);
    }

}
