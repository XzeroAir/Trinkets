package xzeroair.trinkets.client.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;

public class CommandMana extends CommandBase {

	// TODO Move this out of Client, It's a server command, not a client command

	private final List<String> tabCompletionsCommands = Arrays.asList(
			new String[] {
					"help",
					"setMP",
					"setMaxMP"
			}
	);

	private final String commandUsage = "/simpledifficulty help";

	private final String listOfCommands = "   help <command>\n"
			+ "setMP <Amount>\n"
			+ "setMaxMP <Amount>";

	private final String warn_notPlayerAdmin = "You do not have permission, or are not a player ingame!";
	private final String warn_invalidArgs = "Invalid Arguments";
	private final String warn_noItem = "Not holding an item!";
	private final String exportJsonReminder = "(Don't forget to exportJson !)";

	@Override
	public List<String> getAliases() {
		return Arrays.asList(new String[] { "xat" });
	}

	@Override
	public String getName() {
		return "Trinkets and Baubles";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 0;
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return commandUsage;
	}

	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
		if (args.length == 1) {
			return getListOfStringsMatchingLastWord(args, tabCompletionsCommands);
		} else if (args.length == 0) {
			return tabCompletionsCommands;
		} else {
			return Collections.<String>emptyList();
		}
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			this.help(sender);
			return;
		}

		switch (args[0].toLowerCase(Locale.ENGLISH)) {

		case "help":
			this.helpCommand(server, sender, args);
			break;

		default:
			this.help(sender);
			break;
		}

	}

	private void helpCommand(MinecraftServer server, ICommandSender sender, String[] args) {
		if (args.length < 2) {
			this.message(sender, listOfCommands);
			return;
		}

		switch (args[1].toLowerCase()) {
		case "help":
			this.message(
					sender,
					"If you need more help, you can contact the mod author on CurseForge or GitHub"
			);
			return;

		default:
			this.message(sender, "/xat help <command> \n(Replace <command> with a simpledifficulty command name)");
			return;
		}
	}

	private void setMP(MinecraftServer server, ICommandSender sender, String[] args) {
		if (this.isAdminPlayer(sender)) {
			try {
				if (args.length < 2) {
					this.message(sender, warn_invalidArgs + " <modid>");
					return;
				}

				EntityPlayer player = (EntityPlayer) sender.getCommandSenderEntity();
				MagicStats capability = Capabilities.getMagicStats(player);

				capability.setMana(Float.parseFloat(args[1]));

				if (args.length >= 3) {

				}
			} catch (NumberFormatException e) {
				this.message(sender, warn_invalidArgs + " <MP>");
				return;
			}
		}
	}

	private int getMetadataFromStack(ItemStack stack) {
		return stack.getHasSubtypes() ? stack.getMetadata() : -1;
	}

	private String getRegistryName(ItemStack stack) {
		return stack.getItem().getRegistryName().toString();
	}

	private boolean isAdminPlayer(ICommandSender sender) {
		if (this.hasPermissionLevel(sender, 4)) {
			if (sender.getCommandSenderEntity() instanceof EntityPlayer) {
				return true;
			}
		}

		return false;
	}

	private void help(ICommandSender sender) {
		sender.sendMessage(new TextComponentString(this.getUsage(sender)));
	}

	private void message(ICommandSender sender, String message) {
		sender.sendMessage(new TextComponentString(message));
	}

	private boolean hasPermissionLevel(ICommandSender sender, int permLevel) {
		return sender.canUseCommand(permLevel, "Trinkets and Baubles");
	}

	private boolean hasNBTArgument(String[] input) {
		return this.hasArgument("--nbt", input);
	}

	private boolean hasClearArgument(String[] input) {
		return this.hasArgument("--clear", input);
	}

	private boolean hasArgument(String argument, String[] input) {
		if (input == null) {
			return false;
		}

		for (String s : input) {
			if (s.equals(argument)) {
				return true;
			}
		}

		return false;
	}

}
