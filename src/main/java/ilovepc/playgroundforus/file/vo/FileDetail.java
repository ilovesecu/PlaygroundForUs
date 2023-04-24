package ilovepc.playgroundforus.file.vo;

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

    private String fileName; //서버 저장파일명
    private String imageFile;
    private String encImageName;//암호화된 파일명
}

