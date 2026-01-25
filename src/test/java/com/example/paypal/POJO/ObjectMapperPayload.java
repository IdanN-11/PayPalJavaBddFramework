package com.example.paypal.POJO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; // ✅ CORRECT
import com.fasterxml.jackson.annotation.JsonInclude;

public class ObjectMapperPayload {

	public Map<String, Object> orderMapping(HashMap<String, HashMap<String, String>> map,
			List<HashMap<String, String>> itemMap, String OrderId) {
		Map<String, Object> OrderDataMap = new HashMap<>();
		List<Map<String, String>> ItemDataList = new ArrayList<>();

		for (Map.Entry<String, HashMap<String, String>> e : map.entrySet()) {
			if (e.getKey().equals(OrderId)) {
				OrderDataMap.putAll(e.getValue());
			}
		}
		for (Map<String, String> m : itemMap) {
			if (m.get("order_id").equals(OrderId)) {
				ItemDataList.add(m);
			}
		}
		Map<String, Object> root = new LinkedHashMap<>();
		root.put("intent", OrderDataMap.get("intent"));

		Map<String, Object> experience_context = new LinkedHashMap<>();
		experience_context.put("payment_method_preference", OrderDataMap.get("payment_method_preference"));
		experience_context.put("landing_page", OrderDataMap.get("landing_page"));
		experience_context.put("shipping_preference", OrderDataMap.get("shipping_preference"));
		experience_context.put("user_action", OrderDataMap.get("user_action"));
		experience_context.put("return_url", OrderDataMap.get("return_url"));
		experience_context.put("cancel_url", OrderDataMap.get("cancel_url"));
		Map<String, Object> paypal = new LinkedHashMap<>();
		paypal.put("experience_context", experience_context);
		Map<String, Object> payment_source = new LinkedHashMap<>();
		payment_source.put("paypal", paypal);

		root.put("payment_source", payment_source);

		Map<String, Object> item_total = new LinkedHashMap<>();
		item_total.put("currency_code", OrderDataMap.get("currency_code"));
		item_total.put("value", OrderDataMap.get("item_total"));
		Map<String, Object> shipping = new LinkedHashMap<>();
		shipping.put("currency_code", OrderDataMap.get("currency_code"));
		shipping.put("value", OrderDataMap.get("shipping_amount"));

		Map<String, Object> breakdown = new LinkedHashMap<>();
		breakdown.put("item_total", item_total);
		breakdown.put("shipping", shipping);

		Map<String, Object> amount = new LinkedHashMap<>();
		amount.put("currency_code", OrderDataMap.get("currency_code"));
		amount.put("value", OrderDataMap.get("order_value"));
		amount.put("breakdown", breakdown);

		List<Object> purchase_units = new LinkedList<>();
		Map<String, Object> punits = new LinkedHashMap<>();
		punits.put("invoice_id", OrderDataMap.get("invoice_id"));
		punits.put("amount", amount);

		// Itmes data

		List<Object> Item12 = new ArrayList<>();

		for (Map<String, String> m : ItemDataList) {
			Items item = new Items();
			System.out.println(m);
			System.out.println("First");
			if (m.get("order_id").equals(OrderId)) {

				item.name = m.get("item_name");
				item.description = m.get("description");

				UPC upc = new UPC();
				upc.code = m.get("upc_code");
				upc.type = m.get("upc_type");
				UnitAmout unit_amount = new UnitAmout();
				item.Category = m.get("category");
				item.image_url = m.get("image_url");
				item.quantity = Integer.parseInt(m.get("quantity"));
				item.sku = m.get("sku");
				item.url = m.get("item_url");
				unit_amount.currency_code = m.get("currency_code");
				unit_amount.value = m.get("unit_price");
				item.unit_amount = unit_amount;
				item.upc = upc;
			}
			Item12.add(item);
		}
		punits.put("items", Item12);
		purchase_units.add(punits);
		root.put("purchase_units", purchase_units);
		return root;

	}

	public String CreatePayload(String path, String Payload) {
		ObjectMapperPayload o = new ObjectMapperPayload();
		Map<String, Object> root1 = new LinkedHashMap<String, Object>();
		ExcelUtil rh = new ExcelUtil();
		// String path =
		// System.getProperty("user.dir")+"\\src\\test\\resources\\paypal_multi_order_with_items.xlsx";
		System.out.println(rh.readItemData(path));
		System.out.println(rh.readOrdersData(path));
		root1 = o.orderMapping(rh.readOrdersData(path), rh.readItemData(path), Payload);
		System.out.println(root1);
		ObjectMapper mapper = new ObjectMapper()
				.setSerializationInclusion(JsonInclude.Include.NON_NULL);

		String json;
		try {
			json = mapper.writerWithDefaultPrettyPrinter()
					.writeValueAsString(root1);
			return json;
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;

		}

	}
}
