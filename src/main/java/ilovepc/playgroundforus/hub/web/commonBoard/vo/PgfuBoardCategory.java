package ilovepc.playgroundforus.hub.web.commonBoard.vo;

import lombok.Data;

@Data
public class PgfuBoardCategory {
    private int categoryId;
    private int boardGroup;
    private int categoryParent;
    private String categoryValue;
    private int categoryOrder;
    private String categoryColor;
}
