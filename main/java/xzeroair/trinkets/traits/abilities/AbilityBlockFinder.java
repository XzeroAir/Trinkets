package xzeroair.trinkets.traits.abilities;

import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.client.particles.ParticleGreed;
import xzeroair.trinkets.enums.TargetOreType;
import xzeroair.trinkets.init.Abilities;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.traits.abilities.interfaces.IKeyBindInterface;
import xzeroair.trinkets.traits.abilities.interfaces.ITickableAbility;
import xzeroair.trinkets.traits.abilities.interfaces.IToggleAbility;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.OreDictionaryCompat;
import xzeroair.trinkets.util.config.ClientConfig.ClientConfigItems.ClientConfigDragonsEye;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.EntryType;
import xzeroair.trinkets.util.config.ConfigHelper.TreasureEntry;
import xzeroair.trinkets.util.config.trinkets.ConfigDragonsEye;
import xzeroair.trinkets.util.handlers.Counter;
import xzeroair.trinkets.util.helpers.RayTraceHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper.OptionEntry;

public class AbilityBlockFinder extends Ability implements ITickableAbility, IToggleAbility, IKeyBindInterface {

	protected final ConfigDragonsEye serverConfig = TrinketsConfig.SERVER.Items.DRAGON_EYE;
	protected final ClientConfigDragonsEye clientConfig = TrinketsConfig.CLIENT.items.DRAGON_EYE;

	protected boolean firstTick = false;

	public AbilityBlockFinder() {
		super(Abilities.blockDetection);
		firstTick = true;
	}

	private int targetValue = -1;

	public int getTargetValue() {
		return targetValue;
	}

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

	protected void IterateBlocks(EntityLivingBase entity, final Vec3d origin, final World world, AxisAlignedBB aabb) {
		final int i = MathHelper.floor(aabb.minX);
		final int j = MathHelper.floor(aabb.maxX + 1.0D);
		final int k = MathHelper.floor(aabb.minY);
		final int l = MathHelper.floor(aabb.maxY + 1.0D);
		final int i1 = MathHelper.floor(aabb.minZ);
		final int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
		final boolean closest = serverConfig.BLOCKS.closest;
		final TreeMap<Double, Vec3d> collection = new TreeMap();
		final TreasureEntry treasure = this.getTreasure();
		if (treasure == null) {
			return;
		} else if (treasure.getObjectRegistryName().contentEquals("*:*")) {
			return;
		}
		for (int k1 = i; k1 < j; ++k1) {
			for (int l1 = k; l1 < l; ++l1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					final Vec3d pos = new Vec3d(k1, l1, i2);
					final BlockPos bPos = new BlockPos(pos);
					final IBlockState state = world.getBlockState(bPos);
					final Block block = state.getBlock();
					if (!(block.isAir(state, world, bPos)) && treasure.doesBlockMatchEntry(state)) {
						double blockDist = pos.distanceTo(origin);
						collection.put(blockDist, pos);
					}
				}
			}
		}
		try {
			final ResourceLocation isEntityRegName = new ResourceLocation(treasure.getObjectRegistryName());
			if (EntityList.isRegistered(isEntityRegName)) {
				final Class<? extends Entity> e = EntityList.getClass(isEntityRegName);
				if (e != null) {
					final List<? extends Entity> entList = world.getEntitiesWithinAABB(e, aabb);
					if (!entList.isEmpty()) {
						for (final Entity targetEntity : entList) {
							final Vec3d pos = targetEntity.getPositionVector();
							final double chestDist = pos.distanceTo(origin);
							collection.put(chestDist, pos);
						}
					}
				}
			}
		} catch (Exception e) {
		}

		if (!collection.isEmpty() && world.isRemote) {
			final Entry<Double, Vec3d> first = collection.firstEntry();
			final double distance = first.getKey();
			if (first.getKey() > 1.8) {
				this.playSound(entity, new BlockPos(first.getValue()), distance);
			}
			if (closest) {
				for (int p1 = 0; p1 < 3; p1++) {
					this.SpawnParticle(entity.getEntityWorld(), first.getValue(), treasure.getColor());
				}
			} else {
				collection.forEach((dist, pos) -> {
					for (int p1 = 0; p1 < 3; p1++) {
						this.SpawnParticle(entity.getEntityWorld(), pos, treasure.getColor());
					}
				});
			}
		}
	}

	@Override
	public void tickAbility(EntityLivingBase entity) {
		if (serverConfig.oreFinder) {
			final int length = serverConfig.BLOCKS.Blocks.length;
			final World world = entity.getEntityWorld();
			if (targetValue > length) {
				targetValue = -1;
			}
			if (targetValue < 0) {
				return;
			}
			final int vd = serverConfig.BLOCKS.DR.C00_VD;
			final int hd = serverConfig.BLOCKS.DR.C001_HD;
			final AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(hd, vd, hd);
			final int rf = clientConfig.Render_Cooldown;
			final Counter counter = tickHandler.getCounter("refresh_rate", rf, true, true, true, false);
			if (firstTick || counter.Tick()) {
				firstTick = false;
				this.IterateBlocks(entity, entity.getPositionVector(), world, aabb);
			}
		}
	}

	public TreasureEntry getTreasure() {
		final TreeMap<Integer, TreasureEntry> TreasureBlocks = ConfigHelper.TrinketConfigStorage.TreasureBlocks;
		return TreasureBlocks.get(targetValue);
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
						player.world.playSound(player, pos, SoundEvents.ENTITY_ENDERDRAGON_GROWL, SoundCategory.PLAYERS, v * volume, 1.0F);
					}
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getKey() {
		final String kb = ModKeyBindings.DRAGONS_EYE_TARGET.getDisplayName();
		return kb;
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
			final int off = -1;
			final int max = size - 1;

			if (!Aux) {
				targetValue++;
			} else {
				targetValue--;
			}
			if (targetValue >= size) {
				targetValue = off;
			} else if (targetValue < off) {
				targetValue = max;
			}

			if (entity instanceof EntityPlayer) {
				if (entity.world.isRemote) {
					TranslationHelper helper = TranslationHelper.INSTANCE;
					final ItemStack stack = new ItemStack(ModItems.trinkets.TrinketDragonsEye);
					final EntityPlayer player = (EntityPlayer) entity;
					//TODO Try to Render with outline
					if ((targetValue != off)) {
						final TreasureEntry entry = this.getTreasure();
						final String target = this.parseTargetName(
								entry
						).trim();
						final String entryRegName = entry == null ? "NULL" : entry.getObjectRegistryName();
						final String NotFound = helper.formatAddVariables(
								new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.notfound").getFormattedText(),
								new OptionEntry(
										"target",
										true,
										entryRegName
								),
								new OptionEntry("looking", true, helper.toggleCheckTranslation(true))
						);
						final String FoundTarget = helper.formatAddVariables(
								new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.on").getFormattedText(),
								new OptionEntry(
										"target",
										true,
										target
								),
								new OptionEntry("looking", true, helper.toggleCheckTranslation(true))
						);
						//								ranslationHelper.translateDragonEyeTarget(FinderOn.getFormattedText(), targetValue);
						final String Message = target.isEmpty() ? NotFound : FoundTarget;
						player.sendStatusMessage(new TextComponentString(Message), true);
					} else { // Is On
						//												final String offMode = TranslationHelper.INSTANCE.formatLangKeys(stack, FinderOff);
						player.sendStatusMessage(
								new TextComponentString(
										helper.formatAddVariables(
												new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.off").getFormattedText(),
												new OptionEntry("looking", true, helper.toggleCheckTranslation(false))
										)
								), true
						);
					}
				}
			}
			return true;
		}
		return false;
	}

	public String parseTargetName(final TreasureEntry treasure) {
		if (treasure == null) {
			//			return "NONE";
			return "";
		}
		try {
			String target = treasure.getObjectRegistryName();
			if (treasure.getObjectType() == EntryType.OREDICTIONARY) {
				return target.replaceFirst("oreDict:", "").replaceAll("(([ ]?[oO][rR][eE])|([\\[\\]])|([tT][iI][lL][eE][\\.]))", "");
			} else if (treasure.getObjectType() == EntryType.ENTITY) {
				try {
					ResourceLocation t = new ResourceLocation(target);
					if (EntityList.isRegistered(t)) {
						//	Class<? extends Entity> ent = EntityList.getClass(t);
						//	return EntityRegistry.getEntry(ent).newInstance(null).getName();
						return new TextComponentTranslation("entity." + EntityList.getTranslationName(t) + ".name").getFormattedText();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (treasure.getObjectType() == EntryType.MATERIAL) {
				//				return "INVALID ENTRY";
				return "";
			} else {
				final String namespace = treasure.getModID();
				final String pathID = treasure.getObjectID();
				final boolean isWildSpace = namespace.contentEquals("*");
				final boolean isWildPath = pathID.contentEquals("*");
				final int meta = treasure.getMeta();
				if (isWildSpace) {
					if (isWildPath) {
						return "";
					} else {
						return pathID;
					}
				} else {
					if (isWildPath) {
						return namespace + " " + new TextComponentTranslation("stat.blocksButton").getFormattedText();
					} else {
						final Item itemTarget = Item.getByNameOrId(target);
						ItemStack parseName = new ItemStack(itemTarget, 1);
						if (itemTarget == null) {
							if (Block.getBlockFromName(target) != null) {
								target = Block.getBlockFromName(target).getRegistryName().toString();
							}
						}
						target = parseName.getTextComponent().getUnformattedText();
						if ((itemTarget != null) && itemTarget.getHasSubtypes() && (meta != OreDictionaryCompat.wildcard)) {
							final NonNullList<ItemStack> parseMeta = NonNullList.create();
							itemTarget.getSubItems(CreativeTabs.SEARCH, parseMeta);
							for (final ItemStack t : parseMeta) {
								if (t.getMetadata() == meta) {
									parseName = new ItemStack(itemTarget, 1, meta);
									target = parseName.getTextComponent().getUnformattedText();
									break;
								}
							}
						}
					}
				}
				if (target.equalsIgnoreCase("[air]")) {
					return "";
				}
				return target
						.replaceAll(
								"("
										+ "([ ]?[oO][rR][eE])"
										+ "|"
										+ "([\\[\\]])"
										+ "|"
										+ "([tT][iI][lL][eE][\\.])"
										+ ")",
								""
						);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	protected int getColor(String name) {
		return TargetOreType.Color(name);
	}

}
