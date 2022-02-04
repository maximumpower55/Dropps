package me.maximumpower55.dropps.config;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.maximumpower55.dropps.config.gui.screen.DroppsConfigScreen;

public class DroppsModMenuPlugin implements ModMenuApi {
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory() {
		return DroppsConfigScreen::new;
	}
}
