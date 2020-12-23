package de.nwex.partychat.command.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import de.nwex.partychat.PartyChat;
import de.nwex.partychat.command.ClientCommand;
import de.nwex.partychat.util.chat.Chat;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class SendCommand extends ClientCommand {

    @Override
    protected void register() {
        argumentBuilder
            .then(CommandManager.argument("message", StringArgumentType.greedyString())
                .executes(this::execute));
    }

    @Override
    protected String rootLiteral() {
        return "send";
    }

    private int execute(CommandContext<ServerCommandSource> context) {
        new Thread(() -> {
            boolean hadPartyChat = PartyChat.inPartyChat;

            if (!hadPartyChat) {
                PartyChat.ignorePartyChatMessages = true;
                Chat.send("/parties:party chat on");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Chat.send(StringArgumentType.getString(context, "message"));

            if (!hadPartyChat) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Chat.send("/parties:party chat off");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                PartyChat.ignorePartyChatMessages = false;
            }
        }).start();

        return 1;
    }
}
