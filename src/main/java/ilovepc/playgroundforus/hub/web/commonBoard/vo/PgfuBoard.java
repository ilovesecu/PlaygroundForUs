package ilovepc.playgroundforus.hub.web.commonBoard.vo;

import ilovepc.playgroundforus.file.vo.FileDetail;
import ilovepc.playgroundforus.file.vo.FileResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PgfuBoard {
    private int boardId;
    private int boardGroup;
    private String boardTitle;
    private int boardWriter;
    private String boardContent;
    private String regdate;
    private String updatedate;
    private String deletedate;
    private String delYn;

    private String pgfuBoardTagValue; // ,로 구분된 tag value -> 아직 미사용
    //컴포지션
    private PgfuBoardCategory pgfuBoardCategory = new PgfuBoardCategory();
    private List<PgfuBoardTag> pgfuBoardTags = new ArrayList<>();
    private List<FileDetail> pgfuBoardEditorImages = new ArrayList<>();       //업르도된 에디터 에미지
    private List<FileDetail> pgfuBoardEditorDeleteImages = new ArrayList<>(); //지워진 에디터 이미지
}

