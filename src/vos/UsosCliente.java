package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class UsosCliente {
	@JsonProperty(value="idCliente")
	private Long idCliente;

	@JsonProperty(value="total_reservado")
	private int total_reservado;

	@JsonProperty(value="total_estadia")
	private int total_estadia;

	@JsonProperty(value="total_ingresos")
	private double total_ingresos;

	@JsonProperty(value="total_por_recibir")
	private double total_por_recibir;
}
