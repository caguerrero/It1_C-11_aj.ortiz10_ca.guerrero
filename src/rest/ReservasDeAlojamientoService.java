package rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.AlohAndesTransactionManager;
import vos.ReservasDeAlojamiento;

@Path("reservasDeAlojamiento")
public class ReservasDeAlojamientoService {

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

	/**
	 * Metodo GET que trae a todos los serviciosDeAlojamiento en la Base de datos. <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/serviciosDeAlojamiento <br/>
	 * @return	<b>Response Status 200</b> - JSON que contiene a todos los serviciosDeAlojamiento que estan en la Base de Datos <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */			
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getReservasDeAlojamiento() {

		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());

			List<ReservasDeAlojamiento> serviciosDeAlojamiento;
			serviciosDeAlojamiento = tm.getAllReservasDeAlojamiento();
			return Response.status(200).entity(serviciosDeAlojamiento).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo GET que trae al servicioDeAlojamiento en la Base de Datos con el ID dado por parametro <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/serviciosDeAlojamiento/{id} <br/>
	 * @return	<b>Response Status 200</b> - JSON ReservasDeAlojamiento que contiene al servicioDeAlojamiento cuyo ID corresponda al parametro <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@GET
	@Path( "/lookById" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getReservasDeAlojamientoById( @QueryParam( "id1" ) Long id1, @QueryParam( "id2" ) Long id2 )
	{
		try{
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath( ) );

			ReservasDeAlojamiento servicioDeAlojamiento = tm.getReservasDeAlojamientoByIds( id1, id2 );
			return Response.status( 200 ).entity( servicioDeAlojamiento ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


	/**
	 * Metodo que recibe un servicioDeAlojamiento en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al servicioDeAlojamiento. <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/serviciosDeAlojamiento <br/>
	 * @param servicioDeAlojamiento JSON con la informacion del servicioDeAlojamiento que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al servicioDeAlojamiento que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)

	public Response addReservasDeAlojamiento(ReservasDeAlojamiento servicioDeAlojamiento) {

		try{
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath( ) );
			tm.addReservasDeAlojamiento(servicioDeAlojamiento);
			return Response.status(200).entity(servicioDeAlojamiento).build();
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	/**
	 * Metodo que recibe un servicioDeAlojamiento en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se actualiza la Base de datos con la informacion correspondiente al servicioDeAlojamiento.<br/>
	 * @param servicioDeAlojamiento JSON con la informacion del servicioDeAlojamiento que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al servicioDeAlojamiento que se desea modificar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateReservasDeAlojamiento(ReservasDeAlojamiento servicioDeAlojamiento) {
		try{
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath( ) );
			tm.updateReservasDeAlojamiento(servicioDeAlojamiento);
			return Response.status(200).entity(servicioDeAlojamiento).build();
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	/**
	 * Metodo que recibe un servicioDeAlojamiento en formato JSON y lo elimina de la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se elimina de la Base de datos al servicioDeAlojamiento con la informacion correspondiente.<br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/serviciosDeAlojamiento <br/>
	 * @param servicioDeAlojamiento JSON con la informacion del servicioDeAlojamiento que se desea eliminar
	 * @return	<b>Response Status 200</b> - JSON que contiene al servicioDeAlojamiento que se desea eliminar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteReservasDeAlojamiento(ReservasDeAlojamiento servicioDeAlojamiento) {
		try{
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath( ) );
			tm.deleteReservasDeAlojamiento(servicioDeAlojamiento);
			return Response.status(200).entity(servicioDeAlojamiento).build();
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
}