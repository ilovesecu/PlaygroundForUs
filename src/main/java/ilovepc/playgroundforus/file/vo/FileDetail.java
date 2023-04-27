package ilovepc.playgroundforus.file.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class FileDetail {
    /*
     * code return 정의
     *  1 : 업로드 성공
     *  0 : 접근 오류 ( 필수 파라메터 누락)
     * -1 : 회원정보 오류( DB 매칭 실패 )
     * -2 : 파일 포멧 오류
     * -3 : 시스템 오류
     * */
    private int code = 100101;
    private String msg = "파일업로드를 성공했습니다.";

    private int eimId;              //DB에 저장된 번호
    private String fileName;        //서버 저장파일명
    private String imageFile;       //사용자가 업로드한 원본이름 or 서버 저장된 이름 | 이미지W | 이미지H
    private String encFileName;     //암호화된 파일명
    private String blurImgFileName; //블러처리된 이미지 파일명
    private String temp = "0";      //임시파일에 업로드되었는지 여부
    @JsonIgnore
    private String fullPath;        //업로드 파일 Full Path
}

