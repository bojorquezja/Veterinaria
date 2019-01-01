package main.java.com.wonen.veterinaria.controller;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Principal extends JFrame {
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

    public Principal() {

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
        menuA.add(menuAItem1);
        menuAItem2 = new JMenuItem("Articulos");
        menuA.add(menuAItem2);
        menuAItem3 = new JMenuItem("Activos");
        menuA.add(menuAItem3);
        menuBar.add(menuA);

        menuB = new JMenu("Ventas");
        menuBItem1 = new JMenuItem("Pedidos");
        menuB.add(menuBItem1);
        menuSep1 = new JSeparator();
        menuB.add(menuSep1);
        menuBItem2 = new JMenuItem("Clientes");
        menuB.add(menuBItem2);
        menuBItem3 = new JMenuItem("Mascotas");
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
}
