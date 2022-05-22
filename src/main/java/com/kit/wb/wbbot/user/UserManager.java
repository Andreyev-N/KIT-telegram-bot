package com.kit.wb.wbbot.user;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;

import java.util.Map;

@Component
public class UserManager {

	private static final Map<Long, UserEntity> users = new HashMap<Long, UserEntity>();

	public static boolean isUserExist(User user){
		return users.containsKey(user.getId());
	}

	public static UserEntity getUserEntity(User user){
		return users.get(user.getId());
	}

	public static boolean add(Update update){
		return add(update.getMessage().getFrom(), update.getMessage().getChatId().toString(), UserState.NONE);
	}

	public static boolean add(User user, String chatId, UserState state){
		if(isUserExist(user)){
			return false;
		}
		users.put(user.getId(), new UserEntity(state, user, chatId));
		return true;
	}

	public static UserState getUserState(User user){
		if(!isUserExist(user)){
			return UserState.NONE;
		}
		return users.get(user.getId()).getState();
	}

	public static void  updateState(User user, UserState state){
		UserEntity userEntity = users.get(user.getId());
		userEntity.setState(state);
		users.replace(user.getId(), userEntity);
	}

}
