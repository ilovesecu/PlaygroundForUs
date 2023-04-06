package ilovepc.playgroundforus.member.vo;

import lombok.Data;

@Data
public class PgfuAuthPassword {
    private int passwordId;
    private int userNo;
    private String salt;
    private String password;
    private String updateDate;
}
