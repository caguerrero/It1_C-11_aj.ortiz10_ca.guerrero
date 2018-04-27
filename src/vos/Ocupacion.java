package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Ocupacion {
	@JsonProperty(value="idAlojamiento")
	private Long idAlojamiento;
	
	@JsonProperty(value="indice_de_ocupacion")
	private double indice_de_ocupacion;
	
	public Ocupacion(@JsonProperty(value="idAlojamiento") Long idAlojamiento, @JsonProperty(value="indice_de_ocupacion") double indice_de_ocupacion) {
		this.idAlojamiento = idAlojamiento;
		this.indice_de_ocupacion = indice_de_ocupacion;
	}

	public Long getIdAlojamiento() {
		return idAlojamiento;
	}

	public void setIdAlojamiento(Long idAlojamiento) {
		this.idAlojamiento = idAlojamiento;
	}

	public double getIndice_de_ocupacion() {
		return indice_de_ocupacion;
	}

	public void setIndice_de_ocupacion(double indice_de_ocupacion) {
		this.indice_de_ocupacion = indice_de_ocupacion;
	}
}
