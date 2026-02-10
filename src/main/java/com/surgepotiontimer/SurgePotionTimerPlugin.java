package com.surgepotiontimer;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.gameval.ItemID;
import net.runelite.api.events.GameTick;
import net.runelite.api.gameval.VarbitID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.infobox.InfoBoxManager;

@Slf4j
@PluginDescriptor(
	name = "Surge Potion Timer",
	description = "Better surge potion timer, accounts for world lag and accurate in ToB",
	tags = {"surge", "potion", "timer", "better", "accurate", "tob"}
)
public class SurgePotionTimerPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private SurgePotionTimerConfig config;

	@Inject
	private InfoBoxManager infoBoxManager;

	@Inject
	private ItemManager itemManager;

	private SurgePotionInfoBox infoBox;

	boolean surgePotioned;
	int surgePotionInTicks = -1;
	int prevSurgePotionCycles = 0;

	@Override
	protected void startUp() throws Exception
	{
		infoBox = new SurgePotionInfoBox(client, this, config);
		infoBox.setImage(itemManager.getImage(ItemID._4DOSESURGE));
		infoBoxManager.addInfoBox(infoBox);
	}

	@Override
	protected void shutDown() throws Exception
	{
		surgePotioned = false;
		surgePotionInTicks = -1;
		prevSurgePotionCycles = 0;
		infoBoxManager.removeInfoBox(infoBox);
		infoBox = null;
	}

	@Provides
	SurgePotionTimerConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(SurgePotionTimerConfig.class);
	}

	@Subscribe
	public void onGameTick(GameTick gameTick)
	{
		final int surgePotionCycles = client.getVarbitValue(VarbitID.SURGE_POTION_TIMER);
		if (surgePotionCycles > 0)
		{
			surgePotioned = true;
			if (surgePotionCycles != prevSurgePotionCycles)
			{
				prevSurgePotionCycles = surgePotionCycles;
				surgePotionInTicks = surgePotionCycles * 10;
			}

			final boolean tobActive = client.getVarbitValue(6440) > 1;
			final boolean tobRoomActive = client.getVarbitValue(6447) > 0;
			if (!tobActive || tobRoomActive)
			{
				--surgePotionInTicks;
			}
		}
		else
		{
			surgePotioned = false;
			prevSurgePotionCycles = 0;
		}
	}

	public static String to_mmss(int ticks)
	{
		int m = ticks / 100;
		int s = (ticks - m * 100) * 6 / 10;
		return m + (s < 10 ? ":0" : ":") + s;
	}

	public static String to_mmss_precise_short(int ticks)
	{
		int min = ticks / 100;
		int tmp = (ticks - min * 100) * 6;
		int sec = tmp / 10;
		int sec_tenth = tmp - sec * 10;
		return min + (sec < 10 ? ":0" : ":") + sec + "." +
				sec_tenth;
	}
}
