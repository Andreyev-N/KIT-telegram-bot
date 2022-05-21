package com.kit.wb.wbbot.handler;

import com.kit.wb.wbbot.cronJob.CronJobManager;
import com.kit.wb.wbbot.user.UserManager;
import com.kit.wb.wbbot.user.UserState;
import lombok.AllArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public class CronJobHandler implements TelegramHandler{

	CronJobManager cronJobManager;


	@Override
	public String handle(Message message, SendMessage response) {

		if(UserState.JOB_SCHEDULING.equals(UserManager.getUserEntity(message.getFrom()).getState())) {

			if (message.getText().equals("/no")) {
				UserManager.updateState(message.getFrom(), UserState.NONE);
				return "Вы отменили все изменения";
			} else if (message.getText().equals("/yes")) {
				cronJobManager.startCronJob(message);
				UserManager.updateState(message.getFrom(), UserState.JOB);
				return "напоминание включено. Вы можете отключить его коммандой /stop";
			}
			return "Просто выбери /yes или /no";
		} else if(UserState.JOB.equals(UserManager.getUserEntity(message.getFrom()).getState())) {
			if(message.getText().equals("/stop")){
				CronJobManager.stopCronJob(message.getFrom());
				UserManager.updateState(message.getFrom(), UserState.NONE);
				return "Напоминание отключено! Вы вернулись к началу";
			}
			return "Вы в режиме ожидания напоминаний. Вы можете отключить его коммандой /stop";
		}
		return "Какая-то ошибка. Обратись к автору.";

	}
}
