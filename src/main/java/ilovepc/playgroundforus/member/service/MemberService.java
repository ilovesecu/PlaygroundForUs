package ilovepc.playgroundforus.member.service;

import ilovepc.playgroundforus.member.repository.MemberMapper;
import ilovepc.playgroundforus.member.vo.PgfuMemberUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**********************************************************************************************
 * @FileName : MemberService.java 
 * @Date : 2023-04-03 
 * @작성자 : 정승주 
 * @설명 : 회원관련 서비스
 **********************************************************************************************/
@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;

    /**********************************************************************************************
     * @Method 설명 : 회원가입
     * @작성일 : 2023-04-03
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public void registerMember(){

    }


    /**********************************************************************************************
     * @Method 설명 : 회원 아이디 중복체크
     * @작성일 : 2023-04-03
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public int dupleChkUserId(String userId){
        PgfuMemberUser pgfuMemberUser = memberMapper.getUserId(userId);
        log.error("pgfu ==> {}", pgfuMemberUser);
        return 1;
    }

}
