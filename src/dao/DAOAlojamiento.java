package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.*;
public class DAOAlojamiento {

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
	 * Metodo constructor de la clase DAOAlojamiento <br/>
	 */
	public DAOAlojamiento() {
		recursos = new ArrayList<Object>();
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE COMUNICACION CON LA BASE DE DATOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo que obtiene la informacion de todos los Alojamientos en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los Alojamientos que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Alojamiento> getAlojamientos() throws SQLException, Exception {
		ArrayList<Alojamiento> Alojamientos = new ArrayList<Alojamiento>();

		String sql = String.format("SELECT * FROM %1$s.ALOJAMIENTO", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Alojamientos.add(convertResultSetToAlojamiento(rs));
		}
		return Alojamientos;
	}

	/**
	 * Metodo que obtiene la informacion del Alojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del Alojamiento
	 * @return la informacion del Alojamiento que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el Alojamiento conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Alojamiento findAlojamientoById(Long idAlojamiento) throws SQLException, Exception 
	{
		Alojamiento Alojamiento = null;

		String sql = String.format("SELECT * FROM %1$s.ALOJAMIENTO WHERE IDALOJAMIENTO = %2$d", USUARIO, idAlojamiento); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			Alojamiento = convertResultSetToAlojamiento(rs);
		}

		return Alojamiento;
	}

	/**
	 * Metodo que agregar la informacion de un nuevo Alojamiento en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param Alojamiento Alojamiento que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addAlojamiento(Alojamiento Alojamiento) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.ALOJAMIENTO (IDALOJAMIENTO, CAPACIDAD, TAMAÑO, UBICACION, HABILITADO, FECHA_APERTURA) VALUES (%2$s, '%3$s', '%4$s', '%5$s', '%6$s', %7$s)",
				USUARIO, 
				Alojamiento.getIdAlojamiento(),
				Alojamiento.getCapacidad(),
				Alojamiento.getTamaño(),
				Alojamiento.getUbicacion(),
				Alojamiento.getHabilitado(),
				Alojamiento.getFecha_apertura());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza la informacion del Alojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param Alojamiento Alojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateAlojamiento(Alojamiento Alojamiento) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.ALOJAMIENTO ", USUARIO));
		sql.append (String.format (
				"SET  CAPACIDAD = '%1$s', TAMAÑO = '%2$s', UBICACION = '%3$s', HABILITADO = '%4$s', FECHAAPERTURA = '%5$s'",
				Alojamiento.getCapacidad(), Alojamiento.getTamaño(), Alojamiento.getUbicacion(), Alojamiento.getHabilitado(), Alojamiento.getFecha_apertura()));
		sql.append ("WHERE IDALOJAMIENTO = " + Alojamiento.getIdAlojamiento());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza la informacion del Alojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param Alojamiento Alojamiento que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteAlojamiento(Alojamiento Alojamiento) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.ALOJAMIENTO WHERE IDALOJAMIENTO = %2$d", USUARIO, Alojamiento.getIdAlojamiento());

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public List<Alojamiento> getAlojamientosFiltrados(String fecha1, String fecha2, List<String> servicios) throws SQLException, Exception {
		List<Alojamiento> AlojamientosF = new ArrayList<Alojamiento>();
		
		// El %1$s es USUARIO, el %2$s es servicios.size(), el %3$s es fecha1 y el %4$s es fecha2
		String presql = "(SELECT CAPACIDAD, IDALOJAMIENTO, UBICACION, TAMAÑO, HABILITADO, FECHA_APERTURA " + 
		        " FROM(SELECT DISTINCT S.NOMBRE, A.CAPACIDAD, A.IDALOJAMIENTO, A.UBICACION, A.TAMAÑO, A.HABILITADO, A.FECHA_APERTURA " +
				" FROM %1$s.SERVICIOSDEALOJAMIENTO SA INNER JOIN %1$s.SERVICIOS S ON SA.IDSERVICIO = S.IDSERVICIO " +
				" INNER JOIN %1$s.ALOJAMIENTO A ON A.IDALOJAMIENTO = SA.IDALOJAMIENTO " + 
				" WHERE S.NOMBRE = ";
		
		for(int i = 0; i < servicios.size(); i++)
		{
			if(i == 0) {
				presql = presql + " " + "'" + servicios.get(0) + "'";
			}
			else {
				presql += " OR S.NOMBRE = " + "'" + servicios.get(i) + "'";
			}
		}
		presql = presql + ") GROUP BY IDALOJAMIENTO, CAPACIDAD, UBICACION, TAMAÑO, HABILITADO, FECHA_APERTURA " +
		        " HAVING COUNT(IDALOJAMIENTO)>= %2$s)"+ "MINUS (SELECT A.CAPACIDAD, A.IDALOJAMIENTO, A.UBICACION, A.TAMAÑO, A.HABILITADO, A.FECHA_APERTURA " + 
				" FROM RESERVA R INNER JOIN RESERVASDEALOJAMIENTO RA ON R.IDRESERVA = RA.IDRESERVA " + 
				" INNER JOIN ALOJAMIENTO A ON A.IDALOJAMIENTO = RA.IDALOJAMIENTO " + 
				" WHERE ((TO_DATE('%3$s', 'DD/MM/YY') <= INICIOESTADIA AND TO_DATE('%3$s', 'DD/MM/YY') <= FINESTADIA) AND (TO_DATE('%4$s', 'DD/MM/YY') >= INICIOESTADIA AND TO_DATE('%4$s', 'DD/MM/YY') >= FINESTADIA)) " + 
				" OR (TO_DATE('%3$s', 'DD/MM/YY') >= INICIOESTADIA AND TO_DATE('%3$s', 'DD/MM/YY') <= FINESTADIA) OR (TO_DATE('%4$s', 'DD/MM/YY') <= FINESTADIA AND TO_DATE('%4$s', 'DD/MM/YY') >= INICIOESTADIA))";
		String sql = String.format(presql, USUARIO, servicios.size(), fecha1, fecha2);
		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			AlojamientosF.add(convertResultSetToAlojamiento(rs));
		}
		return AlojamientosF;
	}
	
	public ArrayList<Ocupacion> getOcupacionAlojamientos() throws SQLException, Exception {
		ArrayList<Ocupacion> Alojamientos = new ArrayList<Ocupacion>();

		String sql = String.format("SELECT IDALOJAMIENTO, ROUND(Total_Reservado/Total,5) as Indice_De_Ocupacion " + 
				"FROM (SELECT IDALOJAMIENTO, COALESCE(SUM(FINESTADIA-INICIOESTADIA),0) as Total_Reservado " + 
				"FROM %1$s.RESERVA NATURAL RIGHT JOIN %1$s.RESERVASDEALOJAMIENTO NATURAL RIGHT JOIN %1$s.ALOJAMIENTO " + 
				"WHERE SYSDATE>=FINESTADIA OR FINESTADIA IS NULL GROUP BY IDALOJAMIENTO) NATURAL JOIN " + 
				"(SELECT IDALOJAMIENTO, SYSDATE - FECHA_APERTURA as Total FROM %1$s.ALOJAMIENTO) ORDER BY IDALOJAMIENTO", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			Alojamientos.add(convertResultSetToOcupacion(rs));
		}
		return Alojamientos;
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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla BEBEDORES) en una instancia de la clase Alojamiento.
	 * @param resultSet ResultSet con la informacion de un Alojamiento que se obtuvo de la base de datos.
	 * @return Alojamiento cuyos atributos corresponden a los valores asociados a un registro particular de la tabla BEBEDORES.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Alojamiento convertResultSetToAlojamiento(ResultSet resultSet) throws SQLException {
		int capacidad = resultSet.getInt("CAPACIDAD");
		Long idAlojamiento = resultSet.getLong("IDALOJAMIENTO");
		double tamaño = resultSet.getDouble("TAMAÑO");
		String ubicacion = resultSet.getString("UBICACION");
		int habilitado = resultSet.getInt("HABILITADO");
		Date fecha_apertura = resultSet.getDate("FECHA_APERTURA");
		Alojamiento ofAlojamiento = new Alojamiento(capacidad, idAlojamiento, tamaño, ubicacion, habilitado, fecha_apertura);
		return ofAlojamiento;
	}
	private Ocupacion convertResultSetToOcupacion(ResultSet rs) throws SQLException{
		Long idAlojamiento = rs.getLong("IDALOJAMIENTO");
		double indice_de_ocupacion = rs.getDouble("INDICE_DE_OCUPACION");
		Ocupacion alojamiento = new Ocupacion(idAlojamiento, indice_de_ocupacion);
		return alojamiento;
	}
}