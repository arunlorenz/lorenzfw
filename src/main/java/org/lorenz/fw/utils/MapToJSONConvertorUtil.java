package org.lorenz.fw.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

public class MapToJSONConvertorUtil
{

	public static String convertMapToJson(Map<String, Object> map)
	{
		JSONObject jsonObject = new JSONObject(map);
		return jsonObject.toString();
	}

	public static String convertListOfMapsToJson(List<Map<String, Object>> list)
	{
		JSONArray jsonArray = new JSONArray();
		for (Map<String, Object> map : list)
		{
			jsonArray.put(new JSONObject(map));
		}
		return jsonArray.toString();
	}
}
