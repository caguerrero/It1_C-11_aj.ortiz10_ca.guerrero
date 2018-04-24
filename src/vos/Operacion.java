package vos;

import java.sql.Date;

import org.codehaus.jackson.annotate.JsonProperty;

public class Operacion {
	@JsonProperty(value="fechas")
	private Date fechas;
	
	@JsonProperty(value="caracteristicas")
	private String caracteristicas;
	
	public Operacion(@JsonProperty(value="fechas") Date fechas, @JsonProperty(value="caracteristicas") String caracteristicas)
	{
		this.fechas = fechas;
		this.caracteristicas = caracteristicas;
	}

	public Date getFechas() {
		return fechas;
	}

	public void setFechas(Date fechas) {
		this.fechas = fechas;
	}

	public String getCaracteristicas() {
		return caracteristicas;
	}

	public void setCaracteristicas(String caracteristicas) {
		this.caracteristicas = caracteristicas;
	}
}
