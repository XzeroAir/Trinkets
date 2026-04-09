package xzeroair.trinkets.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.magic.MagicStats;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.trinketcontainer.OpenTrinketGui;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.Reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class CommandMain extends CommandBase {

    //formatter:off
    private final List<String> tabCompletionsCommands = Arrays.asList("help", "mana", "race");
    //formatter:on

    private final String commandUsage = "/xat help";

    private final String listOfCommands = "   help <command>\n" + "mana set <Amount>\n" + "mana setMax <Amount>";

    private final String warn_notPlayerAdmin = "You do not have permission, or are not a player ingame!";
    private final String warn_invalidArgs = "Invalid Arguments";

    @Override
    public List<String> getAliases() {
        return Collections.singletonList("xat");
    }

    @Override
    public String getName() {
        return "Trinkets and Baubles";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 4;
    }

    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "/xat <PLAYER> <Mana | Race> <ARG>";
    }

    @Override
    public List<String> getTabCompletions(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args, @Nullable BlockPos targetPos) {
        int length = args.length;
        if (length == 5) {
            switch (args[1].toLowerCase(Locale.ENGLISH)) {
                case "race":
                    switch (args[2].toLowerCase(Locale.ENGLISH)) {
                        case "setRace":
                        case "setImbuedRace":
                            switch (args[3].toLowerCase(Locale.ENGLISH)) {
                                default:
                                    return getListOfStringsMatchingLastWord(args, Element.Registry.getKeys());
                            }
                        default:
                            return getListOfStringsMatchingLastWord(args, Element.Registry.getKeys());
                    }
                default:
                    return Collections.emptyList();
            }
        } else if (length == 4) {
            switch (args[1].toLowerCase(Locale.ENGLISH)) {
                case "mana":
                    switch (args[2].toLowerCase(Locale.ENGLISH)) {
                        case "set":
                            return getListOfStringsMatchingLastWord(args, "0", "100", "200", "300");
                        case "resetBonus":
                            return Collections.emptyList();
                        default:
                            return Collections.emptyList();
                    }
                case "race":
                    switch (args[2].toLowerCase(Locale.ENGLISH)) {
                        case "reset":
                        case "resetImbued":
                            return Collections.emptyList();
                        default:
                            return getListOfStringsMatchingLastWord(args, EntityRace.Registry.getKeys());
                    }
                default:
                    return Collections.emptyList();
            }
        } else if (length == 3) {
            switch (args[1].toLowerCase(Locale.ENGLISH)) {
                case "mana":
                    return getListOfStringsMatchingLastWord(args, "refill", "set", "resetBonus");
                case "race":
                    return getListOfStringsMatchingLastWord(args, "setRace", "setImbuedRace", "reset", "resetImbued", "gui");
                default:
                    return Collections.emptyList();
            }
        } else if (length == 2) {
            return getListOfStringsMatchingLastWord(args, "mana", "race");
        } else if (length == 1) {
            return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, String[] args) throws CommandException {
        int length = args.length;
        if (length == 0) {
            this.help(sender);
            return;
        }
        EntityPlayer entityplayer = getPlayer(server, sender, args[0]);
        if (length > 1) {
            switch (args[1].toLowerCase(Locale.ENGLISH)) {
                case "mana":
                    this.doMagic(entityplayer, server, sender, args);
                    break;
                case "race":
                    this.entityRace(entityplayer, server, sender, args);
                    break;
                default:
                    this.help(sender);
                    break;
            }
        } else {
            this.help(sender);
        }
    }

    private void helpCommand(MinecraftServer server, ICommandSender sender, String[] args) {
        if (args.length < 2) {
            this.message(sender, this.listOfCommands);
            return;
        }

        switch (args[1].toLowerCase()) {
            case "help":
                this.message(sender, "If you need more help, you can contact the mod author on CurseForge or GitHub");
                return;

            default:
                this.message(sender, "/xat help <command> \n(Replace <command> with a command name)");
        }
    }

    private void doMagic(EntityPlayer target, MinecraftServer server, ICommandSender sender, String[] args) {
        if (this.isAdminPlayer(sender)) {
            try {
                int i = 0;
                MagicStats capability = Capabilities.getMagicStats(target);
                if (capability != null && args.length > 2) {
                    switch (args[2].toLowerCase(Locale.ENGLISH)) {
                        case "refill":
                            capability.refillMana();
                            break;
                        case "set":
                            if (args.length > 3) {
                                capability.setMana(Float.parseFloat(args[3]));
                            }
                            break;
                        case "resetbonus":
                            capability.setBonusMana(0);
                            break;
                        default:
                            break;
                    }
                }
            } catch (NumberFormatException e) {
                this.message(sender, this.warn_invalidArgs + " <MP>");
            }
        }
    }

    private void entityRace(EntityPlayer target, MinecraftServer server, ICommandSender sender, String[] args) {
        if (this.isAdminPlayer(sender)) {
            try {
                int length = args.length;
                if (length < 2) {
                    this.message(sender, this.warn_invalidArgs + " <modid>");
                    return;
                }

                EntityProperties capability = Capabilities.getEntityProperties(target);

                String entity = args[0];
                if (capability != null && length > 1) {
                    String prefixCommand = args[1];
                    if (length > 2) {
                        String command = args[2].toLowerCase(Locale.ENGLISH);
                        String race = "";
                        if (length > 3) {
                            race = args[3].toLowerCase(Locale.ENGLISH);
                        }
                        EntityRace r = EntityRace.getByNameOrId(race);
                        //						System.out.println("??? " + race + "|" + r + "|" + command);
                        switch (command) {
                            case "setrace":
                                if (r != null) {
                                    String element = "";
                                    if (length > 4) {
                                        element = args[4].toLowerCase(Locale.ENGLISH);
                                        Element e = Element.getByNameOrId(element);
                                        if (e != null) {
                                            capability.setOriginalRaceCache(new RaceCache(r, e));
                                            break;
                                        }
                                    }
                                    capability.setOriginalRaceCache(new RaceCache(r));
                                }
                                break;
                            case "setimbuedrace":
                                if (r != null) {
                                    String element = "";
                                    if (length > 4) {
                                        element = args[4].toLowerCase(Locale.ENGLISH);
                                        Element e = Element.getByNameOrId(element);
                                        if (e != null) {
                                            capability.setOriginalRaceCache(new RaceCache(r, e));
                                            break;
                                        }
                                    }
                                    capability.setImbuedRaceCache(new RaceCache(r));
                                }
                                break;
                            case "reset":
                                capability.setOriginalRaceCache(null);
                                break;
                            case "resetimbued":
                                capability.setImbuedRaceCache(null);
                                break;
                            case "gui":
                                if (target instanceof EntityPlayerMP) {
                                    NetworkHandler.sendTo(new OpenTrinketGui(Reference.GUI_RACE_SELECTION), (EntityPlayerMP) target);
                                }
                                break;
                            default:
                                break;
                        }
                    }
                }
            } catch (NumberFormatException e) {
                this.message(sender, this.warn_invalidArgs + " <MP>");
            }
        }
    }

    private int getMetadataFromStack(@Nonnull ItemStack stack) {
        return stack.getHasSubtypes() ? stack.getMetadata() : -1;
    }

    private String getRegistryName(@Nonnull ItemStack stack) {
        return stack.getItem().getRegistryName().toString();
    }

    private boolean isAdminPlayer(ICommandSender sender) {
        if (this.hasPermissionLevel(sender, 4)) {
            return sender.getCommandSenderEntity() instanceof EntityPlayer;
        }
        return false;
    }

    private void help(@Nonnull ICommandSender sender) {
        sender.sendMessage(new TextComponentString(this.getUsage(sender)));
    }

    private void message(@Nonnull ICommandSender sender, String message) {
        sender.sendMessage(new TextComponentString(message));
    }

    private boolean hasPermissionLevel(@Nonnull ICommandSender sender, int permLevel) {
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
