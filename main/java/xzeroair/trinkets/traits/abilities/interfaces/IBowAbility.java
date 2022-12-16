package xzeroair.trinkets.traits.abilities.interfaces;

import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;

public interface IBowAbility extends IItemUseAbility {

	default void knockArrow(ArrowNockEvent event) {

	}

	default void looseArrow(ArrowLooseEvent event) {

	}

	default void arrowImpact(ProjectileImpactEvent.Arrow event) {

	}

}
