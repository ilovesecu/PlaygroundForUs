package ilovepc.playgroundforus.member.vo;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
@Data
public class PgfuMemberUser {
    private int userNo;
    private String userId;
    private int loginType;
    private String joinDate;

    //컴포지션
    private PgfuProfile pgfuProfile = new PgfuProfile();
    private PgfuAuthentication pgfuAuthentication = new PgfuAuthentication();
    private PgfuAuthPassword pgfuAuthPassword = new PgfuAuthPassword();
}
