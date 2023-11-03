
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DeleteOldLogsScheduler {

	String [] folders = {"C:\\Temp\\logs"}; 
	
	public static void main(String[] args) throws IOException {
		System.out.println("starting program...!!!!");

		File file = new File("C:\\Temp\\delete_logs_properties.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		String st = br.readLine();
		int duration = 0;
		if (null != st.substring(st.indexOf('=') + 1) && !st.substring(st.indexOf('=') + 1).equals("")
				&& !st.substring(st.indexOf('=') + 1).trim().equals("")) {
			duration = Integer.parseInt(st.substring(st.indexOf('=') + 1));
		}
		System.out.println(st);

		br.close();

		DeleteOldLogsScheduler ste = new DeleteOldLogsScheduler();
		ste.startScheduleTask(duration);

	}
	
	public void startScheduleTask(int cutoffDays) {
		
		int cutoffDuration = 20;

		ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Asia/Kolkata"));

		ZonedDateTime nextRun = now.withSecond(cutoffDuration); // change here for days

		if (now.compareTo(nextRun) > 0) {
			nextRun = nextRun.plusSeconds(cutoffDuration); // change here for days
		}

		Duration duration = Duration.between(now, nextRun);
		long initialDelay = duration.getSeconds();

		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		scheduler.scheduleAtFixedRate(new Runnable() {
			public void run() {
				try {
					getDataFromDatabase(cutoffDays);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}, initialDelay,
				TimeUnit.SECONDS.toSeconds(cutoffDuration), //change here for days
				TimeUnit.SECONDS);

	}

	private void getDataFromDatabase(int cutOffDays) {
		System.out.println("getting data...");
		deleteLogs(cutOffDays);
	}


	public void deleteLogs(int cutOffDays) {
		
		long cutOffDate = System.currentTimeMillis() - (cutOffDays * 24 *60 * 60 * 1000);
		System.out.println("cutoffDays : "+cutOffDays+ "  ------ cutOffDate : "+cutOffDate);
		for(String folderPath : folders) {
			System.out.println("running cron job to delete files in : "+ folderPath);
			deleteFilesOlderThan(new File(folderPath), cutOffDate);
			System.out.println("job completed for folder : "+ folderPath);
			
		}
		}

		 public void deleteFilesOlderThan(File directory, long cutoffDate) {
		      if (directory.isDirectory()) {
		          File[] files = directory.listFiles();
		          if (files != null) {
		              for (File file: files) {
		                  if (file.isDirectory()) {
		                      deleteFilesOlderThan(file, cutoffDate);
		                  } else {
		                      long lastModified = file.lastModified();
		                      System.out.println("file date :"+lastModified);
		                      if (lastModified < cutoffDate) {
		                          boolean deleted = file.delete();
		                          if (deleted) {
		                              System.out.println("Deleted file: " + file.getName());
		                          } else {
		                              System.out.println("Failed to delete file: " + file.getName());
		                          }
		                      }
		                  }
		              }
		          }
		      }
		  }
}