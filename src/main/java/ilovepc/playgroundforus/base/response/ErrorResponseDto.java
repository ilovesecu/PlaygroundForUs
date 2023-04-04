package ilovepc.playgroundforus.base.response;

import ilovepc.playgroundforus.base.constant.Code;

public class ErrorResponseDto extends ResponseDto{
    private ErrorResponseDto(Code errorCode){
        super(false, errorCode.getCode(), errorCode.getMessage());
    }

    private ErrorResponseDto(Code errorCode, String message){
        super(false, errorCode.getCode(), message);
    }

    private ErrorResponseDto(Code errorCode, Exception e){
        super(false, errorCode.getCode(), errorCode.getMessage(e));
    }

    public static ErrorResponseDto of(Code errorCode){
        return new ErrorResponseDto(errorCode);
    }

    public static ErrorResponseDto of(Code errCode, Exception e){
        return new ErrorResponseDto(errCode, e);
    }

    public static ErrorResponseDto of(Code errorCode, String message){
        return new ErrorResponseDto(errorCode, message);
    }
}
