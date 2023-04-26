package ilovepc.playgroundforus.hub.web.commonBoard.controller;

import ilovepc.playgroundforus.auth.PrincipalDetails;
import ilovepc.playgroundforus.base.response.DataResponseDto;
import ilovepc.playgroundforus.hub.web.commonBoard.service.CommonBoardService;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoard;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoardSaveResult;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/hub/commonboard/rest")
public class CommonBoardRestController {
    private final CommonBoardService commonBoardService;

    /**********************************************************************************************
     * @Method 설명 : 게시글 저장
     * @작성일 : 2023-04-18
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    @PostMapping(value = "/post")
    public DataResponseDto<PgfuBoardSaveResult> saveCommonBoardPost(@RequestBody PgfuBoard pgfuBoard, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        //TODO 임시로 해놓은 비로그인 로직
        if(principalDetails != null){
            int userNo = principalDetails.getPgfuMemberUser().getUserNo();
            pgfuBoard.setBoardWriter(userNo);
        }else{
            pgfuBoard.setBoardWriter(1);
        }

        PgfuBoardSaveResult pgfuBoardSaveResult = null;
        try{
            pgfuBoardSaveResult = commonBoardService.commonBoardPostIns(pgfuBoard);
        }catch(Exception e){
            pgfuBoardSaveResult = new PgfuBoardSaveResult();
            pgfuBoardSaveResult.setSuccess(false);
            pgfuBoardSaveResult.setPgfuBoard(pgfuBoard);
            pgfuBoardSaveResult.setErrorMessage(e.getMessage());
        }
        return DataResponseDto.of(pgfuBoardSaveResult);
    }
}
