package ilovepc.playgroundforus.file.vo;

import lombok.Data;

/**********************************************************************************************
 * @FileName : FileExtensionCheckResult.java
 * @Date : 2023-04-21
 * @작성자 : 정승주
 * @설명 : 파일 확장자 검사 결과
 **********************************************************************************************/
@Data
public class FileExtensionCheckResult {
    private boolean result;
    private String fullType;    //fileType + extType (image/png)
    private String fileType;    //image 등등
    private String extType;     // png, jpg 등등
}
