package ilovepc.playgroundforus.base.response;

import ilovepc.playgroundforus.base.constant.Code;
import lombok.Getter;

@Getter
public class DataResponseDto<E> extends ResponseDto{
    private final E data;

    private DataResponseDto(E data){
        super(true, Code.OK.getCode(), Code.OK.getMessage());
        this.data = data;
    }

    private DataResponseDto(E data, String message){
        super(true, Code.OK.getCode(), message);
        this.data = data;
    }

    public static <T> DataResponseDto<T> of(T data){
        return new DataResponseDto<>(data);
    }

    public static <T> DataResponseDto<T> of(T data, String message){
        return new DataResponseDto<>(data, message);
    }

    public static <T> DataResponseDto<T> empty(){
        return new DataResponseDto<>(null);
    }


}
