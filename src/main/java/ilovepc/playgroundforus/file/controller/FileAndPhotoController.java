package ilovepc.playgroundforus.file.controller;

import ilovepc.playgroundforus.base.constant.ServiceType;
import ilovepc.playgroundforus.config.file.image.ImageType;
import ilovepc.playgroundforus.file.service.FileAndPhotoService;
import ilovepc.playgroundforus.file.vo.FileUploadObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/storage")
public class FileAndPhotoController {
    private final FileAndPhotoService fileAndPhotoService;

    /********************************************************************************************** 
     * @Method 설명 : 파일 업로드
     * @작성일 : 2023-04-21 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    @PostMapping(value = "/{type}/{subType}")
    public Object upload(@PathVariable(value = "type")String type, @PathVariable(value = "subType")String subType, FileUploadObject fileUploadObject, HttpServletRequest req){
        //type : ImageType의 키 / 업로드될 폴더명
        //subType : 사용자가 지정한 파일 타입
        fileUploadObject.setClientIp(req.getRemoteAddr()); //IP 수집
        if(subType.equals("image")){
            fileAndPhotoService.uploadImage(type, fileUploadObject);
        }else if(subType.equals("file")){

        }

        return null;
    }
}
