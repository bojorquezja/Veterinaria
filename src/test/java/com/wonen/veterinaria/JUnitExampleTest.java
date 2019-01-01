package test.java.com.wonen.veterinaria;

import org.junit.jupiter.api.*;

@DisplayName("Test Ejemplo 1")
public class JUnitExampleTest {

    private static int val1, val2, val3;

    @BeforeAll
    static void beforeAll(){
        val1 = 3;
        val2 = 5;
    }

    @BeforeEach
    void beforeEach(){
        val3 = 7;
    }

    @AfterEach
    void afterEach(){
        System.out.println("Fin Each");
    }

    @AfterAll
    static void afterAll(){
        System.out.println("Fin Todo");
    }

    @Test
    @DisplayName("prueba suma")
    void suma_numerosDelBefore_sumaTotal(){
        //Dado

        //Accion
        int val5 = val1 + val2 + val3;
        //Resultado
        Assertions.assertTrue(val5 == 15);
    }

    @Test
    @DisplayName("prueba multiplicacion")
    void multiplica_numerosDelBefore_mutiplicaTotal(){
        //Dado
        //Accion
        int val5 = val1*val2*val3;
        //Resultado
        Assertions.assertEquals(val5,105);
    }
}
