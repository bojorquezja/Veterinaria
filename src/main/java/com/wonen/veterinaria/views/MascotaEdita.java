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
import java.time.temporal.ChronoUnit;
import java.util.List;

public class MascotaEdita extends JDialog implements EditaJDialog {
    private JPanel contentPane;
    private JButton btn8;
    private JButton btn9;
    private JTextField tfl1;
    private JTextField tfl2;
    private JTextField tfl4;
    private JTextField tfl3;
    private JTextField tfl5;
    private JComboBox cbo1;
    private JComboBox cbo2;
    private Object entTfl5;
    private JButton btntfl5;
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


    public MascotaEdita() {
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

        btntfl5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selCliente();
            }
        });

        dtp1.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                calculaEdad();
            }
        });
    }

    private void calculaEdad(){
        long edad = 0;
        if (dtp1.getDate() != null) {
            edad = ChronoUnit.YEARS.between(dtp1.getDate(), LocalDate.now());
        }
        tfl4.setText(String.valueOf(edad));
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
        tfl1.setDocument(new MascaraTextField("1", 1, 20, TipoMascaraTextField.SOLO_NUMEROS_ENTEROS));
        tfl2.setDocument(new MascaraTextField("", 0, 255, TipoMascaraTextField.TODO));
        dtp1.getSettings().setFormatForDatesCommonEra("dd/MM/yyyy");
        dtp1.setDate(null);
        tfl3.setDocument(new MascaraTextField("", 0, 255, TipoMascaraTextField.TODO));
        tfl4.setDocument(new MascaraTextField("1", 1, 11, TipoMascaraTextField.SOLO_NUMEROS_ENTEROS));
        tfl4.setEditable(false);
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(TipoMascota.values());
        cbo1.setModel(comboBoxModel);
        DefaultComboBoxModel comboBoxModel2 = new DefaultComboBoxModel(Cuidado.values());
        cbo2.setModel(comboBoxModel2);
        tfl5.setDocument(new MascaraTextField("", 0, 0, TipoMascaraTextField.TODO));
        tfl5.setEditable(false);
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


    private void selCliente() {
        //MODIFICA
        ClienteLista dialog = new ClienteLista();
        Object respuesta = dialog.showConfirmJDialog(ModoLista.SELECT_CRUD, null, null);
        if (respuesta != null){
            String cod = (String) respuesta;
            EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
            Cliente ent = rep.read( Cliente.class, cod); //PK
            tfl5.setText(ent.getNombre());
            entTfl5 = ent;
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
        tfl2.setEditable(permite);
        dtp1.setEnabled(permite);
        tfl3.setEditable(permite);
        cbo1.setEnabled(permite);
        cbo2.setEnabled(permite);
        btntfl5.setEnabled(permite);
    }

    @Override
    public boolean cargaDatosEntidad(){
        //MODIFICAR
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        Mascota ent = rep.read( Mascota.class, pkEntidad);
        //codigo, nombre, fecNacimiento, descripcion, edad, tipomascota, cuidado, propietario
        if (ent != null) {
            tfl1.setText(String.valueOf(ent.getCodigo()));
            tfl2.setText(ent.getNombre());
            dtp1.setDate(ent.getFecNacimiento());
            tfl3.setText(ent.getDescripcion());
            tfl4.setText(String.valueOf(ent.getEdad()));
            cbo1.setSelectedItem(ent.getTipoMascota());
            cbo2.setSelectedItem(ent.getCuidado());
            tfl5.setText(ent.getPropietario().getNombre());
            entTfl5 = ent.getPropietario();
            return true;
        }
        return false;
    }

    @Override
    public String errorAlValidarDatosEntidad(){
        //MODIFICAR
        List<Mascota> listaValid;
        Mascota entPla = new Mascota();
        EntidadRepository rep;
        String sqlWhere;
        Class[] tipo;
        Object[] param;
        if (tfl1.getText().length() ==0){
            return "Codigo en blanco";
        }

        switch(modoEdicion){
            case INSERT:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                sqlWhere = "(codigo = ? ) ";
                tipo = new Class[]{Info.getClase(entPla.getCodigo())};
                param = new Object[]{Integer.parseInt(tfl1.getText()) };
                listaValid = rep.read( entPla.getClass(), sqlWhere, tipo, param);
                if (listaValid.size() > 0){
                    return "Codigo ya fue registrado antes";
                }
                break;
            case UPDATE:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                sqlWhere = "(codigo = ? ) ";
                tipo = new Class[]{Info.getClase(entPla.getCodigo())};
                param = new Object[]{Integer.parseInt(tfl1.getText()) };
                listaValid = rep.read( entPla.getClass(), sqlWhere, tipo, param);
                if (listaValid.size() == 0){
                    return "Codigo no existe";
                }
                break;
            case DELETE:
                break;
            case READ_ONLY:
                break;
        }
        //para todos los casos

        if (tfl2.getText().length() ==0){
            return "Nombre esta en blanco";
        }
        if (dtp1.getDate() != null){
            calculaEdad();
        }
        if (cbo1.getSelectedItem()==null){
            return "Tipo de Mascota esta en blanco";
        }
        if (cbo2.getSelectedItem()==null){
            return "Cuidado esta en blanco";
        }
        if (entTfl5==null){
            return "Propietario esta en blanco";
        }
        return null;
    }

    @Override
    public boolean grabaDatosEntidad(){
        //MODIFICAR
        EntidadRepository rep;
        Mascota ent;
        switch(modoEdicion){
            case INSERT:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                ent = new Mascota(Integer.parseInt(tfl1.getText()), tfl2.getText(), dtp1.getDate(), tfl3.getText(), Conversion.toint(tfl4.getText()),
                        (TipoMascota) cbo1.getSelectedItem(), (Cuidado) cbo2.getSelectedItem(), (Cliente) entTfl5);
                if (rep.create( ent) > 0) {
                    //JOptionPane.showMessageDialog(null, "Datos Grabados");
                    return true;
                }else {
                    JOptionPane.showMessageDialog(null, "No se pudieron grabar los datos");
                    return false;
                }
            case UPDATE:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                ent = new Mascota(Integer.parseInt(tfl1.getText()), tfl2.getText(), dtp1.getDate(), tfl3.getText(), Conversion.toint(tfl4.getText()),
                        (TipoMascota) cbo1.getSelectedItem(), (Cuidado) cbo2.getSelectedItem(), (Cliente) entTfl5);
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
                    if (rep.delete(Mascota.class, Integer.parseInt(tfl1.getText())) > 0) {
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
        MascotaEdita dialog = new MascotaEdita();
        Object respuesta = dialog.showConfirmJDialog(ModoEdicion.UPDATE, 9, null);
        if (respuesta != null){
            System.out.println(respuesta);
            //refresca grid dialog origen
        }
    }

}
