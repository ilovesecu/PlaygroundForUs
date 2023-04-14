package ilovepc.playgroundforus.config;

import ilovepc.playgroundforus.auth.handler.LoginSuccessHandler;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //활성화(스프링 시큐리티 필터(SecurityConfig)가 스프링 필터체인(기본필터체인)에 등록이 된다.)
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.authorizeHttpRequests(
                (authz) -> {
                    try {
                        authz
                            .requestMatchers("/user/**").authenticated()  //인증이 필요해
                            .requestMatchers("/hub/**").authenticated()  //인증이 필요해
                            .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN") //인증 뿐 아니라 역할도 맞아야해.
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .anyRequest().permitAll() //user, manager, admin이 아닌 주소는 모두 허용
                            .and().formLogin().loginPage("/member/loginForm")
                                .usernameParameter("userId") //유저아이디 파라미터 이름 설정
                                .passwordParameter("userPassword") //비밀번호 파라미터 이름 설정
                                .loginProcessingUrl("/auth/loginProc") //스프링 시큐리티가 해당 주소로 오는 로그인을 가로채서 대신 로그인해준다.
                                .successHandler(new LoginSuccessHandler("/hub/commonboard")) //성공 시 핸들러
                                //.failureHandler(customAuthenticationFailureHandler); //실패 시 핸들러
                                //.defaultSuccessUrl("/hub/commonboard") //로그인 성공일 시 /로 이동
                                ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).httpBasic(Customizer.withDefaults());
        return http.build();
    }

    //▼ public BCryptPasswordEncoder encoderPwd() { return new BCryptPasswordEncoder(); }
    //https://jhkimmm.tistory.com/m/24 솔트값 관련 좋은정보
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //authenticationManagerBean 대체
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

    // js, css, image 설정은 보안 설정의 영향 밖에 있도록 만들어주는 설정. [static 폴더 내의 파일]
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


}
