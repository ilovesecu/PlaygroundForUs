package ilovepc.playgroundforus.member.service;

import ilovepc.playgroundforus.GeneralException;
import ilovepc.playgroundforus.base.constant.Code;
import ilovepc.playgroundforus.base.response.DataResponseDto;
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
     * @Method 설명 : 회원 정보 반환 - 아이디 검색
     * @작성일 : 2023-04-03
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public DataResponseDto<PgfuMemberUser> getUserWithId(String userId) throws GeneralException {
        PgfuMemberUser pgfuMemberUser = memberMapper.getUserId(userId);
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
        PgfuMemberUser pgfuMemberUser = memberMapper.getUserId(userId);
        if(pgfuMemberUser == null){
            return 1;
        }
        return 0;
    }

    /**********************************************************************************************
     * @Method 설명 : 닉네임 중복체크
     * @작성일 : 2023-04-05
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public int dupleChkUserWithUserNick(String userNick){
        return 0;
    }


}
