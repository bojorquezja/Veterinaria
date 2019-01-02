package main.java.com.wonen.veterinaria.views;

import main.java.com.wonen.veterinaria.model.Cliente;
import main.java.com.wonen.veterinaria.model.Mascota;
import main.java.com.wonen.veterinaria.model.TipoCliente;
import main.java.com.wonen.veterinaria.repository.EntidadRepository;
import main.java.com.wonen.veterinaria.repository.EntidadRepositoryRepositoryBaseDatos;
import main.java.com.wonen.veterinaria.views.comun.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoLista extends JDialog implements ListaJDialog {
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

    public PedidoLista() {
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
        tfl1.setDocument(new MascaraTextField("", 0, 20, TipoMascaraTextField.SOLO_NUMEROS_ENTEROS));
        tfl2.setDocument(new MascaraTextField("", 0, 255, TipoMascaraTextField.TODO));
        //cbo1
        Object[] cbotext = UtilitariosSwing.arrEnumConTodos(TipoCliente.class);
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(cbotext);
        cbo1.setModel(comboBoxModel);
        //tbl1
        DefaultTableModel tableModel = new DefaultTableModel(0,8);
        Object[] cabecera = new Object[]{"DNI", "Nombre", "Direccion", "Fecha Creado", "Telefono", "Correo", "Tipo Cliente", "Mascotas"};
        tableModel.setColumnIdentifiers(cabecera );
        tbl1.setModel(tableModel);
        Map<Object, Color> relColor = new HashMap<>();
        relColor.put(TipoCliente.MAYORISTA, Color.ORANGE);
        relColor.put(TipoCliente.FRECUENTE, Color.CYAN);
        relColor.put(TipoCliente.NORMAL, Color.WHITE);
        relColor.put(TipoCliente.VIP, Color.GREEN);
        tbl1.getColumnModel().getColumn(0).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 6, relColor ));
        tbl1.getColumnModel().getColumn(1).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 6, relColor ));
        tbl1.getColumnModel().getColumn(2).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 6, relColor ));
        tbl1.getColumnModel().getColumn(3).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 6, relColor ));
        tbl1.getColumnModel().getColumn(4).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 6, relColor ));
        tbl1.getColumnModel().getColumn(5).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 6, relColor ));
        tbl1.getColumnModel().getColumn(6).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 6, relColor ));
        tbl1.getColumnModel().getColumn(7).setCellRenderer(new EstiloCeldaJTable(null, null, 0, null, 6, relColor ));
        //tbl1.getColumnModel().getColumn(3).setCellRenderer(new EstiloCeldaJTable());
    }

    @Override
    public void alBuscar() {
        //MODIFICAR
        List<Cliente> listaBus;
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        String sqlWhere = "(dni like ? or ? = '%%') " +
                "and (nombre like ? or ? = '%%') " +
                "and (tipoCliente = ? or ? = '(Todos)')";
        Object[] param = {"%" + tfl1.getText() + "%", "%" + tfl1.getText() + "%",
                "%" + tfl2.getText() + "%", "%" + tfl2.getText() + "%",
                cbo1.getSelectedItem() , cbo1.getSelectedItem()};
        listaBus = rep.read( Cliente.class, sqlWhere, param);

        DefaultTableModel tableModel = (DefaultTableModel) tbl1.getModel();
        tableModel.setRowCount(0);
        for (Cliente ent : listaBus){
            String sqlWhere2 = "propietario = ?";
            Object[] param2 = {ent.getDni()};
            List<Mascota> mascotasLista = rep.read( Mascota.class, sqlWhere2, param2);
            String strMascotas = "";
            for (Mascota masc : mascotasLista){
                strMascotas += masc.getNombre() + ", ";
            }
            strMascotas = (strMascotas.length() >= 2 ? strMascotas.substring(0, strMascotas.length()-2) : strMascotas);
            //"DNI", "Nombre", "Direccion", "Fecha Creado", "Telefono", "Correo", "Tipo Cliente", "Mascotas"
            Object[] fila = new Object[]{ent.getDni(), ent.getNombre(), ent.getDireccion(), ent.getFecCreado(),
                    ent.getTelefono(), ent.getCorreo(), ent.getTipoCliente(), strMascotas};
            tableModel.addRow(fila);
        }

        //tbl1.setDefaultRenderer(Object.class, new EstiloCeldaJTable());

    }

    @Override
    public void alCrear() {
        //MODIFICAR
        ClienteEdita dialog = new ClienteEdita();
        Object respuesta = dialog.showConfirmJDialog(ModoEdicion.INSERT, null, null);
        if (respuesta != null){
            this.alBuscar(); //refresca
        }
    }

    @Override
    public void alEditar() {
        //MODIFICAR
        if (tbl1.getSelectedRow() >= 0) {
            ClienteEdita dialog = new ClienteEdita();
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
            ClienteEdita dialog = new ClienteEdita();
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
            EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
            Cliente ent = rep.read( Cliente.class, tbl1.getValueAt(tbl1.getSelectedRow(),0)); //PK
            this.retorno = ent;
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
        PedidoLista dialog = new PedidoLista();
        Object respuesta = dialog.showConfirmJDialog(ModoLista.LIST_CRUD, null, null);
        if (respuesta != null){
            System.out.println(respuesta);
        }
    }
}
