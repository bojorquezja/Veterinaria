package main.java.com.wonen.veterinaria.views;

import main.java.com.wonen.veterinaria.model.Cliente;
import main.java.com.wonen.veterinaria.model.Producto;
import main.java.com.wonen.veterinaria.model.TipoCliente;
import main.java.com.wonen.veterinaria.model.TipoProducto;
import main.java.com.wonen.veterinaria.repository.EntidadRepository;
import main.java.com.wonen.veterinaria.repository.EntidadRepositoryRepositoryBaseDatos;
import main.java.com.wonen.veterinaria.service.Conversion;
import main.java.com.wonen.veterinaria.service.Info;
import main.java.com.wonen.veterinaria.views.comun.EditaJDialog;
import main.java.com.wonen.veterinaria.views.comun.MascaraTextField;
import main.java.com.wonen.veterinaria.views.comun.ModoEdicion;
import main.java.com.wonen.veterinaria.views.comun.TipoMascaraTextField;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.event.*;
import java.util.List;

public class ProductoEdita extends JDialog implements EditaJDialog {
    private JPanel contentPane;
    private JButton btn8;
    private JButton btn9;
    private JTextField tfl1;
    private JTextField tfl2;
    private JTextField tfl3;
    private JComboBox cbo1;

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


    public ProductoEdita() {
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
        tfl1.setDocument(new MascaraTextField("1", 1, 50, TipoMascaraTextField.NUMEROS_Y_LETRAS_MAYUSC));
        tfl2.setDocument(new MascaraTextField("", 0, 255, TipoMascaraTextField.TODO));
        tfl3.setDocument(new MascaraTextField("0.0", 1, 50, TipoMascaraTextField.SOLO_NUMEROS_DECIMALES));
        DefaultComboBoxModel comboBoxModel = new DefaultComboBoxModel(TipoProducto.values());
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
        cbo1.setEnabled(permite);
    }

    @Override
    public boolean cargaDatosEntidad(){
        //MODIFICAR
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        Producto ent = rep.read( Producto.class, pkEntidad);
        //codigo, descripcion, preciosoles, tipoproducto
        if (ent != null) {
            tfl1.setText(ent.getCodigo());
            tfl2.setText(ent.getDescripcion());
            tfl3.setText(String.valueOf( ent.getPrecioSoles() ));
            cbo1.setSelectedItem(ent.getTipoProducto());
            return true;
        }
        return false;
    }

    @Override
    public String errorAlValidarDatosEntidad(){
        //MODIFICAR
        List<Producto> listaValid;
        Producto entPla = new Producto();
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
                param = new Object[]{tfl1.getText() };
                listaValid = rep.read( entPla.getClass(), sqlWhere, tipo, param);
                if (listaValid.size() > 0){
                    return "Codigo ya fue registrado antes";
                }
                break;
            case UPDATE:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                sqlWhere = "(codigo = ? ) ";
                tipo = new Class[]{Info.getClase(entPla.getCodigo())};
                param = new Object[]{tfl1.getText() };
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
            return "descripcion esta en blanco";
        }
        if (Conversion.todouble( tfl3.getText() )<=0.0){
            return "Precio en Soles debe ser mayor a cero";
        }
        if (cbo1.getSelectedItem()==null){
            return "tipo de producto esta en blanco";
        }
        return null;
    }

    @Override
    public boolean grabaDatosEntidad(){
        //MODIFICAR
        EntidadRepository rep;
        Producto ent;
        switch(modoEdicion){
            case INSERT:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                ent = new Producto(tfl1.getText(), tfl2.getText(), Conversion.todouble(tfl3.getText()), (TipoProducto) cbo1.getSelectedItem());
                if (rep.create( ent) > 0) {
                    //JOptionPane.showMessageDialog(null, "Datos Grabados");
                    return true;
                }else {
                    JOptionPane.showMessageDialog(null, "No se pudieron grabar los datos");
                    return false;
                }
            case UPDATE:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                ent = new Producto(tfl1.getText(), tfl2.getText(), Conversion.todouble(tfl3.getText()), (TipoProducto) cbo1.getSelectedItem());
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
                    if (rep.delete(Producto.class, tfl1.getText()) > 0) {
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
        ProductoEdita dialog = new ProductoEdita();
        Object respuesta = dialog.showConfirmJDialog(ModoEdicion.UPDATE, "AA1", null);
        if (respuesta != null){
            System.out.println(respuesta);
            //refresca grid dialog origen
        }
    }
}
