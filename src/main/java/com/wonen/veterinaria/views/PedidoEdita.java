package main.java.com.wonen.veterinaria.views;

import com.github.lgooddatepicker.components.DatePicker;
import main.java.com.wonen.veterinaria.model.*;
import main.java.com.wonen.veterinaria.repository.EntidadRepository;
import main.java.com.wonen.veterinaria.repository.EntidadRepositoryRepositoryBaseDatos;
import main.java.com.wonen.veterinaria.service.Conversion;
import main.java.com.wonen.veterinaria.service.Info;
import main.java.com.wonen.veterinaria.views.comun.*;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.util.List;

public class PedidoEdita extends JDialog implements EditaJDialog {
    private JPanel contentPane;
    private JButton btn8;
    private JButton btn9;
    private JTextField tfl1;
    private JTextField tfl2;
    private JButton btntfl2;
    private Object entTfl2;
    private JTextField tfl4;
    private JTextField tfl3;
    private Object entTfl3;
    private JTextField tfl5;
    private JTextField tfl6;
    private JComboBox cbo1;
    private JComboBox cbo2;
    private JComboBox cbo3;
    private DatePicker dtp1;
    private DatePicker dtp2;
    private DatePicker dtp3;
    private DatePicker dtp4;
    private DatePicker dtp5;
    private JTabbedPane tabbedPane1;



    private ModoEdicion modoEdicion;   //INSERT, UPDATE, DELETE, READ_ONLY
    private Object pkEntidad;     //entidad para modoEdicion
    private Object[] otrosParam;    //opcionales
    private Object retorno; //lo que debe regresar la pantalla. null significa sin cambios

    @Override
    public Object showConfirmJDialog(ModoEdicion modoEdita, Object pkEntidad, Object[] otrosParam){
        this.modoEdicion = modoEdita;
        this.pkEntidad = pkEntidad;
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


    public PedidoEdita() {
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

        btn8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alAceptar();
            }
        });

        btn9.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                alCancelar();
            }
        });
        btntfl2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selMascota();
            }
        });
        dtp1.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                alFechaProgramada();
            }
        });
    }

    private void alFechaProgramada(){
        if (dtp1.getDate() == null && cbo2.getSelectedItem() == EstadoPedido.PROGRAMADO){
            cbo2.setSelectedItem(EstadoPedido.CREADO);
        }
        if (dtp1.getDate() != null && cbo2.getSelectedItem() == EstadoPedido.CREADO){
            cbo2.setSelectedItem(EstadoPedido.PROGRAMADO);
        }
    }

    @Override
    public String valoresIniciales() {
        //MODIFICA
        this.cargaLimpiaControles();
        switch(modoEdicion){
            case INSERT:
                this.accesoControlPK(false);
                this.accesoControlResto(true);
                this.accesoControlPago(false);
                this.setTitle("Nuevo registro");
                btn8.setText("Insertar");
                btn9.setText("Cancelar");
                return null;
            case UPDATE:
                if (this.cargaDatosEntidad()) {
                    this.accesoControlPK(false);
                    this.accesoControlResto(true);
                    this.accesoControlPago(false);
                    this.setTitle("Actualice registro");
                    btn8.setText("Actualizar");
                    btn9.setText("Cancelar");
                    return null;
                }else{
                    return "Error al cargar datos";
                }
            case DELETE:
                if (this.cargaDatosEntidad()) {
                    this.accesoControlPK(false);
                    this.accesoControlResto(false);
                    this.accesoControlPago(false);
                    this.setTitle("¿Desea borrar registro?");
                    btn8.setText("Borrar");
                    btn9.setText("Cancelar");
                    return null;
                }else{
                    return "Error al cargar datos";
                }
            case READ_ONLY:
                if (this.cargaDatosEntidad()) {
                    this.accesoControlPK(false);
                    this.accesoControlResto(false);
                    this.accesoControlPago(false);
                    this.setTitle("Ver registro");
                    btn8.setText("Aceptar");
                    btn8.setVisible(false);
                    btn9.setText("Cancelar");
                    return null;
                }else{
                    return "Error al cargar datos";
                }
            case CUSTOM_1:
                //PAGAR
                if (this.cargaDatosEntidad()) {
                    this.accesoControlPK(false);
                    this.accesoControlResto(false);
                    this.accesoControlPago(true);
                    this.setTitle("Pague registro");
                    btn8.setText("Pagar");
                    btn9.setText("Cancelar");
                    return null;
                }else{
                    return "Para pagar debe estar ATENDIDO o PAGADO. Error al cargar datos";
                }
        }
        return "error en seleccion de modo edita";
    }

    @Override
    public void cargaLimpiaControles(){
        //MODIFICAR
        tfl1.setDocument(new MascaraTextField("0", 1, 20, TipoMascaraTextField.SOLO_NUMEROS_ENTEROS));
        tfl2.setEditable(false);
        tfl3.setEditable(false);
        tfl4.setDocument(new MascaraTextField("", 0, 11, TipoMascaraTextField.TODO));
        tfl5.setDocument(new MascaraTextField("0.0", 1, 255, TipoMascaraTextField.SOLO_NUMEROS_DECIMALES));
        tfl5.setEditable(false);
        tfl6.setDocument(new MascaraTextField("0.0", 1, 255, TipoMascaraTextField.SOLO_NUMEROS_DECIMALES));

        dtp1.getSettings().setFormatForDatesCommonEra("dd/MM/yyyy");
        dtp1.setDate(LocalDate.now());
        dtp2.getSettings().setFormatForDatesCommonEra("dd/MM/yyyy");
        if (modoEdicion == ModoEdicion.INSERT) {
            dtp2.setDate(LocalDate.now());
        }else{
            dtp2.setDate(null);
        }
        dtp2.setEnabled(false);
        dtp3.getSettings().setFormatForDatesCommonEra("dd/MM/yyyy");
        dtp3.setDate(null);
        dtp3.setEnabled(false);
        dtp4.getSettings().setFormatForDatesCommonEra("dd/MM/yyyy");
        dtp4.setDate(null);
        dtp4.setEnabled(false);
        dtp5.getSettings().setFormatForDatesCommonEra("dd/MM/yyyy");
        dtp5.setDate(null);
        dtp5.setEnabled(false);

        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(TipoPedido.values());
        cbo1.setModel(comboBoxModel);
        DefaultComboBoxModel comboBoxModel2 = new DefaultComboBoxModel(EstadoPedido.values());
        cbo2.setModel(comboBoxModel2);
        cbo2.setEnabled(false);
        DefaultComboBoxModel comboBoxModel3 = new DefaultComboBoxModel(TipoDocumento.values());
        cbo3.setModel(comboBoxModel3);
    }

    @Override
    public void alAceptar() {
        String error = this.errorAlValidarDatosEntidad();
        if ( error == null) {
            if (this.grabaDatosEntidad()) {
                this.retorno = true;
                dispose();
            }
        }else{
            JOptionPane.showMessageDialog(null, error);
        }
    }

    @Override
    public void alCancelar() {
        this.retorno = null;
        dispose();
    }

    private void selMascota() {
        //MODIFICA

        MascotaLista dialog = new MascotaLista();
        Object respuesta = dialog.showConfirmJDialog(ModoLista.SELECT_CRUD, null, null);
        if (respuesta != null){
            int cod = (int) respuesta;
            EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
            Mascota ent = rep.read( Mascota.class, cod); //PK
            tfl2.setText(ent.getNombre());
            entTfl2 = ent;
            tfl3.setText(ent.getPropietario().getNombre());
            entTfl3 = ent.getPropietario();
        }

    }

    @Override
    public void accesoControlPK(boolean permite){
        //MODIFICAR
        tfl1.setEditable(permite);
    }

    @Override
    public void accesoControlResto(boolean permite){
        //MODIFICAR
        btntfl2.setEnabled(permite);
        cbo1.setEnabled(permite);
        dtp1.setEnabled(permite);
        tfl4.setEditable(permite);
    }
    public void accesoControlPago(boolean permite){
        //MODIFICAR
        cbo3.setEnabled(permite);
        tfl6.setEditable(permite);
    }

    @Override
    public boolean cargaDatosEntidad(){
        //MODIFICAR
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        Pedido ent = rep.read( Pedido.class, pkEntidad);
        //# codigo, observacion, fecCreado, fecProgramada, fecAtendido, fecPagado, fecAnulado, totalSoles, pagadoSoles, documento, estadoPedido, tipoPedido, codCliente, codMascota
        //'100', 'mensaje', '2018-02-02', '2018-02-02', '2018-02-02', '2018-02-02', NULL, '100', '99', 'BOLETA', 'PAGADO', 'MEDICO', '1', '9'
        if (ent != null) {
            if (modoEdicion == ModoEdicion.CUSTOM_1 && (ent.getEstadoPedido() != EstadoPedido.ATENDIDO && ent.getEstadoPedido() != EstadoPedido.PAGADO)){
                return false;
            }
            tfl1.setText(String.valueOf(ent.getCodigo()));
            tfl2.setText(ent.getMascota().getNombre());
            entTfl2 = ent.getMascota();
            tfl3.setText(ent.getCliente().getNombre());
            entTfl3 = ent.getCliente();
            cbo1.setSelectedItem(ent.getTipoPedido());
            dtp1.setDate(ent.getFecProgramada());
            tfl4.setText(ent.getObservacion());
            cbo2.setSelectedItem(ent.getEstadoPedido());
            tfl5.setText(String.valueOf(ent.getTotalSoles()));
            cbo3.setSelectedItem(ent.getTipoDocumento());
            tfl6.setText(String.valueOf(ent.getPagadoSoles()));
            dtp2.setDate(ent.getFecCreado());
            dtp3.setDate(ent.getFecAtendido());
            dtp4.setDate(ent.getFecPagado());
            dtp5.setDate(ent.getFecAnulado());
            return true;
        }
        return false;
    }

    @Override
    public String errorAlValidarDatosEntidad(){
        //MODIFICAR
        List<Pedido> listaValid;
        Pedido entPla = new Pedido();
        EntidadRepository rep;
        String sqlWhere;
        Class[] tipo;
        Object[] param;
        int maxCodigo = 0;

        switch(modoEdicion){
            case INSERT:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                sqlWhere = " ";
                tipo = new Class[]{};
                param = new Object[]{};
                listaValid = rep.read( entPla.getClass(), sqlWhere, tipo, param);
                for (Pedido ped : listaValid){
                    if (maxCodigo < ped.getCodigo()){
                        maxCodigo = ped.getCodigo();
                    }
                }
                maxCodigo++;
                tfl1.setText(String.valueOf(maxCodigo));
                break;
            case UPDATE:
                break;
            case DELETE:
                break;
            case READ_ONLY:
                if (cbo3.getSelectedItem()==null){
                    return "Tipo de Documento esta en blanco";
                }
                break;
        }
        //para todos los casos

        if (tfl2.getText().length() ==0){
            return "Seleccione mascota";
        }
        if (tfl3.getText().length() ==0){
            return "Seleccione cliente";
        }
        if (cbo1.getSelectedItem()==null){
            return "Tipo de Pedido esta en blanco";
        }
        /*if (dtp1.getDate() == null){
            return "Fecha de programacion esta en blanco";
        }*/
        if (dtp5.getDate() != null || cbo2.getSelectedItem() == EstadoPedido.ANULADO){
            return "Pedido Anulado no puede modificarse";
        }
        return null;
    }

    @Override
    public boolean grabaDatosEntidad(){
        //MODIFICAR
        EntidadRepository rep;
        Pedido ent;
        switch(modoEdicion){
            case INSERT:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                ent = new Pedido(Conversion.toint(tfl1.getText()), tfl4.getText(), dtp2.getDate(), dtp1.getDate(), dtp3.getDate(), dtp4.getDate(), dtp5.getDate(),
                        Conversion.todouble(tfl5.getText()), Conversion.todouble(tfl6.getText()), (TipoDocumento) cbo3.getSelectedItem(),
                        (EstadoPedido) cbo2.getSelectedItem(), (TipoPedido) cbo1.getSelectedItem(), (Cliente) entTfl3, (Mascota) entTfl2, null);
                if (rep.create( ent) > 0) {
                    //JOptionPane.showMessageDialog(null, "Datos Grabados");
                    return true;
                }else {
                    JOptionPane.showMessageDialog(null, "No se pudieron grabar los datos");
                    return false;
                }
            case UPDATE:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                ent = new Pedido(Conversion.toint(tfl1.getText()), tfl4.getText(), dtp2.getDate(), dtp1.getDate(), dtp3.getDate(), dtp4.getDate(), dtp5.getDate(),
                        Conversion.todouble(tfl5.getText()), Conversion.todouble(tfl6.getText()), (TipoDocumento) cbo3.getSelectedItem(),
                        (EstadoPedido) cbo2.getSelectedItem(), (TipoPedido) cbo1.getSelectedItem(), (Cliente) entTfl3, (Mascota) entTfl2, null);
                if (rep.update( ent) > 0) {
                    //JOptionPane.showMessageDialog(null, "Datos Grabados");
                    return true;
                }else {
                    JOptionPane.showMessageDialog(null, "No existe registro o no se pudo grabar datos");
                    return false;
                }
            case DELETE:
                if (JOptionPane.showConfirmDialog(null, "Desea Eliminar estos datos?","Borrar datos",JOptionPane.YES_NO_OPTION) == 0){
                    rep = new EntidadRepositoryRepositoryBaseDatos();
                    if (rep.delete(Pedido.class, Conversion.toint(tfl1.getText())) > 0) {
                        //JOptionPane.showMessageDialog(null, "Datos Grabados");
                        return true;
                    }else {
                        JOptionPane.showMessageDialog(null, "No existe registro o no se pudo elimnar datos");
                        return false;
                    }
                }else {
                    return false;
                }
            case READ_ONLY:
                return true;
            case CUSTOM_1:
                //PAGAR
                rep = new EntidadRepositoryRepositoryBaseDatos();
                ent = new Pedido(Conversion.toint(tfl1.getText()), tfl4.getText(), dtp2.getDate(), dtp1.getDate(), dtp3.getDate(), dtp4.getDate(), dtp5.getDate(),
                        Conversion.todouble(tfl5.getText()), Conversion.todouble(tfl6.getText()), (TipoDocumento) cbo3.getSelectedItem(),
                        (EstadoPedido) cbo2.getSelectedItem(), (TipoPedido) cbo1.getSelectedItem(), (Cliente) entTfl3, (Mascota) entTfl2, null);
                if (rep.update( ent) > 0) {
                    //JOptionPane.showMessageDialog(null, "Datos Grabados");
                    return true;
                }else {
                    JOptionPane.showMessageDialog(null, "No existe registro o no se pudo grabar datos");
                    return false;
                }
        }
        return false;
    }

    public static void main(String[] args) {
        PedidoEdita dialog = new PedidoEdita();
        Object respuesta = dialog.showConfirmJDialog(ModoEdicion.UPDATE, 200, null);
        if (respuesta != null){
            System.out.println(respuesta);
            //refresca grid dialog origen
        }
    }
}
