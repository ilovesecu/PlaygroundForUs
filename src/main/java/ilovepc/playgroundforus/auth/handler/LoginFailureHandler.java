package ilovepc.playgroundforus.auth.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.io.IOException;
import java.io.PrintWriter;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    
    /********************************************************************************************** 
     * @Method 설명 : 인증 실패 시
     * @작성일 : 2023-04-17 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String message = "Invalid Username or Password";
        int code = 0;
        if(exception instanceof BadCredentialsException){
            message = "Invalid Username or Password";
            code = 0;
        }else if(exception instanceof InsufficientAuthenticationException){
            message = "Invalid Secret Key";
            code = 1;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("loginError-"+code+"-"+message);
        writer.flush();
        writer.close();
    }
}
