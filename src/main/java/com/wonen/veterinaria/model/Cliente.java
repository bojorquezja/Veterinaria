package main.java.com.wonen.veterinaria.model;

import main.java.com.wonen.veterinaria.repository.Campo;
import main.java.com.wonen.veterinaria.repository.Tabla;

import java.time.LocalDate;
import java.util.*;

@Tabla(nombre="cliente")
public class Cliente {

	@Campo(nombre="dni", esPK = true)
	private String dni;
	@Campo(nombre="nombre")
	private String nombre;
	@Campo(nombre="direccion")
	private String direccion;
	@Campo(nombre="fecCreado")
	private LocalDate fecCreado;
	@Campo(nombre="telefono")
	private int telefono;
	@Campo(nombre="correo")
	private String correo;
	@Campo(nombre="tipoCliente")
	private TipoCliente tipoCliente;
	private List<Mascota> mascotas;

	public Cliente() {
	}

	public Cliente(String dni, String nombre, String direccion, LocalDate fecCreado, int telefono, String correo, TipoCliente tipoCliente, List<Mascota> mascotas) {
		this.dni = dni;
		this.nombre = nombre;
		this.direccion = direccion;
		this.fecCreado = fecCreado;
		this.telefono = telefono;
		this.correo = correo;
		this.tipoCliente = tipoCliente;
		this.mascotas = mascotas;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public LocalDate getFecCreado() {
		return fecCreado;
	}

	public void setFecCreado(LocalDate fecCreado) {
		this.fecCreado = fecCreado;
	}

	public int getTelefono() {
		return telefono;
	}

	public void setTelefono(int telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public TipoCliente getTipoCliente() {
		return tipoCliente;
	}

	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente;
	}

	public List<Mascota> getMascotas() {
		return mascotas;
	}

	public void setMascotas(List<Mascota> mascotas) {
		this.mascotas = mascotas;
	}

	@Override
	public String toString() {
		return "Cliente{" +
				"dni='" + dni + '\'' +
				", nombre='" + nombre + '\'' +
				", direccion='" + direccion + '\'' +
				", fecCreado=" + fecCreado +
				", telefono=" + telefono +
				", correo='" + correo + '\'' +
				", tipoCliente=" + tipoCliente +
				", mascotas=" + mascotas +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Cliente)) return false;
		Cliente cliente = (Cliente) o;
		return telefono == cliente.telefono &&
				Objects.equals(dni, cliente.dni) &&
				Objects.equals(nombre, cliente.nombre) &&
				Objects.equals(direccion, cliente.direccion) &&
				Objects.equals(fecCreado, cliente.fecCreado) &&
				Objects.equals(correo, cliente.correo) &&
				tipoCliente == cliente.tipoCliente &&
				Objects.equals(mascotas, cliente.mascotas);
	}

}
