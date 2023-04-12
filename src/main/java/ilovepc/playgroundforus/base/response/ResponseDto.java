package ilovepc.playgroundforus.base.response;

import ilovepc.playgroundforus.base.constant.Code;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class ResponseDto {
    private final Boolean success;
    private final Integer code;
    private final String message;
    private String reason;

    public static ResponseDto of(Boolean success, Code code){
        return new ResponseDto(success, code.getCode(), code.getMessage());
    }

    public static ResponseDto of(Boolean success, Code code, Exception e){
        return new ResponseDto(success, code.getCode(), code.getMessage(e));
    }

    public static ResponseDto of(Boolean success, Code code, String message){
        return new ResponseDto(success, code.getCode(), code.getMessage(message));
    }

    public static ResponseDto of(Boolean success, Code code, String message, String reason){
        return new ResponseDto(success, code.getCode(), code.getMessage(message), reason);
    }

    public static ResponseDto of(Boolean success, Code code, Exception e, String reason){
        return new ResponseDto(success, code.getCode(), code.getMessage(e), reason);
    }

}
