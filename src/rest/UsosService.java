package rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.AlohAndesTransactionManager;
import vos.Operacion;
import vos.Usos;
import vos.UsosCliente;
import vos.UsosOperador;

@Path("usoComunidad")
public class UsosService {

	//----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Atributo que usa la anotacion @Context para tener el ServletContext de la conexion actual.
	 */
	@Context
	private ServletContext context;

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE INICIALIZACION
	//----------------------------------------------------------------------------------------------------------------------------------
	/**
	 * Metodo que retorna el path de la carpeta WEB-INF/ConnectionData en el deploy actual dentro del servidor.
	 * @return path de la carpeta WEB-INF/ConnectionData en el deploy actual.
	 */
	private String getPath() {
		return context.getRealPath("WEB-INF/ConnectionData");
	}


	private String doErrorMessage(Exception e){
		return "{ \"ERROR\": \""+ e.getMessage() + "\"}" ;
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS REST
	//----------------------------------------------------------------------------------------------------------------------------------
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getUsoComunidad() {

		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());

			List<Usos> usos;
			usos = tm.getUsoComunidad();
			return Response.status(200).entity(usos).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@GET
	@Path("operador" )
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getUsoOperador(@QueryParam( "cedula" ) Long idOperador) {

		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());

			List<UsosOperador> usosOperador;
			usosOperador = tm.getUsoOperador(idOperador);
			return Response.status(200).entity(usosOperador).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@GET
	@Path("cliente")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getUsoCliente(@QueryParam( "cedula" ) Long idCliente) {

		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());

			List<UsosCliente> usosCliente;
			usosCliente = tm.getUsoCliente(idCliente);
			return Response.status(200).entity(usosCliente).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	@GET
	@Path("operacionAlohAndes")
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getOperacionAlohAndes(@QueryParam( "fecha1" ) String fecha1, @QueryParam( "fecha2" ) String fecha2) {

		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());

			List<Operacion> operaciones;
			operaciones = tm.getOperacionAloHandes(fecha1, fecha2);
			return Response.status(200).entity(operaciones).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
}
