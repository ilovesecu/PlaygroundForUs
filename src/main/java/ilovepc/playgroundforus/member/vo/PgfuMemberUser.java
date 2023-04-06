package ilovepc.playgroundforus.member.vo;

import lombok.Data;

@Data
public class PgfuMemberUser {
    private int userNo;
    private String userId;
    private int loginType;
    private String joinDate;
}
