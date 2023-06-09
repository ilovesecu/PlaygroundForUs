package ilovepc.playgroundforus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@SpringBootApplication
public class PlaygroundForUsApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlaygroundForUsApplication.class, args);
    }

    @Bean
    public WebMvcConfigurer corsConfigurer(){
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                //React용 API에 localhost:3000에 대한 CrossOrigin 해제 (no cors)
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowCredentials(true);
            }
        };
    }
}
