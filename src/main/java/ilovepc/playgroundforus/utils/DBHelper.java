package ilovepc.playgroundforus.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class DBHelper {
    //간단하게 instanceof는 특정 Object가 어떤 클래스/인터페이스를 상속/구현했는지를 체크하며
    //Class.isAssignableFrom()은 특정 Class가 어떤 클래스/인터페이스를 상속/구현했는지 체크합니다.

    /**********************************************************************************************
     * @Method 설명 : SQL결과에서 파라미터 타입의 데이터를 반환
     * @작성일 : 2023-04-11
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public static <T> T getData(List<Object>lists, Class<T> clzz){
        if(lists == null) return null;
        for(Object data: lists){
            if(data instanceof List){
                List<Object> _list = (ArrayList)data;
                if(_list.size() > 0 && !clzz.isAssignableFrom(Collections.class) && clzz.isAssignableFrom(_list.get(0).getClass())){
                    return (T)_list.get(0);
                }
            }
        }
        if(lists.size() > 0){
            return (T)lists.get(0);
        }

        try{
            Object a = clzz.getDeclaredConstructor().newInstance();
            return (T)a;
        }catch(Exception e){
            log.error("");
        }
        return null;
    }
    
    /********************************************************************************************** 
     * @Method 설명 : SQL결과에서 파라미터 타입의 리스트를 반환
     * @작성일 : 2023-04-12 
     * @작성자 : 정승주
     * @변경이력 : 
     **********************************************************************************************/
    public static <T> List<T> getList(List<Object> lists, Class<T> clzz){
        for(Object data : lists){
            if(data instanceof List){
                List<Object> _list = (ArrayList) data;
                if(_list.size() > 0 && clzz.isAssignableFrom(_list.get(0).getClass())){
                    return (List<T>) _list;
                }
            }
        }
        return new ArrayList<>();
    }
}
