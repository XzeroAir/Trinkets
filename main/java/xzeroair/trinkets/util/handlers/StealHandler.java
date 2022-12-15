package xzeroair.trinkets.util.handlers;

import java.util.Random;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class StealHandler {

	//Glove Item?

	private Random rand = new Random();
	private EntityLivingBase player;
	int cooldown = 0;
	int counter = 0;

	public StealHandler(EntityLivingBase entity) {
		player = entity;
	}

	public void onUpdate() {

	}

	public void StealWeapon(EntityLivingBase target) {
		boolean success = false;
		for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
			ItemStack stack = target.getItemStackFromSlot(slot);
			if (slot.getSlotType() == EntityEquipmentSlot.Type.HAND) {
				if (!(stack.isEmpty())) {
				}
			}
		}
	}

	public void StealArmor(EntityLivingBase target) {
		boolean success = false;
		//		int item = rand.nextInt(4);
		int counter = -1;
		int counter2 = -1;
		//		EntityEquipmentSlot[] slot = EntityEquipmentSlot.values();

		for (EntityEquipmentSlot slot : EntityEquipmentSlot.values()) {
			ItemStack stack = target.getItemStackFromSlot(slot);
			if (slot.getSlotType() == EntityEquipmentSlot.Type.ARMOR) {
				if (!(stack.isEmpty())) {
					success = true;
					if (player instanceof EntityPlayer) {
						ITextComponent message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GREEN + "Success! You Stole: " + TextFormatting.BOLD + "" + TextFormatting.AQUA + stack.getItem().getRegistryName());
						((EntityPlayer) player).sendStatusMessage(message, true);
						((EntityPlayer) player).addItemStackToInventory(stack);
					} else {
						//						if (player.getItemStackFromSlot(slot).isEmpty()) {
						//							player.setItemStackToSlot(slot, stack);
						//						}
					}
					//				prop.getMagic().spendMana(cost);
					break;
				}
			}
		}
		if (success == true) {
			//			System.out.println("Success!");
		} else {
			if (player instanceof EntityPlayer) {
				ITextComponent message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.DARK_RED + "I Failed!");
				((EntityPlayer) player).sendStatusMessage(message, true);
			}
			//			System.out.println("Failed!");
		}
	}

	public void StealDrops(EntityLivingBase target) {

	}
	//
	//	EntityPlayer player = event.getEntityPlayer();
	//	if ((player == null) || !player.getHeldItemMainhand().isEmpty() || !player.getHeldItemOffhand().isEmpty() || event.getWorld().isRemote || (cooldown != 0)) {
	//		return;
	//	}
	//	RaceProperties prop = Capabilities.getEntityRace(player);
	//	float cost = 20f;
	//	if (prop.getMagic().getMana() < cost) {
	//		return;
	//	}
	//	cooldown = 20;
	//	if (!(event.getTarget() instanceof EntityPlayer)) {
	//		if (prop.getTargetRace().equals(RaceEnum.goblin) && (event.getTarget() != null)) {
	//			Random rand = new Random();
	//			try {
	//				int r0 = 1;//rand.nextInt(3);
	//				if (r0 == 0) {
	//					if (event.getTarget() instanceof EntityLivingBase) {
	//						thieving.StealArmor((EntityLivingBase) event.getTarget());
	//					}
	//					//						boolean success = false;
	//					//						int item = rand.nextInt(4);
	//					//						int counter = -1;
	//					//						for (ItemStack s : event.getTarget().getArmorInventoryList()) {
	//					//							counter++;
	//					//							if ((counter == item) && !(s.isEmpty())) {
	//					//								success = true;
	//					//								//								System.out.println(s.getItem().getRegistryName());
	//					//								ITextComponent message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GREEN + "Success! You Stole: " + TextFormatting.BOLD + "" + TextFormatting.AQUA + s.getItem().getRegistryName());
	//					//								player.sendStatusMessage(message, true);
	//					//								player.addItemStackToInventory(s);
	//					//								prop.getMagic().spendMana(cost);
	//					//								break;
	//					//							}
	//					//						}
	//					//						if (success == true) {
	//					//							System.out.println("Success!");
	//					//						} else {
	//					//							ITextComponent message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.DARK_RED + "I Failed!");
	//					//							player.sendStatusMessage(message, true);
	//					//							System.out.println("Failed!");
	//					//						}
	//				} else if (r0 == 1) {
	//					if (event.getTarget() instanceof EntityLivingBase) {
	//						thieving.StealWeapon((EntityLivingBase) event.getTarget());
	//					}
	//					//						boolean success = false;
	//					//						int item = rand.nextInt(2);
	//					//						int counter = -1;
	//					//						for (ItemStack s : event.getTarget().getHeldEquipment()) {
	//					//							counter++;
	//					//							if ((counter == item) && !s.isEmpty()) {
	//					//								success = true;
	//					//								//								System.out.println(s.getItem().getRegistryName());
	//					//								ITextComponent message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GREEN + "Success! You Stole: " + TextFormatting.BOLD + "" + TextFormatting.AQUA + s.getItem().getRegistryName());
	//					//								player.sendStatusMessage(message, true);
	//					//								player.addItemStackToInventory(s);
	//					//								prop.getMagic().spendMana(cost);
	//					//								break;
	//					//							}
	//					//						}
	//					//						if (success == true) {
	//					//							System.out.println("Success!");
	//					//						} else {
	//					//							System.out.println("Failed!");
	//					//							ITextComponent message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.DARK_RED + "I Failed!");
	//					//							player.sendStatusMessage(message, true);
	//					//						}
	//				} else {
	//					ResourceLocation loc = null;
	//					for (ResourceLocation rloc : LootTableList.getAll()) {
	//						if (rloc.getNamespace().equalsIgnoreCase("minecraft")) {
	//							if (rloc.getPath().startsWith("entities/")) {
	//								if (rloc.getPath().contains("/")) {
	//									String[] sA = rloc.getPath().split("/");
	//									if (sA.length >= 2) {
	//										if (sA[1].equalsIgnoreCase(event.getTarget().getName())) {
	//											loc = rloc;
	//										}
	//									}
	//								}
	//							}
	//						}
	//					}
	//					if (loc == null) {
	//						return;
	//					}
	//					LootTableManager lootManager = new LootTableManager(new File(""));//event.getWorld().getLootTableManager();
	//					LootTable table = lootManager.getLootTableFromLocation(loc);
	//					if (table != null) {
	//						List<LootPool> pool = TrinketReflectionHelper.getPools(table);
	//						if (!pool.isEmpty()) {
	//							int r1 = rand.nextInt(pool.size());
	//							if (pool.get(r1) != null) {
	//								List<LootEntry> entry = TrinketReflectionHelper.getLootEntries(pool.get(r1));
	//								if (!entry.isEmpty()) {
	//									int r2 = rand.nextInt(entry.size());
	//									if ((entry.get(r2) != null)) {
	//										ItemStack s = new ItemStack(Item.getByNameOrId(entry.get(r2).getEntryName()));
	//										ITextComponent message = new TextComponentString(TextFormatting.BOLD + "" + TextFormatting.GREEN + "Success! You Stole: " + TextFormatting.BOLD + "" + TextFormatting.AQUA + s.getItem().getRegistryName());
	//										player.sendStatusMessage(message, true);
	//										EntityItem e = new EntityItem(event.getWorld(), event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, s);
	//										prop.getMagic().spendMana(cost);
	//										event.getWorld().spawnEntity(e);
	//									}
	//								}
	//							}
	//						}
	//					}
	//				}
	//			} catch (Exception e) {
	//
	//			}
	//		}
	//	} else {
	//		EntityPlayer target = (EntityPlayer) event.getTarget();
	//		boolean check = false;
	//		for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
	//			ItemStack stack = target.inventory.getStackInSlot(i);
	//			if (check == true) {
	//				break;
	//			}
	//			if (!stack.isEmpty()) {
	//				System.out.println(stack);
	//				player.addItemStackToInventory(stack);
	//				//					if(player.inventory.) {
	//				//
	//				//					}
	//			}
	//		}
	//	}
}
