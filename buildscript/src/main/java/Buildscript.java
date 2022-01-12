import io.github.coolcrabs.brachyura.decompiler.BrachyuraDecompiler;
import io.github.coolcrabs.brachyura.dependency.JavaJarDependency;
import io.github.coolcrabs.brachyura.fabric.FabricLoader;
import io.github.coolcrabs.brachyura.fabric.FabricMaven;
import io.github.coolcrabs.brachyura.fabric.FabricProject;
import io.github.coolcrabs.brachyura.fabric.Yarn;
import io.github.coolcrabs.brachyura.maven.MavenId;
import io.github.coolcrabs.brachyura.util.AtomicFile;
import io.github.coolcrabs.brachyura.util.Util;
import net.fabricmc.mappingio.tree.MappingTree;

public class Buildscript extends FabricProject {
    @Override
    public String getMcVersion() {
        return "1.18.1";
    }

    @Override
    public MappingTree createMappings() {
        return createMojmap();
    }

    @Override
    public FabricLoader getLoader() {
        return new FabricLoader(FabricMaven.URL, FabricMaven.loader("0.12.12"));
    }

    @Override
    public String getModId() {
        return "dropps";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public void getModDependencies(ModDependencyCollector d) {
        d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", "fabric-api-base", "0.4.1+b4f4f6cdd0"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
        d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", "fabric-lifecycle-events-v1", "1.4.9+3ac43d95cb"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
        d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", "fabric-networking-api-v1", "1.0.9+a02b4463d3"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE);
        d.addMaven("https://lazurite.dev/releases/", new MavenId("dev.lazurite:toolbox-fabric:1.2.7"), ModDependencyFlag.COMPILE);
        d.addMaven("https://lazurite.dev/releases/", new MavenId("dev.lazurite:transporter-fabric:1.3.6"), ModDependencyFlag.COMPILE);
        d.addMaven("https://lazurite.dev/releases/", new MavenId("dev.lazurite:rayon-fabric:1.4.3"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME, ModDependencyFlag.JIJ);
        d.addMaven("https://maven.shedaniel.me/", new MavenId("me.shedaniel.cloth:cloth-config-fabric:6.1.48"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME, ModDependencyFlag.JIJ);
        d.addMaven("https://maven.terraformersmc.com/releases/", new MavenId("com.terraformersmc:modmenu:3.0.1"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
        d.addMaven("https://maven.gegy.dev/", new MavenId("supercoder79:databreaker:0.2.8"), ModDependencyFlag.RUNTIME);
    }

    @Override
    public BrachyuraDecompiler decompiler() {
        return null;
    }
}
