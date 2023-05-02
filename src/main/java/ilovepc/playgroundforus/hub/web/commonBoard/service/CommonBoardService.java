package ilovepc.playgroundforus.hub.web.commonBoard.service;

import ilovepc.playgroundforus.base.constant.BadgeColor;
import ilovepc.playgroundforus.file.service.FileAndPhotoService;
import ilovepc.playgroundforus.file.vo.FileDetail;
import ilovepc.playgroundforus.hub.web.commonBoard.repository.CommonBoardMapper;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoard;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoardCategory;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoardSaveResult;
import ilovepc.playgroundforus.hub.web.commonBoard.vo.PgfuBoardTag;
import ilovepc.playgroundforus.utils.ParamHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonBoardService {
    private final CommonBoardMapper commonBoardMapper;
    private final FileAndPhotoService fileAndPhotoService;
    
    /********************************************************************************************** 
     * @Method 설명 : 게시글 조회
     * @작성일 : 2023-04-28 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public List<PgfuBoard> getCommonBoardWithPaging(){
        try{
            List<PgfuBoard> brdInfos = commonBoardMapper.commonBoardMainSel();
            this.setCategoryColor(brdInfos);
            return brdInfos;
        }catch (Exception e){
            log.error("[getCommonBoardWithPaging] - exception --->",e);
        }
        return new ArrayList<>();
    }

    /**********************************************************************************************
     * @Method 설명 : 카테고리 조회
     * @작성일 : 2023-04-17
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public List<PgfuBoardCategory> getCommonBoardCategoryAll(){
        List<PgfuBoardCategory> pgfuBoardCategorys = commonBoardMapper.getCommonBoardCategoryAll();
        return pgfuBoardCategorys;
    }

    /**********************************************************************************************
     * @Method 설명 : 썸머노트 글쓰기 - 저장
     * @작성일 : 2023-04-17
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    @Transactional(rollbackFor = {Exception.class})
    public PgfuBoardSaveResult commonBoardPostIns(PgfuBoard pgfuBoard) throws Exception {
        String type = "hubCbSm";
        //File move(copy)
        List<FileDetail> fileDetails = (ArrayList<FileDetail>) Optional.ofNullable(pgfuBoard.getPgfuBoardEditorImages()).orElse(new ArrayList<>());
        if(fileDetails.size() > 0){
            //File move(copy)성공 시, content에서 image태그 찾아서 ?temp=1 지워줘야함! 0으로 바꾸던지 -> 바꾼거 안바꾼거 둘 다 가지고있자.
            Map<String,Object> resultMap = fileAndPhotoService.fileConfirm(pgfuBoard.getPgfuBoardEditorImages(), type);
            if((int)resultMap.get("failCnt")>0) {
                throw new RuntimeException("에디터 이미지 임시파일 Confirm 실패!");
            }
            String originalContent = pgfuBoard.getBoardContent();
            String pattern = "\\?temp=1"; //TODO 일반 TEXT중에 ?temp=1이 있으면 어쩌지?
            String replace = "";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(originalContent);
            String newStr = m.replaceAll(replace);
            pgfuBoard.setBoardContent(newStr);
        }
        boolean successSave = false;
        PgfuBoardSaveResult pgfuBoardSaveResult = new PgfuBoardSaveResult();

        //예외처리 - 빈값 처리
        boolean normalFieldNullChk = ParamHelper.nullExcept(pgfuBoard, new String[]{"boardTitle","boardWriter","boardContent"});
        if(!normalFieldNullChk){
            pgfuBoardSaveResult.setSuccess(false);
            pgfuBoardSaveResult.setErrorMessage("custom exception occurred");
            pgfuBoardSaveResult.setErrorCause("normal field null");
            pgfuBoardSaveResult.setPgfuBoard(pgfuBoard);
            return pgfuBoardSaveResult;
        }
        Object[] mokObjs = new Object[]{pgfuBoard.getPgfuBoardTags(), pgfuBoard.getPgfuBoardCategory()};
        String[] fieldNames = new String[]{"pgfuBoardCategory.categoryId"};
        boolean nestedClassNullChk = ParamHelper.nestedParamExcep(mokObjs,fieldNames);
        if(!nestedClassNullChk) {
            pgfuBoardSaveResult.setSuccess(false);
            pgfuBoardSaveResult.setErrorMessage("custom exception occurred");
            pgfuBoardSaveResult.setErrorCause("nestedClass field null");
            pgfuBoardSaveResult.setPgfuBoard(pgfuBoard);
            return pgfuBoardSaveResult;
        }
        boolean listParamExcepChk = ParamHelper.listParamExcep(pgfuBoard.getPgfuBoardTags(),new String[]{"tagValue"}, false);
        if(!listParamExcepChk){
            pgfuBoardSaveResult.setSuccess(false);
            pgfuBoardSaveResult.setErrorMessage("custom exception occurred");
            pgfuBoardSaveResult.setErrorCause("listParam field null");
            pgfuBoardSaveResult.setPgfuBoard(pgfuBoard);
            return pgfuBoardSaveResult;
        }

        successSave = commonBoardMapper.commomBoardIns(pgfuBoard) >= 1; //게시글 저장
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
        if(!successSave){
            throw new RuntimeException("게시글 저장에 실패하였습니다.");
        }
        pgfuBoardSaveResult.setSuccess(true);
        pgfuBoardSaveResult.setPgfuBoard(pgfuBoard);

        //fileAndPhotoService.confirmFilePostIdUpdate(fileDetails,0, pgfuBoard.getBoardId());
        //if(1==1) throw new RuntimeException("게시글 저장에 실패하였습니다2222.");

        //DB 저장까지 성공 하면 temp에 있던 파일들 지워주자. type = "hubCbSm"
        //삭제 되어야할 파일들 실제로 삭제(고민) and DB에서 삭제
        pgfuBoard.getPgfuBoardEditorImages().forEach(v -> {
            int result = fileAndPhotoService.fileRemove(v.getEncFileName(),v.getTemp(),type);
        });
        pgfuBoard.getPgfuBoardEditorDeleteImages().forEach(v -> {
            int result = fileAndPhotoService.fileRemove(v.getEncFileName(),v.getTemp(),type);
        });
        //confirm된 친구들 postNo를 file DB에서 update해주기
        fileAndPhotoService.confirmFilePostIdUpdate(fileDetails,0, pgfuBoard.getBoardId());

        return pgfuBoardSaveResult;
    }

    /**********************************************************************************************
     * @Method 설명 : 태그 저장 - 리스트
     * @작성일 : 2023-04-18
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public int commonBoardTagMultiIns(List<PgfuBoardTag> pgfuBoardTags) throws Exception {
        int tagInsResult = commonBoardMapper.commonBoardTagMultiIns(pgfuBoardTags);
        return tagInsResult;
    }

    /**********************************************************************************************
     * @Method 설명 : 태그 - 게시글 map table multi row insert
     * @작성일 : 2023-04-18
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public int commonBoardTagMapMultiRowIns(List<PgfuBoardTag> pgfuBoardTags, int boardId){
        int mapInsResult = commonBoardMapper.commonBoardTagMapMultiRowIns(pgfuBoardTags, boardId);
        return mapInsResult;
    }
    
    /********************************************************************************************** 
     * @Method 설명 : TagValue로 Tag multi sel
     * @작성일 : 2023-04-18 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public List<PgfuBoardTag> commonBoardTagMultiSelWithTagVal(List<PgfuBoardTag> pgfuBoardTags){
        List<PgfuBoardTag> list = commonBoardMapper.commonBoardTagMultiSelWithTagVal(pgfuBoardTags);
        return list;
    }

    /**********************************************************************************************
     * @Method 설명 : 카테고리 컬러 설정
     * @작성일 : 2023-05-02
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    private void setCategoryColor(List<PgfuBoard> brdInfos){
        brdInfos.forEach(v -> {
            int caId = v.getPgfuBoardCategory().getCategoryId();
            switch (caId){
                case 1:
                    v.getPgfuBoardCategory().setCategoryColor("bg-primary");
                    break;
                case 2:
                    v.getPgfuBoardCategory().setCategoryColor("bg-secondary");
                    break;
                case 3:
                    v.getPgfuBoardCategory().setCategoryColor("bg-success");
                    break;
                case 4:
                    v.getPgfuBoardCategory().setCategoryColor("bg-danger");
                    break;
                case 5:
                    v.getPgfuBoardCategory().setCategoryColor("bg-warning text-dark");
                    break;
                case 6:
                    v.getPgfuBoardCategory().setCategoryColor("bg-info text-dark");
                    break;
                case 7:
                    v.getPgfuBoardCategory().setCategoryColor("bg-light text-dark");
                    break;
                case 8:
                    v.getPgfuBoardCategory().setCategoryColor("bg-dark");
                    break;
            }
        });
    }

}
