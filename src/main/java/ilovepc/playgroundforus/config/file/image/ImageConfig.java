package ilovepc.playgroundforus.config.file.image;

import ilovepc.playgroundforus.base.constant.ServiceType;

import java.util.LinkedHashMap;
import java.util.Map;

public class ImageConfig {
    private static Map<String,Integer> hubCommonBoardSizeMap = new LinkedHashMap<>();

    //클래스가 초기화될 때 실행되고, main() 보다 먼저 수행됩니다.
    static {
        hubCommonBoardSizeMap.put("w",720);
        hubCommonBoardSizeMap.put("b",320);
        hubCommonBoardSizeMap.put("m",160);
    }

    /**********************************************************************************************
     * @Method 설명 : 서비스별 사이즈 정보 저장된 맵 반환
     * @작성일 : 2023-04-21
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public static Map<String,Integer> getSaveImageSizeInfo(ServiceType serviceType){
        switch (serviceType){
            case HUB:
                return hubCommonBoardSizeMap;
            default:
                return null;
        }
    }

    /**********************************************************************************************
     * @Method 설명 : 이미지 업로드 허용 확장자 반환 - 서비스별
     * @작성일 : 2023-04-21
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public static boolean isAllowImageExt(ServiceType serviceType, String extension){
        if(extension == null) return false;
        return "jpg|jpeg|gif|png".contains(extension);
    }
    /**********************************************************************************************
     * @Method 설명 : 파일 업로드 허용 확장자 반환 - 서비스별
     * @작성일 : 2023-04-21
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public static boolean isAllowFileExt(ServiceType serviceType, String extension){
        if(extension == null) return false;
        return "doc|docx|hwp|txt|xml|pdf|xls|xlsx|ppt|pptx|zip".contains(extension);
    }

}
