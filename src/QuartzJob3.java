import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**Este job se encarga de conectarse con el web service para agregar usuarios nuevos a la base de datos por medio de otro web service
 * 
 * @author Daniel García Velasco y Abimael Galindo Rueda
 *
 */
public class QuartzJob3 implements Job {
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
            query=archivo.getString("addUsers");
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
            os.write("".getBytes("UTF-8"));
            os.close();

            conn.disconnect();
		}catch(FileNotFoundException ex) {
		}catch(IOException e) {
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}//Fin del metodo

}//Fin de la clase
