package com.deletelogs.utility;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DeleteLogs {
	
	@Value("${cutoffdays}")
	private int cutOffDays;	
	
	@Scheduled(cron = "* * * * * *")
	public void deleteLogs() {
	String [] folders = {"C:\\Temp\\logs"}; 
	
	long cutOffDate = System.currentTimeMillis() - (cutOffDays * 24 *60 * 60 * 1000);
	
	for(String folderPath : folders) {
		deleteFilesOlderThan(new File(folderPath), cutOffDate);
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
