package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Oferta {

	@JsonProperty(value="costoBase")
	private double costoBase;
	
	@JsonProperty(value="tiempoEnDias")
	private int tiempoEnDias;
	
	@JsonProperty(value="horarioApertura")
	private double horarioApertura;
	
	@JsonProperty(value="horarioCierre")
	private double horarioCierre;
	
	@JsonProperty(value="idOferta")
	private Long idOferta;
	
	@JsonProperty(value="idOperador")
	private Long idOperador;

	/**
	 * @param costoBase
	 * @param nombre
	 * @param rolUniandino
	 */
	public Oferta(@JsonProperty(value="costobase") double costobase,@JsonProperty(value="tiempoEnDias") int tiempoEnDias, @JsonProperty(value="horarioApertura") double horarioApertura,
			      @JsonProperty(value="horarioCierre") double horarioCierre, @JsonProperty(value="idOferta") Long idOferta, @JsonProperty(value="idOperador") Long idOperador) {
		this.costoBase = costobase;
		this.tiempoEnDias = tiempoEnDias;
		this.horarioApertura = horarioApertura;
		this.horarioCierre = horarioCierre;
		this.idOferta = idOferta;
		this.idOperador = idOperador;
	}

	public double getCostoBase() {
		return costoBase;
	}

	public void setCostoBase(double costoBase) {
		this.costoBase = costoBase;
	}

	public int getTiempoEnDias() {
		return tiempoEnDias;
	}

	public void setTiempoEnDias(int tiempoEnDias) {
		this.tiempoEnDias = tiempoEnDias;
	}

	public double getHorarioApertura() {
		return horarioApertura;
	}

	public void setHorarioApertura(double horarioApertura) {
		this.horarioApertura = horarioApertura;
	}

	public double getHorarioCierre() {
		return horarioCierre;
	}

	public void setHorarioCierre(double horarioCierre) {
		this.horarioCierre = horarioCierre;
	}

	public Long getIdOferta() {
		return idOferta;
	}

	public void setIdOferta(Long idOferta) {
		this.idOferta = idOferta;
	}

	public Long getIdOperador() {
		return idOperador;
	}

	public void setIdOperador(Long idOperador) {
		this.idOperador = idOperador;
	}
}