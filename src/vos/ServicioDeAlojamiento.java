package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServicioDeAlojamiento {

	@JsonProperty(value="idServicio")
	private Long idServicio;

	@JsonProperty(value="idOfertaAlojamiento")
	private Long idOfertaAlojamiento;

	/**
	 * @param idServicio
	 * @param idOfertaAlojamiento
	 */
	public ServicioDeAlojamiento(@JsonProperty(value="idServicio") Long idServicio, @JsonProperty(value="idOfertaAlojamiento") Long idOfertaAlojamiento) {
		this.idServicio = idServicio;
		this.idOfertaAlojamiento = idOfertaAlojamiento;
	}

	/**
	 * @return the idServicio
	 */
	public Long getIdServicio() {
		return idServicio;
	}

	/**
	 * @param idServicio the idServicio to set
	 */
	public void setIdServicio(Long idServicio) {
		this.idServicio = idServicio;
	}

	/**
	 * @return the idOfertaAlojamiento
	 */
	public Long getIdOfertaAlojamiento() {
		return idOfertaAlojamiento;
	}

	/**
	 * @param idOfertaAlojamiento the idOfertaAlojamiento to set
	 */
	public void setIdOfertaAlojamiento(Long idOfertaAlojamiento) {
		this.idOfertaAlojamiento = idOfertaAlojamiento;
	}	
}