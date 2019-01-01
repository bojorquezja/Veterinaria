package main.java.com.wonen.veterinaria.views.comun;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EstiloCeldaJTable extends DefaultTableCellRenderer {
    //"": no aplica estilo, null: aplica estilo "dd/MM/yyyy", otro: aplica estilo
    private String formatoFecha;
    //"": no aplica estilo, null: no aplica estilo "", otro: aplica estilo
    private String formatoNumero;
    //compara cada fila de la columna indicada con los valores de colorLetra
    //null: no aplica estilo, otro: aplica estilo
    private int columnaComparaColorLetra;
    private Map<Object, Color> colorLetra;
    //compara cada fila de la columna indicada con los valores de colorFondo
    //null: no aplica estilo, otro: aplica estilo
    private int columnaComparaColorFondo;
    private Map<Object, Color> colorFondo;

    public EstiloCeldaJTable() {
        this.formatoFecha = "dd/MM/yyyy";
        this.formatoNumero = "";
        this.colorLetra = new HashMap<>();
        this.colorFondo = new HashMap<>();
    }

    public EstiloCeldaJTable(String formatoFecha, String formatoNumero, int columnaComparaColorLetra, Map<Object, Color> colorLetra, int columnaComparaColorFondo, Map<Object, Color> colorFondo) {
        //TODO permitir cambio estilo numeros
        this.formatoFecha = (formatoFecha == null ? "dd/MM/yyyy": formatoFecha);
        this.formatoNumero = (formatoNumero == null ? "": formatoNumero);
        this.columnaComparaColorLetra = columnaComparaColorLetra;
        this.colorLetra = (colorLetra == null ? new HashMap<>(): colorLetra);
        this.columnaComparaColorFondo = columnaComparaColorFondo;
        this.colorFondo = (colorFondo == null ? new HashMap<>(): colorFondo);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if( value instanceof Date && this.formatoFecha.length() >0 ) {
            SimpleDateFormat f1 = new SimpleDateFormat(this.formatoFecha);
            value = f1.format((Date) value);
        }
        if( value instanceof LocalDate && this.formatoFecha.length() >0 ) {
            DateTimeFormatter f2 = DateTimeFormatter.ofPattern(this.formatoFecha);
            value = f2.format((LocalDate)value);
        }
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        if (this.colorLetra.size()>0) {
            Color colL = this.colorLetra.get(table.getModel().getValueAt(row, columnaComparaColorLetra));
            if (colL != null) {
                this.setForeground(colL);
            }
        }

        if (this.colorFondo.size()>0) {
            Color colF = this.colorFondo.get(table.getModel().getValueAt(row, columnaComparaColorFondo));
            if (colF != null) {
                this.setBackground(colF);
            }
        }

        if( value instanceof Number ) {
            this.setHorizontalAlignment(SwingConstants.RIGHT);
        }

        return this;
    }
}
