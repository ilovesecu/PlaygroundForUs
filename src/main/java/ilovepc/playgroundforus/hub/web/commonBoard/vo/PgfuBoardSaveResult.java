package ilovepc.playgroundforus.hub.web.commonBoard.vo;

import lombok.Data;

/**********************************************************************************************
 * @FileName : PgfuBoardSaveResult.java
 * @Date : 2023-04-26
 * @작성자 : 정승주
 * @설명 : 게시글 저장 / 수정 시 반환 클래스
 **********************************************************************************************/
@Data
public class PgfuBoardSaveResult {
    private boolean success=false; //실패, 성공
    private PgfuBoard pgfuBoard=new PgfuBoard();
    private String errorMessage=null;
    private String errorCause = null;
}
