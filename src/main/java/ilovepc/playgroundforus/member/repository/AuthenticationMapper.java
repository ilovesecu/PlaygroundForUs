package ilovepc.playgroundforus.member.repository;

import ilovepc.playgroundforus.member.vo.PgfuAuthentication;
import ilovepc.playgroundforus.member.vo.PgfuProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthenticationMapper {
    @Select("SELECT * FROM pgfu_member.authentication WHERE email = #{email}")
    PgfuAuthentication getUserAuthenticationWithEmail(@Param(value = "email")String email);


}
