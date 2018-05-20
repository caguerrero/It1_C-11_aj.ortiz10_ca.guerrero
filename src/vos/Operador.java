package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Operador {

	@JsonProperty(value="cedula_NIT")
	private Long cedula_NIT;
	
	@JsonProperty(value="direccion")
	private String direccion;
	
	@JsonProperty(value="nombre")
	private String nombre;

	@JsonProperty(value="tipo")
	private String tipo;
	
	@JsonProperty(value="horarioApertura")
	private String horarioApertura;
	
	@JsonProperty(value="horarioCierre")
	private String horarioCierre;
	/**
	 * @param cedula_NIT
	 * @param direccion
	 * @param nombre
	 */
	public Operador(@JsonProperty(value="cedula_NIT") Long cedula_NIT, @JsonProperty(value="direccion") String direccion, 
			 @JsonProperty(value="nombre")String nombre, @JsonProperty(value="tipo")String tipo, 
			 @JsonProperty(value="horarioApertura") String horarioApertura, @JsonProperty(value="horarioCierre") String horarioCierre) {
		this.cedula_NIT = cedula_NIT;
		this.direccion = direccion;
		this.nombre = nombre;
		this.tipo = tipo;
		this.horarioApertura = horarioApertura;
		this.horarioCierre = horarioCierre;
	}

	/**
	 * @return the cedula_NIT
	 */
	public Long getCedula_NIT() {
		return cedula_NIT;
	}

	/**
	 * @param cedula_NIT the cedula_NIT to set
	 */
	public void setCedula_NIT(Long cedula_NIT) {
		this.cedula_NIT = cedula_NIT;
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
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
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getHorarioApertura() {
		return horarioApertura;
	}

	public void setHorarioApertura(String horarioApertura) {
		this.horarioApertura = horarioApertura;
	}

	public String getHorarioCierre() {
		return horarioCierre;
	}

	public void setHorarioCierre(String horarioCierre) {
		this.horarioCierre = horarioCierre;
	}
	
}