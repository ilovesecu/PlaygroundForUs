package ilovepc.playgroundforus.auth.handler;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    public LoginSuccessHandler(String defaultTargetUrl){
        setDefaultTargetUrl(defaultTargetUrl);
    }


    /*
        https://codevang.tistory.com/269
    */
    /**********************************************************************************************
     * @Method 설명 : 인증 성공 시
     * @작성일 : 2023-04-14
     * @작성자 : 정승주
     * @변경이력 : filterChain 파라미터 있는걸로 Override했더니 이상함..
     **********************************************************************************************/
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String redirecUrl = "/"; //디폴트 URI - 즐겨찾기, 주소창에 바로 로그인창 띄워서 들어오는 경우 디폴트로 보내준다.

        //1. 접근 권한이 없는 페이지 요청 → 로그인 화면(인터셉트) → 로그인 성공 → 처음 요청한 페이지로 이동
        // Security가 요청을 가로챈 경우 사용자가 원래 요청했던 URI 정보를 저장한 객체
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest = requestCache.getRequest(request, response); //SavedRequest save = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");

        //2. 로그인 버튼을 누름 → 로그인 화면 → 로그인 성공 → 로그인 버튼을 누르는 시점의 페이지로 이동
        HttpSession session = request.getSession();
        String prev = (String) session.getAttribute("prev");

        if(savedRequest != null){
            redirecUrl = savedRequest.getRedirectUrl();
            requestCache.removeRequest(request,response);//세션에서 삭제
        }else if(prev != null && !prev.equals("")){
            redirecUrl = prev;
            session.removeAttribute("prev");
        }

        //-> https://nyximos.tistory.com/115
        //getRedirectStrategy().sendRedirect(request,response, redirecUrl); // -> Ajax로 보낼거니까 이렇게하면 HTML이 반환되어버려서 login.js에서 처리를 못함..
        response.setStatus(HttpServletResponse.SC_OK);
        PrintWriter writer = response.getWriter();
        writer.write(redirecUrl);
        writer.flush();
        writer.close();

        //super.onAuthenticationSuccess(request, response, authentication); //부모를 호출하면 기본적으로 HTML이 반환되어버리는듯.. Ajax라서 데이터가 반환되어야함.
    }
}
