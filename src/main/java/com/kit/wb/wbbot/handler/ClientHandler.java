package com.kit.wb.wbbot.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kit.wb.wbbot.client.ClientManager;
import com.kit.wb.wbbot.client.RestClient;
import com.kit.wb.wbbot.user.UserManager;
import com.kit.wb.wbbot.user.UserState;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@AllArgsConstructor
public class ClientHandler implements TelegramHandler {

	RestClient wbClient;

	@Override
	public String handle(Message message, SendMessage response) {
		if(UserState.CLIENT_INIT.equals(UserManager.getUserEntity(message.getFrom()).getState())){
			ClientManager.addApi(message.getFrom(), message.getText());
			UserManager.updateState(message.getFrom(), UserState.CLIENT);
			return "Хотите узнать заказы /orders или продажи /sells";
		} else if(UserState.CLIENT.equals(UserManager.getUserEntity(message.getFrom()).getState())){
			if (message.getText().equals("/sells")){
				UserManager.updateState(message.getFrom(), UserState.NONE);
				return "Этот раздел еще в разработке";
			} else if(message.getText().equals("/orders")){
				String res = wbClient.get("/api/v1/supplier/orders?dateFrom=2017-04-09T15:21:20&key="
						+ ClientManager.getApi(message.getFrom()));
				UserManager.updateState(message.getFrom(), UserState.NONE);
				return res;
				//JSONObject object = (JSONObject) new JSONParser().parse(res);

			}

		}
		return "Какая-то ошибка. Обратись к автору.";
	}
}
