package ilovepc.playgroundforus.member.repository;

import ilovepc.playgroundforus.member.vo.PgfuMemberUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MemberMapper {
    @Select("SELECT * FROM pgfu_member.user WHERE user_id = #{userId}")
    PgfuMemberUser getUserId(@Param(value = "userId")String userId);
}
