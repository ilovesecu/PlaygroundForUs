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
import ilovepc.playgroundforus.utils.DBHelper;
import ilovepc.playgroundforus.utils.ParamHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

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
    public DataResponseDto<PgfuMemberUser> registerMember(PgfuMemberUser pgfuMemberUser){
        PgfuMemberUser registeredUser = null;
        String responseMessage = "fail";
        if(!this.memberRegisterParamExceptProc(pgfuMemberUser)){ //필수값들이 있는지 체크
            String reason = "Essential parameter must be not null";
            return DataResponseDto.of(registeredUser, responseMessage, reason);
        }
        if(!this.memberRegisterParamExceptProcValue(pgfuMemberUser)){ //검증해야할 값들이 제대로 들어가있는지 검증
            String reason = "Parameter value exception check is fail";
            return DataResponseDto.of(registeredUser, responseMessage, reason);
        }
        //중복 예외처리
        //아이디 중복체크
        if(dupleChkUserWithUserId(pgfuMemberUser.getUserId())!=1) {
            String reason = "아이디가 중복되었습니다";
            return DataResponseDto.of(registeredUser, responseMessage, reason);
        }
        if(dupleChkUserWithUserNick(pgfuMemberUser.getPgfuProfile().getNickname())!=1) {
            String reason = "닉네임이 중복되었습니다";
            return DataResponseDto.of(registeredUser, responseMessage, reason);
        }
        if(dupleChkUserWithEmail(pgfuMemberUser.getPgfuAuthentication().getEmail())!=1) {
            String reason = "이메일이 중복되었습니다";
            return DataResponseDto.of(registeredUser, responseMessage, reason);
        }

        String rawPasswd = pgfuMemberUser.getPgfuAuthPassword().getPassword();
        String encPasswd = passwordEncoder.encode(rawPasswd);
        pgfuMemberUser.getPgfuAuthPassword().setPassword(encPasswd);

        List<Object> sqlResult = memberMapper.registerMemberIns(pgfuMemberUser);
        int pReturn = DBHelper.getData(sqlResult, Integer.class);

        if(pReturn > 0){
            registeredUser = DBHelper.getData(sqlResult, PgfuMemberUser.class);
            responseMessage = "success";
        }
        return DataResponseDto.of(registeredUser, responseMessage);
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

    /********************************************************************************************** 
     * @Method 설명 : 멤버가입 필수 값 예외처리 - Null check
     * @작성일 : 2023-04-12 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    private boolean memberRegisterParamExceptProc(PgfuMemberUser pgfuMemberUser){
        //null 있는지 확인
        boolean normalFieldNullChk = ParamHelper.nullExcept(pgfuMemberUser,new String[]{"userId"});
        Object[] mokObjs = new Object[]{pgfuMemberUser.getPgfuProfile(), pgfuMemberUser.getPgfuAuthentication(), pgfuMemberUser.getPgfuAuthPassword()};
        String[] fieldNames = new String[]{
            "pgfuProfile.nickname",
            "pgfuProfile.introduction",
            "pgfuAuthentication.email",
            "pgfuAuthPassword.password"
        };
        boolean nestedClassNullChk = ParamHelper.nestedParamExcep(mokObjs,fieldNames);
        log.error("normalFieldNullChk : {} / nestedClassNullChk : {}",normalFieldNullChk,nestedClassNullChk);
        return normalFieldNullChk && nestedClassNullChk;
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 멤버가입 값 예외처리 - Value Check
     * @작성일 : 2023-04-12 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    private boolean memberRegisterParamExceptProcValue(PgfuMemberUser pgfuMemberUser){
        String engNumRegex = "^[a-zA-Z0-9]*$";
        String emailRegex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";

        boolean userIdChk = Pattern.matches(engNumRegex,pgfuMemberUser.getUserId());
        boolean emailChk = Pattern.matches(emailRegex, pgfuMemberUser.getPgfuAuthentication().getEmail());
        boolean userNicknameChk = Pattern.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*", pgfuMemberUser.getPgfuProfile().getNickname());
        boolean passwdChk = this.passwordsValidate(pgfuMemberUser.getPgfuAuthPassword().getPassword());

        return userIdChk && emailChk && userNicknameChk && passwdChk;
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 멤버 패스워드 값 검증 
     * @작성일 : 2023-04-12 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public boolean passwordsValidate(String rawPassword){
        boolean speical = !Pattern.matches("[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힝]*", rawPassword); //특수문자 포함
        boolean leastNum = false;//하나 이상의 숫자
        boolean leastChar = false;//하나 이상의 문자 (알파벳)
        boolean notBlank = true;//공백미포함
        boolean length = false; //글자수 5자 이상

        if(rawPassword.length() >= 5){
            length = true;
        }
        for(int i=0; i<rawPassword.length(); i++){
            char ch = rawPassword.charAt(i);
            if((ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')){
                leastChar = true;
            }else if(Character.isDigit(ch)){
                leastNum = true;
            }else if(ch == ' '){
                notBlank = false;
            }
        }
        return speical && leastChar && leastNum && notBlank && length;
    }

}
