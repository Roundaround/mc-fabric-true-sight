package me.roundaround.truesight.mixin;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import me.roundaround.truesight.TrueSightMod;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {
  @Shadow
  @Final
  private MinecraftClient client;

  @Inject(method = "processF3", at = @At(value = "RETURN"), cancellable = true)
  private void aboveWorldRendererReload(int key, CallbackInfoReturnable<Boolean> info) {
    if (!info.getReturnValue() && key == GLFW.GLFW_KEY_M) {
      TrueSightMod.enabled = !TrueSightMod.enabled;
      ((KeyboardAccessor) this).invokeDebugLog(TrueSightMod.enabled ? "truesight.on" : "truesight.off");
      info.setReturnValue(true);
    }
  }

  @Inject(method = "processF3", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/ChatHud;addMessage(Lnet/minecraft/text/Text;)V", shift = At.Shift.AFTER, ordinal = 8))
  private void onDebugHelp(int key, CallbackInfoReturnable<Boolean> info) {
    ChatHud chatHud = this.client.inGameHud.getChatHud();
    chatHud.addMessage(Text.translatable("truesight.debug.help"));
  }
}
