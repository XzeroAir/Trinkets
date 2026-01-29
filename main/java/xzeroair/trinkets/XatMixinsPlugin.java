package xzeroair.trinkets;

import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;

import java.util.Map;

    @IFMLLoadingPlugin.Name("xat")
    @IFMLLoadingPlugin.MCVersion("1.12.2")
    @IFMLLoadingPlugin.SortingIndex(-5000)
    public class XatMixinsPlugin implements IFMLLoadingPlugin {

        public XatMixinsPlugin() {
            MixinBootstrap.init();
            MixinExtrasBootstrap.init();
        }

        @Override
        public String[] getASMTransformerClass() {
            return new String[0];
        }

        @Override
        public String getModContainerClass() {
            return null;
        }

        @Override
        public String getSetupClass() {
            return null;
        }

        @Override
        public void injectData(Map<String, Object> data) { }

        @Override
        public String getAccessTransformerClass() {
            return null;
        }
    }