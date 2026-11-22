package com.compassdirections;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CompassDirectionsPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CompassDirectionsPlugin.class);
		RuneLite.main(args);
	}
}
