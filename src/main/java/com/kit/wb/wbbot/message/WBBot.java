package com.kit.wb.wbbot.message;

import com.kit.wb.wbbot.CredentialUtils;
import com.kit.wb.wbbot.cronJob.CronJobManager;
import com.kit.wb.wbbot.handler.CronJobHandler;
import com.kit.wb.wbbot.handler.DefaultHandler;
import com.kit.wb.wbbot.handler.StartHandler;
import com.kit.wb.wbbot.handler.TelegramHandler;
import com.kit.wb.wbbot.user.UserManager;
import com.kit.wb.wbbot.user.UserState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@Slf4j
public class WBBot extends TelegramLongPollingBot {

	@Autowired
	CronJobManager cronJobManager;

	//@Value("${bot.name}")
	private final String botUsername = CredentialUtils.getBotName();

	//@Value("${bot.token}")
	private final String botToken = CredentialUtils.getBotToken();

	@Override
	public String getBotUsername() {
		return botUsername;
	}

	@Override
	public String getBotToken() {
		return botToken;
	}

	@Override
	public void onUpdateReceived(Update update) {

		Message message = update.getMessage();
		String chatId = message.getChatId().toString();
		User user = message.getFrom();

		log.info("!! new update from [{}], STATE = [{}]", update.getMessage().getFrom().getUserName(), UserManager.getUserState(user));

		SendMessage response = new SendMessage();
		response.setChatId(chatId);
		TelegramHandler handler;

		boolean isUserJustAdded = UserManager.add(update);
		if (isUserJustAdded){
			handler = new DefaultHandler();
		} else {
			UserState state = UserManager.getUserState(user);

			if(UserState.NONE.equals(state)){
				handler = new DefaultHandler();
			} else if(UserState.START.equals(state) || UserState.TIME_ASK.equals(state) || UserState.TEXT_ASK.equals(state)){
				handler = new StartHandler();
			} else {
				handler = new CronJobHandler(cronJobManager);
			}
		}

		String text = handler.handle(message, response);
		response.setText(text);
		try {
			execute(response);
		} catch (TelegramApiException e) {
			log.error("oops!", e);
		}
	}
}
