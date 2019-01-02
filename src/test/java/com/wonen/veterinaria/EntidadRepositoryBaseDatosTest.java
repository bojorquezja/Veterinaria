package test.java.com.wonen.veterinaria;

import main.java.com.wonen.veterinaria.model.*;
import main.java.com.wonen.veterinaria.repository.EntidadRepository;
import main.java.com.wonen.veterinaria.repository.EntidadRepositoryRepositoryBaseDatos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

@DisplayName("Conexion INS DEL UPD a base de datos por entidad")
public class EntidadRepositoryBaseDatosTest {
    @Test
    @DisplayName("SELECT CLIENTE")
    public void read_ClienteExistente_ClienteDeBaseDatos(){
        //Dado
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        LocalDate fecha = LocalDate.of(2018, Month.JANUARY,1);
        Cliente cliA = new Cliente("1", "carlos", "av 123", fecha, 546546, "aaa@sss.com", TipoCliente.MAYORISTA, null);

        //Accion
        Cliente cliB = rep.read( Cliente.class, 1);

        //Resultado
        Assertions.assertEquals(cliA, cliB);
    }

    @Test
    @DisplayName("SELECT PEDIDO")
    public void read_ProductoExistente_ProductoDeBaseDatos() {
        //Dado
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        LocalDate fecha = LocalDate.of(2018, Month.JANUARY,1);
        LocalDate fecha2 = LocalDate.of(2018, Month.FEBRUARY,2);
        LocalDate fecha3 = LocalDate.of(2018, Month.MARCH,3);
        Cliente cliX = new Cliente("1", "carlos", "av 123", fecha, 54654, "aaa@sss.com", TipoCliente.MAYORISTA, null);
        Mascota masX = new Mascota(9, "firulais", fecha3, "dogo flaco", 2, TipoMascota.PERRO, Cuidado.NORMAL, cliX);
        Pedido pedA = new Pedido(100, "mensaje", fecha2, fecha2, fecha2, fecha2, null, 100.0, 99.0, TipoDocumento.BOLETA, EstadoPedido.PAGADO, TipoPedido.MEDICO, cliX, masX, null);

        //Accion
        Pedido pedB = rep.read( Pedido.class, 100);

        //Resultado
        Assertions.assertEquals(pedA, pedB);
    }

    @Test
    @DisplayName("INSERT PRODUCTO")
    public void create_ProductoExistente_InsertaProductoDeBaseDatos() {
        //Dado
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        Producto prdA = new Producto("AA1", "otro", 5.5, TipoProducto.ARTICULO);

        //Accion
        long val = rep.create(prdA);

        //Resultado
        Assertions.assertTrue(val > 0);
    }

    @Test
    @DisplayName("UPDATE PRODUCTO")
    public void create_ProductoExistente_ActualizaProductoDeBaseDatos() {
        //Dado
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        Producto prdA = new Producto("AA1", "ninguno", 7.5, TipoProducto.ARTICULO);

        //Accion
        long val = rep.update(prdA);

        //Resultado
        Assertions.assertTrue(val > 0);
    }

    @Test
    @DisplayName("DELETE PRODUCTO")
    public void delete_ProductoExistente_BorraProductoDeBaseDatos() {
        //Dado
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();

        //Accion
        long val = rep.delete( Producto.class, "AA1");

        //Resultado
        Assertions.assertTrue(val > 0);
    }


    @Test
    @DisplayName("SELECT PRODUCTO WHERE")
    public void sel_ProductosExistente_ProductosWhereDeBaseDatos() {
        //Dado
        List<Producto> listaA = new ArrayList<>();
        listaA.add(new Producto("AA1", "ninguno", 7.5, TipoProducto.ARTICULO));
        listaA.add(new Producto("AB001", "cicatrizante", 10.5, TipoProducto.ARTICULO));
        EntidadRepository rep = new EntidadRepositoryRepositoryBaseDatos();
        Object[] param = new Object[]{"ARTICULO"};

        //Accion

        List<Producto> listaB = rep.read( Producto.class, "tipoproducto = ?", param);

        //Resultado
        Assertions.assertIterableEquals(listaA, listaB);
    }

}
