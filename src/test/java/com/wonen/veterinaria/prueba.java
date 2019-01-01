package test.java.com.wonen.veterinaria;

import java.util.HashMap;
import java.util.Map;

public class prueba {
    public static void main(String[] args) {
        String a = "";
        //System.out.println(Double.parseDouble(a));

        Map<String,String> b = new HashMap<>();
        b.put("a","aa");
        b.put("b","bb");
        b.put("c","cc");

        System.out.println(b.get("a"));
        System.out.println(b.get("d"));
    }
}
