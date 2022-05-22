package com.kit.wb.wbbot.client;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.HashMap;
import java.util.Map;

@Component
public class ClientManager {

	private static final Map<Long, String> clientApiMap = new HashMap<Long, String>();

	public static void addApi(User user, String apiKey){
		clientApiMap.put(user.getId(), apiKey);
	}

	public static String getApi(User user){
		return clientApiMap.get(user.getId());
	}
}
