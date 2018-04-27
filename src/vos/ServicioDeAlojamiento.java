package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServicioDeAlojamiento {

	@JsonProperty(value="idServicio")
	private Long idServicio;

	@JsonProperty(value="idAlojamiento")
	private Long idAlojamiento;

	/**
	 * @param idServicio
	 * @param idAlojamiento
	 */
	public ServicioDeAlojamiento(@JsonProperty(value="idServicio") Long idServicio, @JsonProperty(value="idAlojamiento") Long idAlojamiento) {
		this.idServicio = idServicio;
		this.idAlojamiento = idAlojamiento;
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