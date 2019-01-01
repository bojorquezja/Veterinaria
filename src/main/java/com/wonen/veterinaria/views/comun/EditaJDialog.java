package main.java.com.wonen.veterinaria.views.comun;


public interface EditaJDialog {

    Object showConfirmJDialog(ModoEdicion modoEdita, Object pkEntidad, Object[] otrosParam);
    String valoresIniciales();
    void cargaLimpiaControles();

    void alAceptar();
    void alCancelar();

    void accesoControlPK(boolean permite);
    void accesoControlResto(boolean permite);

    boolean cargaDatosEntidad();
    String errorAlValidarDatosEntidad();
    boolean grabaDatosEntidad();

}
