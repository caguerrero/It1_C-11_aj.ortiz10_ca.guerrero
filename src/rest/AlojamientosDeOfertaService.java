package rest;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.AlohAndesTransactionManager;
import vos.AlojamientosDeOferta;

@Path("alojamientosDeOferta")
public class AlojamientosDeOfertaService {

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
	 * Metodo GET que trae a todos los alojamientosDeOferta en la Base de datos. <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/alojamientosDeOferta <br/>
	 * @return	<b>Response Status 200</b> - JSON que contiene a todos los alojamientosDeOferta que estan en la Base de Datos <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */			
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAlojamientosDeOferta() {

		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());

			List<AlojamientosDeOferta> alojamientosDeOferta;
			alojamientosDeOferta = tm.getAllAlojamientosDeOferta();
			return Response.status(200).entity(alojamientosDeOferta).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo GET que trae al alojamientoDeOferta en la Base de Datos con el ID dado por parametro <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/alojamientosDeOferta/{id} <br/>
	 * @return	<b>Response Status 200</b> - JSON AlojamientoDeOferta que contiene al alojamientoDeOferta cuyo ID corresponda al parametro <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@GET
	@Path( "/lookById" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getAlojamientoDeOfertaById( @QueryParam( "id1" ) Long id1, @QueryParam( "id2" ) Long id2 )
	{
		try{
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath( ) );

			AlojamientosDeOferta alojamientoDeOferta = tm.getAlojamientoDeOfertaByIds( id1, id2 );
			return Response.status( 200 ).entity( alojamientoDeOferta ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}


	/**
	 * Metodo que recibe un alojamientoDeOferta en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al alojamientoDeOferta. <br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/alojamientosDeOferta <br/>
	 * @param alojamientoDeOferta JSON con la informacion del alojamientoDeOferta que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al alojamientoDeOferta que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)

	public Response addAlojamientoDeOferta(AlojamientosDeOferta alojamientoDeOferta) {

		try{
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath( ) );
			tm.addAlojamientoDeOferta(alojamientoDeOferta);
			return Response.status(200).entity(alojamientoDeOferta).build();
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	/**
	 * Metodo que recibe un alojamientoDeOferta en formato JSON y lo elimina de la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se elimina de la Base de datos al alojamientoDeOferta con la informacion correspondiente.<br/>
	 * <b>URL: </b> http://localhost:8080/TutorialParranderos/rest/alojamientosDeOferta <br/>
	 * @param alojamientoDeOferta JSON con la informacion del alojamientoDeOferta que se desea eliminar
	 * @return	<b>Response Status 200</b> - JSON que contiene al alojamientoDeOferta que se desea eliminar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteAlojamientoDeOferta(AlojamientosDeOferta alojamientoDeOferta) {
		try{
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath( ) );
			tm.deleteAlojamientoDeOferta(alojamientoDeOferta);
			return Response.status(200).entity(alojamientoDeOferta).build();
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
}