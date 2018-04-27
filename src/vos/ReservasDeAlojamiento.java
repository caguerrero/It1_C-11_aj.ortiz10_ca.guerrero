package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ReservasDeAlojamiento {
	@JsonProperty(value="idReserva")
	private Long idReserva;

	@JsonProperty(value="idAlojamiento")
	private Long idAlojamiento;

	/**
	 * @param idReserva
	 * @param idAlojamiento
	 */
	public ReservasDeAlojamiento(@JsonProperty(value="idReserva") Long idReserva, @JsonProperty(value="idAlojamiento") Long idAlojamiento) {
		this.idReserva = idReserva;
		this.idAlojamiento = idAlojamiento;
	}

	/**
	 * @return the idReserva
	 */
	public Long getIdReserva() {
		return idReserva;
	}

	/**
	 * @param idReserva the idReserva to set
	 */
	public void setIdReserva(Long idReserva) {
		this.idReserva = idReserva;
	}

	/**
	 * @return the idAlojamiento
	 */
	public Long getIdAlojamiento() {
		return idAlojamiento;
	}

	/**
	 * @param idAlojamiento the idAlojamiento to set
	 */
	public void setIdAlojamiento(Long idAlojamiento) {
		this.idAlojamiento = idAlojamiento;
	}
}