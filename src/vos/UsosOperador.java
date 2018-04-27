package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class UsosOperador {
	@JsonProperty(value="cedula_nit")
	private Long cedula_nit;

	@JsonProperty(value="total_reservado")
	private int total_reservado;

	@JsonProperty(value="total_estadia")
	private int total_estadia;

	@JsonProperty(value="total_ingresos")
	private double total_ingresos;

	@JsonProperty(value="total_por_recibir")
	private double total_por_recibir;

	public UsosOperador(@JsonProperty(value="cedula_nit") Long cedula_nit, @JsonProperty(value="total_reservado") int total_reservado, @JsonProperty(value="total_estadia") int total_estadia, @JsonProperty(value="total_ingresos") double total_ingresos, @JsonProperty(value="total_por_recibir") double total_por_recibir) {
		this.cedula_nit = cedula_nit;
		this.total_reservado = total_reservado;
		this.total_estadia = total_estadia;
		this.total_ingresos = total_ingresos;
		this.total_por_recibir = total_por_recibir;
	}

	public Long getCedula_nit() {
		return cedula_nit;
	}

	public void setCedula_nit(Long cedula_nit) {
		this.cedula_nit = cedula_nit;
	}

	public int getTotal_reservado() {
		return total_reservado;
	}

	public void setTotal_reservado(int total_reservado) {
		this.total_reservado = total_reservado;
	}

	public int getTotal_estadia() {
		return total_estadia;
	}

	public void setTotal_estadia(int total_estadia) {
		this.total_estadia = total_estadia;
	}

	public double getTotal_ingresos() {
		return total_ingresos;
	}

	public void setTotal_ingresos(double total_ingresos) {
		this.total_ingresos = total_ingresos;
	}

	public double getTotal_por_recibir() {
		return total_por_recibir;
	}

	public void setTotal_por_recibir(double total_por_recibir) {
		this.total_por_recibir = total_por_recibir;
	}

}
