package vos;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Reserva {

	@JsonProperty(value="fechaReserva")
	private Date fechaReserva;
	
	@JsonProperty(value="finEstadia")
	private Date finEstadia;
	
	@JsonProperty(value="idReserva")
	private Long idReserva;
	
	@JsonProperty(value="inicioEstadia")
	private Date inicioEstadia;
	
	@JsonProperty(value="precio")
	private double precio;
	
	@JsonProperty(value="idCliente")
	private Long idCliente;
	
	@JsonProperty(value="cancelado")
	private int cancelado;
	/**
	 * @param fechaReserva
	 * @param finEstadia
	 * @param idReserva
	 * @param inicioEstadia
	 * @param precio
	 * @param idCliente
	 * @param idAlojamiento
	 * @param cancelado
	 */
	public Reserva(@JsonProperty(value="fechaReserva")Date fechaReserva, @JsonProperty(value="finEstadia")Date finEstadia, @JsonProperty(value="idReserva")Long idReserva, @JsonProperty(value="inicioEstadia")Date inicioEstadia, 
			@JsonProperty(value="precio")double precio, @JsonProperty(value="idCliente")Long idCliente, @JsonProperty(value="cancelado")int cancelado) {
		this.fechaReserva = fechaReserva;
		this.finEstadia = finEstadia;
		this.idReserva = idReserva;
		this.inicioEstadia = inicioEstadia;
		this.precio = precio;
		this.idCliente = idCliente;
		this.cancelado = cancelado;
	}

	/**
	 * @return the fechaReserva
	 */
	public Date getFechaReserva() {
		return fechaReserva;
	}

	/**
	 * @param fechaReserva the fechaReserva to set
	 */
	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	/**
	 * @return the finEstadia
	 */
	public Date getFinEstadia() {
		return finEstadia;
	}

	/**
	 * @param finEstadia the finEstadia to set
	 */
	public void setFinEstadia(Date finEstadia) {
		this.finEstadia = finEstadia;
	}

	/**
	 * @return the idReserva
	 */
	public Long getIdReserva() {
		return idReserva;
	}

	/**
	 * @param idReserva the idReserva to set
	 */
	public void setIdReserva(Long idReserva) {
		this.idReserva = idReserva;
	}

	/**
	 * @return the inicioEstadia
	 */
	public Date getInicioEstadia() {
		return inicioEstadia;
	}

	/**
	 * @param inicioEstadia the inicioEstadia to set
	 */
	public void setInicioEstadia(Date inicioEstadia) {
		this.inicioEstadia = inicioEstadia;
	}

	/**
	 * @return the precio
	 */
	public double getPrecio() {
		return precio;
	}

	/**
	 * @param precio the precio to set
	 */
	public void setPrecio(double precio) {
		this.precio = precio;
	}

	/**
	 * @return the idCliente
	 */
	public Long getIdCliente() {
		return idCliente;
	}

	/**
	 * @param idCliente the idCliente to set
	 */
	public void setIdCliente(Long idCliente) {
		this.idCliente = idCliente;
	}

	/**
	 * @return the cancelado
	 */
	public int getCancelado() {
		return cancelado;
	}

	/**
	 * @param cancelado the cancelado to set
	 */
	public void setCancelado(int cancelado) {
		this.cancelado = cancelado;
	}
}