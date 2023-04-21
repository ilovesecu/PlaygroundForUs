package ilovepc.playgroundforus.file.vo;

import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Data
@ToString
public class FileUploadObject {
    private int userNo; //업로드 사용자
    private String fileExtension; //파일 확장자
    private String temp = "0";  //임시 업로드 여부
    private MultipartFile[] files;
}
