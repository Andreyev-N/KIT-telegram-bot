package com.kit.wb.wbbot.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kit.wb.wbbot.client.ClientManager;
import com.kit.wb.wbbot.client.OrderResponseEntity;
import com.kit.wb.wbbot.client.RestClient;
import com.kit.wb.wbbot.user.UserManager;
import com.kit.wb.wbbot.user.UserState;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@Slf4j
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
				String res;
				try {
					res = wbClient.get("/api/v1/supplier/orders?dateFrom=2017-04-09T15:21:20&key="
							+ ClientManager.getApi(message.getFrom()));
				} catch (Exception e){
					log.error("can not connect WB api", e);
					return "нет соединения с WB: " + e.getMessage();
				}

				UserManager.updateState(message.getFrom(), UserState.NONE);

				ObjectMapper mapper = new ObjectMapper();

				List<OrderResponseEntity> list;
				try {
					list = mapper.readValue(res, mapper.getTypeFactory().
							constructCollectionType(LinkedList.class, OrderResponseEntity.class));
				} catch (JsonProcessingException e) {
					log.warn("can not parse", e);
					return "не удалось прочитать ответ";
				}

				StringBuilder answer = new StringBuilder("получено " + list.size() + " заказов:\n\n");

				for (int i = 0; i < list.size(); i++) {
					answer.append("заказ ");
					answer.append(i + 1);
					answer.append("\nдата:    ");
					answer.append(list.get(i).getDate());
					answer.append("\nартикул: ");
					answer.append(list.get(i).getSupplierArticle());
					answer.append("\nразмер:  ");
					answer.append(list.get(i).getTechSize());
					answer.append("\nbarcode: ");
					answer.append(list.get(i).getBarcode());
					answer.append("\nцена:    ");
					long endPrice = (100 - list.get(i).getDiscountPercent()) * list.get(i).getTotalPrice();
					answer.append(endPrice);
					answer.append("\nобласть: ");
					answer.append(list.get(i).getOblast());
					answer.append("\n-------");
				}

				log.info(answer.toString());
				return answer.toString();
			}

		}
		return "Какая-то ошибка. Обратись к автору.";
	}
}
