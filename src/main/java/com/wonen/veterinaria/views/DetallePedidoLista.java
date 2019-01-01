package main.java.com.wonen.veterinaria.views;

import main.java.com.wonen.veterinaria.model.DetallePedido;
import main.java.com.wonen.veterinaria.model.Pedido;
import main.java.com.wonen.veterinaria.model.TipoPedido;
import main.java.com.wonen.veterinaria.model.TipoProducto;
import main.java.com.wonen.veterinaria.repository.EntidadRepository;
import main.java.com.wonen.veterinaria.repository.EntidadRepositoryRepositoryBaseDatos;
import main.java.com.wonen.veterinaria.service.Conversion;
import main.java.com.wonen.veterinaria.views.comun.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetallePedidoLista extends JDialog implements ListaJDialog {
    private JTextField tfl1;
    private Object entTfl1;
    private JTextField tfl2;
    private JTextField tfl3;
    private JTable tbl1;
    private JButton btn0;
    private JPanel contentPane;
    private JButton btn1;
    private JButton btn2;
    private JButton btn3;
    private JButton btn9;
    private JComboBox cbo1;
    private JButton btn8;
    private boolean cambio;


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

    public DetallePedidoLista() {
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
        //MODIFICAR
        this.cargaLimpiaControles();
        alBuscar();
        btn0.setText("Buscar");
        btn0.setVisible(false);
        btn1.setText("Crear");
        btn2.setText("Editar");
        btn3.setText("Borrar");
        btn8.setText("Seleccionar");
        btn8.setVisible(false);
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
        cambio = false;
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        entTfl1 = rep.read(Pedido.class, (int) this.valEntidad);
        tfl1.setText(String.valueOf(((Pedido) this.entTfl1).getCodigo()));
        tfl1.setEditable(false);
        tfl2.setText(((Pedido) this.entTfl1).getCliente().getNombre());
        tfl2.setEditable(false);
        tfl3.setText(((Pedido) this.entTfl1).getMascota().getNombre());
        tfl3.setEditable(false);
        //cbo1
        Object[] cbotext = UtilitariosSwing.arrEnumConTodos(TipoPedido.class);
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(cbotext);
        cbo1.setModel(comboBoxModel);
        cbo1.setSelectedItem(((Pedido) this.entTfl1).getTipoPedido());
        cbo1.setEnabled(false);
        //tbl1
        DefaultTableModel tableModel = new DefaultTableModel(0,4);
        Object[] cabecera = new Object[]{"item", "Producto","Cod. Producto", "Descripcion", "Precio Soles", "Cantidad", "Total Soles", "Tipo Producto"};
        tableModel.setColumnIdentifiers(cabecera);
        tbl1.setModel(tableModel);
        Map<Object, Color> relColor = new HashMap<>();
        relColor.put(TipoProducto.ARTICULO, Color.WHITE);
        relColor.put(TipoProducto.SERVICIO, Color.CYAN);
        relColor.put(TipoProducto.ACTIVO, Color.YELLOW);
        tbl1.getColumnModel().getColumn(0).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 7, relColor ));
        tbl1.getColumnModel().getColumn(1).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 7, relColor ));
        tbl1.getColumnModel().getColumn(2).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 7, relColor ));
        tbl1.getColumnModel().getColumn(3).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 7, relColor ));
        tbl1.getColumnModel().getColumn(4).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 7, relColor ));
        tbl1.getColumnModel().getColumn(5).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 7, relColor ));
        tbl1.getColumnModel().getColumn(6).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 7, relColor ));
        tbl1.getColumnModel().getColumn(7).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 7, relColor ));
        UtilitariosSwing.escondeColumnaJTable(tbl1,1);
    }

    @Override
    public void alBuscar() {
        //MODIFICAR
        List<DetallePedido> listaBus;
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        String sqlWhere = "(codpedido = ?) ";
        Object[] param = {Conversion.toint(tfl1.getText())};
        listaBus = rep.read( DetallePedido.class, sqlWhere, param);

        DefaultTableModel tableModel = (DefaultTableModel) tbl1.getModel();
        tableModel.setRowCount(0);
        for (DetallePedido ent : listaBus){
            //"item", "Producto" NV,"Cod. Producto", "Descripcion", "Precio Soles", "Cantidad", "Total Soles", "Tipo Producto"
            Object[] fila = new Object[]{ent.getItem(), ent.getProducto(), ent.getProducto().getCodigo(), ent.getDescripcion(), ent.getPrecioSoles(), ent.getCantidad(), ent.getTotalSoles(), ent.getProducto().getTipoProducto()};
            tableModel.addRow(fila);
        }
    }

    @Override
    public void alCrear() {
        //MODIFICAR
        DetallePedidoEdita dialog = new DetallePedidoEdita();
        Object[] otrosP = {(Pedido) entTfl1};
        Object respuesta = dialog.showConfirmJDialog(ModoEdicion.INSERT, null, otrosP);
        if (respuesta != null){
            cambio = true;
            this.alBuscar(); //refresca
        }
    }

    @Override
    public void alEditar() {
        //MODIFICAR
        if (tbl1.getSelectedRow() >= 0) {
            DetallePedidoEdita dialog = new DetallePedidoEdita();
            Object[] otrosP = {(Pedido) entTfl1};
            Object respuesta = dialog.showConfirmJDialog(ModoEdicion.UPDATE, tbl1.getValueAt(tbl1.getSelectedRow(),0), otrosP);
            if (respuesta != null){
                cambio = true;
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
            DetallePedidoEdita dialog = new DetallePedidoEdita();
            Object[] otrosP = {(Pedido) entTfl1};
            Object respuesta = dialog.showConfirmJDialog(ModoEdicion.DELETE, tbl1.getValueAt(tbl1.getSelectedRow(),0), otrosP);
            if (respuesta != null){
                cambio = true;
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
        //MODIFICAR
        this.retorno = (cambio ? true : null);
        dispose();
    }


    public static void main(String[] args) {
        DetallePedidoLista dialog = new DetallePedidoLista();
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        Pedido pedido = rep.read( Pedido.class, 100);
        Object respuesta = dialog.showConfirmJDialog(ModoLista.LIST_CRUD, pedido.getCodigo(), null);
        if (respuesta != null){
            System.out.println(respuesta);
        }
    }
}
