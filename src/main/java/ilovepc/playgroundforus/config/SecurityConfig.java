package ilovepc.playgroundforus.config;

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
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.authorizeHttpRequests(
                (authz) -> {
                    try {
                        authz
                            .requestMatchers("/user/**").authenticated()
                            .requestMatchers("/manager/**").hasAnyRole("MANAGER", "ADMIN")
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .anyRequest().permitAll()
                            .and().formLogin().loginPage("/login")
                                .usernameParameter("userId") //유저아이디 파라미터 이름 설정
                                .passwordParameter("userPassword") //비밀번호 파라미터 이름 설정
                                .loginProcessingUrl("/auth/loginProc") //스프링 시큐리티가 해당 주소로 오는 로그인을 가로채서 대신 로그인해준다.
                                .defaultSuccessUrl("/") //로그인 성공일 시 /로 이동
                                ;//.failureHandler(customAuthenticationFailureHandler); //실패 시 핸들러
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        ).httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
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
