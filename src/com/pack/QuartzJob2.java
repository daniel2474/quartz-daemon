package com.pack;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Esta clase se conecta con el web service de alpha y extrae informacion de todos los clientes uno por uno y guarda esta informacion en una base de datos por medio de otro web service
 * En caso de error genera un JobExecutionException
 * @author: Daniel Garc�a Velasco y Abimael Rueda Galindo
 * @version: 9/07/2021
 */

public class QuartzJob2 implements Job {

	/**
	 * Este metodo almacena en un JSONObject la respuesta del web service y este JSONObject es enviado al otro web service para ser almacenado
	 */
	@Override
	synchronized public void execute(JobExecutionContext arg0) throws JobExecutionException {
			try {
				JSONObject archivo=Archivo.inicializar();
				
	            String query=archivo.getString("calcularAmonestaciones");
	            HttpRequest request1 = HttpRequest.newBuilder().uri(URI.create(query)).header("Content-Type", "application/json").GET().build();
	            CompletableFuture<String> client = HttpClient.newHttpClient().sendAsync(request1, BodyHandlers.ofString()).thenApply(HttpResponse::body);
	        	String json = "";
	    	   	try {
	    			json = String.valueOf(client.get());
	    		} catch (InterruptedException e) {
	    			e.printStackTrace();
	    		} catch (ExecutionException e) {
	    			e.printStackTrace();
	    		}
	        	System.out.println(json);
	            System.out.println("Fin Usuario que se excedieron mas de 4 horas en el estacionamiento "+ new Date());
				
			}catch(Exception e){
				e.printStackTrace();
			}
	}//cierre de metodo
}//cierre de clase

