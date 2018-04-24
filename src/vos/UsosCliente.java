package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class UsosCliente {
	@JsonProperty(value="idCliente")
	private Long idCliente;

	@JsonProperty(value="dias_total_reservados")
	private int dias_total_reservados;

	@JsonProperty(value="dias_de_estadia_pasados")
	private int dias_de_estadia_pasados;

	@JsonProperty(value="total_cancelado")
	private double total_cancelado;

	@JsonProperty(value="total_por_pagar")
	private double total_por_pagar;

	public UsosCliente(@JsonProperty(value="idCliente") Long idCliente, @JsonProperty(value="dias_total_reservados") int dias_total_reservados,
			@JsonProperty(value="dias_de_estadia_pasados") int dias_de_estadia_pasados, @JsonProperty(value="total_cancelado") double total_cancelado,
			@JsonProperty(value="total_por_pagar") double total_por_pagar)
	{
		this.idCliente = idCliente;
		this.dias_total_reservados = dias_total_reservados;
		this.dias_de_estadia_pasados = dias_de_estadia_pasados;
		this.total_cancelado = total_cancelado;
		this.total_por_pagar = total_por_pagar;
	}

	public Long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
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
