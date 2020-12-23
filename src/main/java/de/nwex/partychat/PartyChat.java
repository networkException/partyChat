package de.nwex.partychat;

import com.mojang.brigadier.CommandDispatcher;
import de.nwex.partychat.command.ClientCommand;
import de.nwex.partychat.command.ClientCommandManager;
import de.nwex.partychat.command.commands.ChatCommand;
import de.nwex.partychat.command.commands.SendCommand;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.server.command.ServerCommandSource;

public class PartyChat implements ClientModInitializer {

    public static boolean inPartyChat = false;
    public static boolean showPartyChatIndicator = false;
    public static boolean ignorePartyChatMessages = false;

    @Override
    public void onInitializeClient() {
    }

    public static void registerCommands(CommandDispatcher<ServerCommandSource> dispatcher) {
        ClientCommandManager.clearClientSideCommands();

        ClientCommand.instantiate(new SendCommand(), dispatcher);
        ClientCommand.instantiate(new ChatCommand(), dispatcher);
    }
}
