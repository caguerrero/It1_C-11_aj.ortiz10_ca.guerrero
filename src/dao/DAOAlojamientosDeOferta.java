package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;
public class DAOAlojamientosDeOferta {

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
	 * Metodo constructor de la clase DAOAlojamientosDeOferta <br/>
	 */
	public DAOAlojamientosDeOferta() {
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
	public ArrayList<AlojamientosDeOferta> getAlojamientosDeOfertas() throws SQLException, Exception {
		ArrayList<AlojamientosDeOferta> alojamientosDeOfertas = new ArrayList<AlojamientosDeOferta>();

		String sql = String.format("SELECT * FROM %1$s.ALOJAMIENTOSDEOFERTA", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			alojamientosDeOfertas.add(convertResultSetToAlojamientosDeOferta(rs));
		}
		return alojamientosDeOfertas;
	}

	/**
	 * Metodo que obtiene la informacion del alojamientosDeOferta en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del alojamientosDeOferta
	 * @return la informacion del alojamientosDeOferta que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el alojamientosDeOferta conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public AlojamientosDeOferta findAlojamientosDeOfertaById(Long id1, Long id2) throws SQLException, Exception
	{
		AlojamientosDeOferta alojamientosDeOferta = null;

		String sql = String.format("SELECT * FROM %1$s.ALOJAMIENTOSDEOFERTA WHERE IDALOJAMIENTO = %2$d AND IDOFERTA = %3$d", USUARIO, id1, id2); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			alojamientosDeOferta = convertResultSetToAlojamientosDeOferta(rs);
		}

		return alojamientosDeOferta;
	}

	/**
	 * Metodo que agregar la informacion de un nuevo alojamientosDeOferta en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param alojamientosDeOferta alojamientosDeOferta que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addAlojamientosDeOferta(AlojamientosDeOferta alojamientosDeOferta) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.ALOJAMIENTOSDEOFERTA (IDALOJAMIENTO, IDOFERTA) VALUES (%2$s, '%3$s')", 
				USUARIO, 
				alojamientosDeOferta.getIdAlojamiento(),
				alojamientosDeOferta.getIdOferta());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza la informacion del alojamientosDeOferta en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param alojamientosDeOferta AlojamientosDeOferta que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteAlojamientosDeOferta(AlojamientosDeOferta alojamientosDeOferta) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.CLIENTE WHERE IDALOJAMIENTO = %2$d AND IDOFERTA = %3$d", USUARIO, alojamientosDeOferta.getIdAlojamiento(), alojamientosDeOferta.getIdOferta());

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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla BEBEDORES) en una instancia de la clase AlojamientosDeOferta.
	 * @param resultSet ResultSet con la informacion de un alojamientosDeOferta que se obtuvo de la base de datos.
	 * @return AlojamientosDeOferta cuyos atributos corresponden a los valores asociados a un registro particular de la tabla BEBEDORES.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public AlojamientosDeOferta convertResultSetToAlojamientosDeOferta(ResultSet resultSet) throws SQLException {
		
		Long idAlojamiento = resultSet.getLong("IDALOJAMIENTO");
		Long idOferta = resultSet.getLong("IDOFERTA");
		
		AlojamientosDeOferta alojamientosDeOferta = new AlojamientosDeOferta(idAlojamiento, idOferta);

		return alojamientosDeOferta;
	}
}