
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzMain {

	public static void main(String[] args) throws SchedulerException {
		JobDetail job=JobBuilder.newJob(QuartzJob.class).build();
		//Trigger t1=TriggerBuilder.newTrigger().withIdentity("CronTrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0 23 1/1 * ? *")).build();
		Trigger t1=TriggerBuilder.newTrigger().withIdentity("CronTrigger").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(30).repeatForever()).build();
		Scheduler sc=StdSchedulerFactory.getDefaultScheduler();
		sc.start();
		sc.scheduleJob(job,t1);
		JobDetail job2=JobBuilder.newJob(QuartzJob2.class).build();
		//Trigger t2=TriggerBuilder.newTrigger().withIdentity("Crontrigger").withSchedule(CronScheduleBuilder.cronSchedule("0 0/30 * 1/1 * ? *")).build();
		Trigger t2=TriggerBuilder.newTrigger().withIdentity("Crontrigger").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(30).repeatForever()).build();
		Scheduler sc2=StdSchedulerFactory.getDefaultScheduler();
		sc2.start();
		sc2.scheduleJob(job2,t2);
	}

}
