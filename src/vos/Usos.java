package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Usos {
	@JsonProperty(value="tipo_usuario")
	private String tipo_usuario;

	@JsonProperty(value="dias_total_reservados")
	private int dias_total_reservados;

	@JsonProperty(value="dias_de_estadia_pasados")
	private int dias_de_estadia_pasados;

	@JsonProperty(value="total_cancelado")
	private double total_cancelado;

	@JsonProperty(value="total_por_pagar")
	private double total_por_pagar;

	public Usos(@JsonProperty(value="tipo_usuario") String tipo_usuario, @JsonProperty(value="dias_total_reservados") int dias_total_reservados, 
			@JsonProperty(value="dias_de_estadia_pasados") int dias_de_estadia_pasados, @JsonProperty(value="total_cancelado") double total_cancelado,
			@JsonProperty(value="total_por_pagar") double total_por_pagar) {
		this.tipo_usuario = tipo_usuario;
		this.dias_total_reservados = dias_total_reservados;
		this.dias_de_estadia_pasados = dias_de_estadia_pasados;
		this.total_cancelado = total_cancelado;
		this.total_por_pagar = total_por_pagar;
	}

	public String getTipo_usuario() {
		return tipo_usuario;
	}

	public void setTipo_usuario(String tipo_usuario) {
		this.tipo_usuario = tipo_usuario;
	}

	public int getDias_total_reservados() {
		return dias_total_reservados;
	}

	public void setDias_total_reservados(int dias_total_reservados) {
		this.dias_total_reservados = dias_total_reservados;
	}

	public int getDias_de_estadia_pasados() {
		return dias_de_estadia_pasados;
	}

	public void setDias_de_estadia_pasados(int dias_de_estadia_pasados) {
		this.dias_de_estadia_pasados = dias_de_estadia_pasados;
	}

	public double getTotal_cancelado() {
		return total_cancelado;
	}

	public void setTotal_cancelado(double total_cancelado) {
		this.total_cancelado = total_cancelado;
	}

	public double getTotal_por_pagar() {
		return total_por_pagar;
	}

	public void setTotal_por_pagar(double total_por_pagar) {
		this.total_por_pagar = total_por_pagar;
	}

}
