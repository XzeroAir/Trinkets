package xzeroair.trinkets.items.base;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.init.ModPotionTypes;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.helpers.MagicHelper;

public class BasePotion extends Potion {

	protected ResourceLocation ICON;

	protected String name;
	protected int maxDuration;
	protected int indexX;
	protected int indexY;

	public BasePotion(String name, int duration, int color, boolean isBadEffect) {
		this(Reference.MODID, name, duration, color, isBadEffect, -1, -1, new ResourceLocation(Reference.MODID, "textures/potions/" + name + ".png"));
	}

	public BasePotion(String MODID, String name, int duration, int color, boolean isBadEffect, int IconX, int IconY, ResourceLocation texture) {
		super(isBadEffect, color);
		indexX = IconX;
		indexY = IconY;
		this.name = name;
		ICON = texture;
		maxDuration = duration;
		this.setPotionName(MODID + ".effect." + name);
		this.setRegistryName(new ResourceLocation(MODID, name));
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderInventoryEffect(PotionEffect effect, Gui gui, int x, int y, float z) {
		if (ICON == null)
			return;
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(ICON);
		if ((indexX >= 0) && (indexY >= 0)) {
			//		int i1 = this.getStatusIconIndex();
			int i1 = (indexX + indexY) * 8;//this.getStatusIconIndex();
			Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0 + ((i1 % 8) * 18), 198 + ((i1 / 8) * 18), 18, 18, 256, 256);
		} else {
			Gui.drawModalRectWithCustomSizedTexture(x + 6, y + 7, 0, 0, 18, 18, 18, 18);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void renderHUDEffect(PotionEffect effect, Gui gui, int x, int y, float z, float alpha) {
		if (ICON == null)
			return;
		Minecraft mc = Minecraft.getMinecraft();
		mc.getTextureManager().bindTexture(ICON);
		if ((indexX >= 0) && (indexY >= 0)) {
			//		int i1 = this.getStatusIconIndex();
			int i1 = (indexX + indexY) * 8;//this.getStatusIconIndex();
			Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, (i1 % 8) * 18, 198 + ((i1 / 8) * 18), 18, 18, 256, 256);
		} else {
			Gui.drawModalRectWithCustomSizedTexture(x + 3, y + 3, 0, 0, 18, 18, 18, 18);
		}
	}

	@Override
	public boolean isReady(int duration, int amplifier) {
		return duration <= 1;
	}

	@Override
	public boolean isInstant() {
		return maxDuration <= 0;
	}

	@Override
	public void affectEntity(Entity source, Entity indirectSource, EntityLivingBase entity, int amplifier, double health) {
		Capabilities.getEntityProperties(entity, prop -> {
			if (name.equals(ModPotionTypes.restore)) {
				prop.setImbuedRace(null);
				MagicHelper.refillMana(entity);
			} else {
				if (name.equals(ModPotionTypes.advancedGlowing)) {
					MagicHelper.refillMana(entity);
				} else if (name.equals(ModPotionTypes.enhancedGlittering)) {
					MagicHelper.refillManaByPrecentage(entity, 0.5F);
				} else {
					MagicHelper.refillManaByPrecentage(entity, 0.25F);
				}
			}
		});
		float heals = 0;
		int thirst = 0;
		int saturation = 0;
		if (name.equals(ModPotionTypes.restore) || name.equals(ModPotionTypes.advancedGlowing)) {
			heals = entity.getMaxHealth();
			thirst = 20;
			saturation = 20;
		} else if (name.equals(ModPotionTypes.enhancedGlittering)) {
			entity.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 300, 0, false, false));
			heals = entity.getMaxHealth() * 0.5F;
			thirst = 10;
			saturation = 10;
		} else {
			heals = entity.getMaxHealth() * 0.25F;
			thirst = 5;
			saturation = 0;
		}
		if (heals > 0) {
			entity.heal(heals);
		}
		if (TrinketsConfig.SERVER.Potion.potion_thirst) {
			if (entity instanceof EntityPlayer) {
				SurvivalCompat.addThirst(entity, thirst, saturation);
				SurvivalCompat.clearThirst(entity);
			}
		}
	}

	@Override
	public boolean hasStatusIcon() {
		return ICON != null;
	}
}