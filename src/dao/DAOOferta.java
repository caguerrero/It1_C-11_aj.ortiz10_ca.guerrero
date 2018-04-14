package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.*;
public class DAOOferta {

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
	 * Metodo constructor de la clase DAOOferta <br/>
	 */
	public DAOOferta() {
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
	public ArrayList<Oferta> getOfertas() throws SQLException, Exception {
		ArrayList<Oferta> ofertas = new ArrayList<Oferta>();

		String sql = String.format("SELECT * FROM %1$s.OFERTA", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			ofertas.add(convertResultSetToOferta(rs));
		}
		return ofertas;
	}

	/**
	 * Metodo que obtiene la informacion del oferta en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del oferta
	 * @return la informacion del oferta que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el oferta conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Oferta findOfertaById(Long id) throws SQLException, Exception
	{
		Oferta oferta = null;

		String sql = String.format("SELECT * FROM %1$s.OFERTA WHERE IDOFERTA = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			oferta = convertResultSetToOferta(rs);
		}

		return oferta;
	}

	/**
	 * Metodo que agregar la informacion de un nuevo oferta en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param oferta oferta que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addOferta(Oferta oferta) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.OFERTA (COSTOBASE, HORARIOAPERTURA, HORARIOCIERRE, IDOFERTA, TIEMPOENDIAS, IDOPERADOR) VALUES (%2$s, '%3$s', '%4$s', '%5$s', '%6$s', '%7$s')", 
				USUARIO, 
				oferta.getCostoBase(),
				oferta.getHorarioApertura(),
				oferta.getHorarioCierre(),
				oferta.getIdOferta(),
				oferta.getTiempoEnDias(),
				oferta.getIdOperador());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza la informacion del oferta en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param oferta Oferta que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateOferta(Oferta oferta) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.OFERTA ", USUARIO));
		sql.append (String.format (
				"SET  COSTOBASE = '%1$s', HORARIOAPERTURA = '%2$s', HORARIOCIERRE = '%3$s', TIEMPOENDIAS = '%4$s', IDOPERADOR = '%5$s'",
				oferta.getCostoBase(), oferta.getHorarioApertura(), oferta.getHorarioCierre(), oferta.getTiempoEnDias(),
				oferta.getIdOperador()));
		sql.append ("WHERE IDOFERTA = " + oferta.getIdOferta());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza la informacion del oferta en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param oferta Oferta que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteOferta(Oferta oferta) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.OFERTA WHERE IDOFERTA = %2$d", USUARIO, oferta.getIdOferta());

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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla BEBEDORES) en una instancia de la clase Oferta.
	 * @param resultSet ResultSet con la informacion de un oferta que se obtuvo de la base de datos.
	 * @return Oferta cuyos atributos corresponden a los valores asociados a un registro particular de la tabla BEBEDORES.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Oferta convertResultSetToOferta(ResultSet resultSet) throws SQLException {
		
		double costoBase = resultSet.getDouble("COSTOBASE");
		int tiempoEndias = resultSet.getInt("TIEMPOENDIAS");
		double horarioApertura = resultSet.getDouble("HORARIOAPERTURA");
		double horarioCierre = resultSet.getDouble("HORARIOCIERRE");
		Long idOferta = resultSet.getLong("IDOFERTA");
		Long idOperador = resultSet.getLong("IDOPERADOR");
		
		Oferta oferta = new Oferta(costoBase, tiempoEndias, horarioApertura, horarioCierre, idOferta, idOperador);

		return oferta;
	}
}