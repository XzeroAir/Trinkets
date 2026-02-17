package xzeroair.trinkets.items.trinkets.cosmetic;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xzeroair.trinkets.Trinkets;
import xzeroair.trinkets.items.base.AccessoryBase;

public class TrinketTraitCosmetic extends AccessoryBase {

    public TrinketTraitCosmetic(String name) {
        super(name);
    }

    @Override
    public void playerRenderLayer(ItemStack stack, EntityLivingBase player, RenderPlayer renderer, boolean isSlim, float partialTicks, float scale) {
        super.playerRenderLayer(stack, player, renderer, isSlim, partialTicks, scale);
    }

    @Override
    public boolean ItemEnabled() {
        return true;
    }

    @Override
    public int getMetadata(int damage) {
        return super.getMetadata(damage);
    }

    @Override
    public int getMetadata(ItemStack stack) {
        return super.getMetadata(stack);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerModels() {

//        Collection<EntityRace> races = EntityRace.Registry.getValuesCollection();
//        for(EntityRace race: races){
//            String modID = race.getRegistryName().getNamespace();
//            if(modID.equals(Reference.MODID)){
//
//            }
//        }
        Trinkets.proxy.registerItemRenderer(this, 0, "inventory");
//        final ModelResourceLocation normal = new ModelResourceLocation(this.getRegistryName().toString(), "inventory");
//        final ModelResourceLocation human = new ModelResourceLocation(this.getRegistryName().toString() + "_human", "inventory");
//        final ModelResourceLocation fairy = new ModelResourceLocation(this.getRegistryName().toString() + "_fairy", "inventory");
//        final ModelResourceLocation dwarf = new ModelResourceLocation(this.getRegistryName().toString() + "_dwarf", "inventory");
//        final ModelResourceLocation titan = new ModelResourceLocation(this.getRegistryName().toString() + "_titan", "inventory");
//        final ModelResourceLocation goblin = new ModelResourceLocation(this.getRegistryName().toString() + "_goblin", "inventory");
//        final ModelResourceLocation elf = new ModelResourceLocation(this.getRegistryName().toString() + "_elf", "inventory");
//        final ModelResourceLocation faelis = new ModelResourceLocation(this.getRegistryName().toString() + "_faelis", "inventory");
//        final ModelResourceLocation dragon = new ModelResourceLocation(this.getRegistryName().toString() + "_dragon", "inventory");
//        ModelBakery.registerItemVariants(this, normal);
//        ModelLoader.setCustomMeshDefinition(this, stack -> {
//                return normal;
//        });
    }


}
