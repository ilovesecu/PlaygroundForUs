package ilovepc.playgroundforus.file.service;

import ilovepc.playgroundforus.base.constant.ServiceType;
import ilovepc.playgroundforus.base.constant.UploadType;
import ilovepc.playgroundforus.config.file.image.ImageConfig;
import ilovepc.playgroundforus.config.file.image.ImageType;
import ilovepc.playgroundforus.file.repository.FileMapper;
import ilovepc.playgroundforus.file.vo.*;
import ilovepc.playgroundforus.utils.EncrypthionHelper;
import ilovepc.playgroundforus.utils.file.BoxBlurFilter;
import ilovepc.playgroundforus.utils.file.FileHelper;
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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileAndPhotoService {
    //프로젝트 루트 절대경로
    @Value("${file.root.directory}")
    protected String ABSOLUT_PATH;

    private final FileHelper fileHelper;
    private final FileMapper fileMapper;

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
                        fileResult.addFileDetail(fileDetailResult);
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
                        fileResult.addFileDetail(fileDetailResult);
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
                        //파일정보 DB 저장. (Yeo에서는 해당 작업이 모두 끝난 후 따로 반복문 돌려서 저장하는데 여기서는 한 반복문안에서 해보자.)
                        EimVO eimVO = new EimVO();
                        eimVO.setBoardId(0);//게시판 종류
                        eimVO.setUserNo(1); //TODO Test로 1
                        eimVO.setEimOriginName(fileName); //원래 파일명
                        eimVO.setEimFileName(fileDetailResult.getFileName()); //서버에 저장된 파일명
                        eimVO.setEimFileSize(multipartFile.getSize());
                        eimVO.setEimWidth(image.getWidth(null));
                        eimVO.setEimHeight(image.getHeight(null));
                        eimVO.setEimExtType(extensionResult.getExtType());
                        eimVO.setEimIp(fileUploadObject.getClientIp());
                        int dbInsResult = fileMapper.editorImageIns(eimVO);
                        if(dbInsResult <= 0){ //DB 삽입 실패 시 예외발생
                            throw new Exception("DB Insert Fail");
                        }
                        String returnImageName = originalFileName+"|"+image.getWidth(null)+"|"+image.getHeight(null);
                        fileDetailResult.setEimId(eimVO.getEimId());
                        fileDetailResult.setFileName(originalFileName);
                        fileDetailResult.setImageFile(returnImageName);
                        fileDetailResult.setTemp(fileUploadObject.getTemp()); //임시폴더 업로드 여부 → confirm 시 영구폴더로 이동

                        fileDetailResult.setEncFileName(EncrypthionHelper.encryptAES256(originalFileName));
                        fileDetailResult.setBlurImgFileName(EncrypthionHelper.encryptAES256(originalFileName+"_blur")); //서버에 저장된 파일명에 _blur를 붙여서 암호화
                        fileDetailResult.setFullPath(uploadDir.getAbsolutePath()+File.separator+originalFileName);
                    }else{ //에러발생
                        log.warn("uploadStatus is fail! [error]-{}",originalFileName);
                        fileDetailResult.setCode(1000097);
                        fileDetailResult.setMsg("업로드 도중 에러 발생!");
                        //오류 상황 이전 업로드 파일 삭제 처리
                        //채팅방용 파일도 삭제 처리해야함
                        fileResult.addErrorCount();
                    }
                }catch(Exception e){
                    log.error("[uploadImage] 파일별 반복 중 에러 발생! e",e);
                    fileDetailResult.setCode(100097);
                    fileDetailResult.setMsg("파일별 반복 중 에러:"+e.getMessage());
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

        if(fileResult.getErrorCount() > 0){
            fileResult.setMsg("파일 업로드에 실패했습니다.");
        }

        return fileResult;
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 이미지 파일 뷰
     * @작성일 : 2023-04-25 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public byte[] getImageToByte(String type, String encImageName, boolean isTempFile){
        //디스크에 있는 파일을 읽어오기 위한 작업
        FileInputStream fileInputStream = null;
        InputStream inputStream = null;
        byte[] byteArray = null;
        boolean blurFlag = false;
        try{
            String decFileName = Optional.ofNullable(EncrypthionHelper.decryptAES256(encImageName)).orElse("");
            decFileName = URLDecoder.decode(decFileName,StandardCharsets.UTF_8);
            log.error("decFileName=>{}",decFileName);

            //blur 확인
            if(!decFileName.equals("") && decFileName.contains("_blur")){
                blurFlag = true;
                decFileName = decFileName.replace("_blur","");
            }
            String[] dotSplitFileName = decFileName.split("\\."); // [1_2023042516121616784] [png]
            String fileFolderTime = dotSplitFileName[0].split("\\_")[1]; //[1] [2023042516121616784]

            //임시폴더에 있는지 확인
            String tempDir = "";
            if(isTempFile){
                tempDir = File.separator + ImageType.get("temp"); //임시 저장 폴더명 가져오기
            }

            // 시간 파일 경로
            String yyyy = fileFolderTime.substring(0,4);
            String MM = fileFolderTime.substring(4,6);
            String dd = fileFolderTime.substring(6,8);
            String HH = fileFolderTime.substring(8,10);
            String originalUploadPath = ABSOLUT_PATH
                    + tempDir
                    + File.separator + ImageType.get(type)
                    + File.separator + yyyy
                    + File.separator + MM
                    + File.separator + dd
                    + File.separator + HH;
            if(blurFlag){
                String blurUploadPath = ABSOLUT_PATH
                        + tempDir
                        + File.separator + ImageType.get("hubBlur")
                        + File.separator + yyyy
                        + File.separator + MM
                        + File.separator + dd
                        + File.separator + HH;
                File blurDirectory = new File(blurUploadPath);
                if(!blurDirectory.exists())blurDirectory.mkdirs();
                //SIZE별 처리 미구현
                File originalFile = new File(originalUploadPath,decFileName);
                File blurFile = new File(blurUploadPath,decFileName);
                if(!blurFile.isFile()){ //Blur 이미지가 없다면 만들어주자.
                    Image image = null;
                    //GIF 미구현
                    /**if (decFileName.contains("gif")) {
                        ImageInputStream imageInputStream = new FileImageInputStream(file);
                        PatchedGIFImageReader reader = new PatchedGIFImageReader(null);
                        reader.setInput(imageInputStream);
                        image = reader.read(0);
                    } else {
                        image = ImageIO.read(file);
                    }*/
                    image = ImageIO.read(originalFile);
                    BufferedImage bufferedImage = (BufferedImage) image;
                    int iterations = 65;
                    float hRadius = 1 / 0.7f;
                    BoxBlurFilter boxBlurFilter = new BoxBlurFilter(hRadius, hRadius, iterations);
                    BufferedImage blurBufferedImg = boxBlurFilter.filter(bufferedImage,null);
                    ImageIO.write(blurBufferedImg, dotSplitFileName[1], new File(blurUploadPath, decFileName));
                }
                File evidentialFile = new File(blurUploadPath,decFileName);
                if(evidentialFile.isFile()){
                    byteArray = new byte[(int)evidentialFile.length()];
                    fileInputStream = new FileInputStream(evidentialFile);
                    fileInputStream.read(byteArray);
                }
            }else{ //원본
                File originalFile = new File(originalUploadPath,decFileName);
                if(originalFile.isFile()){
                    byteArray = new byte[(int)originalFile.length()];
                    fileInputStream = new FileInputStream(originalFile);
                    fileInputStream.read(byteArray);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            if (fileInputStream != null) {
                try { fileInputStream.close(); } catch (IOException e) { }
            }
        }
        return byteArray;
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
