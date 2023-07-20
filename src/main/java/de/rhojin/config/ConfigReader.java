package de.rhojin.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;

import java.io.InputStream;
import java.io.InputStreamReader;

@NoArgsConstructor(staticName = "create")
public class ConfigReader {

    @SneakyThrows
    public Config read() {
        InputStream is = getClass().getResourceAsStream("/config.json");
        JsonElement jsonElement = JsonParser.parseReader(new InputStreamReader(is));
        return new Gson().fromJson(jsonElement, Config.class);
    }
}
