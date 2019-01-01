package main.java.com.wonen.veterinaria.repository;

import main.java.com.wonen.veterinaria.service.Conversion;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

/*
Clase que reemplaza DAO para base de datos para un CRUD simple de solo una entidad
Requiere que las clases entidad usen necesariamente las anotaciones @Tabla y @Campo
Se conecta a una sola base de datos definida en la clase BaseDeDatos
CREATE: INSERT
    Agrega la entidad a la base de datos
    Retorna la cantidad de registros afectados
READ: SELECT
    Trae una entidad desde la base de datos a partir del valor enviado (PK)
    Si una entidad tiene un detalle de muchos items (List) no lo trae, debe traerse a parte
UPDATE: UPDATE
    Actualiza la entidad a la base de datos
    Retorna la cantidad de registros afectados
DELETE: DELETE
    Borra la entidad de la base de datos. Usa @Campo para saber la PK
    Retorna la cantidad de registros afectados
*/
public class EntidadRepositoryRepositoryBaseDatos implements EntidadRepository {
    @Override
    public <E> long create(E entidad){
        Class claEnt = entidad.getClass();
        String nomTabla = this.nombreTabla(claEnt);
        String camposInsert = this.camposTabla(claEnt);
        String camposParamIns = this.paramCamposTabla(claEnt);

        String sql = "INSERT INTO "+ nomTabla
                + " (" + camposInsert + ") "
                + " VALUES (" + camposParamIns + ")";

        try (Connection con = BaseDeDatos.getConnection();
             PreparedStatement stat = con.prepareStatement(sql);){
            Map<String, Object> mapValObj = this.camposYValoresInsert(entidad);
            int cont = 0;
            for (Map.Entry<String, Object> elem : mapValObj.entrySet()){
                cont++;
                this.setParamQuery(stat, cont, elem.getValue());
            }
            stat.execute();
            int contador = stat.getUpdateCount();
            return (long) contador;

        } catch (SQLException e){
            System.out.println("Error: " + e);
        }
        return 0;
    }

    @Override
    public <E,V> E read(Class claEnt, V valPK){
        Object nueClase;
        try {
            nueClase = Class.forName(claEnt.getTypeName()).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e){
            return null;
        }
        List<E> listaEnt;
        String nomTabla = this.nombreTabla(claEnt);
        String camposSelect = this.camposTabla(claEnt);
        String nombreCampoPK = this.nomCampoPK(claEnt);

        String sql = "SELECT " + camposSelect
                + " FROM " + nomTabla
                + " WHERE " + nombreCampoPK + " = ?";

        try (Connection con = BaseDeDatos.getConnection();
             PreparedStatement stat = con.prepareStatement(sql);){
            this.setParamQuery(stat, 1, valPK);

            try (ResultSet rs = stat.executeQuery()) {
                listaEnt = this.resultSetToClase(rs, claEnt);
                if (listaEnt.size() > 0){
                    nueClase = listaEnt.get(0);
                    return (E) nueClase;
                }
            } catch(SQLException e){
                System.out.println("Error: " + e);
            }
            return null;

        } catch (SQLException e){
            System.out.println("Error: " + e);
        }
        return null;
    }

    @Override
    public <E> E read(E entidad) {
        //TODO
        return null;
    }

    @Override
    public <E> List<E> read(Class claEnt, String sqlParamWhere, Object[] sqlParamStringValor) {
        List<E> listaEnt = new ArrayList<>();
        String nomTabla = this.nombreTabla(claEnt);
        String camposSelect = this.camposTabla(claEnt);

        String sql = "SELECT " + camposSelect
                + " FROM " + nomTabla
                + " WHERE " + sqlParamWhere;

        try (Connection con = BaseDeDatos.getConnection();
             PreparedStatement stat = con.prepareStatement(sql);){
            int contPos = 0;
            for (Object elem : sqlParamStringValor) {
                contPos++;
                this.setParamQuery(stat, contPos, elem);
            }
            stat.setFetchSize(100);
            try (ResultSet rs = stat.executeQuery()) {
                listaEnt = this.resultSetToClase(rs, claEnt);
                return listaEnt;
            } catch(SQLException e){
                System.out.println("Error: " + e);
            }
            return listaEnt;

        } catch (SQLException e){
            System.out.println("Error: " + e);
        }
        return listaEnt;
    }

    @Override
    public <E> long update(E entidad) {
        Class claEnt = entidad.getClass();
        String nomTabla = this.nombreTabla(claEnt);
        Map<String, Object> mapPK = this.camposYValoresPK(entidad);
        Map<String, Object> mapValObj = this.camposYValoresUpdate(entidad);
        String camposUpdate = "", nombreCampoPK  = "";
        for (Map.Entry<String, Object> elem : mapValObj.entrySet()){
            camposUpdate += elem.getKey() + " = ?, ";
        }
        camposUpdate = (camposUpdate.length() >=2 ? camposUpdate.substring(0, camposUpdate.length()-2):camposUpdate);
        for (Map.Entry<String, Object> elem2 : mapPK.entrySet()){
            nombreCampoPK += elem2.getKey() + " = ?";
        }

        String sql = "UPDATE "+ nomTabla
                + " SET " + camposUpdate
                + " WHERE " + nombreCampoPK;

        try (Connection con = BaseDeDatos.getConnection();
             PreparedStatement stat = con.prepareStatement(sql);){

            int cont = 0;
            for (Map.Entry<String, Object> elem3 : mapValObj.entrySet()){
                cont++;
                this.setParamQuery(stat, cont, elem3.getValue());
            }
            for (Map.Entry<String, Object> elem4 : mapPK.entrySet()){
                cont++;
                this.setParamQuery(stat, cont, elem4.getValue());
            }
            stat.execute();
            int contador = stat.getUpdateCount();
            return (long) contador;

        } catch (SQLException e){
            System.out.println("Error: " + e);
        }
        return 0;
    }

    @Override
    public long update(Class claEnt, String sqlParamSet, String sqlParamWhere, Object[] sqlParamStringValor) {
        //TODO
        //addBatch() and executeBatch()
        return 0;
    }

    @Override
    public <E> long delete(E entidad) {
        //TODO
        return 0;
    }

    @Override
    public <V> long delete(Class claEnt, V valPK) {
        String nomTabla = this.nombreTabla(claEnt);
        String nombreCampoPK = this.nomCampoPK(claEnt);

        String sql = "DELETE FROM " + nomTabla
                + " WHERE " + nombreCampoPK + " = ?";

        try (Connection con = BaseDeDatos.getConnection();
             PreparedStatement stat = con.prepareStatement(sql);){
            this.setParamQuery(stat, 1, valPK);

            stat.execute();
            int cont = stat.getUpdateCount();
            return (long) cont;

        } catch (SQLException e){
            System.out.println("Error: " + e);
        }
        return 0;
    }

    @Override
    public long delete(Class claEnt, String sqlParamWhere, Object[] sqlParamStringValor) {
        //TODO
        //addBatch() and executeBatch()
        return 0;
    }

    /*
    Metodos privados para facilitar el trabajo de los publicos, se reciclan
     */

    //nombreTabla
    private String nombreTabla(Class claEnt){
        String nomTabla = "";
        try {
            Annotation[] annotations = claEnt.getAnnotations();
            for(Annotation annotation : annotations){
                if(annotation instanceof Tabla){
                    Tabla myAnnotation = (Tabla) annotation;
                    nomTabla = myAnnotation.nombre();
                    return nomTabla;
                }
            }
            return null;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    //nombreCampo1, nombreCampo2, nombreCampo3, nombreCampo4
    private String camposTabla(Class claEnt){
        String camposSelect = "";
        try {
            for (Field field: claEnt.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Campo.class)) {
                    camposSelect += field.getAnnotation(Campo.class).nombre() + ", ";
                }
            }
            camposSelect = ( camposSelect.length() >= 2 ? camposSelect.substring(0, camposSelect.length()-2) : camposSelect);
            return camposSelect;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    //?, ?, ?, ?
    private String paramCamposTabla(Class claEnt){
        String camposSelect = "";
        try {
            for (Field field: claEnt.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Campo.class)) {
                    camposSelect += "?, ";
                }
            }
            camposSelect = (camposSelect.length() >=2 ? camposSelect.substring(0, camposSelect.length()-2) : camposSelect);
            return camposSelect;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    //nombreCampoPK
    private String nomCampoPK(Class claEnt){
        String campoPK = "";
        try {
            for (Field field: claEnt.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Campo.class)) {
                    if (field.getAnnotation(Campo.class).esPK()){
                        campoPK = field.getAnnotation(Campo.class).nombre();
                    }
                }
            }
            return campoPK;
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    //tipoCampoPKClase
    private Class<?> tipoCampoPKClase(Class claEnt){
        Class<?> campoPK = null ;
        try {
            for (Field field: claEnt.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Campo.class)) {
                    if (field.getAnnotation(Campo.class).esPK()){
                        campoPK = field.getType();
                        return campoPK;
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    //valorPK
    private <E,V> V valorPKClase(E entidad){
        Class<?> claEnt = entidad.getClass();
        try {
            for (Field field: claEnt.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Campo.class)) {
                    if (field.getAnnotation(Campo.class).esPK()){
                        return (V) field.get(entidad);
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
        return null;
    }

    //Todos los campos en el orden en que fueron registrados
    private <E> Map<String, Object> camposYValoresInsert(E entidad){
        Map<String, Object> camposValCla = new LinkedHashMap<>();
        Class claEnt = entidad.getClass();
        try {
            for (Field field: claEnt.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Campo.class)) {
                    camposValCla.put(field.getAnnotation(Campo.class).nombre(), field.get(entidad) )  ;
                }
            }
            return camposValCla;
        }catch (Exception e){
            System.out.println(e);
        }
        return camposValCla;
    }

    //Todos los campos en el orden en que fueron registrados menos la PK
    private <E> Map<String, Object> camposYValoresUpdate(E entidad){
        Map<String, Object> camposValCla = new LinkedHashMap<>();
        Class claEnt = entidad.getClass();
        try {
            for (Field field: claEnt.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Campo.class) && !field.getAnnotation(Campo.class).esPK()) {
                    camposValCla.put(field.getAnnotation(Campo.class).nombre(), field.get(entidad) )  ;
                }
            }
            return camposValCla;
        }catch (Exception e){
            System.out.println(e);
        }
        return camposValCla;
    }

    //Todos los campos PK en el orden en que fueron registrados
    private <E> Map<String, Object> camposYValoresPK(E entidad){
        Map<String, Object> camposValCla = new LinkedHashMap<>();
        Class claEnt = entidad.getClass();
        try {
            for (Field field2: claEnt.getDeclaredFields()) {
                field2.setAccessible(true);
                if (field2.isAnnotationPresent(Campo.class) && field2.getAnnotation(Campo.class).esPK()) {
                    camposValCla.put(field2.getAnnotation(Campo.class).nombre(), field2.get(entidad) )  ;
                }
            }
            return camposValCla;
        }catch (Exception e){
            System.out.println(e);
        }
        return camposValCla;
    }

    private <V> void setParamQuery(PreparedStatement stat, int posicion, V valor) throws SQLException{
        switch (valor.getClass().getSimpleName()){
            case "long":
            case "Long":
                stat.setLong(posicion, (Long) valor);
                break;
            case "float":
            case "Float":
                stat.setFloat(posicion, (Float) valor);
                break;
            case "double":
            case "Double":
                stat.setDouble(posicion, (Double) valor);
                break;
            case "int":
            case "Integer":
                stat.setInt(posicion, (Integer) valor);
                break;
            case "LocalDate":
                java.sql.Date fecha = Conversion.toSqlDate((LocalDate) valor);
                stat.setDate(posicion, fecha);
                break;
            case "Date":
                java.sql.Date fecha2 = Conversion.toSqlDate((Date) valor);
                stat.setDate(posicion, fecha2);
                break;
            case "String":
                stat.setString(posicion, (String) valor );
                break;
            default :
                if(valor.getClass().isEnum()){
                    String valEnum = valor.toString();
                    stat.setString(posicion, valEnum );
                }else {
                    //clases: busca el valor de su PK y eso es lo que graba
                    this.setParamQuery(stat, posicion, valorPKClase(valor));
                }
        }
    }

    private <E> List<E> resultSetToClase(ResultSet rsEnt, Class claEnt) throws SQLException{
        List<E> arrlista = new ArrayList<>();
        rsEnt.beforeFirst();
        while (rsEnt.next()){

            try {
                Object nueClase = Class.forName(claEnt.getTypeName()).newInstance();
                //System.out.println(nueClase);
                for (Field field: nueClase.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.isAnnotationPresent(Campo.class)) {
                        this.grabaField(nueClase, rsEnt, field);
                        //System.out.println(campoTabla + " " + tipoDato);
                    }
                }
                //System.out.println(nueClase);
                arrlista.add((E) nueClase);

            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SecurityException e){
                return arrlista;
            }

        }
        return arrlista;
    }

    private void grabaField(Object nueClase, ResultSet rsEnt, Field field) throws SQLException, IllegalAccessException, SecurityException  {
        String campoTabla = field.getAnnotation(Campo.class).nombre();
        switch (field.getType().getSimpleName()){
            case "long":
            case "Long":
                field.setLong(nueClase, rsEnt.getLong(campoTabla));
                break;
            case "float":
            case "Float":
                field.setFloat(nueClase, rsEnt.getFloat(campoTabla));
                break;
            case "double":
            case "Double":
                field.setDouble(nueClase, rsEnt.getDouble(campoTabla));
                break;
            case "int":
            case "Integer":
                field.setInt(nueClase, rsEnt.getInt(campoTabla));
                break;
            case "LocalDate":
                LocalDate fecha = Conversion.toLocalDate(rsEnt.getDate(campoTabla));
                field.set(nueClase, fecha);
                break;
            case "Date":
                Date fecha2 = Conversion.toDate(rsEnt.getDate(campoTabla));
                field.set(nueClase, fecha2);
                break;
            case "String":
                field.set(nueClase, rsEnt.getString(campoTabla));
                break;
            default :
                if(field.getType().isEnum()){
                    //para enum
                    field.set(nueClase, Enum.valueOf((Class<Enum>) field.getType(), rsEnt.getString(campoTabla)));
                }else {
                    //para clases
                    Class tipoClase = field.getType();
                    Object valPKDB = null;

                    if (this.nombreTabla(tipoClase) != null){
                        //clases tipo tabla
                        Class tipoPK = this.tipoCampoPKClase(tipoClase);
                        switch (tipoPK.getSimpleName()){
                            case "long":
                            case "Long":
                                valPKDB = rsEnt.getLong(campoTabla);
                                break;
                            case "float":
                            case "Float":
                                valPKDB = rsEnt.getFloat(campoTabla);
                                break;
                            case "double":
                            case "Double":
                                valPKDB = rsEnt.getDouble(campoTabla);
                                break;
                            case "int":
                            case "Integer":
                                valPKDB = rsEnt.getInt(campoTabla);
                                break;
                            case "LocalDate":
                                LocalDate fechaPK = Conversion.toLocalDate(rsEnt.getDate(campoTabla));
                                valPKDB = fechaPK;
                                break;
                            case "Date":
                                Date fecha2PK = Conversion.toDate(rsEnt.getDate(campoTabla));
                                valPKDB = fecha2PK;
                                break;
                            case "String":
                                valPKDB = rsEnt.getString(campoTabla);
                                break;
                            default :
                                valPKDB = rsEnt.getObject(campoTabla);
                        }
                        Object intClase = read(tipoClase, valPKDB);
                        field.set(nueClase, intClase);
                    }else{
                        //clases no tabla
                        field.set(nueClase, rsEnt.getObject(campoTabla));
                    }

                }
        }
    }

}
