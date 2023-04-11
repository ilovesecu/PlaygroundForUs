package ilovepc.playgroundforus.member.repository;

import ilovepc.playgroundforus.member.vo.PgfuMemberUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MemberMapper {
    @Select("SELECT * FROM pgfu_member.user WHERE user_id = #{userId}")
    PgfuMemberUser getUserWithUserId(@Param(value = "userId")String userId);

    @ResultMap({"ResultMap.integer","ResultMap.pgfuMember"})
    @Select("CALL pgfu_member.REGISTER_MEMBER_PROC(#{userId},#{pgfuProfile.nickname},#{pgfuAuthentication.email},#{pgfuAuthPassword.password},#{pgfuProfile.introduction})")
    List<Object> registerMemberIns(PgfuMemberUser pgfuMemberUser);
}
