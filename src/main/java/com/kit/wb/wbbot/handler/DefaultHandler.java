package com.kit.wb.wbbot.handler;

import com.kit.wb.wbbot.user.UserManager;
import com.kit.wb.wbbot.user.UserState;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.Instant;

@Slf4j
public class DefaultHandler implements TelegramHandler{
	@Override
	public String handle(Message message, SendMessage response) {
		String hello = "Привет! Это личный телеграм бот Андреева Никиты. Введите /start чтобы начать или /getTime чтобы узнать время";
		if(message.getText().equals("/getTime")){
			response.setText(Instant.now().toString());
		} else if(message.getText().equals("/start")) {
			UserManager.updateState(message.getFrom(), UserState.START);
			response.setText("Хотите запустить напоминание с повтором /repeated или без повтора /once?");
		}else {
			response.setText(hello);
		}

		return response.getText();
	}
}
