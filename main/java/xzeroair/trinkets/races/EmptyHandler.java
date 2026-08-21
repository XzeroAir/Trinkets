package xzeroair.trinkets.races;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.capabilities.race.EntityProperties;
import xzeroair.trinkets.capabilities.race.RaceCache;
import xzeroair.trinkets.init.Elements;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.AttributeEntry;
import xzeroair.trinkets.util.helpers.AttributeHelper;

public class EmptyHandler extends EntityRacePropertiesHandler {

    public EmptyHandler(EntityLivingBase e, EntityProperties properties) {
        super(e, properties, new RaceCache(EntityRaces.none, Elements.NEUTRAL));
    }

    @Override
    public void registerRaceAbilities() {
    }

    @Override
    public void endTransformation() {
    }

    @Override
    protected void addNewAttributes() {
        final EntityRace previous = this.getProperties().getPreviousRaceCache().getRace();
        double d = Double.parseDouble(Reference.DECIMALFORMAT.format(1D - this.TransformationProgress()));
        if (d != 0) {
            String[] raceAttributes = previous.getRaceInformation().getAttributes();
            for (String entry : raceAttributes) {
                AttributeEntry attributeShell = ConfigHelper.getAttributeEntry(entry);
                if (attributeShell != null) {
                    String name = attributeShell.getAttribute();
                    double amount = attributeShell.getAmount();
                    int operation = attributeShell.getOperation();
                    boolean isSaved = attributeShell.isSaved();
                    UpdatingAttribute attribute = new UpdatingAttribute(previous.getName() + "." + name, previous.getUUID(), name).setSavedInNBT(false);
                    //					attribute.addModifier(entity, (amount), operation);
                    attribute.addModifier(this.getEntity(), (amount * d), operation);
                }
            }
        }
    }

    @Override
    public void onTick() {
        if (this.getEntity() instanceof EntityPlayer) {
            if (this.isTransforming()) {
                this.applyAdjustedSize();
                this.updateSize();
                this.addNewAttributes();
                this.modifyEyeHeight();
            } else {
                if (this.firstTransformUpdate) {
                    final EntityRace previous = this.getProperties().getPreviousRaceCache().getRace();
                    AttributeHelper.removeAttributesByUUID(this.getEntity(), previous.getUUID());
                    this.modifyEyeHeight();
                    this.firstTransformUpdate = false;
                }
            }
        }
    }

    @Override
    public void interact(PlayerInteractEvent event) {

    }

    @Override
    public boolean isTransformed() {
        return false;
    }
}
