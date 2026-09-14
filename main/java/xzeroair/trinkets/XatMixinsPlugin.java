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
            final Class<?> bootstrap = Class.forName("org.spongepowered.asm.launch.MixinBootstrap");
            final String version = (String) bootstrap.getField("VERSION").get(null);
            if (!this.isSupportedMixinVersion(version)) {
                LOGGER.warn("Mixin {} is older than Trinkets' minimum supported version 0.8.0. Experimental Mixin features will be disabled.", version);
                return false;
            }
            bootstrap.getMethod("init").invoke(null);
            Class.forName("org.spongepowered.asm.mixin.Mixins").getMethod("addConfiguration", String.class).invoke(null, "mixins.trinkets.json");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (ReflectiveOperationException | LinkageError e) {
            LOGGER.warn("Trinkets could not initialize its optional Mixin features.", e);
            return false;
        }
    }

    private boolean isSupportedMixinVersion(String version) {
        if (version == null) {
            return false;
        }
        final String[] parts = version.split("\\.");
        if (parts.length < 2) {
            return false;
        }
        try {
            final int major = Integer.parseInt(parts[0]);
            final int minor = Integer.parseInt(parts[1]);
            return major > 0 || (major == 0 && minor >= 8);
        } catch (NumberFormatException e) {
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
