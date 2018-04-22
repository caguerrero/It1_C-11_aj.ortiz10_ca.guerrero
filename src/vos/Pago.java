package vos;

import org.codehaus.jackson.annotate.JsonProperty;

public class Pago {
	@JsonProperty(value="monto")
	private double monto;
	
	@JsonProperty(value="nombreOp")
	private String nombreOp;
	
	@JsonProperty(value="cedulaOp")
	private Long cedulaOp;
	
	public Pago(@JsonProperty(value="monto") double monto, @JsonProperty(value="nombreOp") String nombreOp, @JsonProperty(value="cedulaOp") Long cedulaOp)
	{
		this.monto = monto;
		this.nombreOp = nombreOp;
		this.cedulaOp = cedulaOp;
	}
}