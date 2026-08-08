package xzeroair.trinkets.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.main.GameConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public abstract class TestMixin {
    @Inject(
            method = "<init>", // constructor
            at = @At("TAIL") // end of method body
    )
    private void trinkets_logAfterCreatingMinecraft(GameConfiguration gameConfig, CallbackInfo ci){
        System.out.println("This is a pretty early log message");
    }
}
