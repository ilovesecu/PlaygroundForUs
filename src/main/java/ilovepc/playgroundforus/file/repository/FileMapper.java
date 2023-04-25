package ilovepc.playgroundforus.file.repository;

import ilovepc.playgroundforus.file.vo.EimVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FileMapper {

    @Insert("INSERT INTO pgfu_storage.editor_image(board_id,user_no,eim_origin_name,eim_file_name,eim_file_size,eim_width,eim_height,eim_ext_type,eim_ip) " +
            "VALUES (#{boardId},#{userNo},#{eimOriginName},#{eimFileName},#{eimFileSize},#{eimWidth},#{eimHeight},#{eimExtType},#{eimIp})")
    int editorImageIns(EimVO eimVO);
}
