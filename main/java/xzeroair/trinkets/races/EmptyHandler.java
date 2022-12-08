package xzeroair.trinkets.races;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import xzeroair.trinkets.init.EntityRaces;
import xzeroair.trinkets.util.handlers.SizeHandler;

public class EmptyHandler extends EntityRacePropertiesHandler {

	public EmptyHandler(EntityLivingBase e) {
		this(e, EntityRaces.none);
	}

	public EmptyHandler(EntityLivingBase e, EntityRace race) {
		super(e, race);
	}

	@Override
	public void onTransform() {
		super.onTransform();
	}

	@Override
	public void onTransformEnd() {
		super.onTransformEnd();
	}

	@Override
	public void onTick() {
		if (this.isTransforming()) {
			SizeHandler.setSize(entity, this.getHeight(), this.getWidth());
			this.updateSize();
		} else {
			if (firstTransformUpdate) {
				if (entity instanceof EntityPlayer) {
					((EntityPlayer) entity).eyeHeight = ((EntityPlayer) entity).getDefaultEyeHeight();
				} else {
					if (!properties.isNormalSize()) {
						entity.height = defaultHeight;
						entity.width = defaultWidth;
					}
				}
				firstTransformUpdate = false;
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
