package ilovepc.playgroundforus.file.service;

import ilovepc.playgroundforus.base.constant.ServiceType;
import ilovepc.playgroundforus.base.constant.UploadType;
import ilovepc.playgroundforus.config.file.image.ImageConfig;
import ilovepc.playgroundforus.config.file.image.ImageType;
import ilovepc.playgroundforus.file.vo.*;
import ilovepc.playgroundforus.utils.EncrypthionHelper;
import ilovepc.playgroundforus.utils.FileHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
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
        UploadType uploadType = ImageType.getUploadType(type);

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
            String folderPath = ABSOLUT_PATH
                    + ""
                    + File.separator + tempDir
                    + File.separator + ImageType.get(type)
                    + File.separator + dateMap.get("yyyy")
                    + File.separator + dateMap.get("MM")
                    + File.separator + dateMap.get("dd")
                    + File.separator + dateMap.get("HH")
                    + File.separator;
            //업로드 폴더 체크 및 생성
            File uploadDir = new File(folderPath);
            if(!uploadDir.exists()) uploadDir.mkdirs();

            //파일별 반복
            for(MultipartFile multipartFile : imageFiles){
                FileDetail fileDetailResult = new FileDetail();
                String fileName = multipartFile.getOriginalFilename(); //사용자가 업로드한 원본이름
                inputStream = multipartFile.getInputStream();
                Integer imWidth=0;
                Integer imHeight=0;
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
                    imageInputStream = ImageIO.createImageInputStream(multipartFile.getInputStream()); //inputStream을 새롭게 넣어줘야함.
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
                    String originalFileName_o = getNewFileName(fileUploadObject.getUserNo(), dateMap, extensionResult.getExtType(), random5Decial, "o");
                    /**gif 원본저장
                    if (orgType.contains("gif") || extFileName.contains("gif")) {
                        multiImage.transferTo(new File(folderPath + originalFilename_o));
                    }*/

                    Map<String, Integer> saveSizeMap = ImageConfig.getSaveImageSizeInfo(serviceType);
                    boolean uploadStatus = true;
                    //GIF가 아닐 때 → 비율 처리 후 업로드 동시 진행
                    if(!extensionResult.getExtType().equals("gif")){
                        uploadStatus = this.imageResizeAndUpload(uploadDir.getAbsolutePath(), originalFileName, extensionResult.getExtType(), image, 0,0,originalFileName_o);
                    }
                    //채팅용 리사이즈 1/2씩 해서 따로 저장하는 부분 있음. (아직은 미구현)
                    //gif 폴더 이동? 로직 미구현

                    if(uploadStatus){ //정상 업로드 완료
                        String returnImageName = originalFileName+"|"+image.getWidth(null)+"|"+image.getHeight(null);
                        fileDetailResult.setFileName(originalFileName);
                        fileDetailResult.setImageFile(returnImageName);
                        fileDetailResult.setEncImageName(EncrypthionHelper.encryptAES256(returnImageName));

                        Map<String,Object> temp = new HashMap<>();
                        temp.put("size", multipartFile.getSize());
                        temp.put("width", image.getWidth(null));
                        temp.put("height", image.getHeight(null));
                        detailMap.put(returnImageName, temp);
                    }else{ //에러발생
                        log.warn("uploadStatus is fail! [error]-{}",originalFileName);
                        fileDetailResult.setCode(1000097);
                        fileDetailResult.setMsg("업로드 도중 에러 발생!");
                        //오류 상황 이전 업로드 파일 삭제 처리
                        //채팅방용 파일도 삭제 처리해야함
                        fileResult.addErrorCount();
                    }

                    //파일정보 DB 저장. (Yeo에서는 해당 작업이 모두 끝난 후 따로 반복문 돌려서 저장하는데 여기서는 한 반복문안에서 해보자.)
                    EimVO eimVO = new EimVO();
                    eimVO.setBoardId(0);//게시판 종류
                    eimVO.setUserNo(1); //Test로 1
                    eimVO.setEimOriginName(fileName); //원래 파일명
                    eimVO.setEimFileName(fileDetailResult.getFileName()); //서버에 저장된 파일명
                    eimVO.setEimFileSize(multipartFile.getSize());
                    eimVO.setEimWidth(image.getWidth(null));
                    eimVO.setEimHeigth(image.getHeight(null));
                }catch(Exception e){
                    log.error("[uploadImage] 파일별 반복 중 에러 발생! e",e);
                    fileDetailResult.setCode(100097);
                    fileDetailResult.setMsg("파일별 반복 중 에러");
                    fileDetailResult.setImageFile(fileName);
                    fileResult.addErrorCount();
                }
                fileResult.addFileDetail(fileDetailResult);
            }//end of For
        }catch (Exception e){
            log.error("[uploadImage] 프로세스 진행 중 에러 발생! e",e);
            fileResult.setCode(100097);
            fileResult.setMsg("프로세스 오류입니다.");
            fileResult.addErrorCount();
        }
        return fileResult;
    }

    /**********************************************************************************************
     * @Method 설명 : 이미지 비율 처리 및 이미지 업로드 동시 진행
     * @작성일 : 2023-04-24
     * @작성자 : 정승주
     * @변경이력 :
     * @Param
     *      uploadPath      : 업로드할 디렉토리
     *      fileName        : 업로드할 파일명 (서버 저장이름, 사용자가 지정한 이름 아님 주의)
     *      fileExt         : 파일 확장자
     *      image           : 이미지 객체
     *      dstW            : 리사이즈 목표 Width
     *      dstH            : 리사이즈 목표 Height
     *      orgFilePath     : originalFileName_o
     **********************************************************************************************/
    private boolean imageResizeAndUpload(String uploadPath, String fileName, String fileExt, Image image, int dstW, int dstH, String orgFileWithPath){
        Double scale;
        //원본 이미지의 사이즈
        Integer imageW = image.getWidth(null);
        Integer imageH = image.getHeight(null);

        int resizeW;
        int resizeH;
        if(dstW == 0 || dstH == 0){ //목표 중 하나라도 0이면 원본 사이즈 그대로 유지
            resizeW = imageW;
            resizeH = imageH;
        }else{
            scale = getScale(imageW, imageH, dstW, dstH); // 목표 사이즈로 줄이기 위한 배율 값 가져오기 (같은 배율로 줄여야함)
            resizeW = ((Double) (imageW.doubleValue() * scale)).intValue();
            resizeH = ((Double) (imageH.doubleValue() * scale)).intValue();
        }

        /*if(extFileName.equals("gif")){ //GIF 비율 따로 처리
            this.resizeGif(imgTargetPath+File.separator+orgFilePath, imgTargetPath+File.separator+fileName, resizeW, resizeH, false);
        }else{
            try{
                BufferedImage originBuffer = ((BufferedImage) image);
                BufferedImage bufferedImage = Thumbnails.of(originBuffer)
                        .size(resizeW, resizeH)
                        .outputFormat(extFileName)
                        .asBufferedImage();
                ImageIO.write(bufferedImage, extFileName, new File(imgTargetPath+File.separator+fileName));
            }catch(Exception e){
                e.printStackTrace();
                return false;
            }
        }*/

        try{
            BufferedImage originBuffer = ((BufferedImage) image);
            BufferedImage bufferedImage = Thumbnails.of(originBuffer)
                    .size(resizeW, resizeH)
                    .outputFormat(fileExt)
                    .asBufferedImage();
            ImageIO.write(bufferedImage, fileExt, new File(uploadPath+File.separator+fileName));
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    /**********************************************************************************************
     * @Method 설명 :  상대적 비율 계산 (같은 비율로 w,h를 줄여야 하니까)
     * @작성일 : 2023-04-24
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public Double getScale(int srcW, int srcH, int dstW, int dstH) {
        Double scale = 1D; //기본적으로 1배율
        if(srcW > dstW || srcH > dstH){ //목표값 중 하나라도 작을 때
            if(srcW > srcH){ //원본 width가 원본 height보다 클 때
                scale = dstW * 1.0 / srcW;
            }else{ //원본 height가 더 클때
                scale = dstH * 1.0 / srcH;
            }
        }
        return scale;
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
                + resultDecimal + (typeEquals ? "" : "_" + type) + "." + extFileName;
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
