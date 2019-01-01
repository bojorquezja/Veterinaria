package main.java.com.wonen.veterinaria;

import main.java.com.wonen.veterinaria.controller.Principal;
import main.java.com.wonen.veterinaria.views.Registro;

import javax.swing.*;

public class AplVeterinaria {
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            //java.util.logging.Logger.getLogger(JFrameWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //login
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                Registro reg = new Registro();
                reg.setTitle("Registrese");
                reg.pack();
                reg.setVisible(true);
                if ( reg.isAprobado()) {
                    Principal principal = new Principal();
                    principal.setTitle("Veterinaria");
                    principal.pack();
                    principal.setVisible(true);
                }
            }
        });
        //Main window
    }
}
