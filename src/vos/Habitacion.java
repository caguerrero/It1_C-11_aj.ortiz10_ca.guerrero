package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Habitacion {

	@JsonProperty(value="idHabitacion")
	private Long idHabitacion;
	
	@JsonProperty(value="categoria")
	private String categoria;
	
	@JsonProperty(value="compartido")
	private String compartido;
	
	@JsonProperty(value="tipo")
	private String tipo;
	
	@JsonProperty(value="numCupos")
	private int numCupos;

	/**
	 * @param idHabitacion
	 * @param categoria
	 * @param compartido
	 * @param tipo
	 */
	public Habitacion(@JsonProperty(value="idHabitacion") Long idHabitacion, @JsonProperty(value="categoria") String categoria, @JsonProperty(value="compartido") String compartido,
			@JsonProperty(value="tipo") String tipo, @JsonProperty(value="numCupos") int numCupos) {
		this.idHabitacion = idHabitacion;
		this.categoria = categoria;
		this.compartido = compartido;
		this.tipo = tipo;
		this.numCupos = numCupos;
	}

	/**
	 * @return the idHabitacion
	 */
	public Long getIdHabitacion() {
		return idHabitacion;
	}

	/**
	 * @param idHabitacion the idHabitacion to set
	 */
	public void setIdHabitacion(Long idHabitacion) {
		this.idHabitacion = idHabitacion;
	}

	/**
	 * @return the categoria
	 */
	public String getCategoria() {
		return categoria;
	}

	/**
	 * @param categoria the categoria to set
	 */
	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	/**
	 * @return the compartido
	 */
	public String getCompartido() {
		return compartido;
	}

	/**
	 * @param compartido the compartido to set
	 */
	public void setCompartido(String compartido) {
		this.compartido = compartido;
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

	public int getNumCupos() {
		return numCupos;
	}

	public void setNumCupos(int numCupos) {
		this.numCupos = numCupos;
	}
	
	
}