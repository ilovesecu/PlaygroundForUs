package ilovepc.playgroundforus.hub.web.commonBoard.vo;

import lombok.Data;

@Data
public class PgfuBoard {
    private int boardId;
    private int boardGroup;
    private String boardTitle;
    private String boardWriter;
    private String boardContent;
    private String regdate;
    private String updatedate;
    private String deletedate;
    private String delYn;
}
