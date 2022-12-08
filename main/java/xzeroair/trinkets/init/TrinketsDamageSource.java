package xzeroair.trinkets.init;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class TrinketsDamageSource extends EntityDamageSource {

	public static final TrinketsDamageSource poison = new TrinketsDamageSource("xat.poison").setIsPoisonDamage(true);
	public static final TrinketsDamageSource bleeding = new TrinketsDamageSource("xat.bleed");
	public static final TrinketsDamageSource water = new TrinketsDamageSource("xat.water");

	private boolean isPoisonDamage;

	public TrinketsDamageSource(String damageType) {
		this(damageType, null);
	}

	public TrinketsDamageSource(String damageType, @Nullable Entity damageSource) {
		super(damageType, damageSource);
		damageSourceEntity = damageSource;
	}

	public boolean isPoisonDamage() {
		return isPoisonDamage;
	}

	public TrinketsDamageSource setIsPoisonDamage(boolean isPoison) {
		isPoisonDamage = isPoison;
		return this;
	}

	public TrinketsDamageSource setDirectSource(Entity damageSource) {
		damageSourceEntity = damageSource;
		return this;
	}

	public static TrinketsDamageSource causeDamageFrom(String damageType, Entity damageSource) {
		return new TrinketsDamageSource(damageType, damageSource);
	}

	@Override
	public ITextComponent getDeathMessage(EntityLivingBase entityLivingBaseIn) {
		//		return new TextComponentTranslation("damagetype." + damageType);
		//		ItemStack itemstack = damageSourceEntity instanceof EntityLivingBase ? ((EntityLivingBase) damageSourceEntity).getHeldItemMainhand() : ItemStack.EMPTY;
		//		String s = "death.attack." + damageType;
		//		String s1 = s + ".item";
		//		ITextComponent source = damageSourceEntity.getDisplayName();//damageSourceEntity != null ? damageSourceEntity.getDisplayName() : "Poison";
		//		boolean flag1 = !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.canTranslate(s1);
		//		return flag1 ? new TextComponentTranslation(s1, new Object[] { entityLivingBaseIn.getDisplayName(), source, itemstack.getTextComponent() })
		//				: new TextComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName(), source });
		//		final ITextComponent source = new TextComponentTranslation("damagetype." + damageType);
		//		ItemStack itemstack = damageSourceEntity instanceof EntityLivingBase ? ((EntityLivingBase) damageSourceEntity).getHeldItemMainhand() : ItemStack.EMPTY;
		final ITextComponent source = new TextComponentTranslation("damagetype." + damageType);
		final String s = "death.attack." + damageType;
		//		ITextComponent source = damageSourceEntity.getDisplayName();//damageSourceEntity != null ? damageSourceEntity.getDisplayName() : "Poison";
		//		boolean flag1 = !itemstack.isEmpty() && itemstack.hasDisplayName() && I18n.canTranslate(s1);
		return new TextComponentTranslation(s, new Object[] { entityLivingBaseIn.getDisplayName(), source });
		//		return source;//new TextComponentTranslation(message);
	}

}
