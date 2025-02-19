package kz.talgat;

import javax.json.*;
import java.io.StringReader;

public class TypeRequest {
    public TypeRequest() {

    }

    public JsonObject subscriber() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "SUBSCRIBE");
        JsonObjectBuilder subscribe = Json.createObjectBuilder();
        JsonObjectBuilder framework_info = Json.createObjectBuilder();
        framework_info.add("user", "dmc-dmp");
        framework_info.add("name", "master");
        subscribe.add("framework_info", framework_info);
        jsonObjectBuilder.add("subscribe", subscribe);
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getHealthRequest() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_HEALTH");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject setRoleQuota() {
        String json = "{\"type\":\"\",\"update_quota\":{\"force\":false,\"quota_configs\":[{\"role\":\"test1\",\"limits\":{\"cpus\":{\"value\":10},\"mem\":{\"value\":2048},\"disk\":{\"value\":4096}}},{\"role\":\"test2\",\"limits\":{\"cpus\":{\"value\":1},\"mem\":{\"value\":256},\"disk\":{\"value\":512}}}]}}";
        JsonReader jsonReader = Json.createReader(new StringReader(json));
        JsonObject object = jsonReader.readObject();
        jsonReader.close();
        System.out.println(object.toString());
        return object;
    }

    public JsonObject getFlags() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_FLAGS");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getOffers() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "OFFERS");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getVersion() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_VERSION");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getMetrics() {
        long nanoseconds = 5000000000L;
        JsonObjectBuilder nanosecondsJs = Json.createObjectBuilder();
        nanosecondsJs.add("nanoseconds", nanoseconds);
        JsonObjectBuilder timeout = Json.createObjectBuilder();
        timeout.add("timeout", nanosecondsJs);
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_METRICS")
                .add("get_metrics", timeout);
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getLoggingLevel() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_LOGGING_LEVEL");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject setLoggingLevel(int level) {
        JsonObjectBuilder duration = Json.createObjectBuilder();
        duration.add("nanoseconds", 60000000000L);

        JsonObjectBuilder set_logging_level = Json.createObjectBuilder();
        set_logging_level.add("duration", duration)
                .add("level", level);

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "SET_LOGGING_LEVEL")
                .add("set_logging_level", set_logging_level);
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getState() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_STATE");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getAgents() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_AGENTS");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getFrameworks() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_FRAMEWORKS");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getExecutors() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_EXECUTORS");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getTasks() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_TASKS");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getRoles() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_ROLES");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getWeights() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_WEIGHTS");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject setWeights(String roleName, Double roleWeight) {
        JsonArrayBuilder weight_infos_arr = Json.createArrayBuilder();
        JsonObjectBuilder weight_infos_obj = Json.createObjectBuilder();
        weight_infos_obj.add("role", roleName);
        weight_infos_obj.add("weight", roleWeight);
        weight_infos_arr.add(weight_infos_obj);
        JsonArray jarray = weight_infos_arr.build();

        JsonObjectBuilder update_weights = Json.createObjectBuilder();
        update_weights.add("weight_infos", jarray);

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "UPDATE_WEIGHTS")
                .add("update_weights", update_weights);
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getMaster() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_MASTER");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getReserveResources() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_MASTER");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject setReserveResources(String agentId, String role, String principal, Double cpusScal, Double memScal) {
        JsonArrayBuilder resArrayJson = Json.createArrayBuilder();
        resArrayJson.add(setObjectResources("cpus", principal, role, cpusScal));
        resArrayJson.add(setObjectResources("mem", principal, role, memScal));
        JsonArray jarrayResources = resArrayJson.build();

        JsonObjectBuilder agent_id = Json.createObjectBuilder();
        agent_id.add("agent_id", agentId);

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "RESERVE_RESOURCES")
                .add("reserve_resources", agentId)
                .add("resources", jarrayResources);
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    public JsonObject getQuota() {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("type", "GET_QUOTA");
        JsonObject result = jsonObjectBuilder.build();
        return result;
    }

    private JsonObjectBuilder setObjectResources(String nameSource, String principal, String role, Double value) {
        JsonObjectBuilder res_obj = Json.createObjectBuilder();
        res_obj.add("type", "SCALAR");
        res_obj.add("name", nameSource);
        JsonObjectBuilder reservation = Json.createObjectBuilder();
        reservation.add("principal", principal);
        res_obj.add("reservation", reservation);
        res_obj.add("role", role);
        JsonObjectBuilder scalar = Json.createObjectBuilder();
        scalar.add("value", value);
        res_obj.add("scalar", scalar);
        return res_obj;
    }


}
