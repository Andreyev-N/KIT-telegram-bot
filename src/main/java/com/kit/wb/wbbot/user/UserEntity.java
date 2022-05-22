package com.kit.wb.wbbot.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.telegram.telegrambots.meta.api.objects.User;

@Data
@AllArgsConstructor
public class UserEntity {

	private UserState state;
	private User user;
	private String chatId;

}
