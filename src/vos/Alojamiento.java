package vos;

import java.sql.Date;
import org.codehaus.jackson.annotate.JsonProperty;

public class Alojamiento {

	@JsonProperty(value="capacidad")
	private int capacidad;
	
	@JsonProperty(value="idAlojamiento")
	private Long idAlojamiento;
	
	@JsonProperty(value="tama�o")
	private double tama�o;
	
	@JsonProperty(value="ubicacion")
	private String ubicacion;
	
	@JsonProperty(value="habilitado")
	private int habilitado;
	
	@JsonProperty(value="fecha_apertura")
	private Date fecha_apertura;
	
	/**
	 * @param capacidad
	 * @param idOfertaAlojamiento
	 * @param tama�o
	 * @param ubicacion
	 * @param habilitado
	 * @param fecha_apertura
	 */
	public Alojamiento(@JsonProperty(value="capacidad")int capacidad, @JsonProperty(value="idAlojamiento")Long idAlojamiento, 
			@JsonProperty(value="tama�o")double tama�o, @JsonProperty(value="ubicacion")String ubicacion, @JsonProperty(value="habilitado")int habilitado, @JsonProperty(value="fecha_apertura")Date fecha_apertura) {
		this.capacidad = capacidad;
		this.idAlojamiento = idAlojamiento;
		this.tama�o = tama�o;
		this.ubicacion = ubicacion;
		this.habilitado = habilitado;
		this.fecha_apertura = fecha_apertura;
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
	 * @return the tama�o
	 */
	public double getTama�o() {
		return tama�o;
	}

	/**
	 * @param tama�o the tama�o to set
	 */
	public void setTama�o(double tama�o) {
		this.tama�o = tama�o;
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

	public int getHabilitado() {
		return habilitado;
	}

	public void setHabilitado(int habilitado) {
		this.habilitado = habilitado;
	}

	public Date getFecha_apertura() {
		return fecha_apertura;
	}

	public void setFecha_apertura(Date fecha_apertura) {
		this.fecha_apertura = fecha_apertura;
	}
}