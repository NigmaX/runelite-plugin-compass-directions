package com.compassletters;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup(CompassLettersConfig.GROUP)
public interface CompassLettersConfig extends Config
{
	String GROUP = "compassletters";

	@ConfigItem(
		keyName = "showNorth",
		name = "Draw N",
		description = "Also draw N (the game's compass already shows it)",
		position = 0
	)
	default boolean showNorth()
	{
		return false;
	}

	@ConfigItem(
		keyName = "textColor",
		name = "Letter color",
		description = "Color of the E, S and W letters",
		position = 1
	)
	default Color textColor()
	{
		return new Color(20, 20, 20);
	}

	@ConfigItem(
		keyName = "northColor",
		name = "N color",
		description = "Color of the N letter when 'Draw N' is on",
		position = 2
	)
	default Color northColor()
	{
		return new Color(20, 20, 20);
	}

	@ConfigItem(
		keyName = "outline",
		name = "Outline",
		description = "Draw a black outline around the letters",
		position = 5
	)
	default boolean outline()
	{
		return false;
	}

	@Range(min = 6, max = 24)
	@ConfigItem(
		keyName = "fontSize",
		name = "Font size",
		description = "Size of the letters",
		position = 3
	)
	default int fontSize()
	{
		return 11;
	}

	@Range(min = 10, max = 100)
	@ConfigItem(
		keyName = "distance",
		name = "Distance from center (%)",
		description = "How far from the compass center the letters sit, as a percent of the compass radius",
		position = 4
	)
	default int distance()
	{
		return 62;
	}
}
