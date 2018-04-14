package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Apartamento {

	@JsonProperty(value="idApartamento")
	private Long idApartamento;
	
	@JsonProperty(value="menaje")
	private String menaje;
	
	@JsonProperty(value="numHabitaciones")
	private int numHabitaciones;

	/**
	 * @param idApartamento
	 * @param menaje
	 * @param numHabitaciones
	 */
	public Apartamento(@JsonProperty(value="idApartamento")Long idApartamento, @JsonProperty(value="menaje")String menaje, @JsonProperty(value="numHabitaciones")int numHabitaciones) {
		this.idApartamento = idApartamento;
		this.menaje = menaje;
		this.numHabitaciones = numHabitaciones;
	}

	/**
	 * @return the idApartamento
	 */
	public Long getIdApartamento() {
		return idApartamento;
	}

	/**
	 * @param idApartamento the idApartamento to set
	 */
	public void setIdApartamento(Long idApartamento) {
		this.idApartamento = idApartamento;
	}

	/**
	 * @return the menaje
	 */
	public String getMenaje() {
		return menaje;
	}

	/**
	 * @param menaje the menaje to set
	 */
	public void setMenaje(String menaje) {
		this.menaje = menaje;
	}

	/**
	 * @return the numHabitaciones
	 */
	public int getNumHabitaciones() {
		return numHabitaciones;
	}

	/**
	 * @param numHabitaciones the numHabitaciones to set
	 */
	public void setNumHabitaciones(int numHabitaciones) {
		this.numHabitaciones = numHabitaciones;
	}
	
	
}