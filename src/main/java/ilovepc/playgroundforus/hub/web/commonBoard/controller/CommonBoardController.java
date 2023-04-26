package ilovepc.playgroundforus.hub.web.commonBoard.controller;

import ilovepc.playgroundforus.auth.PrincipalDetails;
import ilovepc.playgroundforus.hub.web.commonBoard.service.CommonBoardService;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoardCategory;
import ilovepc.playgroundforus.member.vo.PgfuMemberUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping(value = "/hub/commonBoard")
@RequiredArgsConstructor
public class CommonBoardController {
    private final CommonBoardService commonBoardService;

    /********************************************************************************************** 
     * @Method 설명 : 글목록 페이지 반환
     * @작성일 : 2023-04-17 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    @GetMapping(value = "")
    public String index(){
        return "pages/hub/commonBoard";
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 글쓰기 페이지 반환
     * @작성일 : 2023-04-17 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    @GetMapping(value = "/writePage")
    public String writePage(Model model, @AuthenticationPrincipal PrincipalDetails principalDetail){
        List<PgfuBoardCategory> pgfuBoardCategorys = commonBoardService.getCommonBoardCategoryAll();
        model.addAttribute("pgfuBoardCategory",pgfuBoardCategorys);
        return "pages/hub/commonBoardWritePage";
    }
}
