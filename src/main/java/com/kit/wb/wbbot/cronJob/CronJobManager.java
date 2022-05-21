package com.kit.wb.wbbot.cronJob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class CronJobManager {

	@Autowired
	private ApplicationContext appContext;

	private static final Map<Long, CronInfoEntity> cronInfoMap = new HashMap<Long, CronInfoEntity>();

	public static void createCronJob(User user, boolean isRepeated){
		CronInfoEntity cronInfoEntity = new CronInfoEntity();
		cronInfoMap.put(user.getId(), cronInfoEntity);
	}

	public static void addTime(User user, long time){
		CronInfoEntity cronInfoEntity = cronInfoMap.get(user.getId());
		cronInfoEntity.setTime(time);
		cronInfoMap.replace(user.getId(), cronInfoEntity);
	}

	public static void addText(User user, String text){
		CronInfoEntity cronInfoEntity = cronInfoMap.get(user.getId());
		cronInfoEntity.setText(text);
		cronInfoMap.replace(user.getId(), cronInfoEntity);
	}

	public static CronInfoEntity getCronInfo(User user){
		return cronInfoMap.get(user.getId());
	}

	public void startCronJob(Message message) {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		CronInfoEntity cronInfoEntity = cronInfoMap.get(message.getFrom().getId());
		Timer timer = new Timer();
		Runnable timerTask = new ScheduledTask(cronInfoEntity.text, message.getChatId().toString(), appContext);
		ScheduledFuture<?> future = executor.scheduleAtFixedRate(timerTask, cronInfoEntity.time, cronInfoEntity.time, TimeUnit.SECONDS);
		cronInfoEntity.setTask(future);
		cronInfoMap.replace(message.getFrom().getId(), cronInfoEntity);



	}

	public static void stopCronJob(User user) {
		CronInfoEntity cronInfoEntity = cronInfoMap.get(user.getId());
		cronInfoEntity.getTask().cancel(true);
		cronInfoMap.remove(user.getId());

	}
}
