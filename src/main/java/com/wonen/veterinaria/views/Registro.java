package main.java.com.wonen.veterinaria.views;

import javax.swing.*;
import java.awt.event.*;
import java.util.Arrays;

public class Registro extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField adminTextField;
    private JPasswordField adminPasswordField;
    private boolean aprobado = false;

    public boolean isAprobado() {
        return aprobado;
    }

    public Registro() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        buttonOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }

    private void onOK() {
        aprobado = false;
        char[] contra = "admin".toCharArray();
        if (adminTextField.getText().equals("admin") && Arrays.equals(adminPasswordField.getPassword(), contra) ){
            aprobado = true;
            dispose();
        }else{
            JOptionPane.showMessageDialog(null, "Usuario y contrase\u00f1a no v\u00e1lidos");
        }

    }

    private void onCancel() {
        aprobado = false;
        dispose();
    }

    public static void main(String[] args) {
        Registro dialog = new Registro();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
