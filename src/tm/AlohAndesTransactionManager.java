package tm;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import dao.DAOApartamento;
import dao.DAOCliente;
import dao.DAOHabitacion;
import dao.DAOOferta;
import dao.DAOAlojamiento;
import dao.DAOAlojamientosDeOferta;
import dao.DAOOperador;
import dao.DAOReserva;
import dao.DAOReservasDeAlojamiento;
import dao.DAOServicio;
import dao.DAOServicioDeAlojamiento;
import vos.Apartamento;
import vos.Cliente;
import vos.Habitacion;
import vos.Ocupacion;
import vos.Oferta;
import vos.Alojamiento;
import vos.AlojamientosDeOferta;
import vos.Operador;
import vos.Pago;
import vos.Popular;
import vos.Reserva;
import vos.ReservasDeAlojamiento;
import vos.Servicio;
import vos.ServicioDeAlojamiento;

public class AlohAndesTransactionManager {

	//----------------------------------------------------------------------------------------------------------------------------------
	// CONSTANTES
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Constante que contiene el path relativo del archivo que tiene los datos de la conexion
	 */
	private static final String CONNECTION_DATA_FILE_NAME_REMOTE = "/conexion.properties";

	/**
	 * Atributo estatico que contiene el path absoluto del archivo que tiene los datos de la conexion
	 */
	private static String CONNECTION_DATA_PATH;
	//----------------------------------------------------------------------------------------------------------------------------------
	// ATRIBUTOS
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Atributo que guarda el usuario que se va a usar para conectarse a la base de datos.
	 */
	private String user;

	/**
	 * Atributo que guarda la clave que se va a usar para conectarse a la base de datos.
	 */
	private String password;

	/**
	 * Atributo que guarda el URL que se va a usar para conectarse a la base de datos.
	 */
	private String url;

	/**
	 * Atributo que guarda el driver que se va a usar para conectarse a la base de datos.
	 */
	private String driver;

	/**
	 * Atributo que representa la conexion a la base de datos
	 */
	private Connection conn;

	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS DE CONEXION E INICIALIZACION
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * <b>Metodo Contructor de la Clase AlohAndesTransactionManager</b> <br/>
	 * <b>Postcondicion: </b>	Se crea un objeto  AlohAndesTransactionManager,
	 * 						 	Se inicializa el path absoluto del archivo de conexion,
	 * 							Se inicializna los atributos para la conexion con la Base de Datos
	 * @param contextPathP Path absoluto que se encuentra en el servidor del contexto del deploy actual
	 * @throws IOException Se genera una excepcion al tener dificultades con la inicializacion de la conexion<br/>
	 * @throws ClassNotFoundException 
	 */
	public AlohAndesTransactionManager(String contextPathP) {

		try {
			CONNECTION_DATA_PATH = contextPathP + CONNECTION_DATA_FILE_NAME_REMOTE;
			initializeConnectionData();
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Metodo encargado de inicializar los atributos utilizados para la conexion con la Base de Datos.<br/>
	 * <b>post: </b> Se inicializan los atributos para la conexion<br/>
	 * @throws IOException Se genera una excepcion al no encontrar el archivo o al tener dificultades durante su lectura<br/>
	 * @throws ClassNotFoundException 
	 */
	private void initializeConnectionData() throws IOException, ClassNotFoundException {

		FileInputStream fileInputStream = new FileInputStream(new File(AlohAndesTransactionManager.CONNECTION_DATA_PATH));
		Properties properties = new Properties();

		properties.load(fileInputStream);
		fileInputStream.close();

		this.url = properties.getProperty("url");
		this.user = properties.getProperty("usuario");
		this.password = properties.getProperty("clave");
		this.driver = properties.getProperty("driver");
		System.out.println("Inicializado");
		//Class.forName(driver);
	}

	/**
	 * Metodo encargado de generar una conexion con la Base de Datos.<br/>
	 * <b>Precondicion: </b>Los atributos para la conexion con la Base de Datos han sido inicializados<br/>
	 * @return Objeto Connection, el cual hace referencia a la conexion a la base de datos
	 * @throws SQLException Cualquier error que se pueda llegar a generar durante la conexion a la base de datos
	 */
	private Connection darConexion() throws SQLException {
		System.out.println("[PARRANDEROS APP] Attempting Connection to: " + url + " - By User: " + user);
		return DriverManager.getConnection(url, user, password);
	}


	//----------------------------------------------------------------------------------------------------------------------------------
	// METODOS TRANSACCIONALES
	//----------------------------------------------------------------------------------------------------------------------------------

	/**
	 * Metodo que modela la transaccion que retorna todos los apartamentos de la base de datos. <br/>
	 * @return List<Apartamento> - Lista de apartamentos que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Apartamento> getAllApartamentos() throws Exception {
		DAOApartamento daoApartamento = new DAOApartamento();
		List<Apartamento> apartamentos;
		try 
		{
			this.conn = darConexion();
			daoApartamento.setConn(conn);

			apartamentos = daoApartamento.getApartamentos();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoApartamento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return apartamentos;
	}

	/**
	 * Metodo que modela la transaccion que busca el apartamento en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del apartamento a buscar. id != null
	 * @return Apartamento - Apartamento que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Apartamento getApartamentoById(Long id) throws Exception {
		DAOApartamento daoApartamento = new DAOApartamento();
		Apartamento apartamento = null;
		try 
		{
			this.conn = darConexion();
			daoApartamento.setConn(conn);
			apartamento = daoApartamento.findApartamentoById(id);
			if(apartamento == null)
			{
				throw new Exception("El apartamento con el id = " + id + " no se encuentra persistido en la base de datos.");
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoApartamento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return apartamento;
	}

	/**
	 * Metodo que modela la transaccion que agrega un apartamento a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el apartamento que entra como parametro <br/>
	 * @param apartamento - el apartamento a agregar. apartamento != null
	 * @throws Exception - Cualquier error que se genere agregando el apartamento
	 */
	public void addApartamento(Apartamento apartamento) throws Exception 
	{
		DAOApartamento daoApartamento = new DAOApartamento( );
		try
		{
			this.conn = darConexion();
			daoApartamento.setConn(conn);
			daoApartamento.addApartamento(apartamento);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoApartamento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al apartamento que entra por parametro.<br/>
	 * Solamente se actualiza si existe el apartamento en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el apartamento que entra como parametro <br/>
	 * @param apartamento - Apartamento a actualizar. apartamento != null
	 * @throws Exception - Cualquier error que se genere actualizando al apartamento.
	 */
	public void updateApartamento(Apartamento apartamento) throws Exception 
	{
		DAOApartamento daoApartamento = new DAOApartamento( );
		try
		{
			this.conn = darConexion();
			daoApartamento.setConn( conn );
			if(daoApartamento.findApartamentoById(apartamento.getIdApartamento()) !=null)
			{daoApartamento.updateApartamento(apartamento);}
			else
			{throw new Exception("El apartamento con el ID " + apartamento.getIdApartamento() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoApartamento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al apartamento que entra por parametro. <br/>
	 * Solamente se actualiza si existe el apartamento en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el apartamento que entra por parametro <br/>
	 * @param Apartamento - apartamento a eliminar. apartamento != null
	 * @throws Exception - Cualquier error que se genere eliminando al apartamento.
	 */
	public void deleteApartamento(Apartamento apartamento) throws Exception 
	{
		DAOApartamento daoApartamento = new DAOApartamento( );
		try
		{
			this.conn = darConexion();
			daoApartamento.setConn( conn );
			if(daoApartamento.findApartamentoById(apartamento.getIdApartamento()) != null)
			{daoApartamento.deleteApartamento(apartamento);}
			else
			{throw new Exception("El apartamento con ID " + apartamento.getIdApartamento() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoApartamento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que retorna todos los clientes de la base de datos. <br/>
	 * @return List<Cliente> - Lista de clientes que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Cliente> getAllClientes() throws Exception {
		DAOCliente daoCliente = new DAOCliente();
		List<Cliente> clientes;
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			clientes = daoCliente.getClientes();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return clientes;
	}

	/**
	 * Metodo que modela la transaccion que busca el cliente en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param id -id del cliente a buscar. id != null
	 * @return Cliente - Cliente que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Cliente getClienteByCedula(Long cedula) throws Exception {
		DAOCliente daoCliente = new DAOCliente();
		Cliente cliente = null;
		try 
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			cliente = daoCliente.findClienteById(cedula);
			if(cliente == null)
			{
				throw new Exception("El cliente con la cedula = " + cedula + " no se encuentra persistido en la base de datos.");
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		}
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return cliente;
	}

	/**
	 * Metodo que modela la transaccion que agrega un cliente a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el cliente que entra como parametro <br/>
	 * @param cliente - el cliente a agregar. apartamento != null
	 * @throws Exception - Cualquier error que se genere agregando el cliente.
	 */
	public void addCliente(Cliente cliente) throws Exception 
	{
		DAOCliente daoCliente = new DAOCliente();
		try
		{
			this.conn = darConexion();
			daoCliente.setConn(conn);
			daoCliente.addCliente(cliente);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al cliente que entra por parametro.<br/>
	 * Solamente se actualiza si existe el cliente en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el cliente que entra como parametro <br/>
	 * @param cliente - Cliente a actualizar. apartamento != null
	 * @throws Exception - Cualquier error que se genere actualizando al cliente.
	 */
	public void updateCliente(Cliente cliente) throws Exception 
	{
		DAOCliente daoCliente = new DAOCliente();
		try
		{
			this.conn = darConexion();
			daoCliente.setConn( conn );
			if(daoCliente.findClienteById(cliente.getCedula()) != null)
			{daoCliente.updateCliente(cliente);}
			else
			{throw new Exception("El cliente con cedula " + cliente.getCedula() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al cliente que entra por parametro. <br/>
	 * Solamente se actualiza si existe el cliente en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el cliente que entra por parametro <br/>
	 * @param Cliente - cliente a eliminar. cliente != null
	 * @throws Exception - Cualquier error que se genere eliminando al apartamento.
	 */
	public void deleteCliente(Cliente cliente) throws Exception 
	{
		DAOCliente daoCliente = new DAOCliente( );
		try
		{
			this.conn = darConexion();
			daoCliente.setConn( conn );
			if(daoCliente.findClienteById(cliente.getCedula()) != null)
			{daoCliente.deleteCliente(cliente);}
			else
			{throw new Exception("El cliente con cedula " + cliente.getCedula() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoCliente.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que retorna todas las habitaciones de la base de datos. <br/>
	 * @return List<Habitacion> - Lista de habitaciones que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Habitacion> getAllHabitaciones() throws Exception {
		DAOHabitacion daoHabitacion = new DAOHabitacion();
		List<Habitacion> habitaciones;
		try 
		{
			this.conn = darConexion();
			daoHabitacion.setConn(conn);
			habitaciones = daoHabitacion.getHabitaciones();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoHabitacion.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return habitaciones;
	}

	/**
	 * Metodo que modela la transaccion que busca el habitacion en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param id -id de la habitacion a buscar. id != null
	 * @return Habitacion - Habitacion que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Habitacion getHabitacionById(Long id) throws Exception {
		DAOHabitacion daoHabitacion = new DAOHabitacion();
		Habitacion habitacion = null;
		try 
		{
			this.conn = darConexion();
			daoHabitacion.setConn(conn);
			habitacion = daoHabitacion.findHabitacionById(id);
			if(habitacion == null)
			{
				throw new Exception("La habitacion con el id = " + id + " no se encuentra persistida en la base de datos.");
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		}
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoHabitacion.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return habitacion;
	}

	/**
	 * Metodo que modela la transaccion que agrega un habitacion a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el habitacion que entra como parametro <br/>
	 * @param habitacion - el habitacion a agregar. apartamento != null
	 * @throws Exception - Cualquier error que se genere agregando el cliente.
	 */
	public void addHabitacion(Habitacion habitacion) throws Exception 
	{
		DAOHabitacion daoHabitacion = new DAOHabitacion();
		try
		{
			this.conn = darConexion();
			daoHabitacion.setConn(conn);
			daoHabitacion.addHabitacion(habitacion);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoHabitacion.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos a la habitacion que entra por parametro.<br/>
	 * Solamente se actualiza si existe la habitacion en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado la habitacion que entra como parametro <br/>
	 * @param habitacion - Habitacion a actualizar. habitacion != null
	 * @throws Exception - Cualquier error que se genere actualizando a la habitacion.
	 */
	public void updateHabitacion(Habitacion habitacion) throws Exception 
	{
		DAOHabitacion daoHabitacion = new DAOHabitacion();
		try
		{
			this.conn = darConexion();
			daoHabitacion.setConn( conn );
			if(daoHabitacion.findHabitacionById(habitacion.getIdHabitacion()) !=null)
			{daoHabitacion.updateHabitacion(habitacion);}
			else
			{throw new Exception("La habitacion con ID " + habitacion.getIdHabitacion() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoHabitacion.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos a la habitacion que entra por parametro. <br/>
	 * Solamente se actualiza si existe el cliente en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el cliente que entra por parametro <br/>
	 * @param habitacion - habitacion a eliminar. cliente != null
	 * @throws Exception - Cualquier error que se genere eliminando a la habitacion.
	 */
	public void deleteHabitacion(Habitacion habitacion) throws Exception 
	{
		DAOHabitacion daoHabitacion = new DAOHabitacion();
		try
		{
			this.conn = darConexion();
			daoHabitacion.setConn( conn );
			if(daoHabitacion.findHabitacionById(habitacion.getIdHabitacion()) != null)
			{daoHabitacion.deleteHabitacion(habitacion);}
			else
			{throw new Exception("La habitacion con ID " + habitacion.getIdHabitacion() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoHabitacion.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que retorna todas las ofertas de alojamiento de la base de datos. <br/>
	 * @return List<OfertaAlojamiento> - Lista de ofertas de alojamiento que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Alojamiento> getAllAlojamiento() throws Exception {
		DAOAlojamiento daoOferta = new DAOAlojamiento();
		List<Alojamiento> ofertas;
		try 
		{
			this.conn = darConexion();
			daoOferta.setConn(conn);
			ofertas = daoOferta.getAlojamientos();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ofertas;
	}

	/**
	 * Metodo que modela la transaccion que busca la ofertaAlojamiento en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param id -id de la ofertaAlojamiento a buscar. id != null
	 * @return ofertaAlojamiento - OfertaAlojamiento que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Alojamiento getAlojamientoById(Long id) throws Exception {
		DAOAlojamiento daoOferta = new DAOAlojamiento();
		Alojamiento oferta = null;
		try 
		{
			this.conn = darConexion();
			daoOferta.setConn(conn);
			oferta = daoOferta.findAlojamientoById(id);
			if(oferta == null)
			{
				throw new Exception("La oferta de alojamiento con el ID = " + id + " no se encuentra persistida en la base de datos.");
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		}
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return oferta;
	}

	/**
	 * Metodo que modela la transaccion que agrega una oferta de alojamiento a la base de datos. <br/>
	 * <b> post: </b> se ha agregado la oferta de alojamiento que entra como parametro <br/>
	 * @param Alojamiento - la ofertaAlojamiento a agregar. ofertaAlojamiento != null
	 * @throws Exception - Cualquier error que se genere agregando la ofertaAlojamiento.
	 */
	public void addAlojamiento(Alojamiento Alojamiento) throws Exception 
	{
		DAOAlojamiento daoOferta = new DAOAlojamiento();
		try
		{
			this.conn = darConexion();
			daoOferta.setConn(conn);
			daoOferta.addAlojamiento(Alojamiento);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos a la oferta de alojamiento que entra por parametro.<br/>
	 * Solamente se actualiza si existe la oferta de alojamiento en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado la oferta de alojamiento que entra como parametro <br/>
	 * @param Alojamiento - OfertaAlojamiento a actualizar. ofertaAlojamiento != null
	 * @throws Exception - Cualquier error que se genere actualizando a la oferta de alojamiento.
	 */
	public void updateAlojamiento(Alojamiento Alojamiento) throws Exception 
	{
		DAOAlojamiento daoOferta = new DAOAlojamiento();
		try
		{
			this.conn = darConexion();
			daoOferta.setConn( conn );
			if(daoOferta.findAlojamientoById(Alojamiento.getIdAlojamiento()) !=null)
			{daoOferta.updateAlojamiento(Alojamiento);}
			else
			{throw new Exception("La oferta de alojamiento con ID " + Alojamiento.getIdAlojamiento() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos a la oferta de alojamiento que entra por parametro. <br/>
	 * Solamente se actualiza si existe la oferta de alojamiento en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado la oferta de alojamiento que entra por parametro <br/>
	 * @param Alojamiento - OfertaAlojamiento a eliminar. ofertaAlojamiento != null
	 * @throws Exception - Cualquier error que se genere eliminando a la oferta de alojamiento.
	 */
	public void deleteAlojamiento(Alojamiento Alojamiento) throws Exception 
	{
		DAOAlojamiento daoOferta = new DAOAlojamiento();
		try
		{
			this.conn = darConexion();
			daoOferta.setConn( conn );
			if(daoOferta.findAlojamientoById(Alojamiento.getIdAlojamiento()) != null)
			{daoOferta.deleteAlojamiento(Alojamiento);}
			else
			{throw new Exception("La oferta de alojamiento con ID " + Alojamiento.getIdAlojamiento() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}
	
	public List<Alojamiento> getAlojamientosFiltrados(String fecha1, String fecha2, List<String> servicios) throws Exception {
		DAOAlojamiento daoAlojamiento = new DAOAlojamiento();
		List<Alojamiento> alojamientos;
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			if(servicios.isEmpty())
			{
				throw new Exception("Debe proporcionar al menos un servicio que desee que tengan los alojamientos.");
			}
			if(fecha1 == null || fecha2 == null)
			{
				throw new Exception("Debe proporcionar las fechas entre las que quiere que esten disponibles los alojamientos.");
			}
			alojamientos = daoAlojamiento.getAlojamientosFiltrados(fecha1, fecha1, servicios);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return alojamientos;
	}
	
	public List<Ocupacion> getOcupacionAlojamientos() throws Exception {
		DAOAlojamiento daoAlojamiento = new DAOAlojamiento();
		List<Ocupacion> ocupacionAlojamientos;
		try 
		{
			this.conn = darConexion();
			daoAlojamiento.setConn(conn);
			ocupacionAlojamientos = daoAlojamiento.getOcupacionAlojamientos();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ocupacionAlojamientos;
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	// TRANSACCIONES DE LA TABLA OPERADORES
	//----------------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Metodo que modela la transaccion que retorna todos los operadores de la base de datos. <br/>
	 * @return List<Operador> - Lista de operadores que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Operador> getAllOperadores() throws Exception {
		DAOOperador daoOperador = new DAOOperador();
		List<Operador> operadores;
		try 
		{
			this.conn = darConexion();
			daoOperador.setConn(conn);
			operadores = daoOperador.getOperadores();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOperador.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return operadores;
	}

	/**
	 * Metodo que modela la transaccion que busca el operador en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del operador a buscar. id != null
	 * @return Operador - Operador que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Operador getOperadorById(Long id) throws Exception {
		DAOOperador daoOperador = new DAOOperador();
		Operador operador = null;
		try 
		{
			this.conn = darConexion();
			daoOperador.setConn(conn);
			operador = daoOperador.findOperadorByCedulaNIT(id);
			if(operador == null)
			{
				throw new Exception("El operador con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOperador.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return operador;
	}

	/**
	 * Metodo que modela la transaccion que agrega un operador a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el operador que entra como parametro <br/>
	 * @param operador - el operador a agregar. operador != null
	 * @throws Exception - Cualquier error que se genere agregando el operador
	 */
	public void addOperador(Operador operador) throws Exception 
	{

		DAOOperador daoOperador = new DAOOperador( );
		try
		{
			this.conn = darConexion();
			daoOperador.setConn(conn);
			daoOperador.addOperador(operador);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOperador.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}



	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al operador que entra por parametro.<br/>
	 * Solamente se actualiza si existe el operador en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el operador que entra como parametro <br/>
	 * @param operador - Operador a actualizar. operador != null
	 * @throws Exception - Cualquier error que se genere actualizando al operador.
	 */
	public void updateOperador(Operador operador) throws Exception 
	{
		DAOOperador daoOperador = new DAOOperador( );
		try
		{
			this.conn = darConexion();
			daoOperador.setConn( conn );
			if(daoOperador.findOperadorByCedulaNIT(operador.getCedula_NIT()) !=null)
			{daoOperador.updateOperador(operador);}
			else
			{throw new Exception("El operador con el ID " + operador.getCedula_NIT() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOperador.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al operador que entra por parametro. <br/>
	 * Solamente se actualiza si existe el operador en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el operador que entra por parametro <br/>
	 * @param Operador - operador a eliminar. operador != null
	 * @throws Exception - Cualquier error que se genere eliminando al operador.
	 */
	public void deleteOperador(Operador operador) throws Exception 
	{
		DAOOperador daoOperador = new DAOOperador( );
		try
		{
			this.conn = darConexion();
			daoOperador.setConn( conn );
			if(daoOperador.findOperadorByCedulaNIT(operador.getCedula_NIT()) != null)
			{daoOperador.deleteOperador(operador);}
			else
			{throw new Exception("El operador con ID " + operador.getCedula_NIT() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOperador.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// TRANSACCIONES DE LA TABLA RESERVAS
	//----------------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Metodo que modela la transaccion que retorna todos los reservas de la base de datos. <br/>
	 * @return List<Reserva> - Lista de reservas que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Reserva> getAllReservas() throws Exception {
		DAOReserva daoReserva = new DAOReserva();
		List<Reserva> reservas;
		try 
		{
			this.conn = darConexion();
			daoReserva.setConn(conn);
			reservas = daoReserva.getReservas();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return reservas;
	}

	/**
	 * Metodo que modela la transaccion que busca el reserva en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del reserva a buscar. id != null
	 * @return Reserva - Reserva que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Reserva getReservaById(Long id) throws Exception {
		DAOReserva daoReserva = new DAOReserva();
		Reserva reserva = null;
		try 
		{
			this.conn = darConexion();
			daoReserva.setConn(conn);
			reserva = daoReserva.findReservaById(id);
			if(reserva == null)
			{
				throw new Exception("El reserva con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return reserva;
	}

	/**
	 * Metodo que modela la transaccion que agrega un reserva a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el reserva que entra como parametro <br/>
	 * @param reserva - el reserva a agregar. reserva != null
	 * @throws Exception - Cualquier error que se genere agregando el reserva
	 */
	public void addReserva(Reserva reserva) throws Exception 
	{

		DAOReserva daoReserva = new DAOReserva( );
		try
		{
			this.conn = darConexion();
			daoReserva.setConn(conn);
			daoReserva.addReserva(reserva);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}



	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al reserva que entra por parametro.<br/>
	 * Solamente se actualiza si existe el reserva en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el reserva que entra como parametro <br/>
	 * @param reserva - Reserva a actualizar. reserva != null
	 * @throws Exception - Cualquier error que se genere actualizando al reserva.
	 */
	public void updateReserva(Reserva reserva) throws Exception 
	{
		DAOReserva daoReserva = new DAOReserva( );
		try
		{
			this.conn = darConexion();
			daoReserva.setConn( conn );
			if(daoReserva.findReservaById(reserva.getIdReserva()) !=null)
			{daoReserva.updateReserva(reserva);}
			else
			{throw new Exception("La reserva con el ID " + reserva.getIdReserva() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al reserva que entra por parametro. <br/>
	 * Solamente se actualiza si existe el reserva en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el reserva que entra por parametro <br/>
	 * @param Reserva - reserva a eliminar. reserva != null
	 * @throws Exception - Cualquier error que se genere eliminando al reserva.
	 */
	public void deleteReserva(Reserva reserva) throws Exception 
	{
		DAOReserva daoReserva = new DAOReserva( );
		try
		{
			this.conn = darConexion();
			daoReserva.setConn( conn );
			if(daoReserva.findReservaById(reserva.getIdReserva()) != null)
			{daoReserva.deleteReserva(reserva);}
			else
			{throw new Exception("La reserva con ID " + reserva.getIdReserva() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReserva.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// TRANSACCIONES DE LA TABLA SERVICIOS
	//----------------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Metodo que modela la transaccion que retorna todos los servicios de la base de datos. <br/>
	 * @return List<Servicio> - Lista de servicios que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Servicio> getAllServicios() throws Exception {
		DAOServicio daoServicio = new DAOServicio();
		List<Servicio> servicios;
		try 
		{
			this.conn = darConexion();
			daoServicio.setConn(conn);
			servicios = daoServicio.getServicios();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicio.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return servicios;
	}

	/**
	 * Metodo que modela la transaccion que busca el servicio en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del servicio a buscar. id != null
	 * @return Servicio - Servicio que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Servicio getServicioById(Long id) throws Exception {
		DAOServicio daoServicio = new DAOServicio();
		Servicio servicio = null;
		try 
		{
			this.conn = darConexion();
			daoServicio.setConn(conn);
			servicio = daoServicio.findServicioById(id);
			if(servicio == null)
			{
				throw new Exception("El servicio con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicio.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return servicio;
	}

	/**
	 * Metodo que modela la transaccion que agrega un servicio a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el servicio que entra como parametro <br/>
	 * @param servicio - el servicio a agregar. servicio != null
	 * @throws Exception - Cualquier error que se genere agregando el servicio
	 */
	public void addServicio(Servicio servicio) throws Exception 
	{

		DAOServicio daoServicio = new DAOServicio( );
		try
		{
			this.conn = darConexion();
			daoServicio.setConn(conn);
			daoServicio.addServicio(servicio);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicio.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}



	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al servicio que entra por parametro.<br/>
	 * Solamente se actualiza si existe el servicio en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el servicio que entra como parametro <br/>
	 * @param servicio - Servicio a actualizar. servicio != null
	 * @throws Exception - Cualquier error que se genere actualizando al servicio.
	 */
	public void updateServicio(Servicio servicio) throws Exception 
	{
		DAOServicio daoServicio = new DAOServicio( );
		try
		{
			this.conn = darConexion();
			daoServicio.setConn( conn );
			if(daoServicio.findServicioById(servicio.getIdServicio()) !=null)
			{daoServicio.updateServicio(servicio);}
			else
			{throw new Exception("La servicio con el ID " + servicio.getIdServicio() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicio.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al servicio que entra por parametro. <br/>
	 * Solamente se actualiza si existe el servicio en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el servicio que entra por parametro <br/>
	 * @param Servicio - servicio a eliminar. servicio != null
	 * @throws Exception - Cualquier error que se genere eliminando al servicio.
	 */
	public void deleteServicio(Servicio servicio) throws Exception 
	{
		DAOServicio daoServicio = new DAOServicio( );
		try
		{
			this.conn = darConexion();
			daoServicio.setConn( conn );
			if(daoServicio.findServicioById(servicio.getIdServicio()) != null)
			{daoServicio.deleteServicio(servicio);}
			else
			{throw new Exception("La servicio con ID " + servicio.getIdServicio() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicio.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// TRANSACCIONES DE LA TABLA SERVICIOSDEALOJAMIENTO
	//----------------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Metodo que modela la transaccion que retorna todos los serviciosDeAlojamiento de la base de datos. <br/>
	 * @return List<ServicioDeAlojamiento> - Lista de serviciosDeAlojamiento que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<ServicioDeAlojamiento> getAllServiciosDeAlojamiento() throws Exception {
		DAOServicioDeAlojamiento daoServicioDeAlojamiento = new DAOServicioDeAlojamiento();
		List<ServicioDeAlojamiento> serviciosDeAlojamiento;
		try 
		{
			this.conn = darConexion();
			daoServicioDeAlojamiento.setConn(conn);
			serviciosDeAlojamiento = daoServicioDeAlojamiento.getAllServiciosDeAlojamiento();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicioDeAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return serviciosDeAlojamiento;
	}

	/**
	 * Metodo que modela la transaccion que busca el servicioDeAlojamiento en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del servicioDeAlojamiento a buscar. id != null
	 * @return ServicioDeAlojamiento - ServicioDeAlojamiento que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public ServicioDeAlojamiento getServicioDeAlojamientoByIds(Long id1,Long id2) throws Exception {
		DAOServicioDeAlojamiento daoServicioDeAlojamiento = new DAOServicioDeAlojamiento();
		ServicioDeAlojamiento servicioDeAlojamiento = null;
		try 
		{
			this.conn = darConexion();
			daoServicioDeAlojamiento.setConn(conn);
			servicioDeAlojamiento = daoServicioDeAlojamiento.findServicioDeAlojamientoByIds(id1, id2);
			if(servicioDeAlojamiento == null)
			{
				throw new Exception("El servicioDeAlojamiento con el idServicio = " + id1+ "y con el idOfertaAlojamiento = "+ id2 + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicioDeAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return servicioDeAlojamiento;
	}

	/**
	 * Metodo que modela la transaccion que agrega un servicioDeAlojamiento a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el servicioDeAlojamiento que entra como parametro <br/>
	 * @param servicioDeAlojamiento - el servicioDeAlojamiento a agregar. servicioDeAlojamiento != null
	 * @throws Exception - Cualquier error que se genere agregando el servicioDeAlojamiento
	 */
	public void addServicioDeAlojamiento(ServicioDeAlojamiento servicioDeAlojamiento) throws Exception 
	{

		DAOServicioDeAlojamiento daoServicioDeAlojamiento = new DAOServicioDeAlojamiento( );
		try
		{
			this.conn = darConexion();
			daoServicioDeAlojamiento.setConn(conn);
			daoServicioDeAlojamiento.addServicioDeAlojamiento(servicioDeAlojamiento);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicioDeAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}



	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al servicioDeAlojamiento que entra por parametro.<br/>
	 * Solamente se actualiza si existe el servicioDeAlojamiento en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el servicioDeAlojamiento que entra como parametro <br/>
	 * @param servicioDeAlojamiento - ServicioDeAlojamiento a actualizar. servicioDeAlojamiento != null
	 * @throws Exception - Cualquier error que se genere actualizando al servicioDeAlojamiento.
	 */
	public void updateServicioDeAlojamiento(ServicioDeAlojamiento servicioDeAlojamiento) throws Exception 
	{
		DAOServicioDeAlojamiento daoServicioDeAlojamiento = new DAOServicioDeAlojamiento( );
		try
		{
			this.conn = darConexion();
			daoServicioDeAlojamiento.setConn( conn );
			if(daoServicioDeAlojamiento.findServicioDeAlojamientoByIds(servicioDeAlojamiento.getIdServicio(),servicioDeAlojamiento.getIdAlojamiento()) !=null)
			{daoServicioDeAlojamiento.updateServicioDeAlojamiento(servicioDeAlojamiento);}
			else
			{throw new Exception("El servicioDeAlojamiento con el IDSERVICIO " + servicioDeAlojamiento.getIdServicio() + " y el IDALOJAMIENTO " + servicioDeAlojamiento.getIdAlojamiento() +  " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicioDeAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al servicioDeAlojamiento que entra por parametro. <br/>
	 * Solamente se actualiza si existe el servicioDeAlojamiento en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el servicioDeAlojamiento que entra por parametro <br/>
	 * @param ServicioDeAlojamiento - servicioDeAlojamiento a eliminar. servicioDeAlojamiento != null
	 * @throws Exception - Cualquier error que se genere eliminando al servicioDeAlojamiento.
	 */
	public void deleteServicioDeAlojamiento(ServicioDeAlojamiento servicioDeAlojamiento) throws Exception 
	{
		DAOServicioDeAlojamiento daoServicioDeAlojamiento = new DAOServicioDeAlojamiento( );
		try
		{
			this.conn = darConexion();
			daoServicioDeAlojamiento.setConn( conn );
			if(daoServicioDeAlojamiento.findServicioDeAlojamientoByIds(servicioDeAlojamiento.getIdServicio(),servicioDeAlojamiento.getIdAlojamiento()) != null )
			{daoServicioDeAlojamiento.deleteServicioDeAlojamiento(servicioDeAlojamiento);}
			else
			{throw new Exception("La servicioDeAlojamiento con IDSERVICIO " + servicioDeAlojamiento.getIdServicio() + " y con IDALOJAMIENTO = " + servicioDeAlojamiento.getIdAlojamiento() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoServicioDeAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// TRANSACCIONES DE LA TABLA AlojamientosDeOferta
	//----------------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Metodo que modela la transaccion que retorna todos los alojamientosDeOferta de la base de datos. <br/>
	 * @return List<AlojamientoDeOferta> - Lista de alojamientosDeOferta que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<AlojamientosDeOferta> getAllAlojamientosDeOferta() throws Exception {
		DAOAlojamientosDeOferta daoAlojamientoDeOferta = new DAOAlojamientosDeOferta();
		List<AlojamientosDeOferta> alojamientosDeOferta;
		try 
		{
			this.conn = darConexion();
			daoAlojamientoDeOferta.setConn(conn);
			alojamientosDeOferta = daoAlojamientoDeOferta.getAlojamientosDeOfertas();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoAlojamientoDeOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return alojamientosDeOferta;
	}

	/**
	 * Metodo que modela la transaccion que busca el alojamientoDeOferta en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del alojamientoDeOferta a buscar. id != null
	 * @return AlojamientoDeOferta - AlojamientoDeOferta que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public AlojamientosDeOferta getAlojamientoDeOfertaByIds(Long id1,Long id2) throws Exception {
		DAOAlojamientosDeOferta daoAlojamientoDeOferta = new DAOAlojamientosDeOferta();
		AlojamientosDeOferta alojamientoDeOferta = null;
		try 
		{
			this.conn = darConexion();
			daoAlojamientoDeOferta.setConn(conn);
			alojamientoDeOferta = daoAlojamientoDeOferta.findAlojamientosDeOfertaById(id1, id2);
			if(alojamientoDeOferta == null)
			{
				throw new Exception("El alojamientoDeOferta con el idServicio = " + id1+ "y con el idOfertaAlojamiento = "+ id2 + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoAlojamientoDeOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return alojamientoDeOferta;
	}

	/**
	 * Metodo que modela la transaccion que agrega un alojamientoDeOferta a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el alojamientoDeOferta que entra como parametro <br/>
	 * @param alojamientoDeOferta - el alojamientoDeOferta a agregar. alojamientoDeOferta != null
	 * @throws Exception - Cualquier error que se genere agregando el alojamientoDeOferta
	 */
	public void addAlojamientoDeOferta(AlojamientosDeOferta alojamientoDeOferta) throws Exception 
	{

		DAOAlojamientosDeOferta daoAlojamientoDeOferta = new DAOAlojamientosDeOferta( );
		try
		{
			this.conn = darConexion();
			daoAlojamientoDeOferta.setConn(conn);
			daoAlojamientoDeOferta.addAlojamientosDeOferta(alojamientoDeOferta);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoAlojamientoDeOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}

	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al alojamientoDeOferta que entra por parametro. <br/>
	 * Solamente se actualiza si existe el alojamientoDeOferta en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el alojamientoDeOferta que entra por parametro <br/>
	 * @param AlojamientoDeOferta - alojamientoDeOferta a eliminar. alojamientoDeOferta != null
	 * @throws Exception - Cualquier error que se genere eliminando al alojamientoDeOferta.
	 */
	public void deleteAlojamientoDeOferta(AlojamientosDeOferta alojamientoDeOferta) throws Exception 
	{
		DAOAlojamientosDeOferta daoAlojamientoDeOferta = new DAOAlojamientosDeOferta( );
		try
		{
			this.conn = darConexion();
			daoAlojamientoDeOferta.setConn( conn );
			if(daoAlojamientoDeOferta.findAlojamientosDeOfertaById(alojamientoDeOferta.getIdOferta(),alojamientoDeOferta.getIdAlojamiento()) != null )
			{daoAlojamientoDeOferta.deleteAlojamientosDeOferta(alojamientoDeOferta);}
			else
			{throw new Exception("La alojamientoDeOferta con IDSERVICIO " + alojamientoDeOferta.getIdOferta() + " y con IDALOJAMIENTO = " + alojamientoDeOferta.getIdAlojamiento() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoAlojamientoDeOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	//----------------------------------------------------------------------------------------------------------------------------------
	// TRANSACCIONES DE LA TABLA OFERTAS
	//----------------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Metodo que modela la transaccion que retorna todos los ofertas de la base de datos. <br/>
	 * @return List<Oferta> - Lista de ofertas que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<Oferta> getAllOfertas() throws Exception {
		DAOOferta daoOferta = new DAOOferta();
		List<Oferta> ofertas;
		try 
		{
			this.conn = darConexion();
			daoOferta.setConn(conn);
			ofertas = daoOferta.getOfertas();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return ofertas;
	}

	/**
	 * Metodo que modela la transaccion que busca el oferta en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del oferta a buscar. id != null
	 * @return Oferta - Oferta que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public Oferta getOfertaById(Long id) throws Exception {
		DAOOferta daoOferta = new DAOOferta();
		Oferta oferta = null;
		try 
		{
			this.conn = darConexion();
			daoOferta.setConn(conn);
			oferta = daoOferta.findOfertaById(id);
			if(oferta == null)
			{
				throw new Exception("El oferta con el id = " + id + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return oferta;
	}

	/**
	 * Metodo que modela la transaccion que agrega un oferta a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el oferta que entra como parametro <br/>
	 * @param oferta - el oferta a agregar. oferta != null
	 * @throws Exception - Cualquier error que se genere agregando el oferta
	 */
	public void addOferta(Oferta oferta) throws Exception 
	{

		DAOOferta daoOferta = new DAOOferta( );
		try
		{
			this.conn = darConexion();
			daoOferta.setConn(conn);
			daoOferta.addOferta(oferta);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}



	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al oferta que entra por parametro.<br/>
	 * Solamente se actualiza si existe el oferta en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el oferta que entra como parametro <br/>
	 * @param oferta - Oferta a actualizar. oferta != null
	 * @throws Exception - Cualquier error que se genere actualizando al oferta.
	 */
	public void updateOferta(Oferta oferta) throws Exception 
	{
		DAOOferta daoOferta = new DAOOferta( );
		try
		{
			this.conn = darConexion();
			daoOferta.setConn( conn );
			if(daoOferta.findOfertaById(oferta.getIdOferta()) !=null)
			{daoOferta.updateOferta(oferta);}
			else
			{throw new Exception("La oferta con el ID " + oferta.getIdOferta() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al oferta que entra por parametro. <br/>
	 * Solamente se actualiza si existe el oferta en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el oferta que entra por parametro <br/>
	 * @param Oferta - oferta a eliminar. oferta != null
	 * @throws Exception - Cualquier error que se genere eliminando al oferta.
	 */
	public void deleteOferta(Oferta oferta) throws Exception 
	{
		DAOOferta daoOferta = new DAOOferta( );
		try
		{
			this.conn = darConexion();
			daoOferta.setConn( conn );
			if(daoOferta.findOfertaById(oferta.getIdOferta()) != null)
			{daoOferta.deleteOferta(oferta);}
			else
			{throw new Exception("La oferta con ID " + oferta.getIdOferta() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}

	public List<Pago> getPagosOperadores( ) throws Exception {
		DAOOperador daoOperador = new DAOOperador();
		List<Pago> pagos = null;
		try
		{
			this.conn = darConexion();
			daoOperador.setConn(conn);
			pagos = daoOperador.darPagos();
			if(pagos.isEmpty())
			{
				throw new Exception("Aun no se ha pagado a ningun operador entre el anio actual y el anio corrido.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOperador.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return pagos;
	}

	public List<Popular> get20OfertasMasPopulares( ) throws Exception {
		DAOOferta daoOferta = new DAOOferta();
		List<Popular> populares = null;
		try
		{
			this.conn = darConexion();
			daoOferta.setConn(conn);
			populares = daoOferta.get20MasPopulares();
			if(populares.isEmpty())
			{
				throw new Exception("No hay ofertas de las que se hayan reservado alojamientos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoOferta.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return populares;
	}

	//----------------------------------------------------------------------------------------------------------------------------------
	// TRANSACCIONES DE LA TABLA RESERVASDEALOJAMIENTO
	//----------------------------------------------------------------------------------------------------------------------------------	

	/**
	 * Metodo que modela la transaccion que retorna todos los serviciosDeAlojamiento de la base de datos. <br/>
	 * @return List<ReservasDeAlojamiento> - Lista de serviciosDeAlojamiento que contiene el resultado de la consulta.
	 * @throws Exception -  Cualquier error que se genere durante la transaccion
	 */
	public List<ReservasDeAlojamiento> getAllReservasDeAlojamiento() throws Exception {
		DAOReservasDeAlojamiento daoReservasDeAlojamiento = new DAOReservasDeAlojamiento();
		List<ReservasDeAlojamiento> serviciosDeAlojamiento;
		try 
		{
			this.conn = darConexion();
			daoReservasDeAlojamiento.setConn(conn);
			serviciosDeAlojamiento = daoReservasDeAlojamiento.getAllReservassDeAlojamiento();
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReservasDeAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return serviciosDeAlojamiento;
	}

	/**
	 * Metodo que modela la transaccion que busca el reservaDeAlojamiento en la base de datos que tiene el ID dado por parametro. <br/>
	 * @param name -id del reservaDeAlojamiento a buscar. id != null
	 * @return ReservasDeAlojamiento - ReservasDeAlojamiento que se obtiene como resultado de la consulta.
	 * @throws Exception -  cualquier error que se genere durante la transaccion
	 */
	public ReservasDeAlojamiento getReservasDeAlojamientoByIds(Long id1,Long id2) throws Exception {
		DAOReservasDeAlojamiento daoReservasDeAlojamiento = new DAOReservasDeAlojamiento();
		ReservasDeAlojamiento reservaDeAlojamiento = null;
		try 
		{
			this.conn = darConexion();
			daoReservasDeAlojamiento.setConn(conn);
			reservaDeAlojamiento = daoReservasDeAlojamiento.findReservasDeAlojamientoByIds(id1, id2);
			if(reservaDeAlojamiento == null)
			{
				throw new Exception("El reservaDeAlojamiento con el idReserva = " + id1+ "y con el idOfertaAlojamiento = "+ id2 + " no se encuentra persistido en la base de datos.");				
			}
		} 
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReservasDeAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
		return reservaDeAlojamiento;
	}

	/**
	 * Metodo que modela la transaccion que agrega un reservaDeAlojamiento a la base de datos. <br/>
	 * <b> post: </b> se ha agregado el reservaDeAlojamiento que entra como parametro <br/>
	 * @param reservaDeAlojamiento - el reservaDeAlojamiento a agregar. reservaDeAlojamiento != null
	 * @throws Exception - Cualquier error que se genere agregando el reservaDeAlojamiento
	 */
	public void addReservasDeAlojamiento(ReservasDeAlojamiento reservaDeAlojamiento) throws Exception 
	{

		DAOReservasDeAlojamiento daoReservasDeAlojamiento = new DAOReservasDeAlojamiento( );
		try
		{
			this.conn = darConexion();
			daoReservasDeAlojamiento.setConn(conn);
			daoReservasDeAlojamiento.addReservasDeAlojamiento(reservaDeAlojamiento);
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReservasDeAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}
	}



	/**
	 * Metodo que modela la transaccion que actualiza en la base de datos al reservaDeAlojamiento que entra por parametro.<br/>
	 * Solamente se actualiza si existe el reservaDeAlojamiento en la Base de Datos <br/>
	 * <b> post: </b> se ha actualizado el reservaDeAlojamiento que entra como parametro <br/>
	 * @param reservaDeAlojamiento - ReservasDeAlojamiento a actualizar. reservaDeAlojamiento != null
	 * @throws Exception - Cualquier error que se genere actualizando al reservaDeAlojamiento.
	 */
	public void updateReservasDeAlojamiento(ReservasDeAlojamiento reservaDeAlojamiento) throws Exception 
	{
		DAOReservasDeAlojamiento daoReservasDeAlojamiento = new DAOReservasDeAlojamiento( );
		try
		{
			this.conn = darConexion();
			daoReservasDeAlojamiento.setConn( conn );
			if(daoReservasDeAlojamiento.findReservasDeAlojamientoByIds(reservaDeAlojamiento.getIdReserva(),reservaDeAlojamiento.getIdAlojamiento()) !=null)
			{daoReservasDeAlojamiento.updateReservasDeAlojamiento(reservaDeAlojamiento);}
			else
			{throw new Exception("El reservaDeAlojamiento con el idReserva " + reservaDeAlojamiento.getIdReserva() + " y el IDALOJAMIENTO " + reservaDeAlojamiento.getIdAlojamiento() +  " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReservasDeAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
	/**
	 * Metodo que modela la transaccion que elimina de la base de datos al reservaDeAlojamiento que entra por parametro. <br/>
	 * Solamente se actualiza si existe el reservaDeAlojamiento en la Base de Datos <br/>
	 * <b> post: </b> se ha eliminado el reservaDeAlojamiento que entra por parametro <br/>
	 * @param ReservasDeAlojamiento - reservaDeAlojamiento a eliminar. reservaDeAlojamiento != null
	 * @throws Exception - Cualquier error que se genere eliminando al reservaDeAlojamiento.
	 */
	public void deleteReservasDeAlojamiento(ReservasDeAlojamiento reservaDeAlojamiento) throws Exception 
	{
		DAOReservasDeAlojamiento daoReservasDeAlojamiento = new DAOReservasDeAlojamiento( );
		try
		{
			this.conn = darConexion();
			daoReservasDeAlojamiento.setConn( conn );
			if(daoReservasDeAlojamiento.findReservasDeAlojamientoByIds(reservaDeAlojamiento.getIdReserva(),reservaDeAlojamiento.getIdAlojamiento()) != null )
			{daoReservasDeAlojamiento.deleteReservasDeAlojamiento(reservaDeAlojamiento);}
			else
			{throw new Exception("La reservaDeAlojamiento con IDRESERVA " + reservaDeAlojamiento.getIdReserva() + " y con IDALOJAMIENTO = " + reservaDeAlojamiento.getIdAlojamiento() + " no se encuentra en la base de datos");}
		}
		catch (SQLException sqlException) {
			System.err.println("[EXCEPTION] SQLException:" + sqlException.getMessage());
			sqlException.printStackTrace();
			throw sqlException;
		} 
		catch (Exception exception) {
			System.err.println("[EXCEPTION] General Exception:" + exception.getMessage());
			exception.printStackTrace();
			throw exception;
		} 
		finally {
			try {
				daoReservasDeAlojamiento.cerrarRecursos();
				if(this.conn!=null){
					this.conn.close();					
				}
			}
			catch (SQLException exception) {
				System.err.println("[EXCEPTION] SQLException While Closing Resources:" + exception.getMessage());
				exception.printStackTrace();
				throw exception;
			}
		}	
	}
}