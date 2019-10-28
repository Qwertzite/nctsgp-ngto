package qwertzite.ngtoplugin;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import qwertzite.mctsg.MctsgPluginLoader;
import qwertzite.mctsg.api.IBuildingEntry;
import qwertzite.mctsg.api.ICityPlanEntry;
import qwertzite.mctsg.api.IFormatEntry;
import qwertzite.mctsg.api.ITSGPlugin;

/**
 * This Plug-in adds ngto file as a mctsg building file format.
 * @author Qwertzite
 * 
 * 2019/10/07
 */
@Mod(
		modid = MctsgNgtoPluginCore.MODID,
		name = MctsgNgtoPluginCore.MOD_NAME,
		version = MctsgNgtoPluginCore.VERSION
	)
public class MctsgNgtoPluginCore implements ITSGPlugin {
	public static final String MODID = "ngto_plugin";
	public static final String MOD_NAME = "MCTSG NGTO Plugin";
	public static final String VERSION = "1.0.0-1.12.2-1.x-0000";
	public static final String RESOURCE_DOMAIN = MODID + ":";
	
	@Mod.Instance(MODID)
	public static MctsgNgtoPluginCore INSTANCE;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MctsgPluginLoader.registerPlugin(this);
	}

	@Override
	public void registerPlans(BiConsumer<String, ICityPlanEntry<?>> registry) {
		// No Plans added
	}

	@Override
	public void registerFormat(Consumer<IFormatEntry<? extends IBuildingEntry>> registry) {
		registry.accept(new NgtoFormat());
	}
}
