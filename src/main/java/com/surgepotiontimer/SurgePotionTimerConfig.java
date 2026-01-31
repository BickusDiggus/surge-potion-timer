package com.surgepotiontimer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("surgepotiontimer")
public interface SurgePotionTimerConfig extends Config
{
	@ConfigItem(
		keyName = "surgePotionMode",
		name = "Display mode",
		description = "Configures how the surge potion timer is displayed.",
		position = 1
	)
	default SurgePotionMode surgePotionMode()
	{
		return SurgePotionMode.SECONDS;
	}
}
