package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Operacion;
import vos.Usos;
import vos.UsosCliente;
import vos.UsosOperador;

public class DAOUsos {
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
	public DAOUsos() {
		recursos = new ArrayList<Object>();
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE COMUNICACION CON LA BASE DE DATOS
	//----------------------------------------------------------------------------------------------------------------------------------

	public ArrayList<Usos> getUsoComunidad() throws SQLException, Exception {
		ArrayList<Usos> usos = new ArrayList<Usos>();

		String sql = String.format("SELECT 'Cliente: ' as TIPO_USUARIO, Dias_Total_Reservados, Dias_De_Estadia_Pasados, Total_Cancelado, Total_Por_Pagar FROM " + 
				"(SELECT Dias_Total_Reservados, Dias_De_Estadia_Pasados, Total_Cancelado, Total-Total_Cancelado as Total_Por_Pagar " + 
				"FROM (SELECT SUM(DiasEstadiaPasados) as Dias_De_Estadia_Pasados, SUM(Precio) as Total_Cancelado " + 
				"FROM ( Select FINESTADIA-INICIOESTADIA as DiasEstadiaPasados, Precio " + 
				"FROM %1$s.Reserva WHERE FINESTADIA<=SYSDATE)) NATURAL JOIN " + 
				"(SELECT SUM(DiasReservados) as Dias_Total_Reservados, SUM(Precio) as Total FROM ( " + 
				"Select IDCLIENTE, FINESTADIA-INICIOESTADIA as DiasReservados, Precio " + 
				"FROM %1$s.Reserva))) UNION SELECT 'Operador Tipo: '||TIPO, Total_Reservado, Total_Estadía, Total_Ingresos, Total_Por_Recibir FROM " + 
				"(SELECT A.TIPO, Total_Reservado, Total_Estadía, Total_Ingresos, Total-Total_Ingresos as Total_Por_Recibir " + 
				"FROM " + 
				"(SELECT TIPO, COALESCE(SUM(FINESTADIA-INICIOESTADIA),0) as Total_Estadía, COALESCE(SUM(Precio),0) AS Total_Ingresos " + 
				"FROM ((((%1$s.OPERADORES LEFT JOIN %1$s.OFERTA ON OPERADORES.CEDULA_NIT=OFERTA.IDOPERADOR)  " + 
				"LEFT JOIN ALOJAMIENTOSDEOFERTA ON OFERTA.IDOFERTA=ALOJAMIENTOSDEOFERTA.IDOFERTA) " + 
				"LEFT JOIN %1$s.ALOJAMIENTO ON %1$s.ALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTOSDEOFERTA.IDALOJAMIENTO) " + 
				"LEFT JOIN %1$s.RESERVASDEALOJAMIENTO ON RESERVASDEALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTO.IDALOJAMIENTO) " + 
				"LEFT JOIN %1$s.RESERVA ON RESERVA.IDRESERVA=RESERVASDEALOJAMIENTO.IDRESERVA " + 
				"WHERE FINESTADIA<=SYSDATE OR FINESTADIA IS NULL GROUP BY TIPO) A  " + 
				"INNER JOIN (SELECT TIPO, COALESCE(SUM(FINESTADIA-INICIOESTADIA),0) as Total_Reservado, COALESCE(SUM(Precio),0) AS Total " + 
				"FROM ((((OPERADORES LEFT JOIN OFERTA ON OPERADORES.CEDULA_NIT=OFERTA.IDOPERADOR)  " + 
				"LEFT JOIN %1$s.ALOJAMIENTOSDEOFERTA ON OFERTA.IDOFERTA=ALOJAMIENTOSDEOFERTA.IDOFERTA) " + 
				"LEFT JOIN %1$s.ALOJAMIENTO ON ALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTOSDEOFERTA.IDALOJAMIENTO) " + 
				"LEFT JOIN %1$s.RESERVASDEALOJAMIENTO ON RESERVASDEALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTO.IDALOJAMIENTO) " + 
				"LEFT JOIN %1$s.RESERVA ON RESERVA.IDRESERVA=RESERVASDEALOJAMIENTO.IDRESERVA " + 
				"GROUP BY TIPO) B ON A.TIPO=B.TIPO) ORDER BY TIPO_USUARIO", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			usos.add(convertResultSetToUso(rs));
		}
		return usos;
	}

	public ArrayList<UsosOperador> getUsoOperador(Long idOperador) throws SQLException, Exception {
		ArrayList<UsosOperador> usosOperador = new ArrayList<UsosOperador>();

		String sql = String.format("SELECT A.CEDULA_NIT, Total_Reservado, Total_Estadia, Total_Ingresos, Total-Total_Ingresos as Total_Por_Recibir " + 
				"FROM (SELECT CEDULA_NIT, COALESCE(SUM(FINESTADIA-INICIOESTADIA),0) as Total_Estadia, COALESCE(SUM(Precio),0) AS Total_Ingresos " + 
				"FROM ((((OPERADORES LEFT JOIN OFERTA ON OPERADORES.CEDULA_NIT=OFERTA.IDOPERADOR)  " + 
				"LEFT JOIN ALOJAMIENTOSDEOFERTA ON OFERTA.IDOFERTA=ALOJAMIENTOSDEOFERTA.IDOFERTA) " + 
				"LEFT JOIN ALOJAMIENTO ON ALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTOSDEOFERTA.IDALOJAMIENTO) " + 
				"LEFT JOIN RESERVASDEALOJAMIENTO ON RESERVASDEALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTO.IDALOJAMIENTO) " + 
				"LEFT JOIN RESERVA ON RESERVA.IDRESERVA=RESERVASDEALOJAMIENTO.IDRESERVA " + 
				"WHERE FINESTADIA<=SYSDATE OR FINESTADIA IS NULL GROUP BY CEDULA_NIT) A INNER JOIN (SELECT CEDULA_NIT, COALESCE(SUM(FINESTADIA-INICIOESTADIA),0) as Total_Reservado, COALESCE(SUM(Precio),0) AS Total " + 
				"FROM ((((OPERADORES LEFT JOIN OFERTA ON OPERADORES.CEDULA_NIT=OFERTA.IDOPERADOR)  " + 
				"LEFT JOIN ALOJAMIENTOSDEOFERTA ON OFERTA.IDOFERTA=ALOJAMIENTOSDEOFERTA.IDOFERTA) " + 
				"LEFT JOIN ALOJAMIENTO ON ALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTOSDEOFERTA.IDALOJAMIENTO) " + 
				"LEFT JOIN RESERVASDEALOJAMIENTO ON RESERVASDEALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTO.IDALOJAMIENTO) " + 
				"LEFT JOIN RESERVA ON RESERVA.IDRESERVA=RESERVASDEALOJAMIENTO.IDRESERVA " + 
				"GROUP BY CEDULA_NIT) B ON A.CEDULA_NIT=B.CEDULA_NIT " + 
				"WHERE A.CEDULA_NIT = %2$s " + 
				"ORDER BY A.CEDULA_NIT", USUARIO, idOperador);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			usosOperador.add(convertResultSetToUsoOperador(rs));
		}
		return usosOperador;
	}

	public ArrayList<UsosCliente> getUsoCliente(Long idCliente) throws SQLException, Exception {
		ArrayList<UsosCliente> usosCliente = new ArrayList<UsosCliente>();

		String sql = String.format("SELECT IDCLIENTE, Dias_Total_Reservados, Dias_De_Estadia_Pasados, Total_Cancelado, Total-Total_Cancelado as Total_Por_Pagar " + 
				"FROM (SELECT IDCLIENTE, SUM(DiasEstadiaPasados) as Dias_De_Estadia_Pasados, SUM(Precio) as Total_Cancelado " + 
				"FROM (Select IDCLIENTE, FINESTADIA-INICIOESTADIA as DiasEstadiaPasados, Precio FROM Reserva " + 
				"WHERE FINESTADIA<=SYSDATE " + 
				"ORDER BY IDCLIENTE) GROUP BY IDCLIENTE) " + 
				"NATURAL JOIN (SELECT IDCLIENTE, SUM(DiasReservados) as Dias_Total_Reservados, SUM(Precio) as Total " + 
				"FROM (Select IDCLIENTE, FINESTADIA-INICIOESTADIA as DiasReservados, Precio FROM Reserva ORDER BY IDCLIENTE) WHERE IDCLIENTE = %2$s " + 
				"GROUP BY IDCLIENTE)", USUARIO, idCliente);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			usosCliente.add(convertResultSetToUsoCliente(rs));
		}
		return usosCliente;
	}

	public ArrayList<Operacion> getOperacionAlohAndes( ) throws SQLException, Exception {
		ArrayList<Operacion> operaciones = new ArrayList<Operacion>();

		String sql = String.format("SELECT FECHAS, CARACTERISTICA FROM " + 
				"(SELECT FECHAS, 'MAYOR DEMANDA' AS CARACTERISTICA " + 
				"FROM " + 
				"(SELECT FECHAS, COUNT(FINESTADIA) AS VECES " + 
				"FROM LISTAFECHAS " + 
				"CROSS JOIN RESERVA " + 
				"WHERE FECHAS BETWEEN TO_DATE('01-01-2017','DD-MM-YYYY') AND TO_DATE('01-01-2019','DD-MM-YYYY') AND  " + 
				"FECHAS>=INICIOESTADIA AND FECHAS<=FINESTADIA   " + 
				"GROUP BY FECHAS " + 
				"HAVING COUNT(FINESTADIA)=(SELECT MAX(VECES) " + 
				"                            FROM " + 
				"                            (SELECT FECHAS, COUNT(FINESTADIA) AS VECES " + 
				"                            FROM LISTAFECHAS " + 
				"                            CROSS JOIN RESERVA " + 
				"                            WHERE FECHAS BETWEEN TO_DATE('01-01-2017','DD-MM-YYYY') AND TO_DATE('01-01-2019','DD-MM-YYYY') AND  " + 
				"                            FECHAS>=INICIOESTADIA AND FECHAS<=FINESTADIA " + 
				"                            GROUP BY FECHAS)))) " + 
				"UNION " + 
				"SELECT FECHAS, CARACTERISTICA FROM " + 
				"(SELECT FECHAS, 'MAYOR INGRESO' AS CARACTERISTICA " + 
				"FROM " + 
				"(SELECT FECHAS, SUM(PRECIO) AS INGRESO " + 
				"FROM LISTAFECHAS " + 
				"CROSS JOIN RESERVA " + 
				"WHERE FECHAS BETWEEN TO_DATE('01-01-2017','DD-MM-YYYY') AND TO_DATE('01-01-2019','DD-MM-YYYY') AND  " + 
				"FECHAS>=INICIOESTADIA AND FECHAS<=FINESTADIA   " + 
				"GROUP BY FECHAS " + 
				"HAVING SUM(PRECIO)=(SELECT MAX(INGRESO) " + 
				"                            FROM " + 
				"                            (SELECT FECHAS, SUM(PRECIO) AS INGRESO " + 
				"                            FROM LISTAFECHAS " + 
				"                            CROSS JOIN RESERVA " + 
				"                            WHERE FECHAS BETWEEN TO_DATE('01-01-2017','DD-MM-YYYY') AND TO_DATE('01-01-2019','DD-MM-YYYY') AND  " + 
				"                            FECHAS>=INICIOESTADIA AND FECHAS<=FINESTADIA " + 
				"                            GROUP BY FECHAS)))) " + 
				"UNION " + 
				"SELECT FECHAS, CARACTERISTICA FROM " + 
				"(SELECT FECHAS, 'MENOR DEMANDA' AS CARACTERISTICA " + 
				"FROM " + 
				"(SELECT FECHAS, COUNT(FINESTADIA) AS VECES " + 
				"FROM LISTAFECHAS " + 
				"CROSS JOIN RESERVA " + 
				"WHERE FECHAS BETWEEN TO_DATE('01-01-2017','DD-MM-YYYY') AND TO_DATE('01-01-2019','DD-MM-YYYY') AND  " + 
				"FECHAS>=INICIOESTADIA AND FECHAS<=FINESTADIA   " + 
				"GROUP BY FECHAS " + 
				"HAVING COUNT(FINESTADIA)=(SELECT MIN(VECES) " + 
				"                            FROM " + 
				"                            (SELECT FECHAS, COUNT(FINESTADIA) AS VECES " + 
				"                            FROM LISTAFECHAS " + 
				"                            CROSS JOIN RESERVA " + 
				"                            WHERE FECHAS BETWEEN TO_DATE('01-01-2017','DD-MM-YYYY') AND TO_DATE('01-01-2019','DD-MM-YYYY') AND  " + 
				"                            FECHAS>=INICIOESTADIA AND FECHAS<=FINESTADIA " + 
				"                            GROUP BY FECHAS)))) ORDER BY CARACTERISTICA", USUARIO);

		PreparedStatement prepStmt = conn.prepareStatement(sql);
		recursos.add(prepStmt);
		ResultSet rs = prepStmt.executeQuery();

		while (rs.next()) {
			operaciones.add(convertResultSetToOperacion(rs));
		}
		return operaciones;
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

	private Usos convertResultSetToUso(ResultSet resultSet) throws SQLException{
		String tipo_usuario = resultSet.getString("TIPO_USUARIO");
		int dias_total_reservados = resultSet.getInt("DIAS_TOTAL_RESERVADOS");
		int dias_de_estadia_pasados = resultSet.getInt("DIAS_DE_ESTADIA_PASADOS");
		double total_cancelado = resultSet.getDouble("TOTAL_CANCELADO");
		double total_por_pagar = resultSet.getDouble("TOTAL_POR_PAGAR");

		Usos uso = new Usos(tipo_usuario, dias_total_reservados, dias_de_estadia_pasados, total_cancelado, total_por_pagar);

		return uso;
	}
	private UsosOperador convertResultSetToUsoOperador(ResultSet resultSet) throws SQLException {
		Long cedula_nit = resultSet.getLong("CEDULA_NIT");
		int total_reservado = resultSet.getInt("TOTAL_RESERVADO");
		int total_estadia = resultSet.getInt("TOTAL_ESTADIA");
		double total_ingresos = resultSet.getDouble("TOTAL_INGRESOS");
		double total_por_recibir = resultSet.getDouble("TOTAL_POR_RECIBIR");

		UsosOperador uso = new UsosOperador(cedula_nit, total_reservado, total_estadia, total_ingresos, total_por_recibir);

		return uso;
	}

	private UsosCliente convertResultSetToUsoCliente(ResultSet resultSet) throws SQLException {
		Long idCliente = resultSet.getLong("IDCLIENTE");
		int dias_total_reservados = resultSet.getInt("DIAS_TOTAL_RESERVADOS");
		int dias_de_estadia_pasados = resultSet.getInt("DIAS_DE_ESTADIA_PASADOS");
		double total_cancelado = resultSet.getDouble("TOTAL_CANCELADO");
		double total_por_pagar = resultSet.getDouble("TOTAL_POR_PAGAR");

		UsosCliente uso = new UsosCliente(idCliente, dias_total_reservados, dias_de_estadia_pasados, total_cancelado, total_por_pagar);

		return uso;
	}

	private Operacion convertResultSetToOperacion(ResultSet resultSet) throws SQLException {
		Date fechas = resultSet.getDate("FECHAS");
		String caracteristicas = resultSet.getString("CARACTERISTICAS");

		Operacion uso = new Operacion(fechas, caracteristicas);

		return uso;
	}
}
