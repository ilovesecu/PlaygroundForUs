package ilovepc.playgroundforus.file.vo;

import lombok.Data;

/**********************************************************************************************
 * @FileName : EimVO.java
 * @Date : 2023-04-24
 * @작성자 : 정승주
 * @설명 : 에디터를 통해 업로드된 이미지 파일 DB VO
 **********************************************************************************************/
@Data
public class EimVO {
    private Integer eimId;
    private Integer boardId;        //게시판 종류
    private Integer postId;         //게시글 번호' -- common board 테이블에서는 board_id가 post_id임
    private Integer userNo;
    private String eimOriginName;  //이미지 업로드시 원래 파일명 (사용자가 업로드한 이름)
    private String eimFileName;    //이미지 업로드시 서버에 저장된 파일명
    private Long eimFileSize;
    private Integer eimWidth;
    private Integer eimHeigth;
    private String eimExtType;     //'파일 확장자'
    private String eimUploadTime;  //파일 업로드 시간
    private String eimIp;          //업로드 아이피



}
