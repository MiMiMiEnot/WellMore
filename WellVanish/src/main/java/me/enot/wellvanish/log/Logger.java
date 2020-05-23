package me.enot.wellvanish.log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import me.enot.wellvanish.WellVanish;
import org.bukkit.Location;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.List;

public class Logger {

    private String playerName;
    private Long date;
    private Long vanishTime;

    private transient Location location;

    public Logger(String playerName, Long date, Location location) {
        this.playerName = playerName;
        this.date = date;
        this.location = location;
    }

    public String getPlayerName() {
        return playerName;
    }

    public Long getDate() {
        return date;
    }

    public Long getVanishTime() {
        return vanishTime;
    }

    public Location getLocation() {
        return location;
    }

    public void log() {
        this.vanishTime = System.currentTimeMillis() - this.date;
        File directory = new File(WellVanish.getPlugin().getDataFolder(), "/data/");
        if (!directory.exists()) directory.mkdirs();
        File playerFile = new File(directory, playerName + ".json");
        if (playerFile.exists()) {
            try {
                final Type TYPE = new TypeToken<List<Logger>>() {
                }.getType();
                Gson json = new Gson();
                JsonReader reader = new JsonReader(new FileReader(playerFile));
                List<Logger> data = json.fromJson(reader, TYPE);
                data.add(this);
                json.toJson(data, new FileWriter(playerFile));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
