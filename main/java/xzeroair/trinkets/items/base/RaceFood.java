package xzeroair.trinkets.items.base;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import xzeroair.trinkets.capabilities.Capabilities;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.races.EntityRace;
import xzeroair.trinkets.races.IRaceProvider;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;

public class RaceFood extends FoodBase implements IRaceProvider {

	private int useDuration;
	private EntityRace race;
	private Element element;
	private EnumAction action;

	public RaceFood(String name, int useDur, EnumAction action, EntityRace race, String uuid) {
		super(name, 2, 4F);
		this.setAlwaysEdible();
		this.action = action;
		useDuration = useDur;
		this.race = race;
		element = Elements.NEUTRAL;
		this.setUUID(uuid);
	}

	public RaceFood(String name, int useDur, EnumAction action, EntityRace race) {
		this(name, useDur, action, race, "");
	}

	@Override
	public EntityRace getRace() {
		return race;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entity) {
		return super.onItemUseFinish(stack, worldIn, entity);
	}

	@Override
	protected void onFoodEaten(ItemStack stack, World worldIn, EntityPlayer player) {
		super.onFoodEaten(stack, worldIn, player);
		Capabilities.getEntityProperties(player, prop -> {
			if (TrinketsConfig.SERVER.Food.food_effects) {
				if (!(prop.getImbuedRace().equals(race))) {
					prop.setImbuedRace(race);
				}
			}
		});
		this.setCooldown(20);
	}

	@Override
	public void onCreated(ItemStack stack, World world, EntityPlayer player) {
		super.onCreated(stack, world, player);
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
