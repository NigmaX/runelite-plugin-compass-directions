package com.compassletters;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Compass Letters",
	description = "Adds E, S and W to the minimap compass, styled like the game's N",
	tags = {"compass", "minimap", "north", "east", "south", "west"}
)
public class CompassLettersPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private CompassLettersOverlay overlay;

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
	}

	@Provides
	CompassLettersConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CompassLettersConfig.class);
	}
}
