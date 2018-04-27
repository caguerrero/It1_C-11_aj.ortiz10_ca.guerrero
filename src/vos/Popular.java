package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Popular {
	@JsonProperty(value="idOferta")
	private Long idOferta;
	
	@JsonProperty(value="cantidadReservas")
	private int cantidadReservas;
	
	/**
	 * @param costoBase
	 * @param nombre
	 */
	public Popular(@JsonProperty(value="idOferta") Long idOferta,@JsonProperty(value="cantidadReservas") int cantidadReservas) {
		this.idOferta = idOferta;
		this.cantidadReservas = cantidadReservas;
	}

	public Long getIdOferta() {
		return idOferta;
	}

	public void setIdOferta(Long idOferta) {
		this.idOferta = idOferta;
	}

	public int getCantidadReservas() {
		return cantidadReservas;
	}

	public void setCantidadReservas(int cantidadReservas) {
		this.cantidadReservas = cantidadReservas;
	}
}
