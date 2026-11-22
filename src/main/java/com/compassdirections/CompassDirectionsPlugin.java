package com.compassdirections;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "Compass Directions",
	description = "Shows N, E, S and W on the minimap compass",
	tags = {"compass", "minimap", "north", "east", "south", "west"}
)
public class CompassDirectionsPlugin extends Plugin
{
	@Inject
	private OverlayManager overlayManager;

	@Inject
	private CompassDirectionsOverlay overlay;

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
	CompassDirectionsConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CompassDirectionsConfig.class);
	}
}
