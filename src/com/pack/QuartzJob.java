package com.pack;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;

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

public class QuartzJob implements Job {

	/**
	 * Este metodo almacena en un JSONObject la respuesta del web service y este JSONObject es enviado al otro web service para ser almacenado
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
				
				query=archivo.getString("getUsuarios");
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
						query="http://192.168.20.102:8090/alpha/updateCliente/"+((JSONObject) miembros.get(i)).get("IDCliente");
						url = new URL(query);
					     conn = (HttpURLConnection) url.openConnection();
					     String basicAuth = "Bearer "+ usuarioLog.get("token");
				        conn.setRequestProperty ("Authorization", basicAuth);
				        
				        conn.setRequestProperty("Content-Type","application/json");
				        conn.setRequestMethod("GET");
				        BufferedReader inn = new BufferedReader(new InputStreamReader(conn.getInputStream()));
				        String output;

				        StringBuffer response = new StringBuffer();
				        while ((output = inn.readLine()) != null) {
				            response.append(output);
				        }

				        inn.close();
				        // printing result from response

						
						/*JSONObject json = new JSONObject(IOUtils.toString(new URL("http://192.168.20.47/ServiciosClubAlpha/api/Miembro/"+((JSONObject) miembros.get(i)).get("IDCliente")), Charset.forName("UTF-8")));
						
						JSONObject json2 = new JSONObject();
						JSONObject estatuscliente = new JSONObject();
						JSONObject lunes = new JSONObject();
						JSONObject martes = new JSONObject();
						JSONObject miercoles = new JSONObject();
						JSONObject jueves = new JSONObject();
						JSONObject viernes = new JSONObject();
						JSONObject sabado = new JSONObject();
						JSONObject domingo = new JSONObject();
						JSONObject tipocliente = new JSONObject();
						JSONObject horario = new JSONObject();
						JSONObject horarioacceso = new JSONObject();
						JSONObject tipomembresia = new JSONObject();
						JSONArray mensajes = new JSONArray();
						JSONObject categoria=new JSONObject();
						JSONObject estatusmembresia=new JSONObject();
						JSONArray otrosclubs = new JSONArray();
						JSONObject estatuscobranza=new JSONObject();
						JSONObject club=new JSONObject();
						
						estatuscliente.put("idStatusCliente", ((JSONObject) (json.get("EstatusCliente"))).get("Id"));
						estatuscliente.put("nombre", ((JSONObject) (json.get("EstatusCliente"))).get("Nombre"));
						
						tipocliente.put("idTipoCliente", ((JSONObject) (json.get("TipoCliente"))).get("Id"));
						tipocliente.put("nombre",  ((JSONObject) (json.get("TipoCliente"))).get("Nombre"));
						
						lunes.put("horaentrada", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraEntrada"));
						lunes.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraSalida"));
						
						martes.put("horaentrada", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraEntrada"));
						martes.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraSalida"));
						

						miercoles.put("horaentrada", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraEntrada"));
						miercoles.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraSalida"));
						

						jueves.put("horaentrada", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraEntrada"));
						jueves.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraSalida"));
						

						viernes.put("horaentrada", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraEntrada"));
						viernes.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraSalida"));
						

						sabado.put("horaentrada", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraEntrada"));
						sabado.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraSalida"));
						
						domingo.put("horaentrada", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraEntrada"));
						domingo.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraSalida"));
						
						horario.put("idLunes", lunes);
						horario.put("idMartes", martes);
						horario.put("idMiercoles", miercoles);
						horario.put("idJueves", jueves);
						horario.put("idViernes", viernes);
						horario.put("idSabado", sabado);
						horario.put("idDomingo", domingo);				
						
						horarioacceso.put("horario", horario);
						horarioacceso.put("idHorarioAcceso",   ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Id"));
						horarioacceso.put("nombre", ((JSONObject) ((JSONObject) (json.get("TipoMembresia"))).get("HorarioAcceso")).get("Nombre"));
						
						tipomembresia.put("idTipoMembresia",((JSONObject) (json.get("TipoMembresia"))).get("Id") );	
						tipomembresia.put("nombre", ((JSONObject) (json.get("TipoMembresia"))).get("Nombre"));
						tipomembresia.put("horarioacceso",horarioacceso);				
						
						JSONArray mens=(JSONArray) json.get("Mensajes");
						for(int i1=0;i1<mens.length();i1++) {
							JSONObject mensaje=new JSONObject();
							mensaje.put("idMensaje", ((JSONObject) mens.get(i1)).get("Id"));
							mensaje.put("descripcion", ((JSONObject) mens.get(i1)).get("Descripcion"));	
							mensajes.put(mensaje);	
						}
						
						categoria.put("id", ((JSONObject) json.get("Categoria")).get("Id"));
						categoria.put("nombre", ((JSONObject) json.get("Categoria")).get("Nombre"));
						
						estatusmembresia.put("idEstatusMembresia", ((JSONObject) json.get("EstatusMembresia")).get("Id"));
						estatusmembresia.put("nombre", ((JSONObject) json.get("EstatusMembresia")).get("Nombre"));
						
						JSONArray otroClubAux=(JSONArray) json.get("HorarioOtroClub");
						for(int i1=0;i1<otroClubAux.length();i1++) {
							JSONObject otroclub=new JSONObject();
							otroclub.put("terminalId", ((JSONObject) otroClubAux.get(i1)).get("TerminalId"));
							otroclub.put("desde", ((JSONObject) otroClubAux.get(i1)).get("Desde"));	
							otroclub.put("hasta", ((JSONObject) otroClubAux.get(i1)).get("Hasta"));	
							otroclub.put("observaciones", ((JSONObject) otroClubAux.get(i1)).get("Observaciones"));
							JSONObject lunes2 = new JSONObject();
							JSONObject martes2 = new JSONObject();
							JSONObject miercoles2 = new JSONObject();
							JSONObject jueves2 = new JSONObject();
							JSONObject viernes2 = new JSONObject();
							JSONObject sabado2 = new JSONObject();
							JSONObject domingo2 = new JSONObject();
							JSONObject horario2 = new JSONObject();
							JSONObject horarioacceso2 = new JSONObject();
							
							lunes2.put("horaentrada",((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraEntrada"));
							lunes2.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Horario")).get("Lunes")).get("HoraSalida"));
							
							martes2.put("horaentrada",((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Horario")).get("Martes")).get("HoraEntrada"));
							martes2.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Horario")).get("Martes")).get("HoraSalida"));
							
							miercoles2.put("horaentrada",((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Horario")).get("Miércoles")).get("HoraEntrada"));
							miercoles2.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Horario")).get("Miércoles")).get("HoraSalida"));
							
							jueves2.put("horaentrada",((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Horario")).get("Jueves")).get("HoraEntrada"));
							jueves2.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Horario")).get("Jueves")).get("HoraSalida"));
							
							viernes2.put("horaentrada",((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Horario")).get("Viernes")).get("HoraEntrada"));
							viernes2.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Horario")).get("Viernes")).get("HoraSalida"));
							
							sabado2.put("horaentrada",((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Horario")).get("Sábado")).get("HoraEntrada"));
							sabado2.put("horasalida", ((JSONObject) ((JSONObject) ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Horario")).get("Sábado")).get("HoraSalida"));
						
							horario2.put("idLunes", lunes2);
							horario2.put("idMartes", martes2);
							horario2.put("idMiercoles", miercoles2);
							horario2.put("idJueves", jueves2);
							horario2.put("idViernes", viernes2);
							horario2.put("idSabado", sabado2);
							horario2.put("idDomingo", domingo2);	
							
							horarioacceso2.put("horario", horario2);
							horarioacceso2.put("idHorarioAcceso",    ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Id"));
							horarioacceso2.put("nombre", ((JSONObject) ((JSONObject) otroClubAux.get(i1)).get("HorarioAcceso")).get("Nombre"));
							
							otroclub.put("horarioacceso", horarioacceso2);
							
							otrosclubs.put(otroclub);	
						}
						
						estatuscobranza.put("idEstatusCobranza", ((JSONObject) json.get("EstatusCobranza")).get("Id"));
						estatuscobranza.put("nombre", ((JSONObject) json.get("EstatusCobranza")).get("Nombre"));
						
						club.put("idClub", ((JSONObject) json.get("Club")).get("Id"));
						club.put("nombre", ((JSONObject) json.get("Club")).get("Nombre"));
						
						json2.put("nombre", json.get("Nombre"));
						json2.put("estatusCliente",estatuscliente );
						json2.put("estatusAcceso", json.get("EstatusAcceso"));
						json2.put("domicilioPago", json.get("DomiciliaPago"));
						json2.put("idCliente", json.get("IdCliente"));
						json2.put("apellidoMaterno", json.get("ApellidoMaterno"));
						json2.put("idClienteSector", json.get("IDClienteSector"));
						json2.put("tipoCliente",tipocliente);
						json2.put("mensualidadPagada",json.get("MensualidadPagada"));
						json2.put("sexo",json.get("Sexo"));
						json2.put("idCaptura",json.get("IDCapturo"));
						json2.put("inicioActividades",json.get("InicioActividades"));
						json2.put("fechaNacimiento",json.get("FechaNacimiento"));
						json2.put("tipoMembresia", tipomembresia);
						json2.put("mensajes", mensajes);
						json2.put("categoria", categoria);
						json2.put("apellidoPaterno", json.get("ApellidoPaterno"));
						json2.put("telefono", json.get("Telefono"));
						json2.put("estatusMembresia", estatusmembresia);
						json2.put("horarioOtroClub", otrosclubs);
						json2.put("noMembresia", json.get("NoMembresia"));
						json2.put("servicio", json.get("Servicios"));
						json2.put("nombreCompleto", json.get("NombreCompleto"));
						json2.put("tieneAcceso", json.get("TieneAcceso"));
						json2.put("email", json.get("EMail"));
						json2.put("idCapturaFecha", json.get("IDCapturoFecha"));
						json2.put("estatusCobranza", estatuscobranza);
						json2.put("idSexo", json.get("IDSexo"));
						json2.put("club", club);
						json2.put("nacionalidad", json.get("Nacionalidad"));
						json2.put("idClienteGrupo", json.get("IDClienteGrupo"));
						json2.put("urlfoto", json.get("UrlFoto"));
						json2.put("fechaFinAcceso", json.get("FechaFinAcceso"));
						query=archivo.getString("agregarCliente");
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
			            conn.disconnect();*/
					}catch(FileNotFoundException ex) {
						ex.printStackTrace();
					}catch(IOException e) {

						e.printStackTrace();
					}
					
				}
				System.out.println("Fin actualizar usuarios "+ new Date());
				
			}catch(Exception e){
				e.printStackTrace();
			}
	}//cierre de metodo
}//cierre de clase

