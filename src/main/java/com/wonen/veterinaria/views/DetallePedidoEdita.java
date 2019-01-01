package main.java.com.wonen.veterinaria.views;

import main.java.com.wonen.veterinaria.model.DetallePedido;
import main.java.com.wonen.veterinaria.model.Pedido;
import main.java.com.wonen.veterinaria.model.Producto;
import main.java.com.wonen.veterinaria.model.TipoProducto;
import main.java.com.wonen.veterinaria.repository.EntidadRepository;
import main.java.com.wonen.veterinaria.repository.EntidadRepositoryRepositoryBaseDatos;
import main.java.com.wonen.veterinaria.service.Conversion;
import main.java.com.wonen.veterinaria.views.comun.*;
import org.jdesktop.swingx.JXDatePicker;

import javax.swing.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class DetallePedidoEdita extends JDialog implements EditaJDialog {
    private JPanel contentPane;
    private JButton btn8;
    private JButton btn9;
    private JTextField tfl1;
    private Object entTfl1;
    private JTextField tfl2;
    private JTextField tfl4;
    private JTextField tfl3;
    private JTextField tfl5;
    private JButton btntfl3;
    private Object entTfl3;
    private JTextField tfl6;
    private JTextField tfl7;

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


    public DetallePedidoEdita() {
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
        btntfl3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selProducto();
            }
        });
        tfl5.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                calculaTotalSoles();
                super.focusLost(e);
            }
        });
        tfl6.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
            }

            @Override
            public void focusLost(FocusEvent e) {
                calculaTotalSoles();
                super.focusLost(e);
            }
        });
    }

    private void calculaTotalSoles(){
        tfl7.setText(String.valueOf( Conversion.todouble(tfl5.getText()) * Conversion.todouble(tfl6.getText()) ));
    }

    @Override
    public String valoresIniciales() {
        //MODIFICAR
        this.cargaLimpiaControles();
        this.accesoControlPK(false);
        switch(modoEdicion){
            case INSERT:
                this.accesoControlResto(true);
                this.setTitle("Nuevo registro");
                btn8.setText("Insertar");
                btn9.setText("Cancelar");
                return null;
            case UPDATE:
                if (this.cargaDatosEntidad()) {
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
                    this.accesoControlResto(false);
                    this.setTitle("Ver registro");
                    btn8.setText("Aceptar");
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
        tfl1.setDocument(new MascaraTextField("", 0, 50, TipoMascaraTextField.SOLO_NUMEROS_ENTEROS));
        tfl1.setText(String.valueOf(((Pedido) otrosParam[0]).getCodigo()));
        entTfl1 = ((Pedido) otrosParam[0]);
        tfl2.setDocument(new MascaraTextField("", 0, 50, TipoMascaraTextField.SOLO_NUMEROS_ENTEROS));
        tfl4.setDocument(new MascaraTextField("", 0, 255, TipoMascaraTextField.TODO));
        tfl5.setDocument(new MascaraTextField("0.0", 1, 50, TipoMascaraTextField.SOLO_NUMEROS_DECIMALES));
        tfl6.setDocument(new MascaraTextField("0.0", 1, 50, TipoMascaraTextField.SOLO_NUMEROS_DECIMALES));
        tfl7.setDocument(new MascaraTextField("0.0", 1, 50, TipoMascaraTextField.SOLO_NUMEROS_DECIMALES));

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

    private void selProducto() {
        //MODIFICA
        ProductoLista dialog = new ProductoLista();
        Object respuesta = dialog.showConfirmJDialog(ModoLista.SELECT_CRUD, null, null);
        if (respuesta != null){
            EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
            Producto ent = rep.read( Producto.class, (String) respuesta);
            tfl3.setText(ent.getDescripcion());
            entTfl3 = ent;
            if (tfl4.getText().length() == 0){
                tfl4.setText(ent.getDescripcion());
            }

        }
    }

    @Override
    public void accesoControlPK(boolean permite){
        //MODIFICAR
        tfl1.setEditable(permite);
        tfl2.setEditable(permite);
    }

    @Override
    public void accesoControlResto(boolean permite){
        //MODIFICAR
        tfl3.setEditable(false);
        btntfl3.setEnabled(permite);
        tfl4.setEditable(permite);
        tfl5.setEditable(permite);
        tfl6.setEditable(permite);
        tfl7.setEditable(false);
    }

    @Override
    public boolean cargaDatosEntidad(){
        //MODIFICAR
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        DetallePedido ent = rep.read( DetallePedido.class, pkEntidad);
        //item, producto, descripcion, precioSoles, cantidad, totalSoles, pedido
        if (ent != null) {
            tfl2.setText(String.valueOf(ent.getItem()));
            tfl3.setText(ent.getProducto().getDescripcion());
            entTfl3 = ent.getProducto();
            tfl4.setText(ent.getDescripcion());
            tfl5.setText(String.valueOf( ent.getPrecioSoles() ));
            tfl6.setText(String.valueOf( ent.getCantidad() ));
            tfl7.setText(String.valueOf( ent.getTotalSoles() ));
            return true;
        }
        return false;
    }

    @Override
    public String errorAlValidarDatosEntidad(){
        //MODIFICAR
        List<DetallePedido> listaValid;
        EntidadRepository rep;
        String sqlWhere;
        Object[] param;
        int maxItem = ((Pedido) otrosParam[0]).getCodigo() * 1000;

        switch(modoEdicion){
            case INSERT:

                rep = new EntidadRepositoryRepositoryBaseDatos();
                sqlWhere = "(codpedido = ? ) ";
                param = new Object[]{((Pedido) entTfl1).getCodigo() };
                listaValid = rep.read( DetallePedido.class, sqlWhere, param);
                for (DetallePedido detP : listaValid){
                    if (maxItem < detP.getItem()){
                        maxItem = detP.getItem();
                    }
                }
                maxItem++;
                tfl2.setText(String.valueOf(maxItem));
                break;
            case UPDATE:
                break;
            case DELETE:
                break;
            case READ_ONLY:
                break;
        }
        //para todos los casos

        if (tfl4.getText().length() ==0){
            return "descripcion esta en blanco";
        }
        if (Conversion.todouble(tfl5.getText()) <=0.0){
            return "Precio en Soles debe ser mayor a cero";
        }
        if (Conversion.todouble(tfl6.getText()) <=0.0){
            return "Cantidad debe ser mayor a cero";
        }
        if (Conversion.todouble(tfl7.getText()) <=0.0){
            return "Total en Soles debe ser mayor a cero";
        }
        return null;
    }

    @Override
    public boolean grabaDatosEntidad(){
        //MODIFICAR
        EntidadRepository rep;
        DetallePedido ent;
        switch(modoEdicion){
            case INSERT:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                ent = new DetallePedido(Conversion.toint(tfl2.getText()), tfl4.getText(), Conversion.todouble(tfl5.getText()), Conversion.todouble(tfl6.getText()), Conversion.todouble(tfl7.getText()), (Producto) entTfl3, (Pedido) entTfl1);
                if (rep.create( ent) > 0) {
                    //JOptionPane.showMessageDialog(null, "Datos Grabados");
                    return true;
                }else {
                    JOptionPane.showMessageDialog(null, "No se pudieron grabar los datos");
                    return false;
                }
            case UPDATE:
                rep = new EntidadRepositoryRepositoryBaseDatos();
                ent = new DetallePedido(Conversion.toint(tfl2.getText()), tfl4.getText(), Conversion.todouble(tfl5.getText()), Conversion.todouble(tfl6.getText()), Conversion.todouble(tfl7.getText()), (Producto) entTfl3, (Pedido) entTfl1);
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
                    if (rep.delete(DetallePedido.class, Conversion.toint(tfl2.getText())) > 0) {
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
        DetallePedidoEdita dialog = new DetallePedidoEdita();
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        Pedido ped = rep.read(Pedido.class, 100);
        Object[] otrosParam = {ped};
        Object respuesta = dialog.showConfirmJDialog(ModoEdicion.UPDATE, 100002, otrosParam);
        if (respuesta != null){
            System.out.println(respuesta);
            //refresca grid dialog origen
        }
    }
}
