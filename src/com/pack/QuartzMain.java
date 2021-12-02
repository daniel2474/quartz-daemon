package com.pack;

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
 * Esta es la clase principal del programa la cual ejecuta 4 tareas de Quartz, job se ejecuta a las 11 de la noche, job2,
 *  job3 y job4 cada 40 min.
 * 
 * @author: Daniel García Velasco y Abimael Rueda Galindo
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
		Trigger t1=TriggerBuilder.newTrigger().withIdentity("CronTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0 23 1/1 * ? *")).build();
		//Trigger t1=TriggerBuilder.newTrigger().withIdentity("CronTrigger").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(3).repeatForever()).build();
		Scheduler sc=StdSchedulerFactory.getDefaultScheduler();
		sc.start();
		sc.scheduleJob(job,t1);
		
		//JobDetail job2=JobBuilder.newJob(QuartzJob2.class).build();
		//Trigger t2=TriggerBuilder.newTrigger().withIdentity("Crontrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/30 * 1/1 * ? *")).build();
		//Trigger t2=TriggerBuilder.newTrigger().withIdentity("Crontrigger").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(30).repeatForever()).build();
		//Scheduler sc2=StdSchedulerFactory.getDefaultScheduler(); 
		//sc2.start();
		//sc2.scheduleJob(job2,t2);
		
		//JobDetail job3=JobBuilder.newJob(QuartzJob3.class).build();
		//Trigger t3=TriggerBuilder.newTrigger().withIdentity("crontrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0 23 1/1 * ? *")).build();
		//Trigger t3=TriggerBuilder.newTrigger().withIdentity("crontrigger").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(30).repeatForever()).build();
		//Scheduler sc3=StdSchedulerFactory.getDefaultScheduler(); 
		//sc3.start();
		//sc3.scheduleJob(job3,t3);
		
		JobDetail job4=JobBuilder.newJob(QuartzJob4.class).build();
		Trigger t4=TriggerBuilder.newTrigger().withIdentity("crontriggeR").withSchedule(CronScheduleBuilder.cronSchedule("0 0 23 1/1 * ? *")).build();
		//Trigger t4=TriggerBuilder.newTrigger().withIdentity("crontriggeR").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(30).repeatForever()).build();
		Scheduler sc4=StdSchedulerFactory.getDefaultScheduler(); 
		sc4.start();
		sc4.scheduleJob(job4,t4);
		
		//JobDetail job5=JobBuilder.newJob(QuartzJob5.class).build();
		//Trigger t5=TriggerBuilder.newTrigger().withIdentity("crontriggeR").withSchedule(CronScheduleBuilder.cronSchedule("0 0/30 * 1/1 * ? *")).build();
		//Trigger t5=TriggerBuilder.newTrigger().withIdentity("crontriggeR").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(60).repeatForever()).build();
		//Scheduler sc5=StdSchedulerFactory.getDefaultScheduler(); 
		//sc5.start();
		//sc5.scheduleJob(job5,t5);
	}// fin del metodo

}
