package xzeroair.trinkets.client.gui.hud.link;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.traits.abilities.AbilityLinkedVitality;
import xzeroair.trinkets.traits.abilities.interfaces.IAbilityInterface;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.abilities.ConfigAbilityLinkedVitality;

public class BondedHud {

    private static final String LINKED_VITALITY = "xat:linked_vitality";

    private final Minecraft mc;

    public BondedHud(Minecraft mc) {
        this.mc = mc;
    }

    public void render(ScaledResolution resolution) {
        final EntityPlayer player = this.mc.player;
        if (player == null) {
            return;
        }
        final IAbilityInterface ability = Capabilities.getEntityProperties(player, null, (properties, result) -> properties.getAbilityHandler().getAbility(LINKED_VITALITY));
        if (!(ability instanceof AbilityLinkedVitality)) {
            return;
        }
        final AbilityLinkedVitality linkedVitality = (AbilityLinkedVitality) ability;
        final ConfigAbilityLinkedVitality.ClientSettings config = TrinketsConfig.CLIENT.ABILITIES.LINKED_VITALITY;
        if (!config.SHOWN || linkedVitality.getLinkedPartnerUUID() == null || !linkedVitality.isLinkedPartnerActive()) {
            return;
        }
        final int screenWidth = resolution.getScaledWidth();
        final int screenHeight = resolution.getScaledHeight();
        final int x = config.usePixelPosition ? config.xPixels : (int) Math.round(screenWidth * config.translatedX);
        final int y = config.usePixelPosition ? config.yPixels : (int) Math.round(screenHeight * config.translatedY);
        final int renderedX = MathHelper.clamp(x, 0, screenWidth);
        final int renderedY = MathHelper.clamp(y, 0, screenHeight);
        final FontRenderer font = this.mc.fontRenderer;
        font.drawString(linkedVitality.getLinkedPartnerName(), renderedX, renderedY, 0xFFFFFF, true);
        font.drawString(this.formatHealth(linkedVitality.getLinkedPartnerHealth(), linkedVitality.getLinkedPartnerMaximumHealth()), renderedX, MathHelper.clamp(renderedY + font.FONT_HEIGHT + 1, 0, screenHeight), 0xFFFFFF, true);
    }

    private String formatHealth(float health, float maximumHealth) {
        final float percentage = maximumHealth <= 0F ? 0F : health / maximumHealth;
        final TextFormatting color = percentage <= 0.25F ? TextFormatting.RED : percentage <= 0.5F ? TextFormatting.YELLOW : TextFormatting.GREEN;
        return color + I18n.format("hud.xat.bonded_hud.health", MathHelper.ceil(health), MathHelper.ceil(maximumHealth));
    }
}
