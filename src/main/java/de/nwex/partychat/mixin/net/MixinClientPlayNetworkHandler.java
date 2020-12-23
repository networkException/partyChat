package de.nwex.partychat.mixin.net;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import de.nwex.partychat.PartyChat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.command.CommandSource;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.CommandTreeS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.ChatUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class MixinClientPlayNetworkHandler {

    @Shadow
    private CommandDispatcher<CommandSource> commandDispatcher;

    @SuppressWarnings("unchecked")
    @Inject(method = "<init>", at = @At("RETURN"))
    public void onInit(MinecraftClient mc, Screen screen, ClientConnection connection, GameProfile profile, CallbackInfo ci) {
        PartyChat.registerCommands((CommandDispatcher<ServerCommandSource>) (Object) commandDispatcher);
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "onCommandTree", at = @At("TAIL"))
    public void onOnCommandTree(CommandTreeS2CPacket packet, CallbackInfo ci) {
        PartyChat.registerCommands((CommandDispatcher<ServerCommandSource>) (Object) commandDispatcher);
    }

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void onChatMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        String message = ChatUtil.stripTextFormat(packet.getMessage().getString().replace("\n", ""));

        if (message.equals("Chat set to party")) {
            PartyChat.inPartyChat = true;

            if (PartyChat.ignorePartyChatMessages) {
                ci.cancel();
                return;
            }

            PartyChat.showPartyChatIndicator = true;
        }

        if (message.equals("Chat set to public")) {
            PartyChat.inPartyChat = false;

            if (PartyChat.ignorePartyChatMessages) {
                ci.cancel();
                return;
            }

            PartyChat.showPartyChatIndicator = false;
        }

        MinecraftClient.getInstance().inGameHud.addChatMessage(
            packet.getLocation(),
            packet.getMessage(),
            packet.getSenderUuid()
        );

        ci.cancel();
    }
}
