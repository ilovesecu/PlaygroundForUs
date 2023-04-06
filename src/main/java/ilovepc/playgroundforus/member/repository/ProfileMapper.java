package ilovepc.playgroundforus.member.repository;

import ilovepc.playgroundforus.member.vo.PgfuMemberUser;
import ilovepc.playgroundforus.member.vo.PgfuProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProfileMapper {
    @Select("SELECT * FROM pgfu_member.profile WHERE nickname = #{nickname}")
    PgfuProfile getProfileWithNickname(@Param(value = "nickname")String nickname);


}
