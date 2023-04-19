package ilovepc.playgroundforus.hub.web.commonBoard.service;

import ilovepc.playgroundforus.hub.web.commonBoard.repository.CommonBoradMapper;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoard;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoardCategory;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoardTag;
import ilovepc.playgroundforus.utils.ParamHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    @Transactional(rollbackFor = {Exception.class})
    public void commonBoardPostIns(PgfuBoard pgfuBoard) throws Exception {
        boolean successSave = false;

        //예외처리 - 빈값 처리
        boolean normalFieldNullChk = ParamHelper.nullExcept(pgfuBoard, new String[]{"boardTitle","boardWriter","boardContent"});
        if(!normalFieldNullChk) return;
        Object[] mokObjs = new Object[]{pgfuBoard.getPgfuBoardTags(), pgfuBoard.getPgfuBoardCategory()};
        String[] fieldNames = new String[]{"pgfuBoardCategory.categoryId"};
        boolean nestedClassNullChk = ParamHelper.nestedParamExcep(mokObjs,fieldNames);
        if(!nestedClassNullChk) return ;
        boolean listParamExcepChk = ParamHelper.listParamExcep(pgfuBoard.getPgfuBoardTags(),new String[]{"tagValue"}, false);
        if(!listParamExcepChk) return;

        successSave = commonBoradMapper.commomBoardIns(pgfuBoard) >= 1; //게시글 저장
        if(successSave){
            //저장할 태그가 있으면 태그를 저장한다.
            List<PgfuBoardTag> pgfuBoardTags = pgfuBoard.getPgfuBoardTags();
            if(pgfuBoardTags != null && pgfuBoardTags.size() > 0){
                this.commonBoardTagMultiIns(pgfuBoardTags); //태그저장
                //generated_key가 차례대로 바인딩 되는 속성 때문에 첫 번째 Tag값이 이미 있는데도 불구하고 첫 번째 Tag값이 삽입되었다고 오판할 수 있다.
                //두개의 리스트의 사이즈가 같으면 모두 새롭게 삽입된 것이므로 다시 select해올 필요가없지만, 그것이 아니라면 전체적으로 모두 다시 SELECT 해와야한다.
                List<PgfuBoardTag> insertedTags = pgfuBoardTags.stream().filter(v -> v.getTagId()!=0).collect(Collectors.toList());
                if(insertedTags.size() != pgfuBoardTags.size()){ //이미 있는 태그라 INSERT IGNORE되어서 tagId가 안넘어온 값들이 있다면 전체 다시 SELECT하여 tagId삽입
                    List<PgfuBoardTag> completedBoardTags = this.commonBoardTagMultiSelWithTagVal(pgfuBoardTags);
                    pgfuBoardTags = completedBoardTags;
                }
                successSave = this.commonBoardTagMapMultiRowIns(pgfuBoardTags, pgfuBoard.getBoardId()) >= 0;
            }
        }
        if(!successSave) throw new RuntimeException("게시글이 잘못 저장되었습니다.");

    }

    /**********************************************************************************************
     * @Method 설명 : 태그 저장 - 리스트
     * @작성일 : 2023-04-18
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public int commonBoardTagMultiIns(List<PgfuBoardTag> pgfuBoardTags) throws Exception {
        int tagInsResult = commonBoradMapper.commonBoardTagMultiIns(pgfuBoardTags);
        return tagInsResult;
    }

    /**********************************************************************************************
     * @Method 설명 : 태그 - 게시글 map table multi row insert
     * @작성일 : 2023-04-18
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public int commonBoardTagMapMultiRowIns(List<PgfuBoardTag> pgfuBoardTags, int boardId){
        int mapInsResult = commonBoradMapper.commonBoardTagMapMultiRowIns(pgfuBoardTags, boardId);
        return mapInsResult;
    }
    
    /********************************************************************************************** 
     * @Method 설명 : TagValue로 Tag multi sel
     * @작성일 : 2023-04-18 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public List<PgfuBoardTag> commonBoardTagMultiSelWithTagVal(List<PgfuBoardTag> pgfuBoardTags){
        List<PgfuBoardTag> list = commonBoradMapper.commonBoardTagMultiSelWithTagVal(pgfuBoardTags);
        return list;
    }

}
