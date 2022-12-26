package com.klarkson.creaternd.content.screens;

import com.klarkson.creaternd.CreateRND;
import com.klarkson.creaternd.content.config.ConfigHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.WarningScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.klarkson.creaternd.api.logging.logCollection;

@Mod.EventBusSubscriber(modid = CreateRND.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CreateLoggingScreen extends WarningScreen {
    private static final Component TITLE = Component.translatable("loggingScreen.creaternd.header").withStyle(ChatFormatting.BOLD);
    private static final Component CONTENT = Component.translatable("loggingScreen.creaternd.message");
    private static final Component CHECK = Component.translatable("loggingScreen.creaternd.check");
    private static final Component NARRATION = TITLE.copy().append("\n").append(CONTENT);
    private final Screen overriddenScreen;

    public CreateLoggingScreen(Screen overriddenScreen) {
        super(TITLE, CONTENT, CHECK, NARRATION);
        this.overriddenScreen = overriddenScreen;
    }

    @SubscribeEvent
    public static void handleGuiOpen(ScreenEvent.Opening screenOpenEvent) {
        if(ConfigHandler.LOGGING.loggingWarningSeen.get()) return;
        if(!(screenOpenEvent.getNewScreen() instanceof TitleScreen titleScreen)) return;

        screenOpenEvent.setNewScreen(new CreateLoggingScreen(screenOpenEvent.getNewScreen()));
        ConfigHandler.LOGGING.loggingWarningSeen.set(true);

    }

    @Override
    protected void initButtons(int lineHeight) {
        this.addRenderableWidget(new Button(this.width / 2 - 75, 100 + lineHeight, 150, 20, CommonComponents.GUI_PROCEED, (button) -> {
            if (this.stopShowing.selected()) {
                ConfigHandler.LOGGING.logging.set(true);
            }

            this.minecraft.setScreen(this.overriddenScreen);
        }));
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
