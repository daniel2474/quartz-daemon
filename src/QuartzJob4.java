import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
/**
 * Este job se encarga de actualizar los movimientos de los clientes, conectandose primero al web service de alpha y guardando la
 * informacion en una base de datos por medio de otra web service
 * @author Daniel García Velasco y Abimael Galindo Rueda
 *
 */
public class QuartzJob4 implements Job {

	/**
	 * En este metodo se hace login para poder hacer uso de las web services, una vez que se hace el login hace una solicitud para
	 * obtener los movimientos de los clientes y guarda estos datos en un json para despues ser mandados a traves de otro web service
	 * para poder ser almacenados en la base de datos
	 * 
	 */
	@Override
	synchronized public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			JSONObject archivo=Archivo.inicializar();
			JSONObject usuario=new JSONObject();
			usuario.put("nombre",archivo.get("nombre"));
			usuario.put("nombreUsuario",archivo.get("nombreUsuario"));
			usuario.put("password",archivo.get("password"));
			
			String query=archivo.getString("login");
			URL url = new URL(query);
		    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    conn.setConnectTimeout(5000);
		    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		    conn.setDoOutput(true);
		    conn.setDoInput(true);
		    conn.setRequestMethod("POST");

		    OutputStream os = conn.getOutputStream();
		    os.write(usuario.toString().getBytes("UTF-8"));
		    os.close();

		    // read the response
		    InputStream in = new BufferedInputStream(conn.getInputStream());
		    String result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
		    JSONObject usuarioLog = new JSONObject(result);
            in.close();
            conn.disconnect();

            JSONObject token=new JSONObject();
            token.put("Token",archivo.get("Token"));
			query= archivo.getString("getUsuarios");
			url = new URL(query);
		    conn = (HttpURLConnection) url.openConnection();
		    conn.setConnectTimeout(5000);
		    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		    conn.setDoOutput(true);
		    conn.setDoInput(true);
		    conn.setRequestMethod("POST");

		    os = conn.getOutputStream();
		    os.write(token.toString().getBytes("UTF-8"));
		    os.close();

		    // read the response
		    in = new BufferedInputStream(conn.getInputStream());
		    result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
		    JSONArray miembros = new JSONArray(result);
            in.close();
            conn.disconnect();

			//JSONObject json = new JSONObject(IOUtils.toString(new URL("https://af3bad3e-a46c-4c71-949a-fc6b245e7cee.mock.pstmn.io/ServiciosClubAlpha/api/Miembro/11184"), Charset.forName("UTF-8")));
			//JSONArray json = new JSONArray(IOUtils.toString(new URL("https://a0d69c82-099e-457e-874b-7b6f98384cbc.mock.pstmn.io/alpha/obtenerCliente"), Charset.forName("UTF-8")));
			for(int i=0;i<miembros.length();i++) {
            	try {					
					JSONObject token2=new JSONObject();
					token2.put("IDCliente", ((JSONObject) miembros.get(i)).get("IDCliente"));
		            token2.put("Token",archivo.get("Token"));
					query=(String) archivo.get("GetMovimientosbyId");
					url = new URL(query);
				    conn = (HttpURLConnection) url.openConnection();
				    conn.setConnectTimeout(5000);
				    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				    conn.setDoOutput(true);
				    conn.setDoInput(true);
				    conn.setRequestMethod("POST");
				    os = conn.getOutputStream();
				    os.write(token2.toString().getBytes("UTF-8"));
				    os.close();
				    // read the response
				    in = new BufferedInputStream(conn.getInputStream());
				    result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
				    JSONArray movimientosCliente = new JSONArray(result);
		            in.close();
		            conn.disconnect();
				    for(int j=0;j<movimientosCliente.length();j++) {
							JSONObject json = (JSONObject) movimientosCliente.get(j);
							JSONObject json2 = new JSONObject();
							json2.put("idclienteMovimiento", json.get("IDClienteMovimiento"));
							json2.put("idcliente",json.get("IDCliente") );
							json2.put("cliente", json.get("Cliente"));
							json2.put("concepto", json.get("Concepto"));
							json2.put("fechaDeAplicacion", json.get("FechaDeAplicacion"));
							json2.put("idordenDeVenta", json.get("IDOrdenDeVenta"));
							json2.put("idordenDeVentaDetalle", json.get("IDOrdenDeVentaDetalle"));
							json2.put("debito",json.get("Debito"));
							json2.put("saldo",json.get("Saldo"));
							query= archivo.getString("agregarMovimientos");
							url = new URL(query);
				            conn = (HttpURLConnection) url.openConnection();
				            String basicAuth = "Bearer "+ usuarioLog.get("token");
	
				            conn.setRequestProperty ("Authorization", basicAuth);
	
				            conn.setConnectTimeout(5000);
				            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
				            conn.setDoOutput(true);
				            conn.setDoInput(true);
				            conn.setRequestMethod("POST");
	
				            os = conn.getOutputStream();
				            os.write(json2.toString().getBytes("UTF-8"));
				            os.close();
	
				            // read the response
				            in = new BufferedInputStream(conn.getInputStream());
				            result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
				            in.close();
				            conn.disconnect();
						
		            }
            	}catch(FileNotFoundException ex) {
				}catch(IOException e) {
				}
				
			}
			System.out.println("Fin");
			
		}catch(Exception e){
			e.printStackTrace();
			System.out.println();
		}
	}//cierre de metodo
}//Fin de la clase
