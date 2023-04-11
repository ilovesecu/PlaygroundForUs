package ilovepc.playgroundforus.member.controller;

import ilovepc.playgroundforus.base.response.DataResponseDto;
import ilovepc.playgroundforus.member.service.MemberService;
import ilovepc.playgroundforus.member.vo.PgfuMemberUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
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

    /**********************************************************************************************
     * @Method 설명 : userId 중복체크 (1:중복X, 0:중복O)
     * @작성일 : 2023-04-06
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    @GetMapping(value = "/duple_id/{userId}") //컨트롤 URI
    public DataResponseDto<Integer> dupleChkUserWithUserId(@PathVariable(value = "userId")String userId){
        int result = memberService.dupleChkUserWithUserId(userId);
        return DataResponseDto.of(result);
    }

    /**********************************************************************************************
     * @Method 설명 : userNickname 중복체크
     * @작성일 : 2023-04-06
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    @GetMapping(value = "/duple_nick/{userNick}") //컨트롤 URI
    public DataResponseDto<Integer> dupleChkUserNickname(@PathVariable(value = "userNick")String userNick){
        int result = memberService.dupleChkUserWithUserNick(userNick);
        return DataResponseDto.of(result);
    }

    /**********************************************************************************************
     * @Method 설명 : userEmail 중복체크
     * @작성일 : 2023-04-06
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    @GetMapping(value = "/duple_email/{userEmail}")
    public DataResponseDto<Integer> dupleChkUserEmail(@PathVariable(value = "userEmail")String userEmail){
        int result = memberService.dupleChkUserWithEmail(userEmail);
        return DataResponseDto.of(result);
    }

    /**********************************************************************************************
     * @Method 설명 : 회원가입 진행
     * @작성일 : 2023-04-11
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    @PostMapping(value = "")
    public DataResponseDto<PgfuMemberUser> registerMember(PgfuMemberUser pgfuMemberUser){
        memberService.registerMember(pgfuMemberUser);
        return null;
    }

}
