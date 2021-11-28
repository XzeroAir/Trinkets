package xzeroair.trinkets.items.trinkets;

import java.util.UUID;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.base.AccessoryBase;
import xzeroair.trinkets.traits.abilities.AbilityWellRested;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.config.trinkets.ConfigTeddyBear;

public class TrinketTeddyBear extends AccessoryBase {

	public static final ConfigTeddyBear serverConfig = TrinketsConfig.SERVER.Items.TEDDY_BEAR;

	public TrinketTeddyBear(String name) {
		super(name);
		this.setUUID("33b34669-715d-4caa-a31e-9c643c52ba66");
		this.setItemAttributes(serverConfig.Attributes);
		ModItems.trinkets.ITEMS.add(this);
	}

	@Override
	public void initAbilities(EntityLivingBase entity) {
		if (serverConfig.sleep_bonus) {
			this.addAbility(entity, Abilities.wellRested, new AbilityWellRested());
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		String crafter = "";
		if (!this.getTagCompoundSafe(stack).getString("crafter.name").isEmpty()) {
			crafter = this.getTagCompoundSafe(stack).getString("crafter.name") + "'s ";
		}
		return crafter + super.getItemStackDisplayName(stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		super.onUpdate(stack, world, entity, itemSlot, isSelected);
		if (this.getTagCompoundSafe(stack).getString("crafter.id").isEmpty()) {
			if (entity instanceof EntityPlayer) {
				if (((EntityPlayer) entity).getUniqueID() != null) {
					this.getTagCompoundSafe(stack).setString("crafter.id", ((EntityPlayer) entity).getUniqueID().toString());
				}
			}
		} else {
			final EntityPlayer player = entity.world.getPlayerEntityByUUID(UUID.fromString(this.getTagCompoundSafe(stack).getString("crafter.id")));
			if ((player != null) && this.getTagCompoundSafe(stack).getString("crafter.name").isEmpty()) {
				this.getTagCompoundSafe(stack).setString("crafter.name", player.getDisplayNameString());
			}
		}
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		super.eventPlayerTick(stack, player);
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		super.playerEquipped(stack, player);
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return serverConfig.enabled;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
		final ModelResourceLocation rembo = new ModelResourceLocation(this.getRegistryName().toString() + "_rembos", "inventory");
		final ModelResourceLocation scary = new ModelResourceLocation(this.getRegistryName().toString() + "_scary", "inventory");
		ModelBakery.registerItemVariants(this, normal, scary, rembo);
		ModelLoader.setCustomMeshDefinition(this, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				final String name = stack.getDisplayName().toLowerCase();
				if (TrinketTeddyBear.this.getTagCompoundSafe(stack).getString("crafter.id").equalsIgnoreCase("6b5d5e9b-1fe8-4c61-a043-1d84ce95765d") || name.contains("rembo") || name.contains("cool") || name.contains("badass")) {
					return rembo;
				} else if (name.contains("scary") || name.contains("freddy") || name.contains("snuggles")) {
					return scary;
				} else {
					return normal;
				}
			}
		});
	}
}