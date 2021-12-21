package xzeroair.trinkets.traits.abilities;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityMinecartChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.client.keybinds.IKeyBindInterface;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.client.particles.ParticleGreed;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.traits.abilities.base.AbilityBase;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.TrinketsConfig.xClient.TrinketItems.Dragon;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;
import xzeroair.trinkets.util.handlers.TickHandler;
import xzeroair.trinkets.util.helpers.CallHelper;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.helpers.RayTraceHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class AbilityBlockFinder extends AbilityBase implements ITickableAbility, IToggleAbility, IKeyBindInterface {

	private final ConfigDragonsEye serverConfig = TrinketsConfig.SERVER.Items.DRAGON_EYE;
	private final Dragon clientConfig = TrinketsConfig.CLIENT.items.DRAGON_EYE;

	private boolean firstTick = false;

	public AbilityBlockFinder() {
		firstTick = true;
	}

	private final List<Vec3d> list = new ArrayList<>();

	private List<Vec3d> getTargetsList() {
		return list;
	}

	int targetValue = -1;

	@Override
	public boolean abilityEnabled() {
		return targetValue > 0;
	}

	@Override
	public IToggleAbility toggleAbility(boolean enabled) {
		return this;
	}

	@Override
	public IToggleAbility toggleAbility(int value) {
		targetValue = value;
		return this;
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		if (serverConfig.oreFinder) {
			if (!entity.getEntityWorld().isRemote) {
				return;
			}
			final int length = serverConfig.BLOCKS.Blocks.length;
			if (((targetValue < 0) || (targetValue > length))) {
				return;
			}
			final String target = CallHelper.getStringFromArray(serverConfig.BLOCKS.Blocks, targetValue);
			if (target.isEmpty()) {
				return;
			}
			final int vd = serverConfig.BLOCKS.DR.C00_VD;
			final int hd = serverConfig.BLOCKS.DR.C001_HD;
			final int rf = clientConfig.C00_RR;
			if (!this.getTargetsList().isEmpty()) {
				this.getTargetsList().clear();
			}
			final List<Vec3d> TrackingList = this.getTargetsList();//new ArrayList<>();
			final String[] itemConfig = target.replace("[", ";").replace("]", "").split(";");
			final String itemString = CallHelper.getStringFromArray(itemConfig, 0);
			final String metaString = itemString.contains(":") ? CallHelper.getStringFromArray(itemConfig, 1) : "";
			final String colorString = itemString.contains(":") ? CallHelper.getStringFromArray(itemConfig, 2) : CallHelper.getStringFromArray(itemConfig, 1);
			String getName = OreTrackingHelper.translateOreName(itemString, metaString);
			if (getName.endsWith(" Chest")) {
				getName = "Chest";
			}
			int color = OreTrackingHelper.getColor(getName);
			if (!colorString.isEmpty()) {
				try {
					color = Integer.parseInt(colorString);
				} catch (final Exception e) {
				}
			}
			final Vec3d targetBlock = this.getClosestBlock(entity, entity.getEntityBoundingBox().grow(hd, vd, hd), itemString, metaString, TrackingList);
			//			Vec3d targetBlock = getClosestBlock(entity, entity.getEntityBoundingBox().grow(hd, vd, hd), target, getMeta(target));
			//						if ((player.ticksExisted % 8) == 0) {
			//						drawPath(player, target, color);
			//						}
			final TickHandler counter = this.getCounter("refresh_rate", rf, true);
			if (firstTick || counter.Tick()) {
				firstTick = false;
				if (entity.getEntityWorld().isRemote) {
					if (serverConfig.BLOCKS.closest) {
						final double d = entity.getDistance(targetBlock.x, targetBlock.y, targetBlock.z);
						if (d < 1.8) {
							return;
						}
						this.playSound(entity, new BlockPos(targetBlock), d);
						for (int i = 0; i < 3; i++) {
							this.SpawnParticle(entity.getEntityWorld(), targetBlock, color);
						}
					} else {
						if (!TrackingList.isEmpty()) {
							double d = entity.getDistance(targetBlock.x, targetBlock.y, targetBlock.z);
							if (!(d < 1.8)) {
								this.playSound(entity, new BlockPos(targetBlock), 1);
							}
							for (final Vec3d pos : TrackingList) {
								d = entity.getDistance(pos.x, pos.y, pos.z);
								if (!(d < 1.8)) {
									for (int i = 0; i < 3; i++) {
										this.SpawnParticle(entity.getEntityWorld(), pos, color);
									}
								}
							}
							//						getTargetsList().clear();
						}
					}
				}
			}
		}
	}

	private Vec3d chestCartDetect(EntityLivingBase player, Vec3d pos, AxisAlignedBB aabb, List<Vec3d> trackingList) {
		final List<EntityMinecartChest> cartList = player.getEntityWorld().getEntitiesWithinAABB(EntityMinecartChest.class, aabb);
		Vec3d target = pos;
		double d = 100;
		if (!cartList.isEmpty()) {
			for (final EntityMinecartChest chestCart : cartList) {
				final double distance = player.getDistance(chestCart);
				if (distance < d) {
					d = distance;
					target = chestCart.getPositionVector();
					if (!serverConfig.BLOCKS.closest) {
						if (!trackingList.contains(target)) {
							trackingList.add(target);
						}
					}
				}
			}
		}
		return target;
	}

	private Vec3d getClosestBlock(EntityLivingBase player, AxisAlignedBB aabb, String target, String meta, List<Vec3d> trackingList) {
		final int i = MathHelper.floor(aabb.minX);
		final int j = MathHelper.floor(aabb.maxX + 1.0D);
		final int k = MathHelper.floor(aabb.minY);
		final int l = MathHelper.floor(aabb.maxY + 1.0D);
		final int i1 = MathHelper.floor(aabb.minZ);
		final int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
		Vec3d closest = new Vec3d(player.posX, player.posY, player.posZ);
		double d = 100;
		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					final BlockPos pos = new BlockPos(k1, l1, i2);
					final IBlockState state = player.world.getBlockState(pos);
					final Block block = state.getBlock();
					final double distance = player.getDistance(pos.getX(), pos.getY(), pos.getZ());
					if (distance < d) {
						if (!block.getRegistryName().toString().contentEquals("minecraft:air")) {
							if (target.contains(":")) {
								if (meta.isEmpty()) {
									if (block.getRegistryName().toString().contentEquals(target)) {
										if (serverConfig.BLOCKS.closest) {
											d = distance;
										}
										closest = new Vec3d(k1, l1, i2);
										if (!serverConfig.BLOCKS.closest) {
											if (!trackingList.contains(closest)) {
												trackingList.add(closest);
											}
										}
									}
								} else {
									if (block.getRegistryName().toString().contentEquals(target) && !meta.isEmpty()) {
										if (meta.contentEquals("*") || meta.contentEquals("" + OreDictionary.WILDCARD_VALUE) || (block.getMetaFromState(state) == Integer.parseInt(meta))) {
											if (serverConfig.BLOCKS.closest) {
												d = distance;
											}
											closest = new Vec3d(k1, l1, i2);
											if (!serverConfig.BLOCKS.closest) {
												if (!trackingList.contains(closest)) {
													trackingList.add(closest);
												}
											}
										}
									}
								}
							} else {
								final ItemStack blockStack = new ItemStack(block, 1, block.getMetaFromState(state));
								if (!blockStack.isEmpty()) {
									for (final String oreDictionary : OreTrackingHelper.getOreNames(blockStack)) {
										if (oreDictionary.contentEquals(target)) {
											if (serverConfig.BLOCKS.closest) {
												d = distance;
											}
											closest = new Vec3d(k1, l1, i2);
											if (!serverConfig.BLOCKS.closest) {
												if (!trackingList.contains(closest)) {
													trackingList.add(closest);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		// Detecting player as a Chest somewhere here
		if (target.equalsIgnoreCase("minecraft:chest")) {
			final Vec3d closestCart = this.chestCartDetect(player, closest, aabb, trackingList);
			final double distance = player.getDistance(closestCart.x, closestCart.y, closestCart.z);
			if (distance < d) {
				if (serverConfig.BLOCKS.closest) {
					d = distance;
				}
				closest = closestCart;
				if (!serverConfig.BLOCKS.closest) {
					if (!trackingList.contains(closest)) {
						trackingList.add(closest);
					}
				}
			}
		}
		return closest;
	}

	protected boolean isBlockInList(IBlockState state, World world, BlockPos pos) {
		if ((state == null) || (world == null) || (pos == null)) {
			return false;
		}
		final Block block = state.getBlock();
		if (block.isAir(state, world, pos)) {
			return false;
		}
		return false;
	}

	@SideOnly(Side.CLIENT)
	protected void drawPath(EntityLivingBase player, Vec3d target, int color) {
		final double d = player.getDistance(target.x, target.y, target.z);
		if (d > 2) {
			final RayTraceHelper.Beam beam = new RayTraceHelper.Beam(player.world, player, d, 1D, false);
			GlStateManager.pushMatrix();
			RayTraceHelper.drawPath(player.getPositionVector().add(0, player.getEyeHeight() * 0.8, 0), target, player.world, beam, color, 2);
			GlStateManager.popMatrix();
		}
	}

	@SideOnly(Side.CLIENT)
	protected void SpawnParticle(World world, Vec3d pos, int color) {
		final double X = Reference.random.nextDouble() + pos.x;
		final double Y = Reference.random.nextDouble() + pos.y;
		final double Z = Reference.random.nextDouble() + pos.z;
		GlStateManager.pushMatrix();
		final ParticleGreed effect = new ParticleGreed(world, new Vec3d(X, Y, Z), color, 1F, false);
		Minecraft.getMinecraft().effectRenderer.addEffect(effect);
		GlStateManager.popMatrix();
	}

	@SideOnly(Side.CLIENT)
	protected void playSound(EntityLivingBase entity, BlockPos pos, double distance) {
		if (clientConfig.Dragon_Growl) {
			if (entity instanceof EntityPlayer) {
				final EntityPlayer player = (EntityPlayer) entity;
				final boolean sneaking = clientConfig.Dragon_Growl_Sneak.contentEquals("SNEAK") && player.isSneaking();
				final boolean standing = clientConfig.Dragon_Growl_Sneak.contentEquals("STAND") && !player.isSneaking();
				final boolean both = clientConfig.Dragon_Growl_Sneak.contentEquals("BOTH");
				final float configVolume = clientConfig.Dragon_Growl_Volume;
				final float volume = ((configVolume / 100));
				final int drH = serverConfig.BLOCKS.DR.C001_HD;
				final int drV = serverConfig.BLOCKS.DR.C00_VD;
				double test = MathHelper.sqrt((drH * drH) + (drV * drV));
				if (test <= 0) {
					test = 1;
				}
				if (((sneaking && !standing) == true) || ((standing && !sneaking) == true) || (both == true)) {
					float v = 1F;//MathHelper.clamp(((volume * 0.1F) * distance), 0.0F, 3F);
					try {
						v = (float) (1F - (distance / test));
					} catch (final Exception e) {
					}
					if (pos != null) {
						player.world.playSound(player, pos, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.RECORDS, v * volume, 1.0F);
					}
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getKey() {
		return ModKeyBindings.DRAGONS_EYE_TARGET.getDisplayName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getAuxKey() {
		return ModKeyBindings.AUX_KEY.getDisplayName();
	}

	@Override
	public boolean onKeyPress(Entity entity, boolean Aux) {
		if (TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder) {
			final int size = TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks.length;
			final int off = size - size - 1;
			final int max = size - 1;

			if (!Aux) {
				if (targetValue < size) {
					targetValue++;
				}
				if (targetValue == size) {
					targetValue = off;
				}
			} else {
				if (targetValue > (off - 1)) {
					targetValue--;
				}
				if (targetValue == (off - 1)) {
					targetValue = max;
				}
			}
			if (targetValue > size) {
				targetValue = off;
			}
			if (entity instanceof EntityPlayer) {
				if (entity.world.isRemote) {
					final ItemStack stack = new ItemStack(ModItems.trinkets.TrinketDragonsEye);
					final EntityPlayer player = (EntityPlayer) entity;
					final TextComponentTranslation UnkownTarget = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.notfound");
					final TextComponentTranslation FinderOn = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.on");
					final TextComponentTranslation FinderOff = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.off");
					//TODO Try to Render with outline
					if ((targetValue != off)) {
						final String NotFound = TranslationHelper.formatLangKeys(stack, UnkownTarget);
						final String FoundTarget = TranslationHelper.translateDragonEyeTarget(FinderOn.getFormattedText(), targetValue);
						final String Message = FoundTarget.isEmpty() ? NotFound : FoundTarget;
						player.sendStatusMessage(new TextComponentString(Message), true);
					} else { // Is On
						final String offMode = TranslationHelper.formatLangKeys(stack, FinderOff);
						player.sendStatusMessage(new TextComponentString(offMode), true);
					}
				}
			}
			return true;
		}
		return false;
	}

	@Override
	public void loadStorage(NBTTagCompound nbt) {
		//		if (nbt.hasKey("xat.blockfinder")) {
		//			targetValue = nbt.getInteger("xat.blockfinder");
		//		}
	}

	@Override
	public void saveStorage(NBTTagCompound nbt) {
		//		nbt.setInteger("xat.blockfinder", -1);
	}

}
