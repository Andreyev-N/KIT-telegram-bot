package com.kit.wb.wbbot.handler;

import com.kit.wb.wbbot.user.UserManager;
import com.kit.wb.wbbot.user.UserState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class StartHandler implements TelegramHandler {
	@Override
	public String handle(Message message, SendMessage response) {
		UserManager.updateState(message.getFrom(), UserState.NONE);
		return  "Этот раздел еще в разработке";
	}
}
