package ilovepc.playgroundforus.file.service;

import ilovepc.playgroundforus.base.constant.ServiceType;
import ilovepc.playgroundforus.config.file.image.ImageType;
import ilovepc.playgroundforus.file.vo.FileDetail;
import ilovepc.playgroundforus.file.vo.FileExtensionCheckResult;
import ilovepc.playgroundforus.file.vo.FileResult;
import ilovepc.playgroundforus.file.vo.FileUploadObject;
import ilovepc.playgroundforus.utils.FileHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileAndPhotoService {
    //프로젝트 루트 절대경로
    @Value("${file.root.directory}")
    protected String ABSOLUT_PATH;

    private final FileHelper fileHelper;

    /********************************************************************************************** 
     * @Method 설명 : 이미지 파일 업로드
     * @작성일 : 2023-04-21 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public FileResult uploadImage(String type, FileUploadObject fileUploadObject){
        FileResult fileResult = new FileResult();
        ServiceType serviceType = ImageType.getServiceType(type);

        MultipartFile[] imageFiles = fileUploadObject.getFiles();
        //예외처리
        if(imageFiles==null || imageFiles.length == 0 || type == null){
            fileResult.setCode(100100);
            fileResult.setMsg("올바른 접근이 아닙니다.");
            fileResult.addErrorCount();
            return fileResult;
        }

        /**회원인경우 , DB에서 회원번호 있는지 검사
        if( checkMember && ! checkUploadValid(memNo ,  photoInfos, type) ){
            return photoInfos;
        }*/
        Map<String, Map<String,Object>> detailMap = new HashMap<>(); //FileDetail을 담기위한 Map 추측
        ImageInputStream imageInputStream = null;
        InputStream inputStream = null;
        try{
            fileResult.setTotalCount(imageFiles.length);
            Map<String,Object> dateMap = getDateMap();

            //임시 저장 여부 - 파일 하나당 설정할 수 있는것이 아닌 요청 하나 당 설정할 수 있음!
            String tempDir = "";
            if("1".equals(fileUploadObject.getTemp())){
                tempDir = File.separator + ImageType.get("temp"); //임시 저장 폴더명 가져오기
            }
            String folderPath = ABSOLUT_PATH + tempDir
                    + File.separator + dateMap.get("yyyy")
                    + File.separator + dateMap.get("MM")
                    + File.separator + dateMap.get("dd")
                    + File.separator + dateMap.get("HH")
                    + File.separator;
            //업로드 폴더 체크 및 생성
            File uploadDir = new File(folderPath);
            if(!uploadDir.exists()) uploadDir.mkdir();

            //파일별 반복
            for(MultipartFile multipartFile : imageFiles){
                try{
                    FileDetail fileDetailResult = new FileDetail();
                }catch(Exception e){
                    log.error("[uploadImage] 파일별 반복 중 에러 발생! e",e);
                    fileResult.setCode(100097);
                    fileResult.setMsg("파일별 반복 중 에러");
                }
            }

            //파일 확장자 검사
            //FileExtensionCheckResult fileExtensionCheckResult = fileHelper.fileTypePermitCheck()



            
        }catch (Exception e){

        }


        return fileResult;
    }


    private Map<String,Object> getDateMap(){
        Date time = new Date();
        Map<String,Object> dateMap = new HashMap<>();
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formatted = sdf.format(time);
        String[] splited = formatted.split("-");
        dateMap.put("yyyy", splited[0]);
        dateMap.put("MM", splited[1]);
        dateMap.put("dd", splited[2]);
        dateMap.put("HH", splited[3]);
        dateMap.put("mm", splited[4]);
        dateMap.put("ss", splited[5]);
        return dateMap;
    }
}
