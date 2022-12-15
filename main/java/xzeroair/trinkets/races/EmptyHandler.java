package xzeroair.trinkets.races;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import xzeroair.trinkets.attributes.UpdatingAttribute;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.util.Reference;
import xzeroair.trinkets.util.config.ConfigHelper;
import xzeroair.trinkets.util.config.ConfigHelper.AttributeEntry;
import xzeroair.trinkets.util.handlers.SizeHandler;
import xzeroair.trinkets.util.helpers.AttributeHelper;

public class EmptyHandler extends EntityRacePropertiesHandler {

	public EmptyHandler(EntityLivingBase e) {
		this(e, EntityRaces.none);
	}

	public EmptyHandler(EntityLivingBase e, EntityRace race) {
		super(e, race);
	}

	@Override
	public void startTransformation() {
		//		progress = 1D;
	}

	@Override
	public void endTransformation() {

	}

	@Override
	protected void initAttributes() {
		final EntityRace previous = properties.getPreviousRace();
		double d = Double.parseDouble(Reference.DECIMALFORMAT.format(1D - this.TransformationProgress()));
		if (d != 0) {
			String[] raceAttributes = previous.getRaceAttributes().getAttributes();
			if (raceAttributes.length > 0) {
				for (String entry : raceAttributes) {
					AttributeEntry attributeShell = ConfigHelper.getAttributeEntry(entry);
					if (attributeShell != null) {
						String name = attributeShell.getAttribute();
						double amount = attributeShell.getAmount();
						int operation = attributeShell.getOperation();
						boolean isSaved = attributeShell.isSaved();
						UpdatingAttribute attribute = new UpdatingAttribute(previous.getName() + "." + name, previous.getUUID(), name).setSavedInNBT(true);
						//					attribute.addModifier(entity, (amount), operation);
						attribute.addModifier(entity, (amount * d), operation);
					}
				}
			}
		}
	}

	@Override
	public void onTick() {
		if (entity instanceof EntityPlayer) {
			if (this.isTransforming()) {
				SizeHandler.setSize(entity, this.getHeight(), this.getWidth());
				this.updateSize();
				this.initAttributes();
				this.eyeHeightHandler();
			} else {
				if (firstTransformUpdate) {
					final EntityRace previous = properties.getPreviousRace();
					AttributeHelper.removeAttributesByUUID(entity, previous.getUUID());
					((EntityPlayer) entity).eyeHeight = ((EntityPlayer) entity).getDefaultEyeHeight();
					firstTransformUpdate = false;
				}
			}
		} else {
			if (this.isTransforming()) {
				this.updateSize();
				this.eyeHeightHandler();
			} else {
				if (firstTransformUpdate) {
					float height = properties.getDefaultHeight();
					float width = properties.getDefaultWidth();
					//					if (entity instanceof EntityAgeable) {
					//						if (entity.isChild()) {
					//							height *= 2;
					//							width *= 2;
					//						}
					//						SizeHandler.setSize(entity, height, width);
					//						((EntityAgeable) entity).setScaleForAge(entity.isChild());
					//					} else {
					SizeHandler.setSize(entity, height, width);
					//					}
					firstTransformUpdate = false;
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
