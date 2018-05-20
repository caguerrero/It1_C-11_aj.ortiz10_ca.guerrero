package dao;


import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.*;
public class DAOCliente {

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
	 * Metodo constructor de la clase DAOCliente <br/>
	 */
	public DAOCliente() {
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
	public ArrayList<Cliente> getClientes() throws SQLException, Exception {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		String sql = String.format("SELECT * FROM %1$s.CLIENTE", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			clientes.add(convertResultSetToCliente(rs));
		}
		return clientes;
	}

	/**
	 * Metodo que obtiene la informacion del cliente en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del cliente
	 * @return la informacion del cliente que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el cliente conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Cliente findClienteById(Long cedula) throws SQLException, Exception
	{
		Cliente cliente = null;

		String sql = String.format("SELECT * FROM %1$s.CLIENTE WHERE CEDULA = %2$d", USUARIO, cedula); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			cliente = convertResultSetToCliente(rs);
		}

		return cliente;
	}

	/**
	 * Metodo que agregar la informacion de un nuevo cliente en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param cliente cliente que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addCliente(Cliente cliente) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.CLIENTE (CEDULA, NOMBRE, ROLUNIANDINO) VALUES (%2$s, '%3$s', '%4$s')", 
				USUARIO, 
				cliente.getCedula(),
				cliente.getNombre(),
				cliente.getRolUniandino());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza la informacion del cliente en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param cliente Cliente que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateCliente(Cliente cliente) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.CLIENTE ", USUARIO));
		sql.append (String.format (
				"SET  NOMBRE = '%1$s', ROLUNIANDINO = '%2$s'",
				cliente.getNombre(), cliente.getRolUniandino()));
		sql.append ("WHERE CEDULA = " + cliente.getCedula());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza la informacion del cliente en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param cliente Cliente que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteCliente(Cliente cliente) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.CLIENTE WHERE CEDULA = %2$d", USUARIO, cliente.getCedula());

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	public ArrayList<Cliente> getClientesFrecuentes(Long idApartamento) throws SQLException, Exception {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		String sql = String.format("SELECT DISTINCT CEDULA, NOMBRE, ROLUNIANDINO FROM (SELECT IDALOJAMIENTO, C.CEDULA, C.NOMBRE, C.ROLUNIANDINO, COUNT(IDRESERVA) as VECES " + 
				"FROM (((%1$s.ALOJAMIENTO NATURAL JOIN %1$s.RESERVASDEALOJAMIENTO) NATURAL JOIN %1$s.RESERVA) INNER JOIN %1$s.CLIENTE C ON RESERVA.IDCLIENTE = C.CEDULA) " + 
				"WHERE IDALOJAMIENTO = %2$s GROUP BY  IDALOJAMIENTO, C.CEDULA, C.NOMBRE, C.ROLUNIANDINO HAVING COUNT(IDRESERVA)>=3 ORDER BY VECES DESC) UNION SELECT DISTINCT CEDULA, NOMBRE, ROLUNIANDINO  " + 
				"FROM (SELECT IDALOJAMIENTO, C.CEDULA, C.NOMBRE, C.ROLUNIANDINO, SUM(FINESTADIA-INICIOESTADIA) AS TOTAL_NOCHES " + 
				"FROM (((%1$s.ALOJAMIENTO NATURAL JOIN %1$s.RESERVASDEALOJAMIENTO) NATURAL JOIN %1$s.RESERVA) INNER JOIN %1$s.CLIENTE C ON RESERVA.IDCLIENTE = C.CEDULA) " + 
				"WHERE IDALOJAMIENTO = %2$s GROUP BY IDALOJAMIENTO, C.CEDULA, C.NOMBRE, C.ROLUNIANDINO HAVING SUM(FINESTADIA-INICIOESTADIA)>=15)", USUARIO, idApartamento);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			clientes.add(convertResultSetToCliente(rs));
		}
		return clientes;
	}

	public ArrayList<Cliente> getClientesConsumoAdmin(Long idAlojamiento, String fechaInicio, String fechaFinal, List<String> filtros) throws SQLException, Exception {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		String presql = "SELECT DISTINCT(CEDULA), NOMBRE, ROLUNIANDINO FROM (%1$s.CLIENTE INNER JOIN %1$s.RESERVA ON CLIENTE.CEDULA=RESERVA.IDCLIENTE) "
				+ "INNER JOIN %1$s.RESERVASDEALOJAMIENTO ON RESERVA.IDRESERVA=RESERVASDEALOJAMIENTO.IDRESERVA " + 
				"WHERE IDALOJAMIENTO = %2$s AND FECHARESERVA BETWEEN TO_DATE('%3$s', 'DD/MM/YYYY') AND TO_DATE('%4$s', 'DD/MM/YYYY') "
				+ "ORDER BY ";

		for(int i = 0; i < filtros.size(); i++)
		{
			if(i == 0) {
				presql = presql + filtros.get(i);
			}
			else
			{
				presql += "," + filtros.get(i);
			}
		}

		System.out.println(presql);

		String sql = String.format(presql, USUARIO, idAlojamiento, fechaInicio, fechaFinal);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			clientes.add(convertResultSetToCliente(rs));
		}
		return clientes;
	}

	public ArrayList<Cliente> getClientesConsumoProveedor(Long idAlojamiento, String fechaInicio, String fechaFinal, List<String> filtros, Long idProveedor) throws SQLException, Exception {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		String sql = String.format("SELECT DISTINCT(CEDULA), NOMBRE, ROLUNIANDINO " + 
				"FROM ((%1$s.CLIENTE INNER JOIN %1$s.RESERVA ON CLIENTE.CEDULA=RESERVA.IDCLIENTE) " + 
				"INNER JOIN %1$s.RESERVASDEALOJAMIENTO ON RESERVA.IDRESERVA=RESERVASDEALOJAMIENTO.IDRESERVA) " + 
				"INNER JOIN %1$s.ALOJAMIENTO ON RESERVASDEALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTO.IDALOJAMIENTO " + 
				"WHERE (ALOJAMIENTO.IDALOJAMIENTO = %2$s) AND (FECHARESERVA BETWEEN TO_DATE('%3$s', 'DD/MM/YYYY') AND TO_DATE('%4$s', 'DD/MM/YYYY')) AND IDOPERADOR = %5$s" + 
				"ORDER BY CEDULA, NOMBRE, ROLUNIANDINO", USUARIO, idAlojamiento, fechaInicio, fechaFinal, idProveedor);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			clientes.add(convertResultSetToCliente(rs));
		}
		return clientes;
	}

	public ArrayList<Cliente> getClientesConsumoNoReserva(Long idAlojamiento, String fechaInicio, String fechaFinal) throws SQLException, Exception {
		ArrayList<Cliente> clientes = new ArrayList<Cliente>();

		String sql = String.format("SELECT DISTINCT(CEDULA), NOMBRE, ROLUNIANDINO " + 
				"FROM (SELECT DISTINCT(CEDULA), NOMBRE, ROLUNIANDINO " + 
				"FROM (%1$s.CLIENTE FULL OUTER JOIN %1$s.RESERVA ON CLIENTE.CEDULA=RESERVA.IDCLIENTE) " + 
				"FULL OUTER JOIN %1$s.RESERVASDEALOJAMIENTO ON RESERVA.IDRESERVA=RESERVASDEALOJAMIENTO.IDRESERVA " + 
				"MINUS SELECT DISTINCT(CEDULA), NOMBRE, ROLUNIANDINO FROM (%1$s.CLIENTE JOIN %1$s.RESERVA " + 
				"ON CLIENTE.CEDULA=RESERVA.IDCLIENTE) " + 
				"JOIN %1$s.RESERVASDEALOJAMIENTO ON RESERVA.IDRESERVA=RESERVASDEALOJAMIENTO.IDRESERVA " + 
				"WHERE IDALOJAMIENTO = %2$s AND FECHARESERVA BETWEEN '%3$s' AND '%4$s' ORDER BY CEDULA, NOMBRE, ROLUNIANDINO", 
				USUARIO, idAlojamiento, fechaInicio, fechaFinal);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			clientes.add(convertResultSetToCliente(rs));
		}
		return clientes;
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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla BEBEDORES) en una instancia de la clase Cliente.
	 * @param resultSet ResultSet con la informacion de un cliente que se obtuvo de la base de datos.
	 * @return Cliente cuyos atributos corresponden a los valores asociados a un registro particular de la tabla BEBEDORES.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Cliente convertResultSetToCliente(ResultSet resultSet) throws SQLException {

		Long cedula = resultSet.getLong("CEDULA");
		String nombre = resultSet.getString("NOMBRE");
		String roluniandino = resultSet.getString("ROLUNIANDINO");

		Cliente cliente = new Cliente(cedula, nombre, roluniandino);

		return cliente;
	}
}
