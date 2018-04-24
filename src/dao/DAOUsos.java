package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import vos.Usos;

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
					"FROM Reserva WHERE FINESTADIA<=SYSDATE)) NATURAL JOIN " + 
					"(SELECT SUM(DiasReservados) as Dias_Total_Reservados, SUM(Precio) as Total FROM ( " + 
					"Select IDCLIENTE, FINESTADIA-INICIOESTADIA as DiasReservados, Precio " + 
					"FROM Reserva))) UNION SELECT 'Operador Tipo: '||TIPO, Total_Reservado, Total_Estadía, Total_Ingresos, Total_Por_Recibir FROM " + 
					"(SELECT A.TIPO, Total_Reservado, Total_Estadía, Total_Ingresos, Total-Total_Ingresos as Total_Por_Recibir " + 
					"FROM " + 
					"(SELECT TIPO, COALESCE(SUM(FINESTADIA-INICIOESTADIA),0) as Total_Estadía, COALESCE(SUM(Precio),0) AS Total_Ingresos " + 
					"FROM ((((OPERADORES LEFT JOIN OFERTA ON OPERADORES.CEDULA_NIT=OFERTA.IDOPERADOR)  " + 
					"LEFT JOIN ALOJAMIENTOSDEOFERTA ON OFERTA.IDOFERTA=ALOJAMIENTOSDEOFERTA.IDOFERTA) " + 
					"LEFT JOIN ALOJAMIENTO ON ALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTOSDEOFERTA.IDALOJAMIENTO) " + 
					"LEFT JOIN RESERVASDEALOJAMIENTO ON RESERVASDEALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTO.IDALOJAMIENTO) " + 
					"LEFT JOIN RESERVA ON RESERVA.IDRESERVA=RESERVASDEALOJAMIENTO.IDRESERVA " + 
					"WHERE FINESTADIA<=SYSDATE OR FINESTADIA IS NULL GROUP BY TIPO) A  " + 
					"INNER JOIN (SELECT TIPO, COALESCE(SUM(FINESTADIA-INICIOESTADIA),0) as Total_Reservado, COALESCE(SUM(Precio),0) AS Total " + 
					"FROM ((((OPERADORES LEFT JOIN OFERTA ON OPERADORES.CEDULA_NIT=OFERTA.IDOPERADOR)  " + 
					"LEFT JOIN ALOJAMIENTOSDEOFERTA ON OFERTA.IDOFERTA=ALOJAMIENTOSDEOFERTA.IDOFERTA) " + 
					"LEFT JOIN ALOJAMIENTO ON ALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTOSDEOFERTA.IDALOJAMIENTO) " + 
					"LEFT JOIN RESERVASDEALOJAMIENTO ON RESERVASDEALOJAMIENTO.IDALOJAMIENTO=ALOJAMIENTO.IDALOJAMIENTO) " + 
					"LEFT JOIN RESERVA ON RESERVA.IDRESERVA=RESERVASDEALOJAMIENTO.IDRESERVA " + 
					"GROUP BY TIPO) B ON A.TIPO=B.TIPO) ORDER BY TIPO_USUARIO", USUARIO);

			PreparedStatement prepStmt = conn.prepareStatement(sql);
			recursos.add(prepStmt);
			ResultSet rs = prepStmt.executeQuery();

			while (rs.next()) {
				usos.add(convertResultSetToUso(rs));
			}
			return usos;
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
}
