package ilovepc.playgroundforus.base.constant;

import ilovepc.playgroundforus.base.GeneralException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
public enum Code {

    // 충돌 방지를 위한 Code format
    // X1XXX: 제이
    // X2XXX: 셀리나
    // X3XXX: 메이슨
    // ex) 메이슨이 닉네임 중복 에러코드를 만든다면
    // USER_NICKNAME_DUPLICATED(13010, HttpStatus.BAD_REQUEST, "User nickname duplicated"),
    //★에러는 시스템 레벨에서 발생하여, 개발자가 어떻게 조치할 수 없는 수준을 의미. ex. JVM OOM
    //★예외는 개발자가 구현한 로직에서 발생하며, 개발자가 다른 방식으로 처리가능한 것들, JVM은 정상 동작. ex. 인덱스범위 검사

    OK(0, HttpStatus.OK, "Ok"),
    BAD_REQUEST(10000, HttpStatus.BAD_REQUEST, "Bad request"),
    VALIDATION_ERROR(10001, HttpStatus.BAD_REQUEST, "Validation error"),
    NOT_FOUND(10002, HttpStatus.NOT_FOUND, "Requested resource is not found"),

    INTERNAL_ERROR(20000, HttpStatus.INTERNAL_SERVER_ERROR, "Internal error"),
    DATA_ACCESS_ERROR(20001, HttpStatus.INTERNAL_SERVER_ERROR, "Data access error"),

    UNAUTHORIZED(40000, HttpStatus.UNAUTHORIZED, "User unauthorized");

    private final Integer code;
    private final HttpStatus httpStatus;
    private final String message;


    public String getMessage(Throwable e){ //Throwable클래스는 예외처리를 할 수 있는 최상위 클래스
        //ex) Bad request - Why Bad Request~
        return this.getMessage()+"-"+e.getMessage();
    }

    public String getMessage(String message){
        return Optional.ofNullable(message)
                .filter(Predicate.not(String::isBlank))
                .orElse(this.getMessage());
    }

    public static Code valueOf(HttpStatus httpStatus){
        if(httpStatus==null){
            throw new GeneralException("");
        }

        return Arrays.stream(Code.values())
                .filter(errorCode -> errorCode.getHttpStatus() == httpStatus)
                .findFirst()
                .orElseGet(()->{
                    if(httpStatus.is4xxClientError()){
                        return Code.BAD_REQUEST;
                    }else if(httpStatus.is5xxServerError()){
                        return Code.INTERNAL_ERROR;
                    }else{
                        return Code.OK;
                    }
                });
    }

    @Override
    public String toString() {
        return String.format("%s (%d)", this.name(), this.getCode());
    }


}
