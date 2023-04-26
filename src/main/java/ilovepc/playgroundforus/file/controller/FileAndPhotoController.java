package ilovepc.playgroundforus.file.controller;

import ilovepc.playgroundforus.base.constant.ServiceType;
import ilovepc.playgroundforus.config.file.image.ImageType;
import ilovepc.playgroundforus.file.service.FileAndPhotoService;
import ilovepc.playgroundforus.file.vo.FileResult;
import ilovepc.playgroundforus.file.vo.FileUploadObject;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
        if(subType.equals("image")){ //editor를 통해 업로드
            FileResult fileResult = fileAndPhotoService.uploadImage(type, fileUploadObject);
            return fileResult;
        }else if(subType.equals("file")){

        }
        return null;
    }
    
    /********************************************************************************************** 
     * @Method 설명 : 파일 업로드 컨펌 → 게시글 저장이 되지 않은 상태에서는 temp 폴더에있는데 그것을 정식 폴더로 옮겨주는 작업
     *               컨펌이 되지 않으면 Scheduler에 의해 삭제 예정
     * @작성일 : 2023-04-25 
     * @작성자 : 정승주
     * @변경이력 : 게시글 저장 서비스에서 FileConfirm 서비스를 불러도 될거같긴 함.
     **********************************************************************************************/
    public Object uploadConfirm(){
        return null;
    }

    /********************************************************************************************** 
     * @Method 설명 : 이미지 뷰 
     * @작성일 : 2023-04-25 
     * @작성자 : 정승주
     * @변경이력 : 위 url에 blur를 명시하지 않았을까..? 너무 쉽게 뚫릴 수 있어서?
     **********************************************************************************************/
    @GetMapping(value = "/{type}/image/{encImageName}",
            produces = {MediaType.IMAGE_PNG_VALUE,
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE
            })
    public byte[] view(
            @PathVariable(value = "type")String type,
            @PathVariable(value = "encImageName")String encImageName,
            @RequestParam(value = "temp", required = false) boolean temp
    ){
        return fileAndPhotoService.getImageToByte(type,encImageName,temp);
    }
    
}
