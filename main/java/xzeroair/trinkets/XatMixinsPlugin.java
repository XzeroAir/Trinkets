package xzeroair.trinkets;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

@IFMLLoadingPlugin.MCVersion("1.12.2")
public class XatMixinsPlugin implements IFMLLoadingPlugin {
    private static final Logger LOGGER = LogManager.getLogger("XAT");

    public XatMixinsPlugin() {
        if (!this.initializeMixinSupport()) {
            LOGGER.warn("Mixin is unavailable. Trinkets will continue without Experimental Mixin features.");
        }
    }

    private boolean initializeMixinSupport() {
        try {
            Class.forName("org.spongepowered.asm.launch.MixinBootstrap").getMethod("init").invoke(null);
            Class.forName("org.spongepowered.asm.mixin.Mixins").getMethod("addConfiguration", String.class).invoke(null, "mixins.trinkets.json");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (ReflectiveOperationException | LinkageError e) {
            LOGGER.warn("Trinkets could not initialize its optional Mixin features.", e);
            return false;
        }
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
    public void injectData(Map<String, Object> data) {}

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
