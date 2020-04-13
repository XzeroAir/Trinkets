package xzeroair.trinkets.items.trinkets;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.Trinket.TrinketProperties;
import xzeroair.trinkets.items.base.AccessoryBase;

public class TrinketArcingOrb extends AccessoryBase {

	public TrinketArcingOrb(String name) {
		super(name);
	}

	int cd, clock = 0;

	int left, right, front, back = 0;

	boolean reset, trigger = false;

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add("#black: " + TextFormatting.BLACK + "Color");
		tooltip.add("#white: " + TextFormatting.WHITE + "Color");
		tooltip.add("#red: " + TextFormatting.RED + "Color");
		tooltip.add("#darkred: " + TextFormatting.DARK_RED + "Color");
		tooltip.add("#blue: " + TextFormatting.BLUE + "Color");
		tooltip.add("#darkblue: " + TextFormatting.DARK_BLUE + "Color");
		tooltip.add("#green: " + TextFormatting.GREEN + "Color");
		tooltip.add("#darkgreen: " + TextFormatting.DARK_GREEN + "Color");
		tooltip.add("#lightpurple: " + TextFormatting.LIGHT_PURPLE + "Color");
		tooltip.add("#darkpurple: " + TextFormatting.DARK_PURPLE + "Color");
		tooltip.add("#yellow: " + TextFormatting.YELLOW + "Color");
		tooltip.add("#aqua: " + TextFormatting.AQUA + "Color");
		tooltip.add("#darkaqua: " + TextFormatting.DARK_AQUA + "Color");
		tooltip.add("#gray: " + TextFormatting.GRAY + "Color");
		tooltip.add("#darkgray: " + TextFormatting.DARK_GRAY + "Color");
		tooltip.add("#gold: " + TextFormatting.GOLD + "Color");
		tooltip.add("#strikethrough: " + TextFormatting.STRIKETHROUGH + "Color");
		tooltip.add("#underline: " + TextFormatting.UNDERLINE + "Color");
		tooltip.add("#italic: " + TextFormatting.ITALIC + "Color");
		tooltip.add("#bold: " + TextFormatting.BOLD + "Color");
		tooltip.add(TextFormatting.AQUA + "#reset: " + TextFormatting.RESET + "Color");
		//		TranslationHelper.addTooltips(stack, TrinketsConfig.SERVER.TITAN_RING.Attributes, tooltip);
	}

	@Override
	public void eventPlayerTick(ItemStack stack, EntityPlayer player) {
		final boolean client = player.world.isRemote;
		TrinketProperties iCap = Capabilities.getTrinketProperties(stack);
		if (iCap != null) {
			if (client) {

				if ((this.left > 0) || (this.right > 0) || (this.front > 0) || (this.back > 0)) {
					this.clock++;
					if (this.clock >= 10) {
						this.left = 0;
						this.right = 0;
						this.front = 0;
						this.back = 0;
						this.clock = 0;
					}
					System.out.println(this.clock);
				}

				final float yaw = player.rotationYaw;

				final boolean pressedLeft = Minecraft.getMinecraft().gameSettings.keyBindLeft.isPressed();
				final boolean pressedRight = Minecraft.getMinecraft().gameSettings.keyBindRight.isPressed();
				final boolean pressedBack = Minecraft.getMinecraft().gameSettings.keyBindBack.isPressed();
				final boolean pressedForward = Minecraft.getMinecraft().gameSettings.keyBindForward.isPressed();
				if (pressedLeft) {
					this.left++;
					this.right = 0;
					this.front = 0;
					this.back = 0;
				}
				if (pressedRight) {
					this.left = 0;
					this.right++;
					this.front = 0;
					this.back = 0;
				}
				if (pressedForward) {
					this.left = 0;
					this.right = 0;
					this.front++;
					this.back = 0;
				}
				if (pressedBack) {
					this.left = 0;
					this.right = 0;
					this.front = 0;
					this.back++;
				}

				final Vec3d look = player.getLookVec();
				final EnumFacing.AxisDirection direction = player.getHorizontalFacing().getAxisDirection();
				final Axis axis = player.getHorizontalFacing().getAxis();

				double x = 0;
				double z = 0;
				if ((axis == Axis.Z)) {
					if ((direction == AxisDirection.POSITIVE)) {
						x = player.getLookVec().z;
						z = -player.getLookVec().x;
					} else {
						x = player.getLookVec().z;
						z = -player.getLookVec().x;
					}
				} else {
					if ((direction == AxisDirection.POSITIVE)) {
						x = player.getLookVec().z;
						z = -player.getLookVec().x;
					} else {
						x = player.getLookVec().z;
						z = -player.getLookVec().x;
					}
				}
				// System.out.println(axis + " " + direction);
				// System.out.println(left + " " + right + " " + front + " " + back);
				// System.out.println(player.getLookVec().x);
				final double spd = 1.25;
				// Need a Timer to reset to 0 if no double tap after a few seconds
				if (player.onGround) {
					if (this.left > 1) {
						player.setVelocity(look.z * spd, 0.3, -look.x * spd);
						System.out.println("Left Triggered");
						this.left = 0;
					}
					if (this.right > 1) {
						player.setVelocity(-look.z * spd, 0.3, look.x * spd);
						System.out.println("Right Triggered");
						this.right = 0;
					}
					if (this.front > 1) {
						player.setVelocity(look.x * spd, 0.3, look.z * spd);
						System.out.println("Front Triggered");
						this.front = 0;
					}
					if (this.back > 1) {
						player.setVelocity(-look.x * 2, 0.5, -look.z * 2);
						System.out.println("Back Triggered");
						this.back = 0;
					}
				} else {
					this.left = 0;
					this.right = 0;
					this.front = 0;
					this.back = 0;
					this.clock = 0;
				}
			}
		}
	}

	@Override
	public boolean playerCanEquip(ItemStack stack, EntityLivingBase player) {
		if (TrinketHelper.AccessoryCheck(player, stack.getItem())) {
			return false;
		} else {
			return super.playerCanEquip(stack, player);
		}
	}

	@Override
	public void playerEquipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 1.9f);
		super.playerEquipped(stack, player);
	}

	@Override
	public void playerUnequipped(ItemStack stack, EntityLivingBase player) {
		player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND, .75F, 2f);
		super.playerUnequipped(stack, player);
	}

	@Override
	public boolean ItemEnabled() {
		return true; // TrinketsConfig.SERVER.DAMAGE_SHIELD.enabled;
	}

	@Override
	public boolean hasDiscription(ItemStack stack) {
		return true;
	}

}
