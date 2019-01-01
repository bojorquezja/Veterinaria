package main.java.com.wonen.veterinaria.model;

import main.java.com.wonen.veterinaria.repository.Campo;
import main.java.com.wonen.veterinaria.repository.Tabla;

import java.time.LocalDate;
import java.util.Objects;

@Tabla(nombre="mascota")
public class Mascota {
	@Campo(nombre = "codigo", esPK = true)
	private int codigo;
	@Campo(nombre = "nombre")
	private String nombre;
	@Campo(nombre = "fecnacimiento")
	private LocalDate fecNacimiento;
	@Campo(nombre = "descripcion")
	private String descripcion;
	@Campo(nombre = "edad")
	private int edad;
	@Campo(nombre = "tipomascota")
	private TipoMascota tipoMascota;
	@Campo(nombre = "cuidado")
	private Cuidado cuidado;
	@Campo(nombre = "propietario")
	private Cliente propietario;

	public Mascota() {
	}

	public Mascota(int codigo, String nombre, LocalDate fecNacimiento, String descripcion, int edad, TipoMascota tipoMascota, Cuidado cuidado, Cliente propietario) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.fecNacimiento = fecNacimiento;
		this.descripcion = descripcion;
		this.edad = edad;
		this.tipoMascota = tipoMascota;
		this.cuidado = cuidado;
		this.propietario = propietario;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public TipoMascota getTipoMascota() {
		return tipoMascota;
	}

	public void setTipoMascota(TipoMascota tipoMascota) {
		this.tipoMascota = tipoMascota;
	}

	public Cuidado getCuidado() {
		return cuidado;
	}

	public void setCuidado(Cuidado cuidado) {
		this.cuidado = cuidado;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFecNacimiento() {
		return fecNacimiento;
	}

	public void setFecNacimiento(LocalDate fecNacimiento) {
		this.fecNacimiento = fecNacimiento;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public Cliente getPropietario() {
		return propietario;
	}

	public void setPropietario(Cliente propietario) {
		this.propietario = propietario;
	}

	@Override
	public String toString() {
		return "Mascota{" +
				"codigo=" + codigo +
				", nombre='" + nombre + '\'' +
				", fecNacimiento=" + fecNacimiento +
				", descripcion='" + descripcion + '\'' +
				", edad=" + edad +
				", tipoMascota=" + tipoMascota +
				", cuidado=" + cuidado +
				", propietario=" + propietario +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Mascota)) return false;
		Mascota mascota = (Mascota) o;
		return codigo == mascota.codigo &&
				edad == mascota.edad &&
				Objects.equals(nombre, mascota.nombre) &&
				Objects.equals(fecNacimiento, mascota.fecNacimiento) &&
				Objects.equals(descripcion, mascota.descripcion) &&
				tipoMascota == mascota.tipoMascota &&
				cuidado == mascota.cuidado &&
				Objects.equals(propietario, mascota.propietario);
	}

}
