package main.java.com.wonen.veterinaria.model;

import main.java.com.wonen.veterinaria.repository.Campo;
import main.java.com.wonen.veterinaria.repository.Tabla;

import java.util.Objects;

@Tabla(nombre = "producto")
public class Producto {
	@Campo(nombre = "codigo", esPK = true)
	private String codigo;
	@Campo(nombre = "descripcion")
	private String descripcion;
	@Campo(nombre = "preciosoles")
	private double precioSoles;
	@Campo(nombre = "tipoproducto")
	private TipoProducto tipoProducto;

	public Producto() {
	}

	public Producto(String codigo, String descripcion, double precioSoles, TipoProducto tipoProducto) {
		this.codigo = codigo;
		this.descripcion = descripcion;
		this.precioSoles = precioSoles;
		this.tipoProducto = tipoProducto;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public double getPrecioSoles() {
		return precioSoles;
	}

	public void setPrecioSoles(double precioSoles) {
		this.precioSoles = precioSoles;
	}

	public TipoProducto getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(TipoProducto tipoProducto) {
		this.tipoProducto = tipoProducto;
	}

	@Override
	public String toString() {
		return "Producto{" +
				"codigo='" + codigo + '\'' +
				", descripcion='" + descripcion + '\'' +
				", precioSoles=" + precioSoles +
				", tipoProducto=" + tipoProducto +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Producto)) return false;
		Producto producto = (Producto) o;
		return Double.compare(producto.precioSoles, precioSoles) == 0 &&
				Objects.equals(codigo, producto.codigo) &&
				Objects.equals(descripcion, producto.descripcion) &&
				tipoProducto == producto.tipoProducto;
	}

}
