package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Servicio {

	@JsonProperty(value="descripcion")
	private String descripcion;
	
	@JsonProperty(value="idServicio")
	private Long idServicio;
	
	@JsonProperty(value="nombre")
	private String nombre;

	/**
	 * @param descripcion
	 * @param idServicio
	 * @param nombre
	 */
	public Servicio(@JsonProperty(value="descripcion") String descripcion, @JsonProperty(value="idServicio") Long idServicio, @JsonProperty(value="nombre")String nombre) {
		this.descripcion = descripcion;
		this.idServicio = idServicio;
		this.nombre = nombre;
	}

	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
}