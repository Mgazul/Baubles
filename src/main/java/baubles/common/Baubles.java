package baubles.common;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.cap.BaubleItem;
import baubles.api.cap.BaublesCapabilities;
import baubles.api.cap.BaublesCapabilities.CapabilityBaubles;
import baubles.api.cap.BaublesContainer;
import baubles.api.cap.IBaublesItemHandler;
import baubles.common.event.CommandBaubles;
import baubles.common.network.PacketHandler;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(
		modid = Baubles.MODID, 
		name = Baubles.MODNAME, 
		version = Baubles.VERSION)
public class Baubles {

	public static final String MODID = "baubles";
	public static final String MODNAME = "PFBaubles";
	public static final String VERSION = "1.0.0";

	@SidedProxy(clientSide = "baubles.client.ClientProxy", serverSide = "baubles.common.CommonProxy")
	public static CommonProxy proxy;

	@Instance(value=Baubles.MODID)
	public static Baubles instance;

	public File modDir;

	public static final Logger log = LogManager.getLogger(MODID.toUpperCase());
	public static final int GUI = 0;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		modDir = event.getModConfigurationDirectory();

		CapabilityManager.INSTANCE.register(IBaublesItemHandler.class,
				new CapabilityBaubles<IBaublesItemHandler>(), BaublesContainer.class);

		CapabilityManager.INSTANCE
				.register(IBauble.class, new BaublesCapabilities.CapabilityItemBaubleStorage(), () -> new BaubleItem(BaubleType.TRINKET));

		proxy.registerEventHandlers();
		PacketHandler.init();
	}

	@EventHandler
	public void init(FMLInitializationEvent evt) {
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		proxy.init();
	}

	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new CommandBaubles());
	}
}
