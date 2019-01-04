package main.java.com.wonen.veterinaria.views;

import com.github.lgooddatepicker.components.DatePicker;
import main.java.com.wonen.veterinaria.model.Cliente;
import main.java.com.wonen.veterinaria.model.TipoCliente;
import main.java.com.wonen.veterinaria.repository.EntidadRepository;
import main.java.com.wonen.veterinaria.repository.EntidadRepositoryRepositoryBaseDatos;
import main.java.com.wonen.veterinaria.service.Conversion;
import main.java.com.wonen.veterinaria.service.Info;
import main.java.com.wonen.veterinaria.views.comun.EditaJDialog;
import main.java.com.wonen.veterinaria.views.comun.MascaraTextField;
import main.java.com.wonen.veterinaria.views.comun.ModoEdicion;
import main.java.com.wonen.veterinaria.views.comun.TipoMascaraTextField;

import javax.swing.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class ClienteEdita extends JDialog implements EditaJDialog {
    private JPanel contentPane;
    private JButton btn8;
    private JButton btn9;
    private JTextField tfl1;
    private JTextField tfl2;
    private JTextField tfl4;
    private JTextField tfl3;
    private JTextField tfl5;
    private JComboBox cbo1;
    private DatePicker dtp1;

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


    public ClienteEdita() {
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
    }

    @Override
    public String valoresIniciales() {
        this.cargaLimpiaControles();
        switch(modoEdicion){
            case INSERT:
                this.accesoControlPK(true);
                this.accesoControlResto(true);
                this.setTitle("Nuevo registro");
                btn8.setText("Insertar");
                btn9.setText("Cancelar");
                return null;
            case UPDATE:
                if (this.cargaDatosEntidad()) {
                    this.accesoControlPK(false);
                    this.accesoControlResto(true);
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
                    this.setTitle("Ver registro");
                    btn8.setText("Aceptar");
                    btn8.setVisible(false);
                    btn9.setText("Cancelar");
                    return null;
                }else{
                    return "Error al cargar datos";
                }
        }
        return "error en seleccion de modo edita";
    }

    @Override
    public void cargaLimpiaControles(){
        //MODIFICAR
        tfl1.setDocument(new MascaraTextField("", 0, 20, TipoMascaraTextField.SOLO_NUMEROS_ENTEROS));
        tfl2.setDocument(new MascaraTextField("", 0, 255, TipoMascaraTextField.TODO));
        tfl3.setDocument(new MascaraTextField("", 0, 255, TipoMascaraTextField.TODO));
        dtp1.getSettings().setFormatForDatesCommonEra("dd/MM/yyyy");
        if (modoEdicion == ModoEdicion.INSERT){
            dtp1.setDate(LocalDate.now());
        }else {
            dtp1.setDate(null);
        }
        dtp1.setEnabled(false);
        tfl4.setDocument(new MascaraTextField("0", 1, 11, TipoMascaraTextField.TELEFONO));
        tfl5.setDocument(new MascaraTextField("", 0, 255, TipoMascaraTextField.CORREO));
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(TipoCliente.values());
        cbo1.setModel(comboBoxModel);
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

    @Override
    public void accesoControlPK(boolean permite){
        //MODIFICAR
        tfl1.setEditable(permite);
    }

    @Override
    public void accesoControlResto(boolean permite){
        //MODIFICAR
        tfl2.setEditable(permite);
        tfl3.setEditable(permite);
        tfl4.setEditable(permite);
        tfl5.setEditable(permite);
        cbo1.setEnabled(permite);
    }

    @Override
    public boolean cargaDatosEntidad(){
        //MODIFICAR
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        Cliente ent = rep.read( Cliente.class, pkEntidad);
        //"DNI", "Nombre", "Direccion", "Fecha Creado", "Telefono", "Correo", "Tipo Cliente"
        if (ent != null) {
            tfl1.setText(ent.getDni());
            tfl2.setText(ent.getNombre());
            tfl3.setText(ent.getDireccion());
            dtp1.setDate(ent.getFecCreado());
            tfl4.setText(String.valueOf(ent.getTelefono()));
            tfl5.setText(ent.getCorreo());
            cbo1.setSelectedItem(ent.getTipoCliente());
            return true;
        }
        return false;
    }

    @Override
    public String errorAlValidarDatosEntidad(){
        //MODIFICAR
        List<Cliente> listaValid;
        Cliente entPla = new Cliente();
        EntidadRepository rep;
        String sqlWhere;
        Class[] tipo;
        Object[] param;
        if (tfl1.getText().length() ==0){
            return "Dni en blanco";
        }

        switch(modoEdicion){
            case INSERT:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                sqlWhere = "(dni = ? ) ";
                tipo = new Class[]{Info.getClase(entPla.getDni())};
                param = new Object[]{tfl1.getText() };
                listaValid = rep.read( entPla.getClass(), sqlWhere, tipo, param);
                if (listaValid.size() > 0){
                    return "Dni ya fue registrado antes";
                }
                break;
            case UPDATE:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                sqlWhere = "(dni = ? ) ";
                tipo = new Class[]{Info.getClase(entPla.getDni())};
                param = new Object[]{tfl1.getText() };
                listaValid = rep.read( entPla.getClass(), sqlWhere, tipo, param);
                if (listaValid.size() == 0){
                    return "Dni no existe";
                }
                break;
            case DELETE:
                break;
            case READ_ONLY:
                break;
        }
        //para todos los casos

        if (tfl2.getText().length() ==0){
            return "nombre esta en blanco";
        }
        if (dtp1.getDate() == null){
            return "fecha de creacion esta en blanco";
        }
        if (cbo1.getSelectedItem()==null){
            return "tipo de cliente esta en blanco";
        }
        return null;
    }

    @Override
    public boolean grabaDatosEntidad(){
        //MODIFICAR
        EntidadRepository rep;
        Cliente ent;
        switch(modoEdicion){
            case INSERT:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                ent = new Cliente(tfl1.getText(), tfl2.getText(), tfl3.getText(), dtp1.getDate(), Conversion.toint(tfl4.getText()),
                        tfl5.getText(), (TipoCliente) cbo1.getSelectedItem(), null);
                if (rep.create( ent) > 0) {
                    //JOptionPane.showMessageDialog(null, "Datos Grabados");
                    return true;
                }else {
                    JOptionPane.showMessageDialog(null, "No se pudieron grabar los datos");
                    return false;
                }
            case UPDATE:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                ent = new Cliente(tfl1.getText(), tfl2.getText(), tfl3.getText(), dtp1.getDate(), Conversion.toint(tfl4.getText()),
                        tfl5.getText(), (TipoCliente) cbo1.getSelectedItem(), null);
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
                    if (rep.delete(Cliente.class, tfl1.getText()) > 0) {
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
        }
        return false;
    }

    public static void main(String[] args) {
        ClienteEdita dialog = new ClienteEdita();
        Object respuesta = dialog.showConfirmJDialog(ModoEdicion.UPDATE, "1", null);
        if (respuesta != null){
            System.out.println(respuesta);
            //refresca grid dialog origen
        }
    }
}
