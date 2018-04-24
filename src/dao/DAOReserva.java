package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import vos.*;

public class DAOReserva {

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
	public DAOReserva() {
		recursos = new ArrayList<Object>();
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE COMUNICACION CON LA BASE DE DATOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo que obtiene la informacion de todos los reservas en la Base de Datos <br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
	 * @return	lista con la informacion de todos los reservas que se encuentran en la Base de Datos
	 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public ArrayList<Reserva> getReservas() throws SQLException, Exception {
		ArrayList<Reserva> reservas = new ArrayList<Reserva>();

		String sql = String.format("SELECT * FROM %1$s.RESERVA", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			reservas.add(convertResultSetToReserva(rs));
		}
		return reservas;
	}

	/**
	 * Metodo que obtiene la informacion del reserva en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param id el identificador del reserva
	 * @return la informacion del reserva que cumple con los criterios de la sentecia SQL
	 * 			Null si no existe el reserva conlos criterios establecidos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public Reserva findReservaById(Long id) throws SQLException, Exception 
	{
		Reserva reserva = null;

		String sql = String.format("SELECT * FROM %1$s.RESERVA WHERE IDRESERVA = %2$d", USUARIO, id); 

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		if(rs.next()) {
			reserva = convertResultSetToReserva(rs);
		}

		return reserva;
	}

	/**
	 * Metodo que agregar la informacion de un nuevo reserva en la Base de Datos a partir del parametro ingresado<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param reserva Reserva que desea agregar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void addReserva(Reserva reserva) throws SQLException, Exception {

		String sql = String.format("INSERT INTO %1$s.RESERVA (FECHARESERVA, FINESTADIA, IDRESERVA, INICIOESTADIA, PRECIO, IDCLIENTE, CANCELADO) VALUES (TO_DATE('%2$s', 'YYYY-MM-DD'), TO_DATE('%3$s', 'YYYY-MM-DD'), %4$s, TO_DATE('%5$s', 'YYYY-MM-DD'), %6$s,%7$s,%8$s)",
				USUARIO,
				reserva.getFechaReserva(),
				reserva.getFinEstadia(),
				reserva.getIdReserva(),
				reserva.getInicioEstadia(),
				reserva.getPrecio(),
				reserva.getIdCliente(),
				reserva.getCancelado());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza la informacion del reserva en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
	 * @param reserva Reserva que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void updateReserva(Reserva reserva) throws SQLException, Exception {

		StringBuilder sql = new StringBuilder();
		sql.append (String.format ("UPDATE %1s.RESERVA ", USUARIO));
		sql.append (String.format (
				"SET FECHARESERVA = TO_DATE('%2$s', 'YYYY-MM-DD'), FINESTADIA = TO_DATE('%3$s', 'YYYY-MM-DD'), IDCLIENTE = %4$s, IDOFERTA = %5$s , INICIOESTADIA = TO_DATE('%6$s', 'YYYY-MM-DD'), PRECIO = %7$s",
				reserva.getFechaReserva(), reserva.getFinEstadia(), reserva.getIdCliente(), 
				reserva.getInicioEstadia(), reserva.getPrecio()));
		sql.append ("WHERE IDRESERVA = " + reserva.getIdReserva());
		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}

	/**
	 * Metodo que actualiza la informacion del reserva en la Base de Datos que tiene el identificador dado por parametro<br/>
	 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
	 * @param reserva Reserva que desea actualizar a la Base de Datos
	 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
	 * @throws Exception Si se genera un error dentro del metodo.
	 */
	public void deleteReserva(Reserva reserva) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.RESERVA WHERE IDRESERVA = %2$d", USUARIO, reserva.getIdReserva());

		System.out.println(sql);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		prepStmt.executeQuery();
	}
	
	public void deleteReservaMasiva(Long idReserva) throws SQLException, Exception {

		String sql = String.format("DELETE FROM %1$s.RESERVA WHERE IDRESERVA = %2$d", USUARIO, idReserva);

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
	 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla RESERVA) en una instancia de la clase Reserva.
	 * @param resultSet ResultSet con la informacion de un reserva que se obtuvo de la base de datos.
	 * @return Reserva cuyos atributos corresponden a los valores asociados a un registro particular de la tabla RESERVA.
	 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
	 */
	public Reserva convertResultSetToReserva(ResultSet resultSet) throws SQLException {

		Date fechaReserva = resultSet.getDate("FECHARESERVA");
		Date finEstadia = resultSet.getDate("FINESTADIA");
		Long idCliente = resultSet.getLong("IDCLIENTE");
		Long idReserva = resultSet.getLong("IDRESERVA");
		Date inicioEstadia = resultSet.getDate("INICIOESTADIA");
		double precio = resultSet.getDouble("PRECIO");
		int cancelado = resultSet.getInt("CANCELADO");

		Reserva res = new Reserva(fechaReserva, finEstadia, idReserva, inicioEstadia, precio, idCliente, cancelado);

		return res;
	}
}