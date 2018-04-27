package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class AlojamientosDeOferta {

	@JsonProperty(value="idAlojamiento")
	private Long idAlojamiento;

	@JsonProperty(value="idOferta")
	private Long idOferta;

	/**
	 * @param cedula
	 * @param nombre
	 * @param rolUniandino
	 */
	public AlojamientosDeOferta(@JsonProperty(value="idAlojamiento") Long idAlojamiento, @JsonProperty(value="idOferta") Long idOferta) {
		this.idAlojamiento = idAlojamiento;
		this.idOferta = idOferta;
	}

	public Long getIdAlojamiento() {
		return idAlojamiento;
	}

	public void setIdAlojamiento(Long idAlojamiento) {
		this.idAlojamiento = idAlojamiento;
	}

	public Long getIdOferta() {
		return idOferta;
	}

	public void setIdOferta(Long idOferta) {
		this.idOferta = idOferta;
	}
	
}