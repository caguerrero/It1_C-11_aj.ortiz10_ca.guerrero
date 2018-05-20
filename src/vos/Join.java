package vos;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Join {
	@JsonProperty(value="cedula")
	private Long cedula;
	
	@JsonProperty(value="nombre")
	private String nombre;
	
	@JsonProperty(value="rolUniandino")
	private String rolUniandino;
	
	@JsonProperty(value="idAlojamiento")
	private Long idAlojamiento;
	
	@JsonProperty(value="fechaReserva")
	private Date fechaReserva;

	/**
	 * @param cedula
	 * @param nombre
	 * @param rolUniandino
	 */
	public Join(@JsonProperty(value="cedula") Long cedula, @JsonProperty(value="nombre") String nombre, @JsonProperty(value="rolUniandino") String rolUniandino,
			    @JsonProperty(value="idAlojamiento") Long idAlojamiento, @JsonProperty(value="fechaReserva")Date fechaReserva) {
		this.cedula = cedula;
		this.nombre = nombre;
		this.rolUniandino = rolUniandino;
		this.idAlojamiento = idAlojamiento;
		this.fechaReserva = fechaReserva;
	}

	/**
	 * @return the cedula
	 */
	public Long getCedula() {
		return cedula;
	}

	/**
	 * @param cedula the cedula to set
	 */
	public void setCedula(Long cedula) {
		this.cedula = cedula;
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

	/**
	 * @return the rolUniandino
	 */
	public String getRolUniandino() {
		return rolUniandino;
	}

	/**
	 * @param rolUniandino the rolUniandino to set
	 */
	public void setRolUniandino(String rolUniandino) {
		this.rolUniandino = rolUniandino;
	}

	public Long getIdAlojamiento() {
		return idAlojamiento;
	}

	public void setIdAlojamiento(Long idAlojamiento) {
		this.idAlojamiento = idAlojamiento;
	}

	public Date getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;
	}
}
