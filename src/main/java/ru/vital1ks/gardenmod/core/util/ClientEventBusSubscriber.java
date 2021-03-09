package ru.vital1ks.gardenmod.core.util;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import ru.vital1ks.gardenmod.GardenMod;
import ru.vital1ks.gardenmod.client.render.tileentity.DryingTableTileEntityRenderer;
import ru.vital1ks.gardenmod.core.init.TileEntityTypeInit;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = GardenMod.MOD_ID, bus = Bus.MOD)
public class ClientEventBusSubscriber {
	@SubscribeEvent
	public static void clientSetup(FMLClientSetupEvent event) {

		
		ClientRegistry.bindTileEntityRenderer(TileEntityTypeInit.DRYING_TABLE.get(), DryingTableTileEntityRenderer::new);
	}
}
