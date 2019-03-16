package net.runelite.client.plugins.ironman;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Player;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.game.HiscoreManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.OverlayUtil;

public class ironmanOverlay extends Overlay {
	Client client;
	ConfigManager configManager;
	HiscoreManager hiScoreManager;
	ironmanConfig ironmanConfig;
	ironmanPlugin ironmanPlugin;
	SpriteManager spriteManager;

	@Inject
	private ironmanOverlay(Client client, SpriteManager spriteManager, HiscoreManager hiScoreManager, ironmanConfig ironmanConfig, ConfigManager configManager, ironmanPlugin ironmanPlugin) {
		this.client = client;
		this.spriteManager = spriteManager;
		this.hiScoreManager = hiScoreManager;
		this.ironmanConfig = ironmanConfig;
		this.ironmanPlugin = ironmanPlugin;
		setPosition(OverlayPosition.DYNAMIC);
		setPriority(OverlayPriority.LOW);
		setLayer(OverlayLayer.ABOVE_SCENE);
	}

	public Dimension render(Graphics2D graphics) {
		for (Player player : this.client.getPlayers()) {
			if (!(player == null || player.getWorldLocation().distanceTo(this.client.getLocalPlayer().getWorldLocation()) > 15 || player == this.client.getLocalPlayer())) {
				int returnIron = 0;
				String name = player.getName().toLowerCase();
				if (this.ironmanPlugin.getIron(name) != null) {
					returnIron = Integer.parseInt(this.ironmanPlugin.getIron(name));
				}
				if (returnIron == 2 || returnIron == 3 || returnIron == 10) {
					Point point2 = player.getCanvasTextLocation(graphics, " ", 240);
					WorldPoint point = player.getWorldLocation();
					BufferedImage resizedImage = new BufferedImage(20, 20, this.spriteManager.getSprite(423, returnIron).getType());
					Graphics2D g = resizedImage.createGraphics();
					g.drawImage(this.spriteManager.getSprite(423, returnIron), 0, 0, 20, 20, null);
					g.dispose();
					OverlayUtil.renderImageLocation(graphics, new Point(point2.getX() - 10, point2.getY()), resizedImage);
					OverlayUtil.renderPolygon(graphics, Perspective.getCanvasTileAreaPoly(this.client, LocalPoint.fromWorld(this.client, point), 1), Color.blue);
				}
			}
		}
		return null;
	}
}