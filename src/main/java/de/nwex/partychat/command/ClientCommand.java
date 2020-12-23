package de.nwex.partychat.command;

import static com.mojang.brigadier.builder.LiteralArgumentBuilder.literal;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.server.command.ServerCommandSource;

public abstract class ClientCommand {

    protected LiteralArgumentBuilder<ServerCommandSource> argumentBuilder;
    protected CommandDispatcher<ServerCommandSource> dispatcher;

    public static void instantiate(ClientCommand clientCommand, CommandDispatcher<ServerCommandSource> dispatcher) {
        if (clientCommand.rootLiteral() == null) {
            return;
        }

        String rootLiteral = clientCommand.rootLiteral();
        String finalRootLiteral = clientCommand.rootLiteral();
        boolean sameRootLiteral = dispatcher.getRoot().getChildren().stream()
            .anyMatch(serverCommandSourceCommandNode -> serverCommandSourceCommandNode.getName().equals(finalRootLiteral));

        if (sameRootLiteral) {
            rootLiteral = "c" + rootLiteral;
        }

        ClientCommandManager.addClientSideCommand(rootLiteral);

        clientCommand.argumentBuilder = literal(rootLiteral);

        clientCommand.register();

        dispatcher.register(clientCommand.argumentBuilder);

        clientCommand.dispatcher = dispatcher;
    }

    protected abstract void register();

    protected abstract String rootLiteral();
}
