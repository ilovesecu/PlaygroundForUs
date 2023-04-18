package ilovepc.playgroundforus.hub.web.commonBoard.repository;

import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoard;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoardCategory;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommonBoradMapper {
    @Select("SELECT * FROM pgfu_common_board.board_category ORDER BY category_order ASC")
    List<PgfuBoardCategory> getCommonBoardCategoryAll();

    @Insert("INSERT INTO pgfu_common_board.board(board_title,board_writer,board_content)VALUES(#{boardTitle},#{boardWriter},#{boardContent})")
    @Options(useGeneratedKeys = true, keyProperty = "boardId")
    int saveCommomBoard(PgfuBoard pgfuBoard);

    //@Insert("INSERT INTO pgfu_common_board.tag(tag_value) VALUES (#{})")
}
