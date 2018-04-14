package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Cliente {

	@JsonProperty(value="cedula")
	private Long cedula;
	
	@JsonProperty(value="nombre")
	private String nombre;
	
	@JsonProperty(value="rolUniandino")
	private String rolUniandino;

	/**
	 * @param cedula
	 * @param nombre
	 * @param rolUniandino
	 */
	public Cliente(@JsonProperty(value="cedula") Long cedula, @JsonProperty(value="nombre") String nombre, @JsonProperty(value="rolUniandino") String rolUniandino) {
		this.cedula = cedula;
		this.nombre = nombre;
		this.rolUniandino = rolUniandino;
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
	
	
}