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
		return new FabricLoader(FabricMaven.URL, FabricMaven.loader("0.13.0"));
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
		d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", "fabric-api-base", "0.4.2+d7c144a8f4"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE, ModDependencyFlag.JIJ);
		d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", "fabric-resource-loader-v0", "0.4.13+d7c144a83a"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE, ModDependencyFlag.JIJ);
		d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", "fabric-screen-api-v1", "1.0.8+d7c144a8f4"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE, ModDependencyFlag.JIJ);
		d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", "fabric-lifecycle-events-v1", "1.4.12+d7c144a83a"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE, ModDependencyFlag.JIJ);
		d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", "fabric-networking-api-v1", "1.0.19+d7c144a8f4"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE, ModDependencyFlag.JIJ);
		d.addMaven(FabricMaven.URL, new MavenId(FabricMaven.GROUP_ID + ".fabric-api", "fabric-rendering-v1", "1.10.5+d7c144a83a"), ModDependencyFlag.RUNTIME, ModDependencyFlag.COMPILE, ModDependencyFlag.JIJ);
		d.addMaven("https://lazurite.dev/releases/", new MavenId("dev.lazurite:toolbox-fabric:1.3.13"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
		d.addMaven("https://lazurite.dev/releases/", new MavenId("dev.lazurite:transporter-fabric:1.3.10"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
		d.addMaven("https://lazurite.dev/releases/", new MavenId("dev.lazurite:rayon-fabric:1.5.1"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME, ModDependencyFlag.JIJ);
		d.addMaven("https://maven.gegy.dev/", new MavenId("dev.lambdaurora:spruceui:3.3.1+1.17"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME, ModDependencyFlag.JIJ);
		d.addMaven("https://maven.terraformersmc.com/releases/", new MavenId("com.terraformersmc:modmenu:3.0.1"), ModDependencyFlag.COMPILE, ModDependencyFlag.RUNTIME);
		d.addMaven("https://maven.gegy.dev/", new MavenId("supercoder79:databreaker:0.2.8"), ModDependencyFlag.RUNTIME);
	}

	@Override
	public int getJavaVersion() {
		return 17;
	}
}
