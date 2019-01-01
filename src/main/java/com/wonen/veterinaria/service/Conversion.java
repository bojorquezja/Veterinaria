package main.java.com.wonen.veterinaria.service;

import java.time.LocalDate;
import java.util.Date;

public class Conversion {
    public static LocalDate toLocalDate(Date date){
        LocalDate fecha = null;
        if (date != null){   //convierte si no es null
            fecha = new java.sql.Date(date.getTime()).toLocalDate();
        }
        return fecha;
    }

    public static LocalDate toLocalDate(java.sql.Date sqlDate){
        LocalDate fecha = null;
        if (sqlDate != null){   //convierte si no es null
            fecha = sqlDate.toLocalDate();
        }
        return fecha;
    }

    public static Date toDate(LocalDate localDate){
        Date fecha = null;
        if (localDate != null){   //convierte si no es null
            fecha = java.sql.Date.valueOf(localDate);
        }
        return fecha;
    }

    public static Date toDate(java.sql.Date sqlDate) {
        Date javaDate = null;
        if (sqlDate != null) {
            javaDate = new Date(sqlDate.getTime());
        }
        return javaDate;
    }

    public static java.sql.Date toSqlDate(LocalDate localDate){
        java.sql.Date fecha = null;
        if (localDate != null){   //convierte si no es null
            fecha = java.sql.Date.valueOf(localDate);
        }
        return fecha;
    }

    public static java.sql.Date toSqlDate(Date date){
        java.sql.Date fecha = null;
        if (date != null){   //convierte si no es null
            fecha = (java.sql.Date) date;
        }
        return fecha;
    }

    public static int toint(String string){
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e ) {
            return 0;
        }
    }

    public static Integer toInteger(String string){
        try {
            return Integer.valueOf(string);
        }
        catch (NumberFormatException e ) {
            return 0;
        }
    }

    public static double todouble(String string){
        try {
            return Double.parseDouble(string);
        }
        catch (NumberFormatException e ) {
            return 0.0;
        }
    }

    public static Double toDouble(String string){
        try {
            return Double.valueOf(string);
        }
        catch (NumberFormatException e ) {
            return 0.0;
        }
    }

    public static long tolong(String string){
        try {
            return Long.parseLong(string);
        }
        catch (NumberFormatException e ) {
            return 0L;
        }
    }

    public static Long toLong(String string){
        try {
            return Long.valueOf(string);
        }
        catch (NumberFormatException e ) {
            return 0L;
        }
    }
}
