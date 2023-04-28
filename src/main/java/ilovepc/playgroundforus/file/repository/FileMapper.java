package ilovepc.playgroundforus.file.repository;

import ilovepc.playgroundforus.file.vo.EimVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO pgfu_storage.editor_image(board_id,user_no,eim_origin_name,eim_file_name,eim_file_size,eim_width,eim_height,eim_ext_type,eim_ip) " +
            "VALUES (#{boardId},#{userNo},#{eimOriginName},#{eimFileName},#{eimFileSize},#{eimWidth},#{eimHeight},#{eimExtType},#{eimIp})")
    @Options(useGeneratedKeys = true, keyProperty = "eimId")
    int editorImageIns(EimVO eimVO);


    //IN쿼리 주의, ${} 적용 주의
    //저장된 게시글에 대한 에디터 이미지들의 post_id를 업데이트 해준다.
    @Update("UPDATE pgfu_storage.editor_image SET post_id=#{postId} WHERE board_id=#{boardId} AND eim_file_name IN (${eimFileName})")
    void editorImagePostIdMultiUpdate(@Param(value = "boardId")int boardId, @Param(value = "eimFileName")String eimFileName, @Param(value = "postId")int postId);
}
