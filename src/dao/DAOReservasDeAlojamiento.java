package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.ReservasDeAlojamiento;

public class DAOReservasDeAlojamiento {
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
		public DAOReservasDeAlojamiento() {
			recursos = new ArrayList<Object>();
		}

		//----------------------------------------------------------------------------------------------------------------------------------
		// METODOS DE COMUNICACION CON LA BASE DE DATOS
		//----------------------------------------------------------------------------------------------------------------------------------

		/**
		 * Metodo que obtiene la informacion de todos los ReservassDeAlojamiento en la Base de Datos <br/>
		 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>
		 * @return	lista con la informacion de todos los ReservassDeAlojamiento que se encuentran en la Base de Datos
		 * @throws SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
		 * @throws Exception Si se genera un error dentro del metodo.
		 */
		public ArrayList<ReservasDeAlojamiento> getAllReservassDeAlojamiento() throws SQLException, Exception {
			ArrayList<ReservasDeAlojamiento> ReservassDeAlojamiento = new ArrayList<ReservasDeAlojamiento>();

			String sql = String.format("SELECT * FROM %1$s.RESERVASDEALOJAMIENTO", USUARIO);

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				ReservassDeAlojamiento.add(convertResultSetToReservasDeAlojamiento(rs));
			}
			return ReservassDeAlojamiento;
		}

		/**
		 * Metodo que obtiene la informacion del ReservasDeAlojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
		 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/> 
		 * @param id el identificador del ReservasDeAlojamiento
		 * @return la informacion del ReservasDeAlojamiento que cumple con los criterios de la sentecia SQL
		 * 			Null si no existe el ReservasDeAlojamiento conlos criterios establecidos
		 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
		 * @throws Exception Si se genera un error dentro del metodo.
		 */
		public ReservasDeAlojamiento findReservasDeAlojamientoByIds(Long id1, Long id2) throws SQLException, Exception 
		{
			ReservasDeAlojamiento ReservasDeAlojamiento = null;

			String sql = String.format("SELECT * FROM %1$s.RESERVASDEALOJAMIENTO WHERE IDRESERVA = %2$d AND IDALOJAMIENTO = %3$d", USUARIO, id1, id2); 

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			if(rs.next()) {
				ReservasDeAlojamiento = convertResultSetToReservasDeAlojamiento(rs);
			}

			return ReservasDeAlojamiento;
		}

		/**
		 * Metodo que agregar la informacion de un nuevo ReservasDeAlojamiento en la Base de Datos a partir del parametro ingresado<br/>
		 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
		 * @param ReservasDeAlojamiento ReservasDeAlojamiento que desea agregar a la Base de Datos
		 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
		 * @throws Exception Si se genera un error dentro del metodo.
		 */
		public void addReservasDeAlojamiento(ReservasDeAlojamiento reservaDeAlojamiento) throws SQLException, Exception {

			String sql = String.format("INSERT INTO %1$s.RESERVASDEALOJAMIENTO (IDALOJAMIENTO , IDRESERVA) VALUES (%2$s, '%3$s')", 
					USUARIO, 
					reservaDeAlojamiento.getIdAlojamiento(),
					reservaDeAlojamiento.getIdReserva());

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			prepStmt.executeQuery();

		}

		/**
		 * Metodo que actualiza la informacion del ReservasDeAlojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
		 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
		 * @param ReservasDeAlojamiento ReservasDeAlojamiento que desea actualizar a la Base de Datos
		 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
		 * @throws Exception Si se genera un error dentro del metodo.
		 */
		public void updateReservasDeAlojamiento(ReservasDeAlojamiento ReservasDeAlojamiento) throws SQLException, Exception {

			StringBuilder sql = new StringBuilder();
			sql.append (String.format ("UPDATE %s.RESERVASDEALOJAMIENTO ", USUARIO));
			sql.append ("WHERE IDRESERVA = " + ReservasDeAlojamiento.getIdReserva() + " AND IDALOJAMIENTO = " + ReservasDeAlojamiento.getIdAlojamiento());
			System.out.println(sql);

			PreparedStatement prepStmt = conn.prepareStatement(sql.toString());
			recursos.add(prepStmt);
			prepStmt.executeQuery();
		}

		/**
		 * Metodo que actualiza la informacion del ReservasDeAlojamiento en la Base de Datos que tiene el identificador dado por parametro<br/>
		 * <b>Precondicion: </b> la conexion a sido inicializadoa <br/>  
		 * @param ReservasDeAlojamiento ReservasDeAlojamiento que desea actualizar a la Base de Datos
		 * @throws SQLException SQLException Genera excepcion si hay error en la conexion o en la consulta SQL
		 * @throws Exception Si se genera un error dentro del metodo.
		 */
		public void deleteReservasDeAlojamiento(ReservasDeAlojamiento ReservasDeAlojamiento) throws SQLException, Exception {

			String sql = String.format("DELETE FROM %1$s.RESERVASDEALOJAMIENTO WHERE IDReservas = %2$d AND IDALOJAMIENTO = %3$d", USUARIO, ReservasDeAlojamiento.getIdReserva	(), ReservasDeAlojamiento.getIdAlojamiento());

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
		 * Metodo que transforma el resultado obtenido de una consulta SQL (sobre la tabla ReservasSDEALOJAMIENTO) en una instancia de la clase ReservasDeAlojamiento.
		 * @param resultSet ResultSet con la informacion de un ReservasDeAlojamiento que se obtuvo de la base de datos.
		 * @return ReservasDeAlojamiento cuyos atributos corresponden a los valores asociados a un registro particular de la tabla ReservasSDEALOJAMIENTO.
		 * @throws SQLException Si existe algun problema al extraer la informacion del ResultSet.
		 */
		public ReservasDeAlojamiento convertResultSetToReservasDeAlojamiento(ResultSet resultSet) throws SQLException {

			Long idReservas = resultSet.getLong("IDRESERVA");
			Long IdAlojamiento = resultSet.getLong("IDALOJAMIENTO");

			ReservasDeAlojamiento serdalo = new ReservasDeAlojamiento(idReservas, IdAlojamiento);

			return serdalo;
		}
}
