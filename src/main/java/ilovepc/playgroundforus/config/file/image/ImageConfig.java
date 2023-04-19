package ilovepc.playgroundforus.config.file.image;

import java.util.LinkedHashMap;
import java.util.Map;

public class ImageConfig {
    private static Map<String,Object> hubCommonBoardSizeMap = new LinkedHashMap<>();

    //클래스가 초기화될 때 실행되고, main() 보다 먼저 수행됩니다.
    static {
        hubCommonBoardSizeMap.put("w",720);
        hubCommonBoardSizeMap.put("b",320);
        hubCommonBoardSizeMap.put("m",160);
    }
}
