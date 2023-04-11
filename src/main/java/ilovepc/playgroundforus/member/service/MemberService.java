package ilovepc.playgroundforus.member.service;

import ilovepc.playgroundforus.base.GeneralException;
import ilovepc.playgroundforus.base.constant.Code;
import ilovepc.playgroundforus.base.response.DataResponseDto;
import ilovepc.playgroundforus.member.repository.AuthenticationMapper;
import ilovepc.playgroundforus.member.repository.MemberMapper;
import ilovepc.playgroundforus.member.repository.ProfileMapper;
import ilovepc.playgroundforus.member.vo.PgfuAuthentication;
import ilovepc.playgroundforus.member.vo.PgfuMemberUser;
import ilovepc.playgroundforus.member.vo.PgfuProfile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

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
    private final ProfileMapper profileMapper;
    private final AuthenticationMapper authenticationMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    /**********************************************************************************************
     * @Method 설명 : 회원가입
     * @작성일 : 2023-04-03
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public void registerMember(PgfuMemberUser pgfuMemberUser){
        String rawPasswd = pgfuMemberUser.getPgfuAuthPassword().getPassword();
        String encPasswd = passwordEncoder.encode(rawPasswd);
        pgfuMemberUser.getPgfuAuthPassword().setPassword(encPasswd);

        List<Object> sqlResult = memberMapper.registerMemberIns(pgfuMemberUser);
        log.error("{}",sqlResult);
        log.error("{}--->{}", rawPasswd, encPasswd);
    }


    /**********************************************************************************************
     * @Method 설명 : 회원 정보 반환 - 아이디 검색
     * @작성일 : 2023-04-03
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public DataResponseDto<PgfuMemberUser> getUserWithId(String userId) throws GeneralException {
        PgfuMemberUser pgfuMemberUser = memberMapper.getUserWithUserId(userId);
        if(pgfuMemberUser==null){
            throw new GeneralException(Code.NOT_FOUND, "pgfuMember Not Found");
        }
        return DataResponseDto.of(pgfuMemberUser);
    }

    /**********************************************************************************************
     * @Method 설명 : 회원 아이디 중복 체크
     * @작성일 : 2023-04-04
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public int dupleChkUserWithUserId(String userId){
        PgfuMemberUser pgfuMemberUser = memberMapper.getUserWithUserId(userId);
        if(pgfuMemberUser == null){ return 1; }
        return 0;
    }

    /**********************************************************************************************
     * @Method 설명 : 닉네임 중복체크
     * @작성일 : 2023-04-05
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public int dupleChkUserWithUserNick(String userNick)throws GeneralException{
        PgfuProfile pgfuProfile = Optional.ofNullable(profileMapper.getProfileWithNickname(userNick)).orElse(null);
        if(pgfuProfile == null){ return 1; }
        return 0;
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 이메일 중복 체크
     * @작성일 : 2023-04-06 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public int dupleChkUserWithEmail(String email){
        PgfuAuthentication pgfuAuthentication = Optional.ofNullable(authenticationMapper.getUserAuthenticationWithEmail(email)).orElse(null);
        if(pgfuAuthentication==null){return 1;}
        return 0;
    }


}
