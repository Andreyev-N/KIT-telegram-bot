package com.kit.wb.wbbot.cronJob;

import com.kit.wb.wbbot.message.WBBot;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.TimerTask;
import java.util.concurrent.Callable;

@Slf4j
@AllArgsConstructor
public class ScheduledTask implements Runnable {


	String text;
	String chatId;

	@Autowired
	private final ApplicationContext appContext;


	@Override
	public void run() {
		WBBot telegramBot = appContext.getBean(WBBot.class);
		SendMessage sendMessage = new SendMessage();
		sendMessage.setText(text);
		sendMessage.setChatId(chatId);
		try {
			telegramBot.execute(sendMessage);
		} catch (TelegramApiException e) {
			log.error("oops", e);
		}

	}
}
