package ilovepc.playgroundforus.hub.web.commonBoard.service;

import ilovepc.playgroundforus.hub.web.commonBoard.repository.CommonBoradMapper;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoard;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoardCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonBoardService {
    private final CommonBoradMapper commonBoradMapper;

    /**********************************************************************************************
     * @Method 설명 : 카테고리 조회
     * @작성일 : 2023-04-17
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public List<PgfuBoardCategory> getCommonBoardCategoryAll(){
        List<PgfuBoardCategory> pgfuBoardCategorys = commonBoradMapper.getCommonBoardCategoryAll();
        return pgfuBoardCategorys;
    }

    /**********************************************************************************************
     * @Method 설명 : 글쓰기 - 저장
     * @작성일 : 2023-04-17
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public void saveCommonBoardPost(PgfuBoard pgfuBoard){
        //int result = commonBoradMapper.saveCommomBoard(pgfuBoard);
        //log.error("{}",result);
        log.error("pgfuBoard:{}",pgfuBoard);
    }

}
