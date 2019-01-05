package com.sample;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class MyDeserializer implements JsonDeserializer<OutputObj> {
    private final String timezoneList_key = "Time Series (Daily)";
    		//"Time Series (5min)";

    @Override
    public OutputObj deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    	OutputObj obj = new OutputObj();
    	
    	
        JsonObject object = json.getAsJsonObject().getAsJsonObject(timezoneList_key);
        Map<String, Timezone> retMap = new Gson().fromJson(object, new TypeToken<HashMap<String, Timezone>>() {}.getType());

    
        List<Timezone> list = new ArrayList<Timezone>(retMap.values());
        obj.timezoneList = retMap;
      
        
        object = json.getAsJsonObject().getAsJsonObject("Meta Data");
        Metadata metadata =  new Gson().fromJson(object, new TypeToken<Metadata>() {}.getType());

        obj.metaData = metadata;
        return obj;
    }

	
}
