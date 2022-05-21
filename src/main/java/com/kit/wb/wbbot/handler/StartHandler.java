package com.kit.wb.wbbot.handler;

import com.kit.wb.wbbot.cronJob.CronInfoEntity;
import com.kit.wb.wbbot.cronJob.CronJobManager;
import com.kit.wb.wbbot.user.UserManager;
import com.kit.wb.wbbot.user.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.Instant;

public class StartHandler implements TelegramHandler {
	@Override
	public String handle(Message message, SendMessage response) {

		if (UserState.START.equals(UserManager.getUserEntity(message.getFrom()).getState())){

			if (message.getText().equals("/repeated")) {
				UserManager.updateState(message.getFrom(), UserState.TIME_ASK);
				CronJobManager.createCronJob(message.getFrom(), true);
				return "Укажите через сколько секунд будет повотряться напоминание?";
			} else if (message.getText().equals("/once")) {
				UserManager.updateState(message.getFrom(), UserState.NONE);
				return "Этот раздел еще в разработке";
			}
			return "Что-то непонятное вводишь. Хотите запустить напоминание с повтором /repeated или без повтора /once?";

		} else if(UserState.TIME_ASK.equals(UserManager.getUserEntity(message.getFrom()).getState())){
			long time;
			try {
				 time = Long.parseLong(message.getText());
			} catch (Exception e){
				return "Что-то непонятное вводишь. Укажите через сколько секунд будет повотряться напоминание? ЦИФРОЙ УКАЖИ ЧЕРЕЗ СКОЛЬКО СЕКУНД";
			}
			if (time <= 0){
				return "Введи нормально. Время должно быть больше 0";
			}
			UserManager.updateState(message.getFrom(), UserState.TEXT_ASK);
			CronJobManager.addTime(message.getFrom(), time);
			return "Укажите текст напоминания";
		} else if (UserState.TEXT_ASK.equals(UserManager.getUserEntity(message.getFrom()).getState())){
			CronJobManager.addText(message.getFrom(), message.getText());
			CronInfoEntity cronInfoEntity = CronJobManager.getCronInfo(message.getFrom());
			UserManager.updateState(message.getFrom(), UserState.JOB_SCHEDULING);
			return "Запустить напоминание каждые " + cronInfoEntity.getTime() + " c текстом \"" + cronInfoEntity.getText() + "\"? /yes или /no";
		}
		return "Какая-то ошибка. Обратись к автору.";
	}
}
