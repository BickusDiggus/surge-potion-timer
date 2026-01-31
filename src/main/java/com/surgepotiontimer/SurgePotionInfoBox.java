package com.surgepotiontimer;

import net.runelite.api.Client;
import net.runelite.client.ui.overlay.infobox.InfoBox;

import javax.inject.Inject;
import java.awt.Color;
import net.runelite.client.ui.overlay.infobox.InfoBoxPriority;

public class SurgePotionInfoBox extends InfoBox
{

	private final SurgePotionTimerPlugin plugin;
	private final SurgePotionTimerConfig config;

	@Inject
	public SurgePotionInfoBox(Client client, SurgePotionTimerPlugin plugin, SurgePotionTimerConfig config)
	{
		super(null, plugin);
		this.plugin = plugin;
		this.config = config;
		setPriority(InfoBoxPriority.MED);
	}

	@Override
	public String getText()
	{
		String str;
		if (config.surgePotionMode() == SurgePotionMode.TICKS)
		{
			str = String.valueOf(plugin.surgePotionInTicks);
		}
		else if (config.surgePotionMode() == SurgePotionMode.DECIMALS)
		{
			str = SurgePotionTimerPlugin.to_mmss_precise_short(plugin.surgePotionInTicks);
		}
		else
		{
			str = SurgePotionTimerPlugin.to_mmss(plugin.surgePotionInTicks);
		}
		return str;
	}

	@Override
	public Color getTextColor()
	{
		if (plugin.surgePotionInTicks < 25)
		{
			return Color.RED;
		}
		return Color.WHITE;
	}

	@Override
	public String getTooltip()
	{
		return "Surge potion";
	}
}
