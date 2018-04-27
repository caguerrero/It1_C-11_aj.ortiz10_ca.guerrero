package rest;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import tm.AlohAndesTransactionManager;
import vos.Alojamiento;
import vos.Ocupacion;

@Path("alojamientos")
public class AlojamientoService {

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
	 * Metodo GET que trae a todos los alojamientos en la Base de datos. <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/It1_C-11_aj.ortiz10_ca.guerrero/rest/ofertasAlojamiento <br/>
	 * @return	<b>Response Status 200</b> - JSON que contiene a todos los ofertasAlojamiento que estan en la Base de Datos <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */			
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getAlojamientos() {

		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager(getPath());

			List<Alojamiento> ofertasAlojamiento;
			ofertasAlojamiento = tm.getAllAlojamiento();
			return Response.status(200).entity(ofertasAlojamiento).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}

	/**
	 * Metodo GET que trae al alojamiento en la Base de Datos con el ID dado por parametro <br/>
	 * <b>Precondicion: </b> el archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>URL: </b> http://localhost:8080/IIt1_aj.ortiz10_ca.guerrero/rest/ofertasAlojamiento/{id} <br/>
	 * @return	<b>Response Status 200</b> - JSON Alojamiento que contiene al alojamiento cuyo ID corresponda al parametro <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@GET
	@Path( "{id: \\d+}" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getAlojamientoById( @PathParam( "id" ) Long id )
	{
		try{
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath( ) );

			Alojamiento Alojamiento = tm.getAlojamientoById( id );
			return Response.status( 200 ).entity( Alojamiento ).build( );			
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	/**
	 * Metodo que recibe un alojamiento en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se agrega a la Base de datos la informacion correspondiente al alojamiento. <br/>
	 * <b>URL: </b> http://localhost:8080/IIt1_aj.ortiz10_ca.guerrero/rest/ofertasAlojamiento <br/>
	 * @param alojamiento JSON con la informacion del alojamiento que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al alojamiento que ha sido agregado <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)

	public Response addAlojamiento(Alojamiento alojamiento) {
		try{
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath( ) );
			tm.addAlojamiento(alojamiento);
			return Response.status(200).entity(alojamiento).build();
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	/**
	 * Metodo que recibe un alojamiento en formato JSON y lo agrega a la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se actualiza la Base de datos con la informacion correspondiente al alojamiento.<br/>
	 * @param alojamiento JSON con la informacion del alojamiento que se desea agregar
	 * @return	<b>Response Status 200</b> - JSON que contiene al alojamiento que se desea modificar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateAlojamiento(Alojamiento alojamiento) {
		try{
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath( ) );
			tm.updateAlojamiento(alojamiento);
			return Response.status(200).entity(alojamiento).build();
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}

	/**
	 * Metodo que recibe un alojamiento en formato JSON y lo elimina de la Base de Datos <br/>
	 * <b>Precondicion: </b> El archivo <em>'conectionData'</em> ha sido inicializado con las credenciales del usuario <br/>
	 * <b>Postcondicion: </b> Se elimina de la Base de datos al alojamiento con la informacion correspondiente.<br/>
	 * <b>URL: </b> http://localhost:8080/It1_aj.ortiz10_ca.guerrero/rest/ofertasAlojamiento <br/>
	 * @param alojamiento JSON con la informacion del alojamiento que se desea eliminar
	 * @return	<b>Response Status 200</b> - JSON que contiene al alojamiento que se desea eliminar <br/>
	 * 			<b>Response Status 500</b> - Excepcion durante el transcurso de la transaccion
	 */
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response deleteAlojamiento(Alojamiento alojamiento) {
		try{
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath( ) );
			tm.deleteAlojamiento(alojamiento);
			return Response.status(200).entity(alojamiento).build();
		}
		catch( Exception e )
		{
			return Response.status( 500 ).entity( doErrorMessage( e ) ).build( );
		}
	}
	
	@GET
	@Path( "filtrar" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getAlojamientosFiltrados(@QueryParam( "fecha1" ) String fecha1, @QueryParam( "fecha2" ) String fecha2, @QueryParam( "servicios" ) String servicios) {
		
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath() );
			
			fecha1 = fecha1.replace("-", "/");
			fecha2 = fecha2.replace("-", "/");
			
			String[] servicios1 = servicios.split(" ");
			List<String> servicios2 = new ArrayList<String>();
			int i = 0;
			while(i < servicios1.length){
				servicios2.add(servicios1[i]);
				i++;
			}
			
			List<Alojamiento> alojamientos;
			alojamientos = tm.getAlojamientosFiltrados(fecha1, fecha2, servicios2);
			return Response.status(200).entity(alojamientos).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
	@GET
	@Path( "indiceOcupacion" )
	@Produces( { MediaType.APPLICATION_JSON } )
	public Response getOcupacionAlojamientos() {
		
		try {
			AlohAndesTransactionManager tm = new AlohAndesTransactionManager( getPath() );
			List<Ocupacion> ocupaciones;
			ocupaciones = tm.getOcupacionAlojamientos();
			return Response.status(200).entity(ocupaciones).build();
		} 
		catch (Exception e) {
			return Response.status(500).entity(doErrorMessage(e)).build();
		}
	}
	
}