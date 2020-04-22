package org.automation;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public final class JsonHelper {
    private JsonHelper() {
    }

    public static JSONObject getJsonObject(final String configJsonPath) {
        JSONObject jsonObject = null;
        JSONParser parser = new JSONParser();
        try (InputStream inputStream = new FileInputStream(configJsonPath)) {
            Reader fileReader = new InputStreamReader(inputStream);
            jsonObject = (JSONObject) parser.parse(fileReader);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
