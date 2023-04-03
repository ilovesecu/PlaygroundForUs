package ilovepc.playgroundforus.member.vo;

import lombok.Data;

@Data
public class PgfuAuthPassword {
    private int passwordId;
    private int userNo;
    String salt;
    String password;
    String updateDate;
}
