package xzeroair.trinkets.events;

import net.minecraft.advancements.Advancement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

/**
 * Reveals the optional Patchouli guide branch after the player earns the
 * Patchouli-independent Glowing Ingot advancement.
 */
public class GuideAdvancementHandler {

    private static final String PATCHOULI = "patchouli";
    private static final ResourceLocation REFINED_RADIANCE = new ResourceLocation("xat", "refined_radiance");
    private static final ResourceLocation PATCHOULI_GUIDE = new ResourceLocation("xat", "patchouli_guide");
    private static final String GUIDE_AVAILABLE = "guide_available";

    @SubscribeEvent
    public void onPlayerLogin(PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            grantGuideAdvancement((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.phase == Phase.END && !event.player.world.isRemote && event.player.ticksExisted % 20 == 0 && event.player instanceof EntityPlayerMP) {
            grantGuideAdvancement((EntityPlayerMP) event.player);
        }
    }

    private void grantGuideAdvancement(EntityPlayerMP player) {
        if (!Loader.isModLoaded(PATCHOULI)) {
            return;
        }

        Advancement refinedRadiance = player.getServer().getAdvancementManager().getAdvancement(REFINED_RADIANCE);
        Advancement patchouliGuide = player.getServer().getAdvancementManager().getAdvancement(PATCHOULI_GUIDE);
        if (refinedRadiance == null || patchouliGuide == null || !player.getAdvancements().getProgress(refinedRadiance).isDone()) {
            return;
        }

        if (!player.getAdvancements().getProgress(patchouliGuide).isDone()) {
            player.getAdvancements().grantCriterion(patchouliGuide, GUIDE_AVAILABLE);
        }
    }
}
