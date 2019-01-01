package main.java.com.wonen.veterinaria.repository;

import java.util.List;
import java.util.Map;

public interface EntidadRepository {
    <E> long create(E entidad);
    <E,V> E read(Class claEnt, V valPK);
    <E> E read(E entidad);
    <E> List<E> read(Class claEnt, String sqlParamWhere, Object[] sqlParamStringValor);
    <E> long update(E entidad);
    long update(Class claEnt, String sqlParamSet, String sqlParamWhere, Object[] sqlParamStringValor);
    <E> long delete(E entidad);
    <V> long delete(Class claEnt, V valPK);
    long delete(Class claEnt, String sqlParamWhere, Object[] sqlParamStringValor);
}
