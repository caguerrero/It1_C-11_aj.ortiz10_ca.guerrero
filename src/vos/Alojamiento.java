package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Alojamiento {

	@JsonProperty(value="capacidad")
	private int capacidad;
	
	@JsonProperty(value="idAlojamiento")
	private Long idAlojamiento;
	
	@JsonProperty(value="tamaño")
	private double tamaño;
	
	@JsonProperty(value="ubicacion")
	private String ubicacion;

	/**
	 * @param capacidad
	 * @param idOfertaAlojamiento
	 * @param tamaño
	 * @param ubicacion
	 */
	public Alojamiento(@JsonProperty(value="capacidad")int capacidad, @JsonProperty(value="idAlojamiento")Long idAlojamiento, 
			@JsonProperty(value="tamaño")double tamaño, @JsonProperty(value="ubicacion")String ubicacion) {
		this.capacidad = capacidad;
		this.idAlojamiento = idAlojamiento;
		this.tamaño = tamaño;
		this.ubicacion = ubicacion;
	}

	/**
	 * @return the capacidad
	 */
	public int getCapacidad() {
		return capacidad;
	}

	/**
	 * @param capacidad the capacidad to set
	 */
	public void setCapacidad(int capacidad) {
		this.capacidad = capacidad;
	}

	/**
	 * @return the idOfertaAlojamiento
	 */
	public Long getIdAlojamiento() {
		return idAlojamiento;
	}

	/**
	 * @param idOfertaAlojamiento the idOfertaAlojamiento to set
	 */
	public void setIdAlojamiento(Long idAlojamiento) {
		this.idAlojamiento = idAlojamiento;
	}

	/**
	 * @return the tamaño
	 */
	public double getTamaño() {
		return tamaño;
	}

	/**
	 * @param tamaño the tamaño to set
	 */
	public void setTamaño(double tamaño) {
		this.tamaño = tamaño;
	}

	/**
	 * @return the ubicacion
	 */
	public String getUbicacion() {
		return ubicacion;
	}

	/**
	 * @param ubicacion the ubicacion to set
	 */
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}	
}