package xzeroair.trinkets.races.elf;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.attributes.JumpAttribute;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.client.races.IRenderRaceHandler;
import xzeroair.trinkets.client.races.elf.RaceElfRenderer;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.races.EntityRacePropertiesHandler;
import xzeroair.trinkets.races.elf.config.ElfConfig;
import xzeroair.trinkets.traits.abilities.AbilityChargedShot;
import xzeroair.trinkets.traits.elements.Element;
import xzeroair.trinkets.util.TrinketsConfig;
import xzeroair.trinkets.util.helpers.AttributeHelper;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.UUID;

public class RaceElf extends EntityRacePropertiesHandler {

    public static final ElfConfig serverConfig = TrinketsConfig.SERVER.races.elf;

    protected UpdatingAttribute bonusSpeed, bonusAtkSpeed, jump;

    public RaceElf(@Nonnull EntityLivingBase e) {
        super(e, EntityRaces.elf);
        bonusSpeed = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), SharedMonsterAttributes.MOVEMENT_SPEED).setSavedInNBT(false);
        bonusAtkSpeed = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), SharedMonsterAttributes.ATTACK_SPEED).setSavedInNBT(false);
        jump = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), JumpAttribute.Jump).setSavedInNBT(false);
    }

    public RaceElf(@Nonnull EntityLivingBase e, Element element) {
        super(e, EntityRaces.elf, element);
        bonusSpeed = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), SharedMonsterAttributes.MOVEMENT_SPEED).setSavedInNBT(false);
        bonusAtkSpeed = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), SharedMonsterAttributes.ATTACK_SPEED).setSavedInNBT(false);
        jump = new UpdatingAttribute(UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"), JumpAttribute.Jump).setSavedInNBT(false);
    }

    @Override
    public void startTransformation() {
        if (serverConfig.charge_shot) {
            this.addAbility(new AbilityChargedShot());
        }
    }

    @Override
    public void whileTransformed() {
        if (entity.world.isRemote) {
            return;
        }
        try {
            if (entity.world.getBiome(entity.getPosition()) != null) {
                final Set<Type> biomeType = BiomeDictionary.getTypes(entity.world.getBiome(entity.getPosition()));
                if (biomeType.contains(Type.FOREST)) {
                    bonusSpeed.addModifier(entity, 0.2, 2);
                    bonusAtkSpeed.addModifier(entity, 0.5, 2);
                    jump.addModifier(entity, 0.2, 2);
                } else {
                    bonusSpeed.removeModifier(entity);
                    bonusAtkSpeed.removeModifier(entity);
                    jump.removeModifier(entity);
                }
            }
        } catch (final Exception e) {
        }
    }

    @Override
    public void endTransformation() {
        AttributeHelper.removeAttributes(entity, UUID.fromString("628dedc0-5f63-4b45-bccb-ecb0fe881b49"));
        //		bonusSpeed.removeModifier();
        //		bonusAtkSpeed.removeModifier();
        //		jump.removeModifier();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IRenderRaceHandler<? super IRenderRaceHandler> getRaceRenderer() {
        if (this.RendererRace == null) {
            this.RendererRace = new RaceElfRenderer(entity, this);
        }
        return RendererRace;
    }
}
