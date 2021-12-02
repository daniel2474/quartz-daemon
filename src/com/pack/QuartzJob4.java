package com.pack;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**Este job se encarga de conectarse con el web service para agregar usuarios nuevos a la base de datos por medio de otro web service
 * 
 * @author Daniel Garc�a Velasco y Abimael Galindo Rueda
 *
 */
public class QuartzJob4 implements Job {
	/**
	 * En este metodo se hace login para obtener un token y asi poder utilizar el web service de addUsers
	 */
	@Override
	synchronized public void execute(JobExecutionContext arg0) throws JobExecutionException {
		try {
			
			JSONObject archivo=Archivo.inicializar();
			JSONObject usuario=new JSONObject();
			usuario.put("nombre",archivo.get("nombre"));
			usuario.put("nombreUsuario",archivo.get("nombreUsuario"));
			usuario.put("password",archivo.get("password"));
			
			String query=(String) archivo.get("login");
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
            
            //bloque
            query=archivo.getString("actualizarReporte");

            JSONObject body=new JSONObject();
            body.put("idClub",4);
            body.put("fechaInicio","2021-01-01");
            body.put("fechaFin","2021-12-31");
			
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
            os.write(body.toString().getBytes("UTF-8"));
            os.close();

            in = new BufferedInputStream(conn.getInputStream());
		    result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
		    JSONArray respuesta = new JSONArray(result);
            in.close();
            conn.disconnect();
            //bloque
            
          //bloque
            query=archivo.getString("actualizarReporte2");
            Date date = new Date();

            ZoneId timeZone = ZoneId.systemDefault();
            LocalDate getLocalDate = date.toInstant().atZone(timeZone).toLocalDate();
            int diff=getLocalDate.getYear()-2014;
            int valor=diff*12;
            valor=valor+getLocalDate.getMonthValue();
            for(int i=2;i<6;i++) {
            	JSONObject body2=new JSONObject();
            	if(i!=4) {
            		body2.put("idClub",i);
                    body2.put("mes",valor);
        			
                    url = new URL(query);
                    conn = (HttpURLConnection) url.openConnection();
                    basicAuth = "Bearer "+ usuarioLog.get("token");

                    conn.setRequestProperty ("Authorization", basicAuth);

                    conn.setConnectTimeout(5000);
                    conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setRequestMethod("POST");
                    os = conn.getOutputStream();
                    os.write(body2.toString().getBytes("UTF-8"));
                    os.close();

                    in = new BufferedInputStream(conn.getInputStream());
        		    result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
        		    respuesta = new JSONArray(result);
                    in.close();
                    conn.disconnect();
            	}
                
                //bloque
            }
            

			System.out.println("Fin actualizar reporte "+ new Date());
		}catch(FileNotFoundException ex) {
			ex.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}//Fin del metodo

}//Fin de la clase
