package me.travis.wurstplus.wurstplustwo.hacks;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

public enum WurstplusCategory {
	WURSTPLUS_CHAT ("Chat", "WurstplusChat", false),
	WURSTPLUS_COMBAT ("PvP", "WurstplusCombat", false),
	WURSTPLUS_MOVEMENT ("Movement", "WurstplusMovement", false),
	WURSTPLUS_RENDER ("Render", "WurstplusRender", false),
	WURSTPLUS_EXPLOIT ("Exploit", "WurstplusExploit", false),
	WURSTPLUS_MISC ("Misc", "WurstplusMisc", false),
	WURSTPLUS_CLIENT ("Client", "WurstplusClient", false),
	WURSTPLUS_BETA ("Beta", "WurstplusBeta", false),
	WURSTPLUS_HIDDEN ("Hidden", "WurstplusHidden", true);




	String name;
	String tag;
	boolean hidden;

	WurstplusCategory(String name, String tag, boolean hidden) {
		this.name   = name;
		this.tag    = tag;
		this.hidden = hidden;
	}

	public boolean is_hidden() {
		return this.hidden;
	}

	public String get_name() {
		return this.name;
	}

	public String get_tag() {
		return this.tag;
	}
}