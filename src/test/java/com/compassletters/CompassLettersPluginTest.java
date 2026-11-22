package com.compassletters;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CompassLettersPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CompassLettersPlugin.class);
		RuneLite.main(args);
	}
}
