package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;
public class DAOHabitacion {

	//----------------------------------------------------------------------------------------------------------------------------------
	// CONSTANTES
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Constante para indicar el usuario Oracle del estudiante
	 */
	//TODO Requerimiento 1H: Modifique la constante, reemplazando al ususario PARRANDEROS por su ususario de Oracle
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
	 * Metodo constructor de la clase DAOHabitacion <br/>
	 */
	public DAOHabitacion() {
		recursos = new ArrayList<Object>();
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE COMUNICACION CON LA BASE DE DATOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo que obtiene la informacion de todos los bebedores en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los bebedores que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Habitacion> getHabitaciones() throws SQLException, Exception {
		ArrayList<Habitacion> habitaciones = new ArrayList<Habitacion>();

		String sql = String.format("SELECT * FROM %1$s.HABITACION", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			habitaciones.add(convertResultSetToHabitacion(rs));
		}
		return habitaciones;
	}

	/**
	 * Metodo que obtiene la informacion del habitacion en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del habitacion
	 * @return la informacion del habitacion que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el habitacion conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Habitacion findHabitacionById(Long idHabitacion) throws SQLException, Exception
	{
		Habitacion habitacion = null;

		String sql = String.format("SELECT * FROM %1$s.HABITACION WHERE IDHABITACION = %2$d", USUARIO, idHabitacion); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			habitacion = convertResultSetToHabitacion(rs);
		}

		return habitacion;
	}

	/**
	 * Metodo que agregar la informacion de un nuevo habitacion en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habitacion habitacion que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addHabitacion(Habitacion habitacion) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.HABITACION (IDHABITACION, CATEGORIA, COMPARTIDO, TIPO) VALUES (%2$s, '%3$s', '%4$s', '%5$s')", 
				USUARIO, 
				habitacion.getIdHabitacion(),
				habitacion.getCategoria(),
				habitacion.getCompartido(),
				habitacion.getTipo());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza la informacion del habitacion en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habitacion Habitacion que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateHabitacion(Habitacion habitacion) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.CLIENTE ", USUARIO));
		sql.append (String.format (
				"SET  CATEGORIA = '%1$s', COMPARTIDO = '%2$s', TIPO = '%3$s'",
				habitacion.getCategoria(), habitacion.getCompartido(), habitacion.getTipo()));
		sql.append ("WHERE IDHABITACION = " + habitacion.getIdHabitacion());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza la informacion del habitacion en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param habitacion Habitacion que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteHabitacion(Habitacion habitacion) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.HABITACION WHERE IDHABITACION = %2$d", USUARIO, habitacion.getIdHabitacion());

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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla BEBEDORES) en una instancia de la clase Habitacion.
	 * @param resultSet ResultSet con la informacion de un habitacion que se obtuvo de la base de datos.
	 * @return Habitacion cuyos atributos corresponden a los valores asociados a un registro particular de la tabla BEBEDORES.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Habitacion convertResultSetToHabitacion(ResultSet resultSet) throws SQLException {
		
		Long idHabitacion = resultSet.getLong("IDHABITACION");
		String categoria = resultSet.getString("CATEGORIA");
		String compartido = resultSet.getString("COMPARTIDO");
		String tipo = resultSet.getString("TIPO");
		Habitacion habitacion = new Habitacion(idHabitacion, categoria, compartido, tipo);
		return habitacion;
	}
}
