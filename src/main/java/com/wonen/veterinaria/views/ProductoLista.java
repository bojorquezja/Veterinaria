package main.java.com.wonen.veterinaria.views;

import main.java.com.wonen.veterinaria.model.*;
import main.java.com.wonen.veterinaria.repository.EntidadRepository;
import main.java.com.wonen.veterinaria.repository.EntidadRepositoryRepositoryBaseDatos;
import main.java.com.wonen.veterinaria.service.Info;
import main.java.com.wonen.veterinaria.views.comun.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductoLista extends JDialog implements ListaJDialog {
    private JTextField tfl1;
    private JTextField tfl2;
    private JTable tbl1;
    private JButton btn0;
    private JPanel contentPane;
    private JButton btn1;
    private JButton btn2;
    private JButton btn3;
    private JButton btn9;
    private JComboBox cbo1;
    private JButton btn8;

    private ModoLista modoLista;    //LIST, SELECT
    private Object valEntidad;     //entidad para modoEdicion
    private Object[] otrosParam;    //opcionales
    private Object retorno; //lo que debe regresar la pantalla. null significa sin cambios

    @Override
    public Object showConfirmJDialog(ModoLista modoLista, Object valEntidad, Object[] otrosParam){
        this.modoLista = modoLista;
        this.valEntidad = valEntidad;
        this.otrosParam = otrosParam;
        String error = this.valoresIniciales();
        if (error != null){
            JOptionPane.showMessageDialog(null, error);
            dispose();
            return null;
        }
        pack();
        setVisible(true);
        return retorno;
    }

    public ProductoLista() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(btn8);

        // call alCancelar() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                alCancelar();
            }
        });

        // call alCancelar() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alCancelar();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        btn0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alBuscar();
            }
        });
        btn8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alSeleccionar();
            }
        });
        btn9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alCancelar();
            }
        });
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alCrear();
            }
        });
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alEditar();
            }
        });
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alBorrar();
            }
        });
        tbl1.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTable table =(JTable) e.getSource();
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                if (e.getClickCount() == 2 && table.getSelectedRow() != -1 && btn8.isVisible()) {
                    alSeleccionar();
                }
            }
        });
    }

    @Override
    public String valoresIniciales() {
        this.cargaLimpiaControles();
        alBuscar();
        btn0.setText("Buscar");
        btn1.setText("Crear");
        btn2.setText("Editar");
        btn3.setText("Borrar");
        btn8.setText("Seleccionar");
        switch(modoLista){
            case LIST_CRUD:
                this.setTitle("Lista de datos");
                btn1.setVisible(true);
                btn2.setVisible(true);
                btn3.setVisible(true);
                btn8.setVisible(false);
                return null;
            case LIST_ONLY:
                this.setTitle("Lista de datos");
                btn1.setVisible(false);
                btn2.setVisible(false);
                btn3.setVisible(false);
                btn8.setVisible(false);
                return null;
            case SELECT_CRUD:
                this.setTitle("Seleccione dato");
                btn1.setVisible(true);
                btn2.setVisible(true);
                btn3.setVisible(true);
                btn8.setVisible(true);
                return null;
            case SELECT_ONLY:
                this.setTitle("Seleccione dato");
                btn1.setVisible(false);
                btn2.setVisible(false);
                btn3.setVisible(false);
                btn8.setVisible(true);
                return null;
        }
        return "error en seleccion de modo lista";
    }

    @Override
    public void cargaLimpiaControles(){
        //MODIFICAR
        tfl1.setDocument(new MascaraTextField("", 0, 20, TipoMascaraTextField.NUMEROS_Y_LETRAS_MAYUSC));
        tfl2.setDocument(new MascaraTextField("", 0, 255, TipoMascaraTextField.TODO));
        //cbo1
        Object[] cbotext = UtilitariosSwing.arrEnumConTodos(TipoProducto.class);
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(cbotext);
        cbo1.setModel(comboBoxModel);
        cbo1.setEnabled(true);
        if (this.valEntidad != null){
            cbo1.setSelectedItem(this.valEntidad);
            cbo1.setEnabled(false);
        }
        //tbl1
        DefaultTableModel tableModel = new DefaultTableModel(0,4);
        Object[] cabecera = new Object[]{"Codigo", "Descripcion", "Precio Soles", "Tipo Producto"};
        tableModel.setColumnIdentifiers(cabecera );
        tbl1.setModel(tableModel);
        Map<Object, Color> relColor = new HashMap<>();
        relColor.put(TipoProducto.ARTICULO, Color.WHITE);
        relColor.put(TipoProducto.SERVICIO, Color.CYAN);
        relColor.put(TipoProducto.ACTIVO, Color.YELLOW);
        Map<Object, Color> relColor2 = new HashMap<>();
        relColor2.put(TipoProducto.ARTICULO, Color.BLACK);
        relColor2.put(TipoProducto.SERVICIO, Color.BLACK);
        relColor2.put(TipoProducto.ACTIVO, Color.BLACK);
        tbl1.getColumnModel().getColumn(0).setCellRenderer(new EstiloCeldaJTable(null, null, 3, relColor2, 3, relColor ));
        tbl1.getColumnModel().getColumn(1).setCellRenderer(new EstiloCeldaJTable(null, null, 3, relColor2, 3, relColor ));
        tbl1.getColumnModel().getColumn(2).setCellRenderer(new EstiloCeldaJTable(null, null, 3, relColor2, 3, relColor ));
        tbl1.getColumnModel().getColumn(3).setCellRenderer(new EstiloCeldaJTable(null, null, 3, relColor2, 3, relColor ));
        tbl1.setDefaultEditor(Object.class, null);
    }

    @Override
    public void alBuscar() {
        //MODIFICAR
        List<Producto> listaBus;
        Producto entPla = new Producto();
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        String sqlWhere = "(codigo like ? or ? = '%%') " +
                "and (descripcion like ? or ? = '%%') " +
                "and (tipoproducto = ? or ? = '(Todos)')";
        Class[] tipo = {Info.getClase(entPla.getCodigo()), Info.getClase(entPla.getCodigo()),
                Info.getClase(entPla.getDescripcion()), Info.getClase(entPla.getDescripcion()),
                cbo1.getSelectedItem().getClass(), cbo1.getSelectedItem().getClass()};
        Object[] param = {"%" + tfl1.getText() + "%", "%" + tfl1.getText() + "%",
                "%" + tfl2.getText() + "%", "%" + tfl2.getText() + "%",
                cbo1.getSelectedItem() , cbo1.getSelectedItem()};

        listaBus = rep.read( entPla.getClass(), sqlWhere, tipo, param);

        DefaultTableModel tableModel = (DefaultTableModel) tbl1.getModel();
        tableModel.setRowCount(0);
        for (Producto ent : listaBus){
            //codigo, descripcion, preciosoles, tipoproducto
            Object[] fila = new Object[]{ent.getCodigo(), ent.getDescripcion(), ent.getPrecioSoles(), ent.getTipoProducto()};
            tableModel.addRow(fila);
        }
    }

    @Override
    public void alCrear() {
        //MODIFICAR
        ProductoEdita dialog = new ProductoEdita();
        Object respuesta = dialog.showConfirmJDialog(ModoEdicion.INSERT, null, null);
        if (respuesta != null){
            this.alBuscar(); //refresca
        }
    }

    @Override
    public void alEditar() {
        //MODIFICAR
        if (tbl1.getSelectedRow() >= 0) {
            ProductoEdita dialog = new ProductoEdita();
            Object respuesta = dialog.showConfirmJDialog(ModoEdicion.UPDATE, tbl1.getValueAt(tbl1.getSelectedRow(),0), null);
            if (respuesta != null){
                this.alBuscar(); //refresca
            }
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione dato valido");
        }
    }

    @Override
    public void alBorrar() {
        //MODIFICAR
        if (tbl1.getSelectedRow() >= 0) {
            ProductoEdita dialog = new ProductoEdita();
            Object respuesta = dialog.showConfirmJDialog(ModoEdicion.DELETE, tbl1.getValueAt(tbl1.getSelectedRow(),0), null);
            if (respuesta != null){
                this.alBuscar(); //refresca
            }
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione dato valido");
        }
    }

    @Override
    public void alSeleccionar() {
        //MODIFICAR
        if (tbl1.getSelectedRow() >= 0) {
            this.retorno = tbl1.getValueAt(tbl1.getSelectedRow(),0);    //PK
            dispose();
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione dato valido");
        }
    }

    @Override
    public void alCancelar() {
        this.retorno = null;
        dispose();
    }

    public static void main(String[] args) {
        ProductoLista dialog = new ProductoLista();
        Object respuesta = dialog.showConfirmJDialog(ModoLista.LIST_CRUD, null, null);
        if (respuesta != null){
            System.out.println(respuesta);
        }
    }
}
