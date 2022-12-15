package xzeroair.trinkets.blocks.materials;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;

public class TrinketBlockMaterial extends Material {

	public static final Material ClothTeddyBear = new TrinketBlockMaterial(MapColor.CLOTH)
			.setBlockMovement(false)
			.setBlockLight(false)
			.setAdventureModeExempt();

	protected boolean blockMovement = true;
	protected boolean isSolid = true;
	protected boolean isLiquid = false;
	protected boolean blockLight = true;

	public TrinketBlockMaterial(MapColor color) {
		super(color);
	}

	protected TrinketBlockMaterial setBlockMovement(boolean blockMovement) {
		this.blockMovement = blockMovement;
		return this;
	}

	@Override
	public boolean blocksMovement() {
		return blockMovement;
	}

	@Override
	public boolean isSolid() {
		return isSolid;
	}

	protected TrinketBlockMaterial setSolid(boolean isSolid) {
		this.isSolid = isSolid;
		return this;
	}

	@Override
	public boolean isLiquid() {
		return !this.isSolid() && isLiquid;
	}

	protected TrinketBlockMaterial setLiquid(boolean isLiquid) {
		this.isLiquid = isLiquid;
		return this;
	}

	@Override
	public boolean blocksLight() {
		return blockLight;
	}

	protected TrinketBlockMaterial setBlockLight(boolean blockLight) {
		this.blockLight = blockLight;
		return this;
	}

}
