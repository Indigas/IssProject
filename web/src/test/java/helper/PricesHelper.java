package helper;

import org.springframework.util.MultiValueMap;
import org.springframework.util.MultiValueMapAdapter;

import java.util.HashMap;

public class PricesHelper {
    public static MultiValueMap<String, String> getPricesParams() {
        MultiValueMap<String, String> map = new MultiValueMapAdapter<>(new HashMap<>());
        map.add("dayFrom1", "1");
        map.add("dayFrom3", "3");
        map.add("dayFrom5", "7");

        map.add("dayPrice1", "10");
        // mistake to check if price will be persisted to car
        map.add("dayPrice4", "8");
        map.add("dayPrice5", "5");
        return map;
    }
}
