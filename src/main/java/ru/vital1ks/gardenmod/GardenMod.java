package ru.vital1ks.gardenmod;

import net.minecraft.client.gui.ScreenManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import ru.vital1ks.gardenmod.client.render.tileentity.DryingTableTileEntityRenderer;
import ru.vital1ks.gardenmod.core.init.BlockInit;
import ru.vital1ks.gardenmod.core.init.EntityTypeInit;
import ru.vital1ks.gardenmod.core.init.ItemInit;
import ru.vital1ks.gardenmod.core.init.RecipeSerializerInit;
import ru.vital1ks.gardenmod.core.init.TileEntityTypeInit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GardenMod.MOD_ID)
public class GardenMod
{
	public static final String MOD_ID = "gardenmod";
    private static final Logger LOGGER = LogManager.getLogger();

    public GardenMod() {
    	IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        
        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);
        TileEntityTypeInit.TILEENTITYTYPE.register(bus);
        RecipeSerializerInit.RECIPE_SERIALIZER.register(bus);
        
        
        
        MinecraftForge.EVENT_BUS.register(this);

        
    }

    private void setup(final FMLCommonSetupEvent event)
    {
    }  
}