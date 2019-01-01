package main.java.com.wonen.veterinaria.model;

import main.java.com.wonen.veterinaria.repository.Campo;
import main.java.com.wonen.veterinaria.repository.Tabla;

import java.time.LocalDate;
import java.util.*;

@Tabla(nombre="pedido")
public class Pedido {
	@Campo(nombre = "codigo", esPK = true)
	private int codigo;
	@Campo(nombre = "observacion")
	private String observacion;
	@Campo(nombre = "fecCreado")
	private LocalDate fecCreado;
	@Campo(nombre = "fecProgramada")
	private LocalDate fecProgramada;
	@Campo(nombre = "fecAtendido")
	private LocalDate fecAtendido;
	@Campo(nombre = "fecPagado")
	private LocalDate fecPagado;
	@Campo(nombre = "fecAnulado")
	private LocalDate fecAnulado;
	@Campo(nombre = "totalSoles")
	private double totalSoles;
	@Campo(nombre = "pagadoSoles")
	private double pagadoSoles;
	@Campo(nombre = "documento")
	private Documento documento;
	@Campo(nombre = "estadoPedido")
	private EstadoPedido estadoPedido;
	@Campo(nombre = "tipopedido")
	private TipoPedido tipoPedido;
	@Campo(nombre = "codCliente")
	private Cliente cliente;
	@Campo(nombre = "codMascota")
	private Mascota mascota;
	private List<DetallePedido> detallePedidos;

	public Pedido() {
	}

	public Pedido(int codigo, String observacion, LocalDate fecCreado, LocalDate fecProgramada, LocalDate fecAtendido, LocalDate fecPagado, LocalDate fecAnulado, double totalSoles, double pagadoSoles, Documento documento, EstadoPedido estadoPedido, TipoPedido tipoPedido, Cliente cliente, Mascota mascota, List<DetallePedido> detallePedidos) {
		this.codigo = codigo;
		this.observacion = observacion;
		this.fecCreado = fecCreado;
		this.fecProgramada = fecProgramada;
		this.fecAtendido = fecAtendido;
		this.fecPagado = fecPagado;
		this.fecAnulado = fecAnulado;
		this.totalSoles = totalSoles;
		this.pagadoSoles = pagadoSoles;
		this.documento = documento;
		this.estadoPedido = estadoPedido;
		this.tipoPedido = tipoPedido;
		this.cliente = cliente;
		this.mascota = mascota;
		this.detallePedidos = detallePedidos;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Mascota getMascota() {
		return mascota;
	}

	public void setMascota(Mascota mascota) {
		this.mascota = mascota;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public LocalDate getFecCreado() {
		return fecCreado;
	}

	public void setFecCreado(LocalDate fecCreado) {
		this.fecCreado = fecCreado;
	}

	public LocalDate getFecProgramada() {
		return fecProgramada;
	}

	public void setFecProgramada(LocalDate fecProgramada) {
		this.fecProgramada = fecProgramada;
	}

	public LocalDate getFecAtendido() {
		return fecAtendido;
	}

	public void setFecAtendido(LocalDate fecAtendido) {
		this.fecAtendido = fecAtendido;
	}

	public LocalDate getFecPagado() {
		return fecPagado;
	}

	public void setFecPagado(LocalDate fecPagado) {
		this.fecPagado = fecPagado;
	}

	public LocalDate getFecAnulado() {
		return fecAnulado;
	}

	public void setFecAnulado(LocalDate fecAnulado) {
		this.fecAnulado = fecAnulado;
	}

	public double getTotalSoles() {
		return totalSoles;
	}

	public void setTotalSoles(double totalSoles) {
		this.totalSoles = totalSoles;
	}

	public double getPagadoSoles() {
		return pagadoSoles;
	}

	public void setPagadoSoles(double pagadoSoles) {
		this.pagadoSoles = pagadoSoles;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public EstadoPedido getEstadoPedido() {
		return estadoPedido;
	}

	public void setEstadoPedido(EstadoPedido estadoPedido) {
		this.estadoPedido = estadoPedido;
	}

	public TipoPedido getTipoPedido() {
		return tipoPedido;
	}

	public void setTipoPedido(TipoPedido tipoPedido) {
		this.tipoPedido = tipoPedido;
	}

	public List<DetallePedido> getDetallePedidos() {
		return detallePedidos;
	}

	public void setDetallePedidos(List<DetallePedido> detallePedidos) {
		this.detallePedidos = detallePedidos;
	}

	@Override
	public String toString() {
		return "Pedido{" +
				"codigo=" + codigo +
				", observacion='" + observacion + '\'' +
				", fecCreado=" + fecCreado +
				", fecProgramada=" + fecProgramada +
				", fecAtendido=" + fecAtendido +
				", fecPagado=" + fecPagado +
				", fecAnulado=" + fecAnulado +
				", totalSoles=" + totalSoles +
				", pagadoSoles=" + pagadoSoles +
				", documento=" + documento +
				", estadoPedido=" + estadoPedido +
				", tipoPedido=" + tipoPedido +
				", cliente=" + cliente +
				", mascota=" + mascota +
				", detallePedidos=" + detallePedidos +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Pedido)) return false;
		Pedido pedido = (Pedido) o;
		return codigo == pedido.codigo &&
				Double.compare(pedido.totalSoles, totalSoles) == 0 &&
				Double.compare(pedido.pagadoSoles, pagadoSoles) == 0 &&
				Objects.equals(observacion, pedido.observacion) &&
				Objects.equals(fecCreado, pedido.fecCreado) &&
				Objects.equals(fecProgramada, pedido.fecProgramada) &&
				Objects.equals(fecAtendido, pedido.fecAtendido) &&
				Objects.equals(fecPagado, pedido.fecPagado) &&
				Objects.equals(fecAnulado, pedido.fecAnulado) &&
				documento == pedido.documento &&
				estadoPedido == pedido.estadoPedido &&
				tipoPedido == pedido.tipoPedido &&
				Objects.equals(cliente, pedido.cliente) &&
				Objects.equals(mascota, pedido.mascota) &&
				Objects.equals(detallePedidos, pedido.detallePedidos);
	}

}
