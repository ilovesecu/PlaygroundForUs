package ilovepc.playgroundforus.utils;

import ilovepc.playgroundforus.base.constant.ServiceType;
import ilovepc.playgroundforus.config.file.image.ImageConfig;
import ilovepc.playgroundforus.file.vo.FileExtensionCheckResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Locale;

@Component
@Slf4j
public class FileHelper {

    /********************************************************************************************** 
     * @Method 설명 : 파일 확장자 검사 (true:허용 / false:비허용)
     * @작성일 : 2023-04-21 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public FileExtensionCheckResult fileTypePermitCheck(InputStream inputStream, ServiceType serviceType){
        FileExtensionCheckResult result = new FileExtensionCheckResult();
        try{
            //String mimeType = tika.detect(inputStream);
            String mimeType = new Tika().detect(inputStream);
            String[] mimeTypeSplited = mimeType.split("/");
            result.setFullType(mimeType);
            result.setFileType(mimeTypeSplited[0]);
            result.setExtType(mimeTypeSplited[1]);
            if(result.getFileType().equals("image")){
                result.setResult(ImageConfig.isAllowImageExt(serviceType,result.getExtType().toLowerCase(Locale.ROOT)));
            }else{
                result.setResult(ImageConfig.isAllowFileExt(serviceType, result.getExtType().toLowerCase(Locale.ROOT)));
            }
        }catch(Exception e){
            log.error("[fileTypePermitCheck] error!! -> e",e);
        }
        return result;
    }
}
