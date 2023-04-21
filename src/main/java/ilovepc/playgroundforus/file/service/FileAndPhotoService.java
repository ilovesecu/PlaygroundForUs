package ilovepc.playgroundforus.file.service;

import ilovepc.playgroundforus.base.constant.ServiceType;
import ilovepc.playgroundforus.config.file.image.ImageConfig;
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

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
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
                FileDetail fileDetailResult = new FileDetail();
                String fileName = multipartFile.getOriginalFilename(); //사용자가 업로드한 원본이름
                inputStream = multipartFile.getInputStream();
                try{
                    //파일 확장자 검사
                    FileExtensionCheckResult extensionResult = fileHelper.fileTypePermitCheck(inputStream, serviceType);
                    if(!(extensionResult.isResult() && extensionResult.getFileType().equals("image"))){
                        //허용되지 않는 확장자
                        fileDetailResult.setCode(100098);
                        fileDetailResult.setMsg("지원하지 않는 파일입니다. : "+fileName);
                        fileDetailResult.setImageFile(fileName);
                        fileResult.addErrorCount();
                        continue;
                    }
                    //업로드 실행
                    String random5Decial = get5MillisTime();
                    Image image = null;
                    imageInputStream = ImageIO.createImageInputStream(inputStream);

                    image = ImageIO.read(imageInputStream);
                    if(image == null){
                        fileDetailResult.setCode(100096);
                        fileDetailResult.setMsg("이미지 파일이 아닙니다.");
                        fileResult.addErrorCount();
                        continue;
                    }
                    //서버 저장 file name = jpg 고정
                    //서버에 저장될 이름 (사용자가 업로드한 이름이 아님 주의)
                    String originalFileName = getNewFileName(fileUploadObject.getUserNo(), dateMap, extensionResult.getExtType(), random5Decial, "");

                }catch(Exception e){
                    log.error("[uploadImage] 파일별 반복 중 에러 발생! e",e);
                    fileDetailResult.setCode(100097);
                    fileDetailResult.setMsg("파일별 반복 중 에러");
                    fileDetailResult.setImageFile(fileName);
                    fileResult.addErrorCount();
                }
            }

        }catch (Exception e){

        }


        return fileResult;
    }

    /**********************************************************************************************
     * @Method 설명 : 파일명 생성 (회원번호_날짜+밀리초랜덤_타입.확장자)
     * @작성일 : 2023-04-21
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    private String getNewFileName(int userNo, Map<String,Object>dateMap, String extFileName, String resultDecimal, String type){
        boolean typeEquals = type.equals("");
        return userNo + "_"
                + dateMap.get("yyyy")
                + dateMap.get("MM")
                + dateMap.get("dd")
                + dateMap.get("HH")
                + dateMap.get("mm")
                + dateMap.get("ss")
                + resultDecimal + (type.equals("") ? "" : "_" + type) + "." + extFileName;
    }

    /********************************************************************************************** 
     * @Method 설명 : 랜덤값 5자리 숫자 반환
     * @작성일 : 2023-04-21 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    private String get5MillisTime(){
        String result = "00000";
        for(int i=0; i<3; i++){
            long mstime = System.currentTimeMillis();
            long seconds = mstime / 1000;
            double decimal = (mstime - (seconds * 1000)) / 1000d;
            String s = String.valueOf(decimal * Math.random());
            try{
                result = s.split("\\.")[1].substring(0, 5);
                break;
            }catch(IndexOutOfBoundsException e){
                // Retry
            }
        }
        return result;
    }
    

    /**********************************************************************************************
     * @Method 설명 : 파일 업로드 폴더를 위한 현재 날짜,시,분,초를 key로 하는 Map 반환
     * @작성일 : 2023-04-21
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
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
