package xzeroair.trinkets.mixin;

import net.minecraft.launchwrapper.Launch;
import net.minecraftforge.common.config.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.Mixins;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import xzeroair.trinkets.util.ConstantsConfigLang;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class XatMixinConfigPlugin implements IMixinConfigPlugin {
    private static final Logger LOGGER = LogManager.getLogger("TRINKETS");
    private static final String CATEGORY = "general.experimental mixins";
    private static final String CONFIG_FILE = "config/trinkets/Trinkets_And_Baubles.cfg";
    private static final String MIXIN_PACKAGE = "xzeroair.trinkets.mixin.";
    private static final Set<String> REPORTED_TARGETS = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void onLoad(String mixinPackage) {
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (mixinClassName.endsWith("EntityPlayerMixin")) {
            return this.isEnabled(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_VANILLA_PLAYER_SIZE_UPDATES_NAME);
        }

        if (mixinClassName.endsWith("PlayerShadowMixin")) {
            return this.isEnabled(ConstantsConfigLang.CONFIG_EXPERIMENTAL_MIXINS_RACE_SCALED_PLAYER_SHADOWS_NAME);
        }
        return false;
    }

    private boolean isEnabled(String property) {
        final File configFile = new File(Launch.minecraftHome, CONFIG_FILE);
        final Configuration config = new Configuration(configFile);
        try {
            config.load();
            return config.getBoolean(property, CATEGORY, false, "Experimental Mixin option. Requires restart.");
        } catch (Exception ignored) {
            return false;
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        this.warnExternalMixinOverlap(targetClassName);
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    private void warnExternalMixinOverlap(String targetClassName) {
        final Set<IMixinInfo> mixins = Mixins.getMixinsForClass(targetClassName);
        if (mixins == null || mixins.isEmpty()) {
            return;
        }

        final List<String> trinketsMixins = new ArrayList<>();
        final List<String> externalMixins = new ArrayList<>();
        for (IMixinInfo mixin : mixins) {
            final String mixinName = mixin.getClassName();
            final String description = mixinName + " (" + mixin.getConfig().getName() + ", priority " + mixin.getPriority() + ")";
            if (mixinName.startsWith(MIXIN_PACKAGE)) {
                trinketsMixins.add(description);
            } else {
                externalMixins.add(description);
            }
        }

        if (externalMixins.isEmpty() || !REPORTED_TARGETS.add(targetClassName)) {
            return;
        }

        Collections.sort(trinketsMixins);
        Collections.sort(externalMixins);
        LOGGER.warn("Mixin overlap on {}. Trinkets [{}]; external [{}]. This is not proof of a conflict, but include it with latest.log if startup or gameplay fails.", targetClassName, String.join(", ", trinketsMixins), String.join(", ", externalMixins));
    }
}
