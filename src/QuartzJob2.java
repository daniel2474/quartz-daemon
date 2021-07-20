import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Esta clase se conecta con el web service de alpha y extrae informacion de todos los pedidos de los clientes uno por uno y guarda esta informacion en una base de datos por medio de otro web service
 * En caso de error genera un JobExecutionException
 * @author: Daniel García Velasco y Abimael Rueda Galindo
 * @version: 9/07/2021
 */


public class QuartzJob2 implements Job {
	
	/**
	 * Este metodo almacena en un JSONObject la respuesta del web service y este JSONObject es enviado al otro web service para ser almacenado
	 */
	@Override
	synchronized public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			int i=0,cont=0;
			JSONObject usuario=new JSONObject();
			usuario.put("email","bloodgigametal@gmail.com");
			usuario.put("nombre","daniel");
			usuario.put("nombreUsuario","daniel2474");
			usuario.put("password","farmacia123");
			
			String query="Http://localhost:8080/auth/login";
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

	        //query="http://192.168.20.26/ServiciosClubAlpha/api/Pagos/GetPedidoById";
	        query="https://af3bad3e-a46c-4c71-949a-fc6b245e7cee.mock.pstmn.io/ServiciosClubAlpha/api/Pagos/GetPedidoById";
			while(true) {
				try {
					JSONObject json2=new JSONObject();
					json2.put("NoPedido",i);
					json2.put("Token","77D5BDD4-1FEE-4A47-86A0-1E7D19EE1C74");
					url = new URL(query);
				    conn = (HttpURLConnection) url.openConnection();
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
				    JSONObject jsonObject = new JSONObject(result);

		            in.close();
		            conn.disconnect();
		            cont=0;
				    JSONObject json=new JSONObject();
				    JSONArray pedidoDetalle=new JSONArray();

				    JSONArray detalle=(JSONArray) jsonObject.get("Detalle");
					
				    for(int i1=0;i1<detalle.length();i1++){
				    	JSONObject deta=new JSONObject();
				    	deta.put("noPedido", ((JSONObject) detalle.get(i1)).get("NoPedido"));
				    	deta.put("idCliente", ((JSONObject) detalle.get(i1)).get("IDCliente"));
				    	deta.put("idOrdenVentaDetalle",((JSONObject) detalle.get(i1)).get("IDOrdendeVentaDetalle"));
				    	deta.put("concepto", ((JSONObject) detalle.get(i1)).get("Concepto"));
				    	deta.put("cantidad", ((JSONObject) detalle.get(i1)).get("Cantidad"));
				    	deta.put("importe", ((JSONObject) detalle.get(i1)).get("Importe"));
				    	deta.put("fechaInicio", ((JSONObject) detalle.get(i1)).get("FechaInicio"));
				    	deta.put("fechaFin", ((JSONObject) detalle.get(i1)).get("FechaFin"));
				    	deta.put("idOrdenDeVenta", ((JSONObject) detalle.get(i1)).get("IDOrdendeVenta"));
				    	deta.put("idProdServ", ((JSONObject) detalle.get(i1)).get("IDProdServ"));
				    	deta.put("precioUnitario",((JSONObject) detalle.get(i1)).get("PrecioUnitario"));
				    	deta.put("idCasillero", ((JSONObject) detalle.get(i1)).get("IDCasillero"));
				    	deta.put("descuento", ((JSONObject) detalle.get(i1)).get("Descuento"));
				    	deta.put("iVA", ((JSONObject) detalle.get(i1)).get("IVA"));
				    	deta.put("subImporte",((JSONObject) detalle.get(i1)).get("SubImporte"));
				    	pedidoDetalle.put(deta);
				    }
				    
					
				    
				    json.put("noPedido",jsonObject.get("NoPedido") );
				    json.put("idCliente",jsonObject.get("IDCliente") );
				    json.put("uRLLiga", jsonObject.get("URLLigaPago"));
				    json.put("status", jsonObject.get("Status"));
				    json.put("creado", jsonObject.get("Creado"));
				    json.put("pagoFecha", jsonObject.get("PagadoFecha"));
				    json.put("canceladoFecha", jsonObject.get("CanceladoFecha"));
				    json.put("pagado", jsonObject.get("Pagado"));
				    json.put("cancelado", jsonObject.get("Cancelado"));
				    json.put("correoCliente", jsonObject.get("CorreoCliente"));
				    json.put("pedidoDetalle", pedidoDetalle);
				    System.out.println(json);
				    
				    

				    String query2="http://localhost:8080/alpha/agregarPedido";
					URL url2 = new URL(query2);
		            HttpURLConnection conn2 = (HttpURLConnection) url2.openConnection();
		            String basicAuth = "Bearer "+ usuarioLog.get("token");

		            conn2.setRequestProperty ("Authorization", basicAuth);
		            conn2.setConnectTimeout(5000);
		            conn2.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
		            conn2.setDoOutput(true);
		            conn2.setDoInput(true);
		            conn2.setRequestMethod("POST");

		            OutputStream os2 = conn2.getOutputStream();
		            os2.write(json.toString().getBytes("UTF-8"));
		            os2.close();

		            // read the response
		            InputStream in2 = new BufferedInputStream(conn2.getInputStream());
		            String result2 = org.apache.commons.io.IOUtils.toString(in2, "UTF-8");
		            JSONObject jsonObject2 = new JSONObject(result2);
		            System.out.println(jsonObject2);

		            in2.close();
		            conn2.disconnect();
		            i++;
	            }catch(FileNotFoundException e) {
	            	i++;
	            	cont++;
	    			if(cont>500) 
	    				break;
	    		}catch(JSONException e) {
	    			
	    		} catch (MalformedURLException e) {
	    			e.printStackTrace();
	    		} catch (IOException e) {
	    			cont++;
	    			if(cont>500)break;
	    			i++;
	    		}
			}
			System.out.println("Fin");
		}catch(Exception e) {
			
		}
		
		
	}//cierre de metodo
}//cierre de clase
