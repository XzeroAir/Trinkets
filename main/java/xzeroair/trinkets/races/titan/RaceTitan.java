package xzeroair.trinkets.races.titan;

import javax.annotation.Nonnull;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDeadBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.api.TrinketHelper;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.network.IncreasedAttackRangePacket;
import xzeroair.trinkets.network.IncreasedReachPacket;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.titan.config.TitanConfig;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.RayTraceHelper;

public class RaceTitan extends EntityRacePropertiesHandler {

	public static final TitanConfig serverConfig = TrinketsConfig.SERVER.races.titan;

	public RaceTitan(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.titan);
	}

	@Override
	public void onTick() {
		super.onTick();
		if (cooldown > 0) {
			cooldown--;
		} else {
			cooldown = 0;
		}
	}

	@Override
	public void whileTransformed() {
		boolean flag = (entity instanceof EntityPlayer) && (((EntityPlayer) entity).isCreative() || ((EntityPlayer) entity).isSpectator());
		if (serverConfig.trample) {
			AxisAlignedBB aabb = entity.getEntityBoundingBox().expand(1, 0, 1);
			final int i = MathHelper.floor(aabb.minX);
			final int j = MathHelper.floor(aabb.maxX + 1.0D);
			final int i1 = MathHelper.floor(aabb.minZ);
			final int j1 = MathHelper.floor(aabb.maxZ + 1.0D);
			for (int k1 = i; k1 < j; ++k1) {
				for (int i2 = i1; i2 < j1; ++i2) {
					final BlockPos pos = new BlockPos(k1, entity.getPosition().getY() - 1, i2);
					IBlockState block = entity.world.getBlockState(pos);
					if (block.getBlock() == Blocks.FARMLAND) {
						entity.world.setBlockState(pos, Blocks.DIRT.getDefaultState());
					}
					IBlockState block2 = entity.world.getBlockState(pos.add(0, 1, 0));
					if ((block2.getBlock() instanceof BlockBush)
							|| (block2.getBlock() instanceof BlockDeadBush)) {
						entity.world.destroyBlock(pos.add(0, 1, 0), true);
					}
				}
			}
		}
		if (serverConfig.sink && entity.isInWater() && !TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketSea)) {
			if (entity instanceof EntityPlayer) {
				if (!flag) {
					entity.motionY -= 0.2f;
				}
			} else {
				entity.motionY -= 0.1f;
			}
		}
		if (entity.isRiding()) {
			if (entity.getRidingEntity() instanceof EntityBoat) {
				if (serverConfig.sink && !TrinketHelper.AccessoryCheck(entity, ModItems.trinkets.TrinketSea)) {
					if (!flag) {
						final EntityBoat boat = (EntityBoat) entity.getRidingEntity();
						boat.motionY -= 0.02F;
					}
				}
			} else if (entity.getRidingEntity() instanceof EntityMinecart) {
				entity.dismountRidingEntity();
			}
		}
	}

	float cooldown = 0;

	@Override
	@SideOnly(Side.CLIENT)
	public void interact(PlayerInteractEvent event) {

		EntityPlayer player = event.getEntityPlayer();
		KeyBinding lClick = Minecraft.getMinecraft().gameSettings.keyBindAttack;
		KeyBinding rClick = Minecraft.getMinecraft().gameSettings.keyBindUseItem;
		IAttributeInstance reach = player.getAttributeMap().getAttributeInstance(EntityPlayer.REACH_DISTANCE);
		if ((reach.getAttributeValue() > 5)) {
			RayTraceHelper.Beam beam = new RayTraceHelper.Beam(player.world, player, reach.getAttributeValue() * 0.8, 1D, true);
			RayTraceHelper.rayTraceEntity(beam, target -> {
				if (target != null) {
					if (lClick.isKeyDown() && (cooldown == 0)) {
						NetworkHandler.INSTANCE.sendToServer(new IncreasedAttackRangePacket(player, target, Capabilities.getEntityRace(player)));
						cooldown = player.getCooldownPeriod();
						RayTraceHelper.drawAttackSweep(player.world, beam.getEnd(), EnumParticleTypes.SWEEP_ATTACK);
					} else if (rClick.isKeyDown()) {
						NetworkHandler.INSTANCE.sendToServer(new IncreasedReachPacket(player, target, target.getPositionVector()));
					}
					return true;
				} else {
					return false;
				}
			});
		}
	}

	@Override
	public void blockBroken(BreakEvent event) {
		if (!serverConfig.miningExtend || ((serverConfig.miningExtendInverted && !event.getPlayer().isSneaking()) || (!serverConfig.miningExtendInverted && event.getPlayer().isSneaking()))) {
			return;
		}
		if (!(entity instanceof EntityPlayer)) {
			return;
		}
		BlockPos pos1 = event.getPos().add(-1, -1, -1);
		BlockPos pos2 = event.getPos().add(1, 1, 1);
		final ItemStack heldItemStack = entity instanceof EntityPlayer ? ((EntityPlayer) entity).inventory.getCurrentItem() : entity.getActiveItemStack();

		Iterable<BlockPos> blockList = BlockPos.getAllInBox(pos1, pos2);
		blockList.forEach(block -> {
			World world = event.getWorld();
			this.shouldHarvestBlock((EntityPlayer) entity, heldItemStack, world, event.getState(), event.getPos(), block);
		});

	}

	public void shouldHarvestBlock(EntityPlayer entity, ItemStack harvestTool, World world, IBlockState harvestedBlockState, BlockPos harvestedPos, BlockPos targetPos) {
		IBlockState targetBlockState = world.getBlockState(targetPos);
		if (harvestedPos.equals(targetPos)) {
			return;
		}
		boolean redstone1 = ((targetBlockState.getBlock() == Blocks.REDSTONE_ORE) || (targetBlockState.getBlock() == Blocks.LIT_REDSTONE_ORE));
		boolean redstone2 = ((harvestedBlockState.getBlock() == Blocks.LIT_REDSTONE_ORE) || (harvestedBlockState.getBlock() == Blocks.REDSTONE_ORE));
		if (targetBlockState.getBlock() != harvestedBlockState.getBlock()) {
			if (redstone2) {
				if (redstone1) {
				} else {
					return;
				}
			} else {
				return;
			}
		}
		if (ForgeHooks.canHarvestBlock(targetBlockState.getBlock(), entity, world, targetPos)) {
			targetBlockState.getBlock().harvestBlock(world, entity, targetPos, targetBlockState, world.getTileEntity(targetPos), harvestTool);
			world.setBlockToAir(targetPos);
		}
		//Taken out of Forgehook.canHarvestBlock()
		//        IBlockState state = world.getBlockState(pos);
		//        state = state.getBlock().getActualState(state, world, pos);
		//        if (state.getMaterial().isToolNotRequired())
		//        {
		//            return true;
		//        }
		//
		//        ItemStack stack = player.getHeldItemMainhand();
		//        String tool = block.getHarvestTool(state);
		//        if (stack.isEmpty() || tool == null)
		//        {
		//            return player.canHarvestBlock(state);
		//        }
		//
		//        int toolLevel = stack.getItem().getHarvestLevel(stack, tool, player, state);
		//        if (toolLevel < 0)
		//        {
		//            return player.canHarvestBlock(state);
		//        }
		//
		//        return toolLevel >= block.getHarvestLevel(state);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderPlayerPre(RenderPlayerEvent.Pre event) {
		if (entity.isRiding()) {
			double t = 0;
			t = (100 - properties.getSize()) * 0.01D;
			double t1 = (entity.height * 100) / (1.8 * 100);
			double t2 = (1.8 * 100) / (entity.height * 100);
			t *= t2 * 0.5D;
			GlStateManager.translate(0, t, 0);
		}
	}

}
