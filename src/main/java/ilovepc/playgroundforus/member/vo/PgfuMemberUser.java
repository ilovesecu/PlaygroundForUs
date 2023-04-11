package ilovepc.playgroundforus.member.vo;

import lombok.Data;

@Data
public class PgfuMemberUser {
    private int userNo;
    private String userId;
    private int loginType;
    private String joinDate;

    //컴포지션
    private PgfuProfile pgfuProfile;
    private PgfuAuthentication pgfuAuthentication;
    private PgfuAuthPassword pgfuAuthPassword;
}
