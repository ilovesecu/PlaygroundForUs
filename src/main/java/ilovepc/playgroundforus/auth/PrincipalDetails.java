package ilovepc.playgroundforus.auth;

import ilovepc.playgroundforus.member.vo.PgfuMemberUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
public class PrincipalDetails implements UserDetails {
    private PgfuMemberUser pgfuMemberUser; //컴포지션

    public PrincipalDetails(PgfuMemberUser pgfuMemberUser){
        this.pgfuMemberUser = pgfuMemberUser;
    }

    //해당 User의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return null;
            }
        });
        return null;
    }

    @Override
    public String getPassword() {
        return pgfuMemberUser.getPgfuAuthPassword().getPasswords();
    }

    @Override
    public String getUsername() {
        return pgfuMemberUser.getUserId();
    }

    @Override //계정이 만료 안되었니?
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override //계정이 안잠겼니?
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override  //비밀번호기간이 안지났니?
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override //계정이 활성화 되어있니?
    public boolean isEnabled() {
        //사이트에서 1년 동안 로그인하지 않으면 휴면으로 하려고 한다면 (물론 User객체에 최종 로그인 시간이 있어야함)
        //현재시간 - 최종로그인시간 = 1년초과 시 return false; 하면 된다.
        return true;
    }
}
