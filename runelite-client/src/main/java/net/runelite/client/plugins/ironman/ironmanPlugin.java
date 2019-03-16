package net.runelite.client.plugins.ironman;

import com.google.common.base.Strings;
import com.google.inject.Provides;
import javax.annotation.Nullable;
import javax.inject.Inject;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.MessageNode;
import net.runelite.api.events.ChatMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.HiscoreManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.Text;

@PluginDescriptor(description = "irony", enabledByDefault = false, name = "Ironman overheads", tags = {"iron,ironman,overhead"})
public class ironmanPlugin extends Plugin
{
	String CONFIG_GROUP = "ironman";
	String KEY_PREFIX = "iron_";
	@Inject
	private Client client;
	@Inject
	private ConfigManager configManager;
	@Inject
	private HiscoreManager hiscoreManager;
	@Inject
	private ironmanConfig ironmanConfig;
	private int isLocalAIron = 0;
	@Inject
	private ironmanOverlay overlay;
	@Inject
	private OverlayManager overlayManager;

	public void startUp()
	{
		this.overlayManager.add(this.overlay);
	}

	public void shutDown()
	{
		this.overlayManager.remove(this.overlay);
	}

	@Provides
	private ironmanConfig provideConfig(ConfigManager configManager)
	{
		return (ironmanConfig) configManager.getConfig(ironmanConfig.class);
	}

	private void setIron(String displayName, String type)
	{
		if (Strings.isNullOrEmpty(type))
		{
			this.configManager.unsetConfiguration(this.CONFIG_GROUP, this.KEY_PREFIX + displayName);
			System.out.println("removing " + displayName);
			return;
		}
		String check = this.configManager.getConfiguration(this.CONFIG_GROUP, this.KEY_PREFIX + displayName);
		if (check == null)
		{
			this.configManager.setConfiguration(this.CONFIG_GROUP, this.KEY_PREFIX + displayName, type);
			System.out.println("adding " + displayName + " type " + type + " ch " + check);
		}
	}

	private void addIronman(String name, String type)
	{
		setIron(name, type);
	}

	@Nullable
	public String getIron(String name)
	{
		return this.configManager.getConfiguration(this.CONFIG_GROUP, this.KEY_PREFIX + name);
	}

	@Subscribe
	public void onChatMessage(ChatMessage chatMessage)
	{
		MessageNode msg = chatMessage.getMessageNode();
		if (msg != null && msg.getValue() != null && msg.getType() != null)
		{
			if (msg.getType() == ChatMessageType.PUBLIC || msg.getType() == ChatMessageType.CLANCHAT || msg.getType() == ChatMessageType.PRIVATE_MESSAGE_RECEIVED)
			{
				if (msg.getName().contains("<img=2>"))
				{
					addIronman(Text.standardize(msg.getName().toLowerCase()), "2");
				}
				if (msg.getName().contains("<img=10>"))
				{
					addIronman(Text.standardize(msg.getName().toLowerCase()), "10");
				}
				if (msg.getName().contains("<img=3>"))
				{
					addIronman(Text.standardize(msg.getName().toLowerCase()), "3");
				}
			}
		}
	}
}