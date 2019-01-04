package main.java.com.wonen.veterinaria.views;

import com.github.lgooddatepicker.components.DatePicker;
import main.java.com.wonen.veterinaria.model.*;
import main.java.com.wonen.veterinaria.repository.EntidadRepository;
import main.java.com.wonen.veterinaria.repository.EntidadRepositoryRepositoryBaseDatos;
import main.java.com.wonen.veterinaria.service.Conversion;
import main.java.com.wonen.veterinaria.service.Info;
import main.java.com.wonen.veterinaria.views.comun.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PedidoLista extends JDialog implements ListaJDialog {
    private JPanel contentPane;
    private JTextField tfl1;
    private JTextField tfl2;
    private JTextField tfl3;
    private JTextField tfl4;
    private JTable tbl1;
    private JButton btn0;
    private JButton btn1;
    private JButton btn2;
    private JButton btn3;
    private JButton btn8;
    private JButton btn9;
    private JButton btnD;
    private JButton btnA;
    private JButton btnP;
    private JComboBox cbo1;
    private JComboBox cbo2;
    private JCheckBox chk1;
    private JCheckBox chk2;
    private DatePicker dtp1;
    private DatePicker dtp2;
    private DatePicker dtp3;
    private DatePicker dtp4;

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
        btnD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alDetalle();
            }
        });
        btnA.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alAtender();
            }
        });
        btnP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alPagar();
            }
        });

        chk1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alChkFecProgramada();
            }
        });
        chk2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                alChkFecPagado();
            }
        });
    }

    private void alChkFecProgramada(){
        if (chk1.isSelected()){
            dtp1.setEnabled(true);
            dtp2.setEnabled(true);
        }
        if (!chk1.isSelected()){
            dtp1.setEnabled(false);
            dtp2.setEnabled(false);
        }
    }

    private void alChkFecPagado(){
        if (chk2.isSelected()){
            dtp3.setEnabled(true);
            dtp4.setEnabled(true);
        }
        if (!chk2.isSelected()){
            dtp3.setEnabled(false);
            dtp4.setEnabled(false);
        }
    }

    @Override
    public String valoresIniciales() {
        //MODIFICAR
        this.cargaLimpiaControles();
        alBuscar();
        btn0.setText("Buscar");
        btn1.setText("Crear");
        btn2.setText("Editar");
        btn3.setText("Anular / Activar");
        btn8.setText("Seleccionar");
        btnD.setText("Items del Pedido");
        btnA.setText("Atender / Desatender");
        btnP.setText("Pagar");
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
        tfl3.setDocument(new MascaraTextField("", 0, 255, TipoMascaraTextField.TODO));
        tfl4.setDocument(new MascaraTextField("", 0, 255, TipoMascaraTextField.TODO));
        //cbos
        Object[] cbotext = UtilitariosSwing.arrEnumConTodos(TipoPedido.class);
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(cbotext);
        cbo1.setModel(comboBoxModel);
        Object[] cbotext2 = UtilitariosSwing.arrEnumConTodos(EstadoPedido.class);
        DefaultComboBoxModel comboBoxModel2 = new DefaultComboBoxModel(cbotext2);
        cbo2.setModel(comboBoxModel2);
        //Check y DatePickers
        chk1.setSelected(true);
        dtp1.getSettings().setFormatForDatesCommonEra("dd/MM/yyyy");
        dtp1.setDate(LocalDate.now().minusDays(7));
        dtp1.setEnabled(true);
        dtp2.getSettings().setFormatForDatesCommonEra("dd/MM/yyyy");
        dtp2.setDate(LocalDate.now().plusDays(7));
        dtp2.setEnabled(true);

        chk2.setSelected(false);
        dtp3.getSettings().setFormatForDatesCommonEra("dd/MM/yyyy");
        dtp3.setDate(null);
        dtp3.setEnabled(false);
        dtp4.getSettings().setFormatForDatesCommonEra("dd/MM/yyyy");
        dtp4.setDate(null);
        dtp4.setEnabled(false);

        //tbl1
        DefaultTableModel tableModel = new DefaultTableModel(0,8);
        Object[] cabecera = new Object[]{"Codigo", "Mascota", "Cliente", "Tipo", "Fec. Program.", "Total Soles",
                                        "Pagado", "Fec. Pagado", "Fec. Atendido", "Fec. Creado", "Fec. Anulado",
                                        "Doc. Pago", "Estado", "Observacion"};
        tableModel.setColumnIdentifiers(cabecera );
        tbl1.setModel(tableModel);
        Map<Object, Color> relColor = new HashMap<>();
        relColor.put(EstadoPedido.CREADO, Color.WHITE);
        relColor.put(EstadoPedido.PROGRAMADO, Color.CYAN);
        relColor.put(EstadoPedido.ATENDIDO, Color.YELLOW);
        relColor.put(EstadoPedido.PAGADO, Color.GREEN);
        relColor.put(EstadoPedido.ANULADO, Color.RED);
        Map<Object, Color> relColor2 = new HashMap<>();
        relColor2.put(EstadoPedido.CREADO, Color.BLACK);
        relColor2.put(EstadoPedido.PROGRAMADO, Color.BLACK);
        relColor2.put(EstadoPedido.ATENDIDO, Color.BLACK);
        relColor2.put(EstadoPedido.PAGADO, Color.BLACK);
        relColor2.put(EstadoPedido.ANULADO, Color.WHITE);
        tbl1.getColumnModel().getColumn(0).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(1).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(2).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(3).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(4).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(5).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(6).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(7).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(8).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(9).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(10).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(11).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(12).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        tbl1.getColumnModel().getColumn(13).setCellRenderer(new EstiloCeldaJTable(null, null, 12, relColor2, 12, relColor ));
        //tbl1.getColumnModel().getColumn(3).setCellRenderer(new EstiloCeldaJTable());
        //tbl1.setDefaultRenderer(Object.class, new EstiloCeldaJTable());
        tbl1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        //tbl1.scrollRectToVisible(tbl1.getCellRect(0, 8, true));
        tbl1.setDefaultEditor(Object.class, null);
    }

    @Override
    public void alBuscar() {
        //MODIFICAR
        List<Pedido> listaBus;  //busqueda
        Pedido entPla = new Pedido();  //plantilla
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        String sqlWhere = "(codigo >= ? or ? = 0) " +
                "and (observacion like ? or ? = '%%') " +
                "and (fecProgramada >= ? or isnull( ? )) " +
                "and (fecProgramada <= ? or isnull( ? )) " +
                "and (fecPagado >= ? or isnull( ? )) " +
                "and (fecPagado <= ? or isnull( ? )) " +
                "and (tipopedido = ? or ? = '(Todos)') " +
                "and (estadoPedido = ? or ? = '(Todos)') ";

        Class[] tipo = {Info.getClase(entPla.getCodigo()), Info.getClase(entPla.getCodigo()),
                Info.getClase(entPla.getObservacion()), Info.getClase(entPla.getObservacion()),
                Info.getClase(entPla.getFecProgramada()), Info.getClase(entPla.getFecProgramada()),
                Info.getClase(entPla.getFecProgramada()), Info.getClase(entPla.getFecProgramada()),
                Info.getClase(entPla.getFecPagado()), Info.getClase(entPla.getFecPagado()),
                Info.getClase(entPla.getFecPagado()), Info.getClase(entPla.getFecPagado()),
                cbo1.getSelectedItem().getClass(), cbo1.getSelectedItem().getClass(),
                cbo2.getSelectedItem().getClass(), cbo2.getSelectedItem().getClass()};

        Object[] param = {Conversion.toint(tfl1.getText()), Conversion.toint(tfl1.getText()),
                "%" + tfl3.getText() + "%", "%" + tfl3.getText() + "%",
                (chk1.isSelected() ? dtp1.getDate() : null ), (chk1.isSelected() ? dtp1.getDate() : null ),
                (chk1.isSelected() ? dtp2.getDate() : null ), (chk1.isSelected() ? dtp2.getDate() : null ),
                (chk2.isSelected() ? dtp3.getDate() : null ), (chk2.isSelected() ? dtp3.getDate() : null ),
                (chk2.isSelected() ? dtp4.getDate() : null ), (chk2.isSelected() ? dtp4.getDate() : null ),
                cbo1.getSelectedItem() , cbo1.getSelectedItem(),
                cbo2.getSelectedItem() , cbo2.getSelectedItem()};
        listaBus = rep.read( entPla.getClass(), sqlWhere, tipo, param);

        DefaultTableModel tableModel = (DefaultTableModel) tbl1.getModel();
        tableModel.setRowCount(0);
        for (Pedido ent : listaBus){
            boolean correcto = true;
            if (tfl2.getText().length() > 0){
                Cliente entPla2 = new Cliente();  //plantilla
                String sqlWhere2 = "nombre like ?";
                Class[] tipo2 = {Info.getClase(entPla2.getNombre())};
                Object[] param2 = {"%" + tfl2.getText() + "%"};
                List<Cliente> cliLista = rep.read( entPla2.getClass(), sqlWhere2, tipo2, param2);
                correcto = (cliLista.size()>0 ? true : false);
            }
            if (tfl4.getText().length() > 0){
                Mascota entPla3 = new Mascota();  //plantilla
                String sqlWhere3 = "nombre like ?";
                Class[] tipo3 = {Info.getClase(entPla3.getNombre())};
                Object[] param3 = {"%" + tfl4.getText() + "%"};
                List<Mascota> masLista = rep.read( entPla3.getClass(), sqlWhere3, tipo3, param3);
                correcto = (masLista.size()>0 ? true : false);
            }
            if (correcto) {
                //"Codigo", "Mascota", "Cliente", "Tipo", "Fec. Program.", "Total Soles",
                //"Pagado", "Fec. Pagado", "Fec. Atendido", "Fec. Creado", "Fec. Anulado",
                //"Doc. Pago", "Estado", "Observacion"
                Object[] fila = new Object[]{ent.getCodigo(), ent.getMascota().getNombre(), ent.getCliente().getNombre(), ent.getTipoPedido(), ent.getFecProgramada(), ent.getTotalSoles(),
                        ent.getPagadoSoles(), ent.getFecPagado(), ent.getFecAtendido(), ent.getFecCreado(), ent.getFecAnulado(),
                        ent.getTipoDocumento(), ent.getEstadoPedido(), ent.getObservacion()};
                tableModel.addRow(fila);
            }
        }

    }

    @Override
    public void alCrear() {
        //MODIFICAR
        PedidoEdita dialog = new PedidoEdita();
        Object respuesta = dialog.showConfirmJDialog(ModoEdicion.INSERT, null, null);
        if (respuesta != null){
            this.alBuscar(); //refresca
        }
    }

    @Override
    public void alEditar() {
        //MODIFICAR
        if (tbl1.getSelectedRow() >= 0) {
            ModoEdicion modoEdicion;
            EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
            Pedido ped = rep.read(Pedido.class, tbl1.getValueAt(tbl1.getSelectedRow(),0));
            if (ped.getEstadoPedido() == EstadoPedido.ATENDIDO || ped.getEstadoPedido() == EstadoPedido.PAGADO || ped.getEstadoPedido() == EstadoPedido.ANULADO){
                JOptionPane.showMessageDialog(null, "No se puede Editar si esta ATENDIDO, PAGADO O ANULADO");
                modoEdicion = ModoEdicion.READ_ONLY;
            }else{
                modoEdicion = ModoEdicion.UPDATE;
            }
            PedidoEdita dialog = new PedidoEdita();
            Object respuesta = dialog.showConfirmJDialog(modoEdicion , tbl1.getValueAt(tbl1.getSelectedRow(),0), null);
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
            EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
            Pedido ped = rep.read(Pedido.class, tbl1.getValueAt(tbl1.getSelectedRow(),0));
            if (ped.getEstadoPedido() != EstadoPedido.ANULADO){
                if (JOptionPane.showConfirmDialog(null, "Desea Anular Pedido?","Anular Pedido", JOptionPane.YES_NO_OPTION) == 0) {
                    ped.setEstadoPedido(EstadoPedido.ANULADO);
                    ped.setFecAnulado(LocalDate.now());
                    if ( rep.update(ped) == 0 ){
                        JOptionPane.showMessageDialog(null, "No se pudo ANULAR Pedido");
                    }else{
                        this.alBuscar(); //refresca
                    }
                }
            }else{
                if (JOptionPane.showConfirmDialog(null, "Desea Activar Pedido?","Activar Pedido", JOptionPane.YES_NO_OPTION) == 0) {
                    EstadoPedido estPed;
                    if (ped.getFecPagado() != null){
                        estPed = EstadoPedido.PAGADO;
                    }else if (ped.getFecAtendido() != null){
                        estPed = EstadoPedido.ATENDIDO;
                    }else if (ped.getFecProgramada() != null){
                        estPed = EstadoPedido.PROGRAMADO;
                    }else {
                        estPed = EstadoPedido.CREADO;
                    }
                    ped.setEstadoPedido(estPed);
                    ped.setFecAnulado(null);
                    if ( rep.update(ped) == 0 ){
                        JOptionPane.showMessageDialog(null, "No se pudo Activar Pedido");
                    }else{
                        this.alBuscar(); //refresca
                    }
                }
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
            Pedido ent = rep.read( Pedido.class, tbl1.getValueAt(tbl1.getSelectedRow(),0)); //PK
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

    private void alDetalle() {
        //MODIFICAR
        if (tbl1.getSelectedRow() >= 0) {
            ModoLista modoLis;
            EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
            Pedido ped = rep.read(Pedido.class, tbl1.getValueAt(tbl1.getSelectedRow(),0));
            if (ped.getEstadoPedido() == EstadoPedido.ATENDIDO || ped.getEstadoPedido() == EstadoPedido.PAGADO || ped.getEstadoPedido() == EstadoPedido.ANULADO){
                JOptionPane.showMessageDialog(null, "No se puede Editar el Detalle del Pedido si esta ATENDIDO, PAGADO o ANULADO");
                modoLis = ModoLista.LIST_ONLY;
            }else{
                modoLis = ModoLista.LIST_CRUD;
            }
            DetallePedidoLista dialog = new DetallePedidoLista();
            Object respuesta = dialog.showConfirmJDialog(modoLis , ped.getCodigo(), null);
            if (respuesta != null){
                Pedido ped2 = rep.read(Pedido.class, ped.getCodigo());
                String sqlWhere2 = "(codpedido = ?) ";
                Class[] tipo2 = {Info.getClase(ped.getCodigo())};
                Object[] param2 = {ped.getCodigo()};
                List<DetallePedido> detPed2 = rep.read( DetallePedido.class, sqlWhere2, tipo2, param2);
                double totSoles = 0;
                for (DetallePedido ent : detPed2){
                    totSoles += ent.getTotalSoles();
                }
                ped2.setTotalSoles(totSoles);
                rep.update(ped2);
                this.alBuscar(); //refresca
            }
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione dato valido");
        }
    }

    private void alAtender() {
        //MODIFICAR
        if (tbl1.getSelectedRow() >= 0) {
            EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
            Pedido ped = rep.read(Pedido.class, tbl1.getValueAt(tbl1.getSelectedRow(),0));
            if (ped.getEstadoPedido() == EstadoPedido.PROGRAMADO ){
                if (JOptionPane.showConfirmDialog(null, "Desea Atender Pedido?","Atender Pedido", JOptionPane.YES_NO_OPTION) == 0) {
                    ped.setEstadoPedido(EstadoPedido.ATENDIDO);
                    ped.setFecAtendido(LocalDate.now());
                    if ( rep.update(ped) == 0 ){
                        JOptionPane.showMessageDialog(null, "No se pudo ATENDER Pedido");
                    }else{
                        this.alBuscar(); //refresca
                    }
                }
            }else if (ped.getEstadoPedido() == EstadoPedido.ATENDIDO ){
                if (JOptionPane.showConfirmDialog(null, "Desea Desatender Pedido?","Desatender Pedido", JOptionPane.YES_NO_OPTION) == 0) {
                    ped.setEstadoPedido(EstadoPedido.PROGRAMADO);
                    ped.setFecAtendido(null);
                    if ( rep.update(ped) == 0 ){
                        JOptionPane.showMessageDialog(null, "No se pudo Desatender Pedido");
                    }else{
                        this.alBuscar(); //refresca
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null, "Solo se puede ATENDER los PROGRAMADOS. Solo de puede Desatender los ATENDIDOS");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Seleccione dato valido");
        }
    }

    private void alPagar() {
        //MODIFICAR
        if (tbl1.getSelectedRow() >= 0) {
            EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
            Pedido ped = rep.read(Pedido.class, tbl1.getValueAt(tbl1.getSelectedRow(),0));
            if (ped.getEstadoPedido() == EstadoPedido.ATENDIDO || ped.getEstadoPedido() == EstadoPedido.PAGADO ){
                PedidoEdita dialog = new PedidoEdita();
                Object respuesta = dialog.showConfirmJDialog(ModoEdicion.CUSTOM_1 , ped.getCodigo(), null);
                if (respuesta != null){
                    Pedido ped2 = rep.read(Pedido.class, ped.getCodigo());
                    if (ped2.getPagadoSoles() > 0.0){
                        ped2.setEstadoPedido(EstadoPedido.PAGADO);
                        ped2.setFecPagado(LocalDate.now());
                    }else{
                        ped2.setEstadoPedido(EstadoPedido.ATENDIDO);
                        ped2.setFecPagado(null);
                    }
                    rep.update(ped2);
                    this.alBuscar(); //refresca
                }
            }else{
                JOptionPane.showMessageDialog(null, "No se puede Pagar a menos que este ATENDIDO O PAGADO");
            }

        }else{
            JOptionPane.showMessageDialog(null, "Seleccione dato valido");
        }
    }

    public static void main(String[] args) {
        PedidoLista dialog = new PedidoLista();
        Object respuesta = dialog.showConfirmJDialog(ModoLista.LIST_CRUD, null, null);
        if (respuesta != null){
            System.out.println(respuesta);
        }
    }
}
