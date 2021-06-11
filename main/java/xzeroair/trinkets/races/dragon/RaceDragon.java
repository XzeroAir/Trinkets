package xzeroair.trinkets.races.dragon;

import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.client.keybinds.KeyHandler;
import xzeroair.trinkets.client.keybinds.ModKeyBindings;
import xzeroair.trinkets.entity.MovingThrownProjectile;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.init.ModItems;
import xzeroair.trinkets.items.effects.EffectsDragonsEye;
import xzeroair.trinkets.network.NetworkHandler;
import xzeroair.trinkets.network.keybinds.KeyPressPacket;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.dragon.config.DragonConfig;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.compat.SurvivalCompat;
import xzeroair.trinkets.util.helpers.DrawingHelper;
import xzeroair.trinkets.util.helpers.OreTrackingHelper;
import xzeroair.trinkets.util.helpers.TranslationHelper;

public class RaceDragon extends EntityRacePropertiesHandler {

	public static final DragonConfig serverConfig = TrinketsConfig.SERVER.races.dragon;

	public RaceDragon(@Nonnull EntityLivingBase e, EntityProperties properties) {
		super(e, properties, EntityRaces.dragon);
	}

	@Override
	public void startTransformation() {
	}

	@Override
	public void endTransformation() {
		this.removeFlyingAbility();
	}

	private void addFlyingAbility() {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (!player.isCreative() && (serverConfig.creative_flight == true)) {
				if ((player.capabilities.allowFlying != true)) {
					player.capabilities.allowFlying = true;
					if (serverConfig.creative_flight_speed && player.world.isRemote) {
						player.capabilities.setFlySpeed((float) serverConfig.flight_speed);
					}
				}
			}
		}
	}

	private void removeFlyingAbility() {
		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;
			if (!player.isCreative() && (serverConfig.creative_flight == true)) {
				if ((player.capabilities.allowFlying == true)) {
					player.capabilities.isFlying = false;
					player.capabilities.allowFlying = false;
					if (serverConfig.creative_flight_speed && player.world.isRemote) {
						if (player.capabilities.getFlySpeed() != 0.05F) {
							player.capabilities.setFlySpeed(0.05f);
						}
					}
				}
			}
		}
	}

	@Override
	public void whileTransformed() {
		if (entity instanceof EntityPlayer) { // Start Player Effects
			if (properties.getKeyPressed() == 0) {
				NightVision = !NightVision;
			}
			if (!(serverConfig.breath_damage == 0)) {
				if (properties.getKeyPressed() == 2) {
					this.DragonBreath();
				} else {
					if (breathStage != 0) {
						breathStage = 0;
					}
				}
			}
			EntityPlayer player = (EntityPlayer) entity;
			if (properties.showTraits()) {
				this.addFlyingAbility();
			} else {
				this.removeFlyingAbility();
			}
			if (NightVision) {
				player.addPotionEffect(new PotionEffect(MobEffects.NIGHT_VISION, 210, 0, false, false));
				if (player.isPotionActive(MobEffects.NIGHT_VISION)) {
					if (entity.world.isRemote) {
						player.getActivePotionEffect(MobEffects.NIGHT_VISION).setPotionDurationMax(true);
					}
				}
			} else {
				if (player.isPotionActive(MobEffects.NIGHT_VISION)) {
					player.removePotionEffect(MobEffects.NIGHT_VISION);
				}
			}
			PotionEffect fireResist = new PotionEffect(MobEffects.FIRE_RESISTANCE, 150, 0, false, false);
			player.addPotionEffect(fireResist);
			if (player.isPotionActive(MobEffects.FIRE_RESISTANCE)) {
				if (entity.world.isRemote) {
					player.getActivePotionEffect(MobEffects.FIRE_RESISTANCE).setPotionDurationMax(true);
				}
			}
			if (player.isBurning()) {
				player.extinguish();
			}
			if (serverConfig.compat.tan.immuneToHeat) {
				SurvivalCompat.immuneToHeat(player);
			}
			if (player.world.isRemote) {
				EffectsDragonsEye.playerTicks(FinderTarget, player);
			}
		} else { // End player effects
		}
	}

	double breathStage = 0;

	public void DragonBreath() {

		if (breathStage > 2) {
			breathStage = 0;
		}
		if (breathStage == 0) {
			if (!properties.getMagic().spendMana(serverConfig.breath_cost)) {
				return;
			}
			int bcolor = properties.getTraitColorHandler().getDecimal();
			EntityLivingBase entity = this.entity;
			World world = entity.world;
			float headPosX = (float) (entity.posX + (1.8F * 1 * 0.3F * Math.cos(((entity.rotationYaw + 90) * Math.PI) / 180)));
			float headPosZ = (float) (entity.posZ + (1.8F * 1 * 0.3F * Math.sin(((entity.rotationYaw + 90) * Math.PI) / 180)));
			float headPosY = (float) ((entity.posY + (entity.getEyeHeight() * 0.8)));// - 0.10000000149011612D);//(float) (entity.posY + (1.8F * 1 * 0.3F));
			double d2 = entity.getLookVec().x;
			double d3 = entity.getLookVec().y;
			double d4 = entity.getLookVec().z;
			//			float inaccuracy = 0.25F;
			//			d2 = d2 + (Reference.random.nextGaussian() * 0.007499999832361937D * inaccuracy);
			//			d3 = d3 + (Reference.random.nextGaussian() * 0.007499999832361937D * inaccuracy);
			//			d4 = d4 + (Reference.random.nextGaussian() * 0.007499999832361937D * inaccuracy);
			world.playSound((EntityPlayer) null, entity.posX, entity.posY, entity.posZ, SoundEvents.ENTITY_ENDERDRAGON_SHOOT, SoundCategory.PLAYERS, 0.5F, 0.4F / ((Reference.random.nextFloat() * 0.4F) + 0.8F));
			if (!world.isRemote) {
				MovingThrownProjectile breath = new MovingThrownProjectile(world, entity, d2, d3, d4, bcolor);
				breath.setPosition(headPosX, headPosY, headPosZ);
				breath.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, 1.5F, 1.0F);
				world.spawnEntity(breath.setColor(bcolor));
			}
		}
		breathStage += 1;
	}

	boolean NightVision = false;
	int FinderTarget = -1;
	KeyHandler Aux;
	KeyHandler DE_NV;
	KeyHandler DE_Target;
	KeyHandler Dragon_Breath;

	@Override
	@SideOnly(Side.CLIENT)
	public void onClientTick() {
		if (!(entity instanceof EntityPlayer) || !FMLClientHandler.instance().getClient().inGameHasFocus) {
			return;
		}
		if (Aux == null) {
			Aux = new KeyHandler(ModKeyBindings.AUX_KEY);
		}
		if (DE_NV == null) {
			DE_NV = new KeyHandler(ModKeyBindings.DRAGONS_EYE_ABILITY);
		}
		if (DE_Target == null) {
			DE_Target = new KeyHandler(ModKeyBindings.DRAGONS_EYE_TARGET);
		}
		if (Dragon_Breath == null) {
			Dragon_Breath = new KeyHandler(ModKeyBindings.RACE_ABILITY);
		}
		EntityPlayer player = (EntityPlayer) entity;
		DE_NV.handler(true, press -> {
			int auxDown = 0;
			if (Aux.isDown()) {
				auxDown = 1;
			}
			properties.KeyPressed(0);
			NetworkHandler.INSTANCE.sendToServer(new KeyPressPacket(player, 0, auxDown));
			return true;
		}, null, null);
		if (TrinketsConfig.SERVER.Items.DRAGON_EYE.oreFinder != false) {
			final int size = TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks.length;
			final int off = size - size - 1;
			final int max = size - 1;

			DE_Target.handler(
					true,
					press -> {
						if (!Aux.isDown()) {
							if (FinderTarget < size) {
								FinderTarget++;
							}
							if (FinderTarget == size) {
								FinderTarget = off;
							}
						} else {
							if (FinderTarget > (off - 1)) {
								FinderTarget--;
							}
							if (FinderTarget == (off - 1)) {
								FinderTarget = max;
							}
						}
						if (FinderTarget > size) {
							FinderTarget = off;
						}
						ItemStack stack = new ItemStack(ModItems.trinkets.TrinketDragonsEye);
						TextComponentTranslation UnkownTarget = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.notfound");
						TextComponentTranslation FinderOn = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.on");
						TextComponentTranslation FinderOff = new TextComponentTranslation(stack.getTranslationKey() + ".treasurefinder.off");
						if ((FinderTarget != off)) {
							String Type = TrinketsConfig.SERVER.Items.DRAGON_EYE.BLOCKS.Blocks[FinderTarget];
							String getName = "Air";
							if (Type.contains(":") || Type.contains("[") || Type.contains("]")) {
								Type = Type.toLowerCase();
								getName = OreTrackingHelper.translateOreName(Type);
							} else {
								Type = Type.replace("ore", "");
								final String first = Type.substring(0, 1).toUpperCase();
								final String second = Type.substring(1).toLowerCase();
								getName = first + second;
							}
							String translatedTarget = OreTrackingHelper.translateOreName(Type);
							NonNullList<ItemStack> target = OreDictionary.getOres(Type, false);
							if (!target.isEmpty()) {
								translatedTarget = target.get(0).getDisplayName();
							}
							String FoundTarget = Type.contains(":") || Type.contains("[") || Type.contains("]") ? OreTrackingHelper.translateOreName(Type) : translatedTarget.replace(" ore", "").replace(" Ore", "");
							String NotFound = TranslationHelper.formatLangKeys(stack, UnkownTarget);
							String Message = getName.equalsIgnoreCase("Air") ? NotFound : FoundTarget.replace("Chest", "Treasure Chests");
							player.sendStatusMessage(new TextComponentString(Message), true);
							// Here
						} else { // Is On
							String offMode = TranslationHelper.formatLangKeys(stack, FinderOff);
							player.sendStatusMessage(new TextComponentString(offMode), true);
						}
						int auxDown = 0;
						if (Aux.isDown()) {
							auxDown = 1;
						}
						properties.KeyPressed(1);
						NetworkHandler.INSTANCE.sendToServer(new KeyPressPacket(player, 1, auxDown));
						return true;
					}, null, null
			);
		}
		Dragon_Breath.handler(true, null, press -> {
			int auxDown = 0;
			if (Aux.isDown()) {
				auxDown = 1;
			}
			properties.KeyPressed(2);
			NetworkHandler.INSTANCE.sendToServer(new KeyPressPacket(player, 2, auxDown));
			return true;
		}, null);
	}

	@Override
	public void savedNBTData(NBTTagCompound compound) {
		compound.setBoolean("Dragon.NightVision", NightVision);
		compound.setInteger("Dragon.Target", FinderTarget);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound) {
		if (compound.hasKey("Dragon.NightVision")) {
			NightVision = compound.getBoolean("Dragon.NightVision");
		}
		if (compound.hasKey("Dragon.Target")) {
			FinderTarget = compound.getInteger("Dragon.Target");
		}
	}

	public static final ResourceLocation TEXTURE = new ResourceLocation(Reference.MODID + ":" + "textures/dragon_wings.png");
	int tick = 0;
	float armSwing = 0;
	int wingFrames = 0;

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderLayer(RenderLivingBase renderer, boolean isFake, boolean isSlim, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if (!TrinketsConfig.CLIENT.rendering || (!properties.showTraits())) {
			return;
		}
		GlStateManager.pushMatrix();
		if (properties.showTraits()) {
			float flap = MathHelper.cos((limbSwing * 0.6662F) + (float) Math.PI);
			Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
			if (entity.isSneaking()) {
				GlStateManager.translate(0F, 0.2F, 0F);
			}
			if (renderer instanceof RenderPlayer) {
				RenderPlayer rend = (RenderPlayer) renderer;
				rend.getMainModel().bipedBody.postRender(scale);
			}
			GlStateManager.scale(scale, scale, scale);
			GlStateManager.rotate(90, 0, 1, 0);
			GlStateManager.translate(0, -2F, 0);
			if (entity.hasItemInSlot(EntityEquipmentSlot.CHEST)) {
				GlStateManager.translate(-0.4F, -1F, 0F);
			}

			int base = entity.onGround ? 40 : 50;
			int wing = 20;
			int tip = -20;
			int rotX = 0;
			//			int rotY = 0;
			int rotZ = -10;

			double[][] frames = new double[111][10];

			wingFrames = frames.length;

			int v1 = frames.length;
			int v2 = v1 * 10;

			int angleTick = (tick * v1) / v2;
			this.getWingFrames(angleTick, base, wing, tip, rotX, rotZ, frames);
			//			angleTick = 0;//v1 - 0;
			//			angleTick = v1 - 0;
			if (angleTick >= (frames.length)) {
				angleTick = frames.length - 1;
			}

			double x = -12;
			double y = -20;
			double z = 0;
			int width = 8;
			int height = 32;
			int uWidth = 16;
			int vHeight = 64;
			int texWidth = 64;
			int texHeight = 64;

			int innerWidth = 16;
			int innerHeight = 32;
			int innerUWidth = 32;
			int innerVHeight = 64;
			int innerTexWidth = 64;
			int innerTexHeight = 64;

			int outerwidth = 8;
			int outHeight = 32;
			int outerUWidth = 16;
			int outerVHeight = 64;
			int outerTexWidth = 64;
			int outerTexHeight = 64;
			GlStateManager.color(properties.getTraitColorHandler().getRed(), properties.getTraitColorHandler().getGreen(), properties.getTraitColorHandler().getBlue(), properties.getTraitOpacity());
			GlStateManager.pushMatrix();
			GlStateManager.disableLighting();
			GlStateManager.enableBlend();
			GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

			GlStateManager.scale(0.8, 0.8, 0.8);

			GlStateManager.pushMatrix();
			GlStateManager.rotate((float) frames[angleTick][0], 0, 1, 0);
			//			GlStateManager.rotate((float) frames[angleTick][4], 0, 0, 1);
			//			GlStateManager.rotate((float) -frames[angleTick][3], 1, 0, 0);
			DrawingHelper.Draw(x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight);
			GlStateManager.translate(x, y, z);
			GlStateManager.rotate((float) frames[angleTick][1], 0, 1, 0);
			GlStateManager.translate(-x, -y, -z);
			DrawingHelper.Draw(x - innerWidth, y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight);
			GlStateManager.translate(x - innerWidth, y, z);
			GlStateManager.rotate((float) (frames[angleTick][1] + (float) frames[angleTick][2]), 0, 1, 0);
			GlStateManager.translate(-(x - innerWidth), -y, -z);
			DrawingHelper.Draw((x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight);
			GlStateManager.popMatrix();

			GlStateManager.pushMatrix();
			GlStateManager.rotate((float) -frames[angleTick][0], 0, 1, 0);
			//			GlStateManager.rotate((float) frames[angleTick][4], 0, 0, 1);
			//			GlStateManager.rotate((float) frames[angleTick][3], 1, 0, 0);
			DrawingHelper.Draw(x, y, z, 48, 0, uWidth, vHeight, width, height, texWidth, texHeight);
			GlStateManager.translate(x, y, z);
			GlStateManager.rotate((float) -frames[angleTick][1], 0, 1, 0);
			GlStateManager.translate(-x, -y, -z);
			DrawingHelper.Draw(x - innerWidth, y, z, 16, 0, innerUWidth, innerVHeight, innerWidth, innerHeight, innerTexWidth, innerTexHeight);
			GlStateManager.translate(x - innerWidth, y, z);
			GlStateManager.rotate((float) -(frames[angleTick][1] + (float) frames[angleTick][2]), 0, 1, 0);
			GlStateManager.translate(-(x - innerWidth), -y, -z);
			DrawingHelper.Draw((x - innerWidth) - outerwidth, y, z, 0, 0, outerUWidth, outerVHeight, outerwidth, outHeight, outerTexWidth, outerTexHeight);
			GlStateManager.popMatrix();

			GlStateManager.enableLighting();
			GlStateManager.disableBlend();
			GlStateManager.popMatrix();
			if (!entity.onGround) {
				tick += 1;
			}
			if (((tick >= v2) || ((flap == armSwing) && entity.onGround))) {
				tick = 0;
			}
			armSwing = flap;
		}
		GlStateManager.color(1, 1, 1, 1);
		GlStateManager.popMatrix();
	}

	private double[][] getWingFrames(int tickPos, double base, double wing, double tip, double rotX, double rotZ, double[][] frames) {
		frames[0] = new double[] { base, wing, tip, rotX, rotZ };
		int position = 1;
		int s = this.frames(15);
		for (int i1 = 0; i1 < s; i1++) {
			frames[position][0] = base - i1;
			frames[position][1] = wing - i1;
			frames[position][2] = tip;
			frames[position][3] = rotX;
			frames[position][4] = rotZ;
			position++;
		}
		int pos1 = position;
		int s1 = this.frames(10);
		for (int i1 = 0; i1 < s1; i1++) {
			frames[position][0] = frames[pos1 - 1][0] - i1;
			frames[position][1] = frames[pos1 - 1][1] - i1;
			frames[position][2] = frames[pos1 - 1][2];
			frames[position][3] = frames[pos1 - 1][3];
			frames[position][4] = frames[pos1 - 1][4];
			position++;
		}
		int pos2 = position;
		int s2 = this.frames(10);
		for (int i1 = 0; i1 < s2; i1++) {
			frames[position][0] = frames[pos2 - 1][0];
			frames[position][1] = frames[pos2 - 1][1] + i1;
			frames[position][2] = frames[pos2 - 1][2];
			frames[position][3] = frames[pos2 - 1][3] + (i1);
			frames[position][4] = frames[pos2 - 1][4] - (i1);
			position++;
		}
		int pos3 = position;
		int s3 = this.frames(10);
		for (int i1 = 0; i1 < s3; i1++) {
			frames[position][0] = frames[pos3 - 1][0] + i1;
			frames[position][1] = frames[pos3 - 1][1] + i1;
			frames[position][2] = frames[pos3 - 1][2];
			frames[position][3] = frames[pos3 - 1][3] + (i1);
			frames[position][4] = frames[pos3 - 1][4];
			position++;
		}
		int pos4 = position;
		int s4 = this.frames(10);
		for (int i1 = 0; i1 < s4; i1++) {
			frames[position][0] = frames[pos4 - 1][0] + i1;
			frames[position][1] = frames[pos4 - 1][1] + i1;
			frames[position][2] = frames[pos4 - 1][2];
			frames[position][3] = frames[pos4 - 1][3] + i1;
			frames[position][4] = frames[pos4 - 1][4];
			position++;
		}
		int pos5 = position;
		int s5 = this.frames(10);
		for (int i1 = 0; i1 < s5; i1++) {
			frames[position][0] = frames[pos5 - 1][0] + i1;
			frames[position][1] = frames[pos5 - 1][1] + i1;
			frames[position][2] = frames[pos5 - 1][2];
			frames[position][3] = frames[pos5 - 1][3];
			frames[position][4] = frames[pos5 - 1][4];
			position++;
		}
		int pos6 = position;
		int s6 = this.frames(10);
		for (int i1 = 0; i1 < s6; i1++) {
			frames[position][0] = frames[pos6 - 1][0];
			frames[position][1] = frames[pos6 - 1][1] + i1;
			frames[position][2] = frames[pos6 - 1][2];
			frames[position][3] = frames[pos6 - 1][3];
			frames[position][4] = frames[pos6 - 1][4];
			position++;
		}
		int pos7 = position;
		int s7 = this.frames(10);
		for (int i1 = 0; i1 < s7; i1++) {
			frames[position][0] = frames[pos7 - 1][0];
			frames[position][1] = frames[pos7 - 1][1] + i1;
			frames[position][2] = frames[pos7 - 1][2] - i1;
			frames[position][3] = frames[pos7 - 1][3];
			frames[position][4] = frames[pos7 - 1][4];
			position++;
		}
		int pos8 = position;
		int s8 = this.frames(10);
		for (int i1 = 0; i1 < s8; i1++) {
			frames[position][0] = frames[pos8 - 1][0];
			frames[position][1] = frames[pos8 - 1][1] - i1;
			frames[position][2] = frames[pos8 - 1][2];
			frames[position][3] = frames[pos8 - 1][3] - (i1);
			frames[position][4] = frames[pos8 - 1][4] + (i1);
			position++;
		}
		int pos9 = position;
		int s9 = this.frames(15);
		for (int i1 = 0; i1 < s9; i1++) {
			frames[position][0] = frames[pos9 - 1][0];
			frames[position][1] = frames[pos9 - 1][1] - i1;
			frames[position][2] = frames[pos9 - 1][2] - i1;
			frames[position][3] = frames[pos9 - 1][3] - (i1);
			frames[position][4] = frames[pos9 - 1][4];
			position++;
		}
		//		int pos10 = position;
		//		int s10 = this.frames(10);
		//		for (int i1 = 0; i1 < s10; i1++) {
		//			frames[position][0] = frames[pos10 - 1][0] + (i1 / 2);
		//			frames[position][1] = frames[pos10 - 1][1] + (i1 / 2);
		//			frames[position][2] = frames[pos10 - 1][2] - i1;
		//			frames[position][3] = frames[pos10 - 1][3];
		//			frames[position][4] = frames[pos10 - 1][4];
		//			position++;
		//		}
		//		System.out.println(wingFrames);
		//		int posL = position;
		//		int sL = this.frames(1000);
		//		for (int i1 = 0; i1 < sL; i1++) {
		//			frames[position][0] = frames[posL - 1][0];
		//			frames[position][1] = frames[posL - 1][1];
		//			frames[position][2] = frames[posL - 1][2];
		//			frames[position][3] = frames[posL - 1][3];
		//			frames[position][4] = frames[posL - 1][4];
		//			position++;
		//		}
		return frames;
	}

	private double[][] section(int length, int position, int savedPos, int incBase, int incWing, int incTip, int incRotX, int incRotY, double[][] frames) {
		int sL = this.frames(length);
		for (int i1 = 0; i1 < sL; i1++) {
			frames[position][0] = frames[savedPos - 1][0] + incBase;
			frames[position][1] = frames[savedPos - 1][1] + incWing;
			frames[position][2] = frames[savedPos - 1][2] + incTip;
			frames[position][3] = frames[savedPos - 1][3] + incRotX;
			frames[position][4] = frames[savedPos - 1][4] + incRotY;
		}
		return frames;
	}

	private int frames(int amount) {
		if ((wingFrames - amount) >= 0) {
			wingFrames -= amount;
			return amount;
		} else {
			if (wingFrames > 0) {
				return wingFrames;
			} else {
				return 0;
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void doRenderPlayerPre(RenderPlayerEvent.Pre event) {
		if (entity.isRiding()) {
			double t = 0;
			t = ((100 - properties.getSize()) * 0.01D);
			//			t = 1.8 - ((properties.getSize()) * 0.01D);
			//			t = 1.8;//+ ((100 - properties.getSize()) * 0.01D);
			//			//			double t1 = (entity.height * 100) / (1.8 * 100);
			//			//			double t2 = (1.8 * 100) / (entity.height * 100);
			//			if (entity.getRidingEntity() instanceof EntityHorse) {
			//				t = (1.8);
			//			} else if (entity.getRidingEntity() instanceof EntityBoat) {
			//				//				t += entity.height * 3;
			//			}
			//			System.out.println(t);
			GlStateManager.translate(0, t, 0);
		}
	}

}