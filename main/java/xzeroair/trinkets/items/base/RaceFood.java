package xzeroair.trinkets.items.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceFood extends FoodBase {

	private int useDuration;
	private EntityRace race;
	private EnumAction action;

	public RaceFood(String name, int useDur, EnumAction action, EntityRace race, String uuid) {
		super(name, 0, 0);
		this.setAlwaysEdible();
		this.action = action;
		useDuration = useDur;
		this.race = race;
		this.setUUID(uuid);
	}

	public RaceFood(String name, int useDur, EnumAction action, EntityRace race) {
		//		this(name, useDur, action, race, UUID.randomUUID().toString());
		this(name, useDur, action, race, "");
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		EntityProperties prop = Capabilities.getEntityRace(entityLiving);
		if (prop != null) {
			if (TrinketsConfig.SERVER.Food.food_effects) {
				if (!(prop.getImbuedRace().equals(race))) {
					prop.setImbuedRace(race);
					prop.sendInformationToAll();
				}
			}
		}
		this.setCooldown(20);
		super.onItemUseFinish(stack, worldIn, entityLiving);
		return stack;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return useDuration;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return action;
	}

}
