package ilovepc.playgroundforus.member.vo;

import lombok.Data;

@Data
public class PgfuProfile {
    private int profileId;
    private int userNo;
    private String nickname;
    private String imageUrl;
    private String introduction;
    private String joinDate;
    private String updateDate;
}
