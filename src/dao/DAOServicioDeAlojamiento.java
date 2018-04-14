package dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;

public class DAOServicioDeAlojamiento {

	//----------------------------------------------------------------------------------------------------------------------------------
	// CONSTANTES
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Constante para indicar el usuario Oracle del estudiante
	 */
	public final static String USUARIO = "ISIS2304A911810";


	//----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Arraylits de recursos que se usan para la ejecucion de sentencias SQL
	 */
	private ArrayList<Object> recursos;

	/**
	 * Atributo que genera la conexion a la base de datos
	 */
	private Connection conn;

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE INICIALIZACION
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo constructor de la clase DAOReserva <br/>
	 */
	public DAOServicioDeAlojamiento() {
		recursos = new ArrayList<Object>();
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE COMUNICACION CON LA BASE DE DATOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo que obtiene la informacion de todos los serviciosDeAlojamiento en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los serviciosDeAlojamiento que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<ServicioDeAlojamiento> getAllServiciosDeAlojamiento() throws SQLException, Exception {
		ArrayList<ServicioDeAlojamiento> serviciosDeAlojamiento = new ArrayList<ServicioDeAlojamiento>();

		String sql = String.format("SELECT * FROM %1$s.SERVICIOSDEALOJAMIENTO", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			serviciosDeAlojamiento.add(convertResultSetToServicioDeAlojamiento(rs));
		}
		return serviciosDeAlojamiento;
	}

	/**
	 * Metodo que obtiene la informacion del servicioDeAlojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del servicioDeAlojamiento
	 * @return la informacion del servicioDeAlojamiento que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el servicioDeAlojamiento conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ServicioDeAlojamiento findServicioDeAlojamientoByIds(Long id1, Long id2) throws SQLException, Exception 
	{
		ServicioDeAlojamiento servicioDeAlojamiento = null;

		String sql = String.format("SELECT * FROM %1$s.SERVICIOSDEALOJAMIENTO WHERE IDSERVICIO = %2$d AND IDOFERTAALOJAMIENTO = %3$d", USUARIO, id1, id2); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			servicioDeAlojamiento = convertResultSetToServicioDeAlojamiento(rs);
		}

		return servicioDeAlojamiento;
	}

	/**
	 * Metodo que agregar la informacion de un nuevo servicioDeAlojamiento en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param servicioDeAlojamiento ServicioDeAlojamiento que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addServicioDeAlojamiento(ServicioDeAlojamiento servicioDeAlojamiento) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.SERVICIOSDEALOJAMIENTO (IDOFERTAALOJAMIENTO , IDSERVICIO) VALUES (%2$s, '%3$s')", 
				USUARIO, 
				servicioDeAlojamiento.getIdOfertaAlojamiento(),
				servicioDeAlojamiento.getIdServicio());

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza la informacion del servicioDeAlojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param servicioDeAlojamiento ServicioDeAlojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateServicioDeAlojamiento(ServicioDeAlojamiento servicioDeAlojamiento) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.SERVICIOSDEALOJAMIENTO ", USUARIO));
		sql.append ("WHERE IDSERVICIO = " + servicioDeAlojamiento.getIdServicio() + " AND IDOFERTAALOJAMIENTO = " + servicioDeAlojamiento.getIdOfertaAlojamiento());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza la informacion del servicioDeAlojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param servicioDeAlojamiento ServicioDeAlojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteServicioDeAlojamiento(ServicioDeAlojamiento servicioDeAlojamiento) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.SERVICIOSDEALOJAMIENTO WHERE IDSERVICIO = %2$d AND IDOFERTAALOJAMIENTO = %3$d", USUARIO, servicioDeAlojamiento.getIdServicio(), servicioDeAlojamiento.getIdOfertaAlojamiento());

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS AUXILIARES
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo encargado de inicializar la conexion del DAO a la Base de Datos a partir del parametro <br/>
	 * <b>Postcondicion: </b> el atributo conn es inicializado <br/>
	 * @param connection la conexion generada en el TransactionManager para la comunicacion con la Base de Datos
	 */
	public void setConn(Connection connection){
		this.conn = connection;
	}

	/**
	 * Metodo que cierra todos los recursos que se encuentran en el arreglo de recursos<br/>
	 * <b>Postcondicion: </b> Todos los recurso del arreglo de recursos han sido cerrados.
	 */
	public void cerrarRecursos() {
		for(Object ob : recursos){
			if(ob instanceof PreparedStatement)
				try {
					((PreparedStatement) ob).close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
		}
	}

	/**
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla SERVICIOSDEALOJAMIENTO) en una instancia de la clase ServicioDeAlojamiento.
	 * @param resultSet ResultSet con la informacion de un servicioDeAlojamiento que se obtuvo de la base de datos.
	 * @return ServicioDeAlojamiento cuyos atributos corresponden a los valores asociados a un registro particular de la tabla SERVICIOSDEALOJAMIENTO.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public ServicioDeAlojamiento convertResultSetToServicioDeAlojamiento(ResultSet resultSet) throws SQLException {

		Long idServicio = resultSet.getLong("IDSERVICIO");
		Long idOfertaAlojamiento = resultSet.getLong("IDOFERTAALOJAMIENTO");

		ServicioDeAlojamiento serdalo = new ServicioDeAlojamiento(idServicio, idOfertaAlojamiento);

		return serdalo;
	}
}