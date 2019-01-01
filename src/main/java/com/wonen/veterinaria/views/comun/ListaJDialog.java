package main.java.com.wonen.veterinaria.views.comun;

public interface ListaJDialog {

    Object showConfirmJDialog(ModoLista modoLista, Object valEntidad, Object[] otrosParam);
    String valoresIniciales();
    void cargaLimpiaControles();

    void alBuscar();
    void alCrear();
    void alEditar();
    void alBorrar();
    void alSeleccionar();
    void alCancelar();



}
