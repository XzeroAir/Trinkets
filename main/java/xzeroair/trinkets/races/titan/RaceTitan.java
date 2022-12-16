<<<<<<< Updated upstream
package xzeroair.trinkets.races.titan;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.traits.abilities.other.AbilityHeavy;
import xzeroair.trinkets.traits.abilities.other.AbilityLargeHands;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceTitan extends EntityRacePropertiesHandler {

	public static final TitanConfig serverConfig = TrinketsConfig.SERVER.races.titan;
	public static List<String> disallowedMounts = Arrays.asList(serverConfig.mountBlacklist);

	//	private UpdatingAttribute speed, attack;

	public RaceTitan(@Nonnull EntityLivingBase e) {
		super(e, EntityRaces.titan);
		disallowedMounts = Arrays.asList(serverConfig.mountBlacklist);
	}

	@Override
	public void startTransformation() {
		this.addAbility(new AbilityLargeHands());
		if (serverConfig.sink) {
			this.addAbility(new AbilityHeavy());
		}
	}

	@Override
	public void whileTransformed() {
		if (!entity.world.isRemote && entity.isRiding()) {
			final Entity mount = entity.getRidingEntity();
			if ((mount != null) && !this.mountEntity(mount)) {
				entity.dismountRidingEntity();
			}
		}
	}

	@Override
	public boolean mountEntity(Entity mount) {
		if (this.isCreativePlayer()) {
			return true;
		} else if (!serverConfig.canMount) {
			return false;
		} else if (!disallowedMounts.isEmpty()) {
			try {
				final ResourceLocation regName = EntityRegistry.getEntry(mount.getClass()).getRegistryName();
				final String modID = regName.getNamespace();
				final String entityID = regName.getPath();
				final boolean doesWildcardExist = disallowedMounts.contains(modID + ":*");
				final boolean exists = disallowedMounts.contains(regName.toString());
				if (doesWildcardExist || exists) {
					return serverConfig.whitelist;
				} else {
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			return !serverConfig.whitelist;
		} else {
			return true;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
		super.doRenderPlayerPre(entity, x, y, z, renderer, partialTick);
		if (entity.limbSwingAmount > 0) {
			entity.limbSwingAmount -= 0.04F;
		}
	}

}
=======
package xzeroair.trinkets.races.titan;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.traits.abilities.other.AbilityHeavy;
import xzeroair.trinkets.traits.abilities.other.AbilityLargeHands;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceTitan extends EntityRacePropertiesHandler {

	public static final TitanConfig serverConfig = TrinketsConfig.SERVER.races.titan;
	public static List<String> disallowedMounts = Arrays.asList(serverConfig.mountBlacklist);

	//	private UpdatingAttribute speed, attack;

	public RaceTitan(@Nonnull EntityLivingBase e) {
		super(e, EntityRaces.titan);
		disallowedMounts = Arrays.asList(serverConfig.mountBlacklist);
	}

	@Override
	public void startTransformation() {
		this.addAbility(new AbilityLargeHands());
		if (serverConfig.sink) {
			this.addAbility(new AbilityHeavy());
		}
	}

	@Override
	public void whileTransformed() {
		if (!entity.world.isRemote && entity.isRiding()) {
			final Entity mount = entity.getRidingEntity();
			if ((mount != null) && !this.mountEntity(mount)) {
				entity.dismountRidingEntity();
			}
		}
	}

	@Override
	public boolean mountEntity(Entity mount) {
		if (this.isCreativePlayer()) {
			return true;
		} else if (!serverConfig.canMount) {
			return false;
		} else if (!disallowedMounts.isEmpty()) {
			try {
				final ResourceLocation regName = EntityRegistry.getEntry(mount.getClass()).getRegistryName();
				final String modID = regName.getNamespace();
				final String entityID = regName.getPath();
				final boolean doesWildcardExist = disallowedMounts.contains(modID + ":*");
				final boolean exists = disallowedMounts.contains(regName.toString());
				if (doesWildcardExist || exists) {
					return serverConfig.whitelist;
				} else {
				}
			} catch (final Exception e) {
				e.printStackTrace();
			}
			return !serverConfig.whitelist;
		} else {
			return true;
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderPlayerPre(EntityPlayer entity, double x, double y, double z, RenderPlayer renderer, float partialTick) {
		super.doRenderPlayerPre(entity, x, y, z, renderer, partialTick);
		if (entity.limbSwingAmount > 0) {
			entity.limbSwingAmount -= 0.04F;
		}
	}

}
>>>>>>> Stashed changes
