package ilovepc.playgroundforus.utils;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
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
                if(val instanceof Number) { //숫자일 때 0값이면 false
                    int a = (Integer)val;
                    if(a == 0)return false;
                }
                if(val instanceof String){ //문자일 떄 ""면 false
                    String a = (String)val;
                    if(a.equals("")) return false;
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            log.error("[ParamHelper] - blankExcept error occurred! ->",e);
            return false;
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
            return false;
        }
        return true;
    }
    
    /********************************************************************************************** 
     * @Method 설명 : List파라미터 중 해당 값이 null, 빈값이 아닌지 체크..
     * @작성일 : 2023-04-19 
     * @작성자 : 정승주
     * @변경이력 :  @param
     *                  strict : true : 리스트 null, size = 0까지 체크 (null, size = 0이면 false 반환)
     **********************************************************************************************/
    public static boolean listParamExcep(List<?>params, String[]getNames, boolean strict){
        try{
            if(params == null || params.size() == 0){
                if(strict) return false; //strict 모드라면 fail.
                return true;
            }

            for(Object obj : params){
                if(obj == null) return false;
                for(String getName : getNames){
                    Method getter = getMethodGet(getName, obj.getClass());
                    Object val = getter.invoke(obj);
                    if(val == null) return false;
                    if(val instanceof String){
                        String a = (String)val;
                        if(a.equals("")) return false;
                    }
                }
            }
        }catch (Exception e){
            log.error("[ParamHelper] - listParamExcep error occureed! -> ",e);
            return false;
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
