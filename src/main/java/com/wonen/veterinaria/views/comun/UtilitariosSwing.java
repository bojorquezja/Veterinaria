package main.java.com.wonen.veterinaria.views.comun;

import main.java.com.wonen.veterinaria.model.TipoMascota;

import javax.swing.*;
import java.util.jar.JarEntry;

public class UtilitariosSwing {
    public static <T extends Enum<T>> Object[] arrEnumConTodos(Class<T> tipoEnum){
        int cont=0;
        Object[] arrCTod = new Object[tipoEnum.getEnumConstants().length + 1];
        arrCTod[cont++] = "(Todos)";
        for (T cons : tipoEnum.getEnumConstants()) {
            arrCTod[cont++] = cons;
        }
        return arrCTod;
    }

    public static void escondeColumnaJTable(JTable tabla, int numColumna){
        tabla.getColumnModel().getColumn(numColumna).setMinWidth(0);
        tabla.getColumnModel().getColumn(numColumna).setMaxWidth(0);
        tabla.getColumnModel().getColumn(numColumna).setWidth(0);
    }
}
