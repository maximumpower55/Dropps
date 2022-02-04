package me.maximumpower55.dropps.config;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.jetbrains.annotations.NotNull;

import me.maximumpower55.dropps.DroppsMod;
import net.fabricmc.loader.api.FabricLoader;

public class DroppsConfig {
	private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("dropps.json");

	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private final DroppsMod mod;

	public DroppsConfig(@NotNull DroppsMod mod) {
		this.mod = mod;
	}

	public final void load() {
		if(!Files.exists(PATH)) {
			save();
		} else {
			try {
				JsonReader reader = GSON.newJsonReader(Files.newBufferedReader(PATH));

				LinkedHashMap<String, Object> configAsMap = GSON.fromJson(reader, LinkedHashMap.class);

				throwVelocity = (double)configAsMap.get("throwVelocity");
				itemMerge = (boolean)configAsMap.get("itemMerge");
				autoPickup = (boolean)configAsMap.get("autoPickup");

				reader.close();
			} catch(Exception e) {
				mod.logger.error("Failed to load config", e);
			} finally {
				mod.logger.info("Config Loaded.");
			}
		}
	}

	public final void save() {
		try {
			JsonWriter writer = GSON.newJsonWriter(Files.newBufferedWriter(PATH));

			LinkedHashMap<String, Object> configAsMap = new LinkedHashMap<>();

			configAsMap.put("throwVelocity", throwVelocity);
			configAsMap.put("itemMerge", itemMerge);
			configAsMap.put("autoPickup", autoPickup);

			GSON.toJson(GSON.toJsonTree(configAsMap, LinkedHashMap.class), writer);

			writer.close();
		} catch(Exception e) {
			mod.logger.error("Failed to save config", e);
		} finally {
			mod.logger.info("Config Saved.");
		}
	}

	public double throwVelocity = 1.5;

	public boolean itemMerge = false;

	public boolean autoPickup = true;
}
