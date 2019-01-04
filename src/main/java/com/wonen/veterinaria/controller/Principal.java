package main.java.com.wonen.veterinaria.controller;

import main.java.com.wonen.veterinaria.model.TipoProducto;
import main.java.com.wonen.veterinaria.views.ClienteLista;
import main.java.com.wonen.veterinaria.views.MascotaLista;
import main.java.com.wonen.veterinaria.views.PedidoLista;
import main.java.com.wonen.veterinaria.views.ProductoLista;
import main.java.com.wonen.veterinaria.views.comun.ModoLista;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Principal extends JFrame implements ActionListener {
    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    private JMenuBar menuBar;
    private JMenu menuA;
    private JMenu menuB;
    private JMenuItem menuAItem1;
    private JSeparator menuSep1;
    private JMenuItem menuAItem2;
    private JMenuItem menuAItem3;
    private JMenuItem menuBItem1;
    private JMenuItem menuBItem2;
    private JMenuItem menuBItem3;

    public Principal(){

        setContentPane(contentPane);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        menuBar = new JMenuBar();

        menuA = new JMenu("Configurar");
        menuAItem1 = new JMenuItem("Servicios");
        menuAItem1.addActionListener(this);
        menuA.add(menuAItem1);
        menuAItem2 = new JMenuItem("Articulos");
        menuAItem2.addActionListener(this);
        menuA.add(menuAItem2);
        menuAItem3 = new JMenuItem("Activos");
        menuAItem3.addActionListener(this);
        menuA.add(menuAItem3);
        menuBar.add(menuA);

        menuB = new JMenu("Ventas");
        menuBItem1 = new JMenuItem("Pedidos");
        menuBItem1.addActionListener(this);
        menuB.add(menuBItem1);
        menuSep1 = new JSeparator();
        menuB.add(menuSep1);
        menuBItem2 = new JMenuItem("Clientes");
        menuBItem2.addActionListener(this);
        menuB.add(menuBItem2);
        menuBItem3 = new JMenuItem("Mascotas");
        menuBItem3.addActionListener(this);
        menuB.add(menuBItem3);
        menuBar.add(menuB);

        setJMenuBar(menuBar);

        tabbedPane1.setTitleAt(0,"Resumen");



    }

    private void onCancel() {
        // add your code here if necessary
        System.exit(0);
    }

    public static void main(String[] args) {
        Principal pantalla = new Principal();
        pantalla.pack();
        pantalla.setSize(800,600);
        pantalla.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menuAItem1){
            //Servicios
            ProductoLista dialog = new ProductoLista();
            Object respuesta = dialog.showConfirmJDialog(ModoLista.LIST_CRUD, TipoProducto.SERVICIO, null);
            if (respuesta != null){
                JOptionPane.showMessageDialog(null, "Error al abrir ventana");
            }
        }
        if (e.getSource() == menuAItem2){
            //Articulos
            ProductoLista dialog = new ProductoLista();
            Object respuesta = dialog.showConfirmJDialog(ModoLista.LIST_CRUD, TipoProducto.ARTICULO, null);
            if (respuesta != null){
                JOptionPane.showMessageDialog(null, "Error al abrir ventana");
            }
        }
        if (e.getSource() == menuAItem3){
            //Activos
            ProductoLista dialog = new ProductoLista();
            Object respuesta = dialog.showConfirmJDialog(ModoLista.LIST_CRUD, TipoProducto.ACTIVO, null);
            if (respuesta != null){
                JOptionPane.showMessageDialog(null, "Error al abrir ventana");
            }
        }
        if (e.getSource() == menuBItem1){
            //Pedidos
            PedidoLista dialog = new PedidoLista();
            Object respuesta = dialog.showConfirmJDialog(ModoLista.LIST_CRUD, null, null);
            if (respuesta != null){
                JOptionPane.showMessageDialog(null, "Error al abrir ventana");
            }
        }
        if (e.getSource() == menuBItem2){
            //Clientes
            ClienteLista dialog = new ClienteLista();
            Object respuesta = dialog.showConfirmJDialog(ModoLista.LIST_CRUD, null, null);
            if (respuesta != null){
                JOptionPane.showMessageDialog(null, "Error al abrir ventana");
            }
        }
        if (e.getSource() == menuBItem3){
            //Mascotas
            MascotaLista dialog = new MascotaLista();
            Object respuesta = dialog.showConfirmJDialog(ModoLista.LIST_CRUD, null, null);
            if (respuesta != null){
                JOptionPane.showMessageDialog(null, "Error al abrir ventana");
            }
        }
    }
}
