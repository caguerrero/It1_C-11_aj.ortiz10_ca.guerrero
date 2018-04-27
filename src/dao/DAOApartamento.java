package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;
public class DAOApartamento {

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
	 * Metodo constructor de la clase DAOApartamento <br/>
	 */
	public DAOApartamento() {
		recursos = new ArrayList<Object>();
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE COMUNICACION CON LA BASE DE DATOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo que obtiene la informacion de todos los apartamentos en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los apartamentos que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Apartamento> getApartamentos() throws SQLException, Exception {
		ArrayList<Apartamento> apartamentos = new ArrayList<Apartamento>();

		String sql = String.format("SELECT * FROM %1$s.APARTAMENTO", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			apartamentos.add(convertResultSetToApartamento(rs));
		}
		return apartamentos;
	}

	public ArrayList<Apartamento> getApartamentosDisponibles() throws SQLException, Exception {
		ArrayList<Apartamento> apartamentosDisponibles = new ArrayList<Apartamento>();

		String sql = String.format("SELECT IDAPARTAMENTO, MENAJE, NUMHABITACIONES FROM %1$s.APARTAMENTO INNER JOIN " + 
				"((SELECT * FROM %1$s.ALOJAMIENTO) MINUS (SELECT DISTINCT CAPACIDAD, A.IDALOJAMIENTO, TAMAÑO, UBICACION, HABILITADO, FECHA_APERTURA "
				+ "FROM %1$s.ALOJAMIENTO A INNER JOIN %1$s.RESERVASDEALOJAMIENTO RA ON A.IDALOJAMIENTO = RA.IDALOJAMIENTO)) M ON APARTAMENTO.IDAPARTAMENTO = M. IDALOJAMIENTO", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			apartamentosDisponibles.add(convertResultSetToApartamento(rs));
		}
		return apartamentosDisponibles;
	}
	
	/**
	 * Metodo que obtiene la informacion del apartamento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del apartamento
	 * @return la informacion del apartamento que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el apartamento conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Apartamento findApartamentoById(Long id) throws SQLException, Exception 
	{
		Apartamento apartamento = null;

		String sql = String.format("SELECT * FROM %1$s.APARTAMENTO WHERE IDAPARTAMENTO = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			apartamento = convertResultSetToApartamento(rs);
		}

		return apartamento;
	}

	/**
	 * Metodo que agregar la informacion de un nuevo apartamento en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param apartamento apartamento que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addApartamento(Apartamento apartamento) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.APARTAMENTO (IDAPARTAMENTO, MENAJE, NUMHABITACIONES) VALUES (%2$s, '%3$s', '%4$s')", 
				USUARIO, 
				apartamento.getIdApartamento(),
				apartamento.getMenaje(),
				apartamento.getNumHabitaciones());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza la informacion del apartamento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param apartamento Apartamento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateApartamento(Apartamento apartamento) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.APARTAMENTO ", USUARIO));
		sql.append (String.format (
				"SET  MENAJE = '%1$s', NUMHABITACIONES = '%2$s'",
				apartamento.getMenaje(), apartamento.getNumHabitaciones()));
		sql.append ("WHERE IDAPARTAMENTO = " + apartamento.getIdApartamento());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza la informacion del apartamento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param apartamento Apartamento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteApartamento(Apartamento apartamento) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.APARTAMENTO WHERE IDPARTAMENTO = %2$d", USUARIO, apartamento.getIdApartamento());

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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla BEBEDORES) en una instancia de la clase Apartamento.
	 * @param resultSet ResultSet con la informacion de un apartamento que se obtuvo de la base de datos.
	 * @return Apartamento cuyos atributos corresponden a los valores asociados a un registro particular de la tabla BEBEDORES.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Apartamento convertResultSetToApartamento(ResultSet resultSet) throws SQLException {
		Long idapartamento = resultSet.getLong("IDAPARTAMENTO");
		String menaje = resultSet.getString("MENAJE");
		int numHabitaciones = resultSet.getInt("NUMHABITACIONES");
		Apartamento ap = new Apartamento(idapartamento, menaje, numHabitaciones);
		return ap;
	}


}