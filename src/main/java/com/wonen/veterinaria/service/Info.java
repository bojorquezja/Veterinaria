package main.java.com.wonen.veterinaria.service;

import java.time.LocalDate;
import java.util.Date;

public class Info {
    public static Class<Integer> getClase(int intVar) {
        return Integer.TYPE;
    }
    public static Class<Integer> getClase(Integer integerCVar) {
        return Integer.class;
    }

    public static Class<Long> getClase(long longVar) {
        return Long.TYPE;
    }
    public static Class<Long> getClase(Long longCVar) {
        return Long.class;
    }

    public static Class<Boolean> getClase(boolean booleanVar) {
        return Boolean.TYPE;
    }
    public static Class<Boolean> getClase(Boolean booleanCVar) {
        return Boolean.class;
    }

    public static Class<Float> getClase(float floatVar) {
        return Float.TYPE;
    }
    public static Class<Float> getClase(Float floatCVar) {
        return Float.class;
    }

    public static Class<Double> getClase(double doubleVar) {
        return Double.TYPE;
    }
    public static Class<Double> getClase(Double doubleCVar) {
        return Double.class;
    }

    public static Class<String> getClase(String string) {
        return String.class;
    }

    public static Class<LocalDate> getClase(LocalDate localDate) {
        return LocalDate.class;
    }
    public static Class<Date> getClase(Date date) {
        return Date.class;
    }

}
