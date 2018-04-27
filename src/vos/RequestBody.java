package vos;

import java.sql.Date;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RequestBody {
	@XmlElement private Date fecha1;
    @XmlElement private Date fecha2;
    @XmlElement private ArrayList<String> servicios;
    public RequestBody(@XmlElement Date fecha1, @XmlElement Date fecha2, @XmlElement ArrayList<String> servicios) {
    	this.fecha1 = fecha1;
    	this.fecha2 = fecha2;
    	this.servicios = servicios;
    }
	public Date getFecha1() {
		return fecha1;
	}
	public void setFecha1(Date fecha1) {
		this.fecha1 = fecha1;
	}
	public Date getFecha2() {
		return fecha2;
	}
	public void setFecha2(Date fecha2) {
		this.fecha2 = fecha2;
	}
	public ArrayList<String> getServicios() {
		return servicios;
	}
	public void setServicios(ArrayList<String> servicios) {
		this.servicios = servicios;
	}
    
}