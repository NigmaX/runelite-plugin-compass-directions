package com.compassdirections;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.api.widgets.Widget;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

class CompassDirectionsOverlay extends Overlay
{
	/** Compass widgets for fixed, resizable classic and resizable modern layouts. */
	private static final int[] COMPASS_WIDGETS = {
		InterfaceID.Toplevel.COMPASSCLICK,
		InterfaceID.ToplevelOsrsStretch.COMPASSCLICK,
		InterfaceID.ToplevelPreEoc.COMPASSCLICK,
	};

	/** How far north of the player (in local units, 128 per tile) to probe the minimap. */
	private static final int NORTH_PROBE = 4 * 128;

	private final Client client;
	private final CompassDirectionsConfig config;

	@Inject
	CompassDirectionsOverlay(Client client, CompassDirectionsConfig config)
	{
		this.client = client;
		this.config = config;
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_WIDGETS);
		setPriority(PRIORITY_HIGH);
	}

	@Override
	public Dimension render(Graphics2D g)
	{
		Widget compass = findCompass();
		if (compass == null)
		{
			return null;
		}

		Rectangle bounds = compass.getBounds();
		double cx = bounds.getCenterX();
		double cy = bounds.getCenterY();
		double radius = Math.min(bounds.width, bounds.height) / 2.0 * config.distance() / 100.0;

		double[] north = northOnScreen();
		if (north == null)
		{
			return null;
		}
		// Screen y grows downward, so east is north turned 90 degrees clockwise.
		double nx = north[0];
		double ny = north[1];
		double ex = -ny;
		double ey = nx;

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Font font = FontManager.getRunescapeBoldFont().deriveFont((float) config.fontSize());
		g.setFont(font);

		// Every letter is turned like the dial's N: its top points north.
		// Rotating "up" (0, -1) by theta gives (sin theta, -cos theta), so theta = atan2(nx, -ny).
		double rotation = Math.atan2(nx, -ny);

		Color color = config.textColor();
		boolean outline = config.outline();
		if (config.showNorth())
		{
			drawLetter(g, "N", cx + nx * radius, cy + ny * radius, rotation, config.northColor(), outline);
		}
		drawLetter(g, "E", cx + ex * radius, cy + ey * radius, rotation, color, outline);
		drawLetter(g, "S", cx - nx * radius, cy - ny * radius, rotation, color, outline);
		drawLetter(g, "W", cx - ex * radius, cy - ey * radius, rotation, color, outline);

		return null;
	}

	/**
	 * Unit vector pointing north on screen, taken from where RuneLite places a
	 * point north of the player on the minimap, so it always matches the minimap's
	 * rotation (and the game's compass).
	 */
	private double[] northOnScreen()
	{
		Player player = client.getLocalPlayer();
		if (player == null)
		{
			return null;
		}
		LocalPoint here = player.getLocalLocation();
		if (here == null)
		{
			return null;
		}
		LocalPoint ahead = new LocalPoint(here.getX(), here.getY() + NORTH_PROBE, here.getWorldView());
		Point a = Perspective.localToMinimap(client, here);
		Point b = Perspective.localToMinimap(client, ahead);
		if (a == null || b == null)
		{
			return null;
		}
		double dx = b.getX() - a.getX();
		double dy = b.getY() - a.getY();
		double len = Math.hypot(dx, dy);
		if (len < 1e-6)
		{
			return null;
		}
		return new double[]{dx / len, dy / len};
	}

	private Widget findCompass()
	{
		for (int id : COMPASS_WIDGETS)
		{
			Widget w = client.getWidget(id);
			if (w != null && !w.isHidden())
			{
				return w;
			}
		}
		return null;
	}

	/** Draws a letter centered on (x, y), rotated by {@code rotation} radians. */
	private static void drawLetter(Graphics2D g, String text, double x, double y, double rotation,
		Color color, boolean outline)
	{
		GlyphVector gv = g.getFont().createGlyphVector(g.getFontRenderContext(), text);
		Rectangle2D glyph = gv.getVisualBounds();

		AffineTransform t = new AffineTransform();
		t.translate(x, y);
		t.rotate(rotation);
		t.translate(-glyph.getCenterX(), -glyph.getCenterY());
		Shape shape = t.createTransformedShape(gv.getOutline());

		if (outline)
		{
			g.setColor(Color.BLACK);
			g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
			g.draw(shape);
		}
		g.setColor(color);
		g.fill(shape);
	}
}
