package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Categorizacion {
	@JsonProperty(value="semana")
	private int semana;
	
	@JsonProperty(value="categoria")
	private String categoria;
	
	@JsonProperty(value="idAlojamiento")
	private Long idAlojamiento;
	
	@JsonProperty(value="maximo")
	private int maximo;
	
	public Categorizacion(@JsonProperty(value="semana") int semana, @JsonProperty(value="categoria") String categoria,
	                      @JsonProperty(value="idAlojamiento") Long idAlojamiento, @JsonProperty(value="maximo") int maximo)
	{
		this.semana = semana;
		this.categoria = categoria;
		this.idAlojamiento = idAlojamiento;
		this.maximo = maximo;
	}

	public int getSemana() {
		return semana;
	}

	public void setSemana(int semana) {
		this.semana = semana;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public Long getIdAlojamiento() {
		return idAlojamiento;
	}

	public void setIdAlojamiento(Long idAlojamiento) {
		this.idAlojamiento = idAlojamiento;
	}

	public int getMaximo() {
		return maximo;
	}

	public void setMaximo(int maximo) {
		this.maximo = maximo;
	}
	
	
}
