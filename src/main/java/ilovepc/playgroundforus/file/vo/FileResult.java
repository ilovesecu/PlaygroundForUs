package ilovepc.playgroundforus.file.vo;

import lombok.Data;

import java.util.ArrayList;

@Data
public class FileResult {
    /*
     * code return 정의
     *  1 : 업로드 성공
     *  0 : 접근 오류 ( 필수 파라메터 누락)
     * -1 : 회원정보 오류( DB 매칭 실패 )
     * -2 : 파일 포멧 오류
     * -3 : 시스템 오류
     * */
    private int code = 100101;
    private String msg ="파일 업로드를 성공했습니다.";
    private int errorCount = 0;
    private int totalCount = 0;

    private ArrayList<FileDetail> fileDetails = new ArrayList<>();

    public void addFileDetail(FileDetail fileDetail){
        fileDetails.add(fileDetail);
    }

    //에러 카운트 + 1
    public void addErrorCount(){
        this.errorCount += 1;
    }

}
