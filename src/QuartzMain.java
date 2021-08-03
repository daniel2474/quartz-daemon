
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Esta es la clase principal del programa la cual ejecuta 2 tareas de Quartz, una de ellas a las 11 de la noche y la otra cada 40 min
 * @author: Daniel Garc�a Velasco y Abimael Rueda Galindo
 * @version: 9/07/2021
 *
 */

public class QuartzMain {
	
	/**
	 * Metodo main de la clase
	 * @param args
	 * @throws SchedulerException
	 */

	public static void main(String[] args) throws SchedulerException {
		JobDetail job=JobBuilder.newJob(QuartzJob.class).build();
		//Trigger t1=TriggerBuilder.newTrigger().withIdentity("CronTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0 23 1/1 * ? *")).build();
		Trigger t1=TriggerBuilder.newTrigger().withIdentity("CronTrigger").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(3).repeatForever()).build();
		Scheduler sc=StdSchedulerFactory.getDefaultScheduler();
		sc.start();
		sc.scheduleJob(job,t1);
		JobDetail job2=JobBuilder.newJob(QuartzJob2.class).build();
		Trigger t2=TriggerBuilder.newTrigger().withIdentity("Crontrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/30 * 1/1 * ? *")).build();
		//Trigger t2=TriggerBuilder.newTrigger().withIdentity("Crontrigger").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(30).repeatForever()).build();
		//Scheduler sc2=StdSchedulerFactory.getDefaultScheduler(); 
		//sc2.start();
		//sc2.scheduleJob(job2,t2);
	}// fin del metodo

}
