package ilovepc.playgroundforus.hub.web.commonBoard.repository;

import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoard;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoardCategory;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoardTag;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CommonBoardMapper {
    @Select("SELECT * FROM pgfu_common_board.board_category ORDER BY category_order ASC")
    List<PgfuBoardCategory> getCommonBoardCategoryAll();

    @Insert("INSERT INTO pgfu_common_board.board(board_title,category_id,board_writer,board_content)VALUES(#{boardTitle},#{pgfuBoardCategory.categoryId},#{boardWriter},#{boardContent})")
    @Options(useGeneratedKeys = true, keyProperty = "boardId")
    int commomBoardIns(PgfuBoard pgfuBoard);

    // INSERT IGNORE로 이미 있는 것 무시하여 multi row insert
    int commonBoardTagMultiIns(List<PgfuBoardTag> pgfuBoardTag);

    // tag value로 tag table 전체조회 (멀티셀)
    List<PgfuBoardTag> commonBoardTagMultiSelWithTagVal(List<PgfuBoardTag> pgfuBoardTags);

    // tag map multi row insert
    int commonBoardTagMapMultiRowIns(@Param(value = "pgfuBoardTags") List<PgfuBoardTag> pgfuBoardTags, @Param(value = "boardId")int boardId);

    // 게시글 조회 - 검색조건에 따라 파라미터 추가
    List<PgfuBoard> commonBoardMainSel();
}
