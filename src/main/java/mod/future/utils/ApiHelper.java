package mod.future.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class ApiHelper {

    public static String key = "42ee7dd3-1c71-443f-a619-3c089f6441e5";

    public static JsonObject getResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String input;
                while ((input = in.readLine()) != null)
                    response.append(input);
                in.close();
                Gson gson = new Gson();
                return gson.fromJson(response.toString(), JsonObject.class);
            }

            if (urlString.startsWith("https://api.hypixel.net/")) {
                InputStream errorStream = conn.getErrorStream();
                try (Scanner scanner = new Scanner(errorStream)) {
                    scanner.useDelimiter("\\Z");
                    String error = scanner.next();
                    Gson gson = new Gson();
                    return gson.fromJson(error, JsonObject.class);
                }
            }

            if (urlString.startsWith("https://api.mojang.com/users/profiles/minecraft/") && conn.getResponseCode() == 204) {
                System.out.println("Player does not exist");
            } else {
                System.out.println("General Failure: " + conn.getResponseCode());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static JsonObject getJsonResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            conn.setRequestProperty("accept", "application/json");
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String input;
            while ((input = in.readLine()) != null)
                response.append(input);
            in.close();
            Gson gson = new Gson();
            return gson.fromJson(response.toString(), JsonObject.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static boolean checkIfPlayerIsReal(String name) {
        JsonObject hypixelResponse = getJsonResponse("https://api.hypixel.net/v2/player?name=" + name + "&key=" + key);

        if (hypixelResponse == null) {
            System.out.println("Failed to get response from Hypixel API");
            return false;
        }
        if (!hypixelResponse.get("success").getAsBoolean()) {
            System.out.println("API request failed for player: " + name);
            return false;
        }
        if (hypixelResponse.get("player").isJsonNull()) {
            System.out.println("Player not found: " + name);
            return false;
        }
        return true;
    }

    public static String getName(String uuid) {
        JsonObject nameResponse = getResponse("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid.replaceAll("-", ""));
        return nameResponse == null ? null : nameResponse.getAsJsonObject().get("name").getAsString();
    }

    public static String getUUID(String name) {
        JsonObject mojangResponse = getJsonResponse("https://api.mojang.com/users/profiles/minecraft/" + name);
        if (mojangResponse == null || !mojangResponse.has("id")) {
            return null;
        }
        return mojangResponse.get("id").getAsString();
    }


    public static void checkIfPlayerIsRealAsync(String name, Consumer<Boolean> callback) {
        new Thread(() -> {
            boolean isReal = checkIfPlayerIsReal(name);
            callback.accept(isReal);
        }).start();
    }



}
