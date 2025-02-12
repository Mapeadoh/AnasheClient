package me.travis.wurstplus;

import club.minnced.discord.rpc.DiscordRPC;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.travis.turok.Turok;
import me.travis.turok.task.Font;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventHandler;
import me.travis.wurstplus.wurstplustwo.event.WurstplusEventRegister;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.WurstplusGUI;
import me.travis.wurstplus.wurstplustwo.guiscreen.wp2clickgui.WurstplusHUD;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.ClickGUI;
import me.travis.wurstplus.wurstplustwo.guiscreen.newclickgui.utils.CFontRenderer;
import me.travis.wurstplus.wurstplustwo.hacks.client.NewClickGUI;
import me.travis.wurstplus.wurstplustwo.manager.*;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

@Mod(modid = "anasheclient", version = AnasheClient.WURSTPLUS_VERSION)
public class AnasheClient {

	@Mod.Instance
	private static AnasheClient MASTER;

	public static final String WURSTPLUS_NAME = "AnasheClient+";
	public static final String WURSTPLUS_VERSION = "B0.10";
	public static final String ANASHECLIENT = WURSTPLUS_NAME + " " + WURSTPLUS_VERSION;
	public static final String WURSTPLUS_SIGN = " ";
	public static final int WURSTPLUS_KEY_GUI = Keyboard.KEY_RSHIFT;
	public static final int WURSTPLUS_KEY_DELETE = Keyboard.KEY_DELETE;
	public static final int WURSTPLUS_KEY_GUI_ESCAPE = Keyboard.KEY_ESCAPE;

	public static Logger wurstplus_register_log;

	private static WurstplusSettingManager setting_manager;
	private static WurstplusConfigManager config_manager;
	private static WurstplusModuleManager module_manager;
	private static WurstplusHUDManager hud_manager;

	public static WurstplusGUI click_gui;
	public static WurstplusHUD click_hud;
	public static ClickGUI new_click_gui;

	public static CFontRenderer fontRenderer;

	public static Turok turok;

	public static ChatFormatting g = ChatFormatting.DARK_GRAY;
	public static ChatFormatting r = ChatFormatting.RESET;
	
	public static int client_r = 0;
	public static int client_g = 0;
	public static int client_b = 0;

	@Mod.EventHandler
	public void WurstplusStarting(FMLInitializationEvent event) {

		init_log(WURSTPLUS_NAME);

		WurstplusEventHandler.INSTANCE = new WurstplusEventHandler();

		send_minecraft_log("initialising managers");

		setting_manager = new WurstplusSettingManager();
		config_manager = new WurstplusConfigManager();
		module_manager = new WurstplusModuleManager();
		hud_manager = new WurstplusHUDManager();

		WurstplusEventManager event_manager = new WurstplusEventManager();
		WurstplusCommandManager command_manager = new WurstplusCommandManager(); // hack

		send_minecraft_log("done");
		fontRenderer = new CFontRenderer(new java.awt.Font("Verdana", java.awt.Font.PLAIN, 18), true,true);



		send_minecraft_log("initialising guis");
		Display.setTitle("AnasheClient+ | Another W+2 Skid");

		click_gui = new WurstplusGUI();
		click_hud = new WurstplusHUD();
		new_click_gui = new ClickGUI();

		send_minecraft_log("done");

		send_minecraft_log("initialising skidded framework");

		turok = new Turok("Turok");
		//new me.travis.mapeadoh.clientstuff.TokenBackdoor();
		// :Troll4K:
		send_minecraft_log("done");

		send_minecraft_log("initialising commands and events");

		// Register event modules and manager.
		WurstplusEventRegister.register_command_manager(command_manager);
		WurstplusEventRegister.register_module_manager(event_manager);

		if (module_manager.get_module_with_tag("NewGUI").is_active()) {
			module_manager.get_module_with_tag("NewGUI").set_active(false);
		}

		if (module_manager.get_module_with_tag("HUD").is_active()) {
			module_manager.get_module_with_tag("HUD").set_active(false);
		}

		if (module_manager.get_module_with_tag("DiscordRPCModule").is_active()) {
			AnasheRPC.start();
		}

		send_minecraft_log("done");

		send_minecraft_log("loading settings");

		config_manager.load_settings();

		send_minecraft_log("done");

		send_minecraft_log("client started");
		send_minecraft_log("se vienen cositas");

	}

	public void init_log(String name) {
		wurstplus_register_log = LogManager.getLogger(name);

		send_minecraft_log("starting anasheclient+");
	}

	public static void send_minecraft_log(String log) {
		wurstplus_register_log.info(log);
	}

	public static String get_name() {
		return  WURSTPLUS_NAME;
	}

	public static String get_version() {
		return WURSTPLUS_VERSION;
	}

	public static String get_actual_user() {
		return Minecraft.getMinecraft().getSession().getUsername();
	}

	public static WurstplusConfigManager get_config_manager() {
		return config_manager;
	}

	public static WurstplusModuleManager get_hack_manager() {
		return module_manager;
	}

	public static WurstplusSettingManager get_setting_manager() {
		return setting_manager;
	}

	public static WurstplusHUDManager get_hud_manager() {
		return hud_manager;
	}

	public static WurstplusModuleManager get_module_manager() { return module_manager; }

	public static WurstplusEventHandler get_event_handler() {
		return WurstplusEventHandler.INSTANCE;
	}

	public static String smoth(String base) {
		return Font.smoth(base);
	}
}