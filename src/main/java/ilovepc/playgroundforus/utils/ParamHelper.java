package ilovepc.playgroundforus.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;

@Slf4j
public class ParamHelper {
    
    /********************************************************************************************** 
     * @Method 설명 : 필수 파라미터가 비어있는지 확인 
     * @작성일 : 2023-04-12 
     * @작성자 : 정승주
     * @변경이력 : 나중에는 @validate 같은걸로 해보도록
     **********************************************************************************************/
    public static boolean nullExcept(Object obj, String[] fieldNames){
        try {
            Class<?> clazz = obj.getClass();
            for(String fieldName : fieldNames) {
                Method getter = getMethodGet(fieldName, clazz);
                Object val = getter.invoke(obj);
                if(val == null) return false;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("[ParamHelper] - blankExcept error occurred! ->",e);
        }
        return true;
    }

    /**********************************************************************************************
     * @Method 설명 : 컴포지션 클래스에 대한 필수 파라미터가 비어있는지 확인 - fieldName은 obj.fieldName으로 한다.
     * @작성일 : 2023-04-12
     * @작성자 : 정승주
     * @변경이력 :
     **********************************************************************************************/
    public static boolean nestedParamExcep(Object[] mokObjs, String[] fieldNames){
        try{
            for(Object mokObj : mokObjs){
                Class<?> clazz = mokObj.getClass();
                String className = clazz.getSimpleName();
                for(String fieldName : fieldNames){
                    String[] fieldNameSplited = fieldName.split("\\.");
                    String inputClassName = fieldNameSplited[0];
                    String inputGetterName = fieldNameSplited[1];
                    if(inputClassName.toLowerCase(Locale.ROOT).equals(className.toLowerCase(Locale.ROOT))){
                        Method getter = getMethodGet(inputGetterName, clazz);
                        Object val = getter.invoke(mokObj);
                        if(val == null) return false;
                    }
                }
            }
        }catch(Exception e){
            log.error("[ParamHelper] - nestedParamExcep error occureed! -> ",e);
        }
        return true;
    }

    private static Method getMethodGet(String fieldName, Class<?> clazz) throws NoSuchMethodException {
        char[] cArr = fieldName.toCharArray();
        cArr[0] = Character.toUpperCase(cArr[0]);
        String getterName = new String(cArr);
        getterName = "get" + getterName;

        Method getter = clazz.getDeclaredMethod(getterName);
        return getter;
    }

}
