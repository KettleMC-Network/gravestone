package de.maxhenkel.gravestone.proxy;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import de.maxhenkel.gravestone.*;
import de.maxhenkel.gravestone.blocks.BlockGraveStone;
import de.maxhenkel.gravestone.events.BlockEvents;
import de.maxhenkel.gravestone.events.DeathEvents;
import de.maxhenkel.gravestone.events.UpdateCheckEvents;
import de.maxhenkel.gravestone.gui.GuiHandler;
import de.maxhenkel.gravestone.tileentity.TileEntityGraveStone;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

public class CommonProxy {

    public void preinit(FMLPreInitializationEvent event) {
        Configuration c = null;
        try {
            c = new Configuration(event.getSuggestedConfigurationFile());
            Config config = new Config(c);
            config.setInstance();
        } catch (Exception e) {
            Log.w("Could not create config file: " + e.getMessage());
        }

        Log.setLogger(event.getModLog());
    }

    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new UpdateCheckEvents());
        MinecraftForge.EVENT_BUS.register(new DeathEvents());
        MinecraftForge.EVENT_BUS.register(new BlockEvents());
        GameRegistry.registerBlock(ModBlocks.GRAVESTONE, null, BlockGraveStone.NAME);
        //registerBlock(ModBlocks.GRAVESTONE);
        registerItem(ModItems.DEATH_INFO);
        registerItem(ModItems.GRAVESTONE);

        NetworkRegistry.INSTANCE.registerGuiHandler(Main.instance(), new GuiHandler());

        GameRegistry.registerTileEntity(TileEntityGraveStone.class, "TileEntityGaveStone");

        GameRegistry.addRecipe(new ItemStack(ModBlocks.GRAVESTONE), "CXX", "CXX", "DDD", Character.valueOf('C'), Blocks.cobblestone, Character.valueOf('D'), Blocks.dirt);

        GameRegistry.addRecipe(new ItemStack(ModBlocks.GRAVESTONE), "XXC", "XXC", "DDD", Character.valueOf('C'), Blocks.cobblestone, Character.valueOf('D'), Blocks.dirt);

    }

    public void postinit(FMLPostInitializationEvent event) {

    }

    private void registerItem(Item i) {
        GameRegistry.registerItem(i, i.getUnlocalizedName().replace("item.", ""));
    }

    private void registerBlock(Block b) {
        GameRegistry.registerBlock(b, b.getUnlocalizedName().replace("tile.", ""));
    }

}
