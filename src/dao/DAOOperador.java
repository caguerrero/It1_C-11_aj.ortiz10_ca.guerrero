package dao;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import vos.*;

public class DAOOperador {

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
	 * Metodo constructor de la clase DAOOperador <br/>
	 */
	public DAOOperador() {
		recursos = new ArrayList<Object>();
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE COMUNICACION CON LA BASE DE DATOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo que obtiene la informacion de todos los operadores en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los operadores que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Operador> getOperadores() throws SQLException, Exception {
		ArrayList<Operador> operadores = new ArrayList<Operador>();

		String sql = String.format("SELECT * FROM %1$s.OPERADORES", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			operadores.add(convertResultSetToOperador(rs));
		}
		return operadores;
	}

	/**
	 * Metodo que obtiene la informacion del operador en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del operador
	 * @return la informacion del operador que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el operador conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Operador findOperadorByCedulaNIT(Long id) throws SQLException, Exception 
	{
		Operador operador = null;

		String sql = String.format("SELECT * FROM %1$s.OPERADORES WHERE CEDULA_NIT = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			operador = convertResultSetToOperador(rs);
		}

		return operador;
	}

	/**
	 * Metodo que agregar la informacion de un nuevo operador en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param operador Operador que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addOperador(Operador operador) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.OPERADORES (CEDULA_NIT, DIRECCION,NOMBRE, TIPO) VALUES (%2$s, '%3$s', '%4$s','%5$s')", 
				USUARIO, 
				operador.getCedula_NIT(), 
				operador.getDireccion(),
				operador.getNombre(),
				operador.getTipo());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();

	}

	/**
	 * Metodo que actualiza la informacion del operador en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param operador Operador que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateOperador(Operador operador) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %s.OPERADORES ", USUARIO));
		sql.append (String.format (
				"SET DIRECCION = '%1$s',NOMBRE = '%2$s',TIPO = '%2$s' ",
				operador.getDireccion(), operador.getNombre()));
		sql.append ("WHERE CEDULA_NIT = " + operador.getCedula_NIT());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza la informacion del operador en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param operador Operador que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteOperador(Operador operador) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.OPERADORES WHERE CEDULA_NIT = %2$d", USUARIO, operador.getCedula_NIT());

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public ArrayList<Pago> darPagos() throws SQLException, Exception
	{
		    ArrayList<Pago> pagos = new ArrayList<Pago>();
			String sql = String.format("SELECT OP.NOMBRE, OP.CEDULA_NIT, NVL(SUM(PRECIO), 0) AS DINERO_RECIBIDO " + 
					"FROM %1$s.RESERVA R RIGHT JOIN %1$s.ALOJAMIENTOSDEOFERTA AO ON R.IDALOJAMIENTO = AO.IDALOJAMIENTO " + 
					"RIGHT JOIN %1$s.OFERTA O ON AO.IDOFERTA = O.IDOFERTA " + 
					"RIGHT JOIN %1$s.OPERADORES OP ON O.IDOPERADOR = OP.CEDULA_NIT WHERE CANCELADO = 1 AND R.FINESTADIA BETWEEN SYSDATE - 365 AND SYSDATE " + 
					"GROUP BY OP.NOMBRE, OP.CEDULA_NIT, R.FINESTADIA ORDER BY R.FINESTADIA", USUARIO);
			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while(rs.next()) {
				pagos.add(convertResultSetToPago(rs));
			}
			return pagos;
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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla OPERADORES) en una instancia de la clase Operador.
	 * @param resultSet ResultSet con la informacion de un operador que se obtuvo de la base de datos.
	 * @return Operador cuyos atributos corresponden a los valores asociados a un registro particular de la tabla OPERADORES.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Operador convertResultSetToOperador(ResultSet resultSet) throws SQLException {

		Long cedula_NIT = resultSet.getLong("CEDULA_NIT");
		String direccion = resultSet.getString("DIRECCION");
		String nombre = resultSet.getString("NOMBRE");
		String tipo = resultSet.getString("TIPO");

		Operador op = new Operador(cedula_NIT, direccion, nombre, tipo);

		return op;
	}
	
	public Pago convertResultSetToPago(ResultSet resultSet) throws SQLException {

		Long cedulaOp = resultSet.getLong("CEDULA_NIT");
		String nombreOp = resultSet.getString("NOMBRE");
		Double monto = resultSet.getDouble("DINERO_RECIBIDO");

		Pago pago = new Pago(monto, nombreOp, cedulaOp);

		return pago;
	}
}