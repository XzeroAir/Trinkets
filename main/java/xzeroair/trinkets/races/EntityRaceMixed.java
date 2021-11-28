package xzeroair.trinkets.races;

import net.minecraft.util.math.MathHelper;

public class EntityRaceMixed extends EntityRace {

	EntityRace primary;
	EntityRace sub;

	public EntityRaceMixed(String name, String uuid) {
		super(name, uuid, 0, 0);
		//TODO set up a getter for sub race Colors
	}

	/*-----------------------------------Code-------------------------------------*/
	public EntityRace getPrimary() {
		return primary;
	}

	public EntityRaceMixed setPrimary(EntityRace primary) {
		this.primary = primary;
		return this;
	}

	public EntityRace getSub() {
		return sub;
	}

	public EntityRaceMixed setSub(EntityRace sub) {
		this.sub = sub;
		return this;
	}

	//	@Override
	//	public EntityRacePropertiesHandler getRaceHandler(EntityLivingBase e) {
	//		return new RaceMixed(e, primary, sub, Capabilities.getEntityRace(e));
	//	}

	@Override
	public int getMagicAffinity() {
		//			c = a * ((b / (a * 0.5D)));
		//			c = a * (((a * 0.5D) / (b)));
		int a = this.getPrimary().magicAffinityValue;
		int b = this.getSub().magicAffinityValue;
		if (a > b) {
			return MathHelper.floor(((1.5 * a) + b) / 3);
		} else if (a < b) {
			return MathHelper.floor((a + (1.5 * b)) / 3);
		} else {
			return MathHelper.floor((a + b) / 2);
		}
	}

}
