package xzeroair.trinkets.vip;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.util.Reference;

public class VIPHandler {

    private static TreeMap<String, VipUser> Vips = new TreeMap<>();
    private boolean VIPLoadAttempted = false;
    public static VIPHandler instance = new VIPHandler();

    public void popVIPList() {
        if (!this.VIPLoadAttempted) {
            this.VIPLoadAttempted = true;
            this.loadJsonFromUrl(Reference.VIP_LIST);
        }
    }

    private void loadJsonFromUrl(String url) {
        final TreeMap<String, VipUser> loaded = new TreeMap<>();
        int skipped = 0;
        try {
            final URL link = new URL(url);
            if (!"https".equalsIgnoreCase(link.getProtocol())) {
                throw new IllegalArgumentException("VIP source must use HTTPS");
            }
            final URLConnection connection = link.openConnection();
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(15000);
            try (InputStream stream = connection.getInputStream(); InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
                final JsonElement element = new JsonParser().parse(reader);
                if (!element.isJsonObject()) {
                    throw new IllegalArgumentException("V3 root is not an object");
                }
                final JsonObject root = element.getAsJsonObject();
                if (!root.has("schemaVersion") || root.get("schemaVersion").getAsInt() != 3 || !root.has("users") || !root.get("users").isJsonObject() || !root.has("groups") || !root.get("groups").isJsonArray()) {
                    throw new IllegalArgumentException("unsupported VIP document");
                }
                final TreeMap<Integer, JsonObject> groups = groups(root.getAsJsonArray("groups"));
                final List<Integer> defaultIds = ids(object(root, "defaults"), "groupIds", Collections.singletonList(1), groups);
                if (groups.isEmpty() || defaultIds.isEmpty()) {
                    throw new IllegalArgumentException("missing usable default group");
                }
                for (Map.Entry<String, JsonElement> entry : root.getAsJsonObject("users").entrySet()) {
                    try {
                        final String key = entry.getKey().replace("-", "").toLowerCase();
                        if (!key.matches("[0-9a-f]{32}") || !entry.getValue().isJsonObject()) {
                            throw new IllegalArgumentException();
                        }
                        final JsonObject raw = entry.getValue().getAsJsonObject();
                        final String id = string(raw, "id", dashed(key)).replace("-", "").toLowerCase();
                        if (!key.equals(id) || !id.matches("[0-9a-f]{32}")) {
                            throw new IllegalArgumentException();
                        }
                        final List<Integer> groupIds = ids(raw, "groupIds", defaultIds, groups);
                        final JsonObject primaryGroup = groups.get(groupIds.get(0));
                        final List<String> userQuotes = strings(raw, "quotes");
                        List<String> groupQuotes = strings(raw, "groupQuotes");
                        if (groupQuotes.isEmpty()) {
                            groupQuotes = strings(primaryGroup, "quotes");
                        }
                        final VipUser user = new VipUser(dashed(key), string(raw, "username", key));
                        user.setQuotes(userQuotes.isEmpty() ? groupQuotes : userQuotes);
                        final VipPackage vipGroup = new VipPackage(string(primaryGroup, "name", "Vips"), groupIds.get(0));
                        vipGroup.setGroupQuotes(groupQuotes);
                        // Current gameplay uses one primary group; remaining ordered IDs are reserved for future GUI behavior.
                        user.setGroups(Collections.singletonList(vipGroup));
                        loaded.put(key, user);
                    } catch (Exception ignored) {
                        skipped++;
                    }
                }
            }
            Vips = loaded;
            Trinkets.LOGGER.info("Trinkets and Baubles: Loaded {} VIP entries from V3; skipped {} invalid entries", loaded.size(), skipped);
        } catch (Exception e) {
            Trinkets.LOGGER.warn("Trinkets and Baubles: VIP V3 list unavailable or invalid; cosmetics disabled for this session");
        }
    }

    private static TreeMap<Integer, JsonObject> groups(JsonArray rawGroups) {
        final TreeMap<Integer, JsonObject> result = new TreeMap<>();
        for (JsonElement element : rawGroups) {
            if (!element.isJsonObject()) {
                continue;
            }
            final JsonObject group = element.getAsJsonObject();
            try {
                final int id = group.get("id").getAsInt();
                if (!result.containsKey(id)) {
                    result.put(id, group);
                }
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    private static List<Integer> ids(JsonObject object, String key, List<Integer> fallback, TreeMap<Integer, JsonObject> groups) {
        final List<Integer> result = new ArrayList<>();
        if (object.has(key) && object.get(key).isJsonArray()) {
            for (JsonElement value : object.getAsJsonArray(key)) {
                try {
                    final int id = value.getAsInt();
                    if (groups.containsKey(id) && !result.contains(id)) {
                        result.add(id);
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return result.isEmpty() ? new ArrayList<>(fallback) : result;
    }

    private static JsonObject object(JsonObject object, String key) {
        return object.has(key) && object.get(key).isJsonObject() ? object.getAsJsonObject(key) : new JsonObject();
    }

    private static String string(JsonObject object, String key, String fallback) {
        try {
            return object.has(key) && object.get(key).isJsonPrimitive() ? object.get(key).getAsString() : fallback;
        } catch (Exception ignored) {
            return fallback;
        }
    }

    private static List<String> strings(JsonObject object, String key) {
        final List<String> result = new ArrayList<>();
        if (object.has(key) && object.get(key).isJsonArray()) {
            for (JsonElement item : object.getAsJsonArray(key)) {
                if (item.isJsonPrimitive() && item.getAsJsonPrimitive().isString()) {
                    result.add(item.getAsString());
                }
            }
        }
        return result;
    }

    private static String dashed(String key) {
        return key.substring(0, 8) + "-" + key.substring(8, 12) + "-" + key.substring(12, 16) + "-" + key.substring(16, 20) + "-" + key.substring(20);
    }

    public TreeMap<String, VipUser> getVips() {
        return Vips;
    }

    public boolean IsVipLoaded() {
        return this.VIPLoadAttempted;
    }
}
