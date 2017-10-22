package xzeroair.trinkets.util.handlers;

import net.minecraft.block.Block;

public class BlockDataHandler {
	
    private final Block blockID;
    private final int damage;
    
    public BlockDataHandler(Block a, int b)
    {
        blockID = a;
        damage = b;
    }
    
    public Block getBlockID()
    {
        return blockID;
    }
    
    public int getDamage()
    {
        return damage;
    }
    @Override
    public boolean equals(Object o)
    {
        if (o instanceof BlockDataHandler)
        {
        	BlockDataHandler comp = (BlockDataHandler)o;
            return comp.getBlockID() == blockID && comp.getDamage() == damage;
        }
        return false;
    }
    
    @Override
    public int hashCode()
    {
        return blockID.getUnlocalizedName().hashCode();
    }

}
