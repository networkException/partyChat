package de.nwex.partychat.mixin.render;

import com.mojang.blaze3d.systems.RenderSystem;
import de.nwex.partychat.PartyChat;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public abstract class MixinChatHud {

    @Shadow @Final private MinecraftClient client;

    @Shadow public abstract double getChatScale();

    @Inject(method = "render", at = @At("RETURN"))
    private void render(MatrixStack matrices, int tickDelta, CallbackInfo ci) {
        if (PartyChat.showPartyChatIndicator) {
            double e = this.client.options.chatOpacity * 0.8999999761581421D + 0.10000000149011612D;
            double d = this.getChatScale();

            RenderSystem.pushMatrix();
            RenderSystem.translatef(2.0F, 8.0F, 0.0F);
            RenderSystem.scaled(d, d, 1.0D);

            int m = (int) (128.0D * e);
            matrices.push();
            RenderSystem.enableBlend();
            matrices.translate(0.0D, 0.0D, 50.0D);
            this.client.textRenderer
                .drawWithShadow(matrices, new LiteralText("You are in party chat").styled(style -> style.withColor(Formatting.GREEN)), 0.0F, 1.0F,
                    16777215 + (m << 24));
            matrices.pop();
            RenderSystem.disableAlphaTest();
            RenderSystem.disableBlend();

            RenderSystem.popMatrix();
        }
    }
}
