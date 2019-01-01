package main.java.com.wonen.veterinaria.model;

import main.java.com.wonen.veterinaria.repository.Campo;
import main.java.com.wonen.veterinaria.repository.Tabla;

import java.util.Objects;

@Tabla(nombre="detpedido")
public class DetallePedido {
	@Campo(nombre = "item", esPK = true)
	private int item;
	@Campo(nombre = "codproducto")
	private Producto producto;
	@Campo(nombre = "descripcion")
	private String descripcion;
	@Campo(nombre = "precioSoles")
	private double precioSoles;
	@Campo(nombre = "cantidad")
	private double cantidad;
	@Campo(nombre = "totalSoles")
	private double totalSoles;
	@Campo(nombre = "codpedido")
	private Pedido pedido;

	public DetallePedido() {
	}

	public DetallePedido(int item, String descripcion, double precioSoles, double cantidad, double totalSoles, Producto producto, Pedido pedido) {
		this.item = item;
		this.descripcion = descripcion;
		this.precioSoles = precioSoles;
		this.cantidad = cantidad;
		this.totalSoles = totalSoles;
		this.producto = producto;
		this.pedido = pedido;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public int getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
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

	public double getCantidad() {
		return cantidad;
	}

	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	public double getTotalSoles() {
		return totalSoles;
	}

	public void setTotalSoles(double totalSoles) {
		this.totalSoles = totalSoles;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	@Override
	public String toString() {
		return "DetallePedido{" +
				"item=" + item +
				", descripcion='" + descripcion + '\'' +
				", precioSoles=" + precioSoles +
				", cantidad=" + cantidad +
				", totalSoles=" + totalSoles +
				", producto=" + producto +
				", pedido=" + pedido +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof DetallePedido)) return false;
		DetallePedido that = (DetallePedido) o;
		return item == that.item &&
				Double.compare(that.precioSoles, precioSoles) == 0 &&
				Double.compare(that.cantidad, cantidad) == 0 &&
				Double.compare(that.totalSoles, totalSoles) == 0 &&
				Objects.equals(descripcion, that.descripcion) &&
				Objects.equals(producto, that.producto) &&
				Objects.equals(pedido, that.pedido);
	}

}
