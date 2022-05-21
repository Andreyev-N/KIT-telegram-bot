package com.kit.wb.wbbot.handler;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

public interface TelegramHandler {
	public String handle(Message message, SendMessage response);
}
