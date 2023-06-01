package ilovepc.playgroundforus.api.gallery.vo;

import lombok.Data;

@Data
public class Criteria {
    private int page;
    private int amount;
    private String type;
    private String keyword;
}
