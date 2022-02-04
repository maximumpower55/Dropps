package me.maximumpower55.dropps.config.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import org.jetbrains.annotations.Nullable;

import dev.lambdaurora.spruceui.Position;
import dev.lambdaurora.spruceui.background.DirtTexturedBackground;
import dev.lambdaurora.spruceui.option.SpruceDoubleOption;
import dev.lambdaurora.spruceui.option.SpruceToggleBooleanOption;
import dev.lambdaurora.spruceui.screen.SpruceScreen;
import dev.lambdaurora.spruceui.util.RenderUtil;
import dev.lambdaurora.spruceui.widget.SpruceButtonWidget;
import dev.lambdaurora.spruceui.widget.container.SpruceOptionListWidget;
import me.maximumpower55.dropps.DroppsMod;
import me.maximumpower55.dropps.config.DroppsConfig;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class DroppsConfigScreen extends SpruceScreen {
	private final DroppsConfig config = DroppsMod.getInstance().config;

	private final Screen parent;

	private SpruceOptionListWidget list;

	public DroppsConfigScreen(@Nullable Screen parent) {
		super(new TranslatableComponent("title.dropps.config"));

		this.parent = parent;
	}

	@Override
	protected void init() {
		super.init();

		int bottomCenter = width / 2 - 65;

		list = new SpruceOptionListWidget(Position.of(0, 22), width, height - 57);
		list.setBackground(DirtTexturedBackground.DARKENED);

		list.addOptionEntry(new SpruceDoubleOption(
			"option.dropps.throwVelocity",
			1.0, 5.0, .25f,
			() -> config.throwVelocity,
			newValue -> {
				config.throwVelocity = newValue;
			},
			option -> option.getDisplayText(new TextComponent(String.valueOf(config.throwVelocity))),
			null
		), null);

		list.addOptionEntry(new SpruceToggleBooleanOption(
			"option.dropps.itemMerge",
			() -> config.itemMerge,
			newValue -> {
				config.itemMerge = newValue;
			},
			null
		), null);

		list.addOptionEntry(new SpruceToggleBooleanOption(
			"option.dropps.autoPickup",
			() -> config.autoPickup,
			newValue -> {
				config.autoPickup = newValue;
			},
			null
		), null);

		addWidget(list);

		addWidget(new SpruceButtonWidget(Position.of(this, bottomCenter + 69, height - 27), 130, 20, CommonComponents.GUI_DONE, button -> {
			config.save();
			onClose();
		}));
		addWidget(new SpruceButtonWidget(Position.of(this, bottomCenter - 69, height - 27), 130, 20, CommonComponents.GUI_CANCEL, button -> onClose()));
	}

	@Override
	public void renderBackground(PoseStack poseStack) {
		RenderUtil.renderBackgroundTexture(0, 0, width, height, 0, 64, 64, 64, 255);
	}

	@Override
	public void renderTitle(PoseStack poseStack, int mouseX, int mouseY, float delta) {
		drawCenteredString(poseStack, font, title, width / 2, 8, 0xFFFFFF);
	}

	@Override
	public void onClose() {
		minecraft.setScreen(parent);
	}
}
