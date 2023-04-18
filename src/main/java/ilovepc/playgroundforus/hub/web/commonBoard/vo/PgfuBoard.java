package ilovepc.playgroundforus.hub.web.commonBoard.vo;

import lombok.Data;

import java.util.List;

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

    private String pgfuBoardTagValue; // ,로 구분된 tag value
    //컴포지션
    private PgfuBoardCategory pgfuBoardCategory;
    private List<PgfuBoardTag> pgfuBoardTags;
}

