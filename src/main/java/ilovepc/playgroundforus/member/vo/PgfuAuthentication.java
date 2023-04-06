package ilovepc.playgroundforus.member.vo;

import lombok.Data;

@Data
public class PgfuAuthentication {
    private int authenticationId;
    private int userNo;
    private int getherAgree;        //'개인정보 수집동의 0: reject, 1: accept'
    private String cellPhone;
    private String email;
    private String birthday;
    private int sex;         //'0:m, 1:f, 2:etc'
}
