package ilovepc.playgroundforus.api.board.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/free_board")
public class BoardController {
    /**********************************************************************************************
     * @Method 설명 : 자유게시판 리스트 가져오기
     * @작성일 : 2023-05-26
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    @GetMapping("/posts")
    public void getFreeBoardList(){

    }



}
