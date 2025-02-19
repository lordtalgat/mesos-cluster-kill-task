package kz.talgat;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ActionRequest {
    public ActionRequest() {
    }

    public JsonObject setKillTask() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "KILL");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public String setTearDown(String frameworkId) {
        return "frameworkId=" + frameworkId;
    }
}
