package ilovepc.playgroundforus.config.file.image;

import ilovepc.playgroundforus.base.constant.ServiceType;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageType {
    /** 타입별 photo folder */
    private static final Map<String,String> IMAGE_TYPE = new HashMap<>();
    /** 파입업로드/다운로드 가능한 타입 목록 */
    public static List<String> FILE_TYPE_ARR;

    //클래스가 초기화될 때 실행되고, main() 보다 먼저 수행됩니다.
    static{
        //////////////////////////////////////
        // 공통 타입
        //////////////////////////////////////
        IMAGE_TYPE.put("temp"      , "yeo_tmp");     // 임시저장용

        //////////////////////////////////////
        // HUB
        //////////////////////////////////////
        IMAGE_TYPE.put("hubCbSm"        ,"hub_sm"             + File.separator);  // CommonBaord summernote
        IMAGE_TYPE.put("hubCb"          ,"hub_mate"           + File.separator);  // CommonBaord 글쓰기
        IMAGE_TYPE.put("hubProfile"     ,"hub_profile"        + File.separator);  // 프로필

        //////////////////////////////////////
        // 파입업로드/다운로드 가능한 타입
        //////////////////////////////////////
        FILE_TYPE_ARR = Arrays.asList("hubCb");
    }

    //Type에 해당하는 폴더명을 반환 (항상 뒤에 separator가 붙음)
    public static String get(String key){ return IMAGE_TYPE.get(key); }

    public static ServiceType getServiceType(String key){
        if(key==null)return ServiceType.HUB;

        switch (key){
            case "hubCb":
            case "hubProfile":
                return ServiceType.HUB;
            default:
                return null;
        }
    }
}
