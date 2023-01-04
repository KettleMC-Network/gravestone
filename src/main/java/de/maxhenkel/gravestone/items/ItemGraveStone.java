package de.maxhenkel.gravestone.items;

import de.maxhenkel.gravestone.Main;
import de.maxhenkel.gravestone.ModBlocks;
import de.maxhenkel.gravestone.blocks.BlockGraveStone;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemReed;

public class ItemGraveStone extends ItemReed {

    public ItemGraveStone(Block block) {
        super(block);
        setUnlocalizedName(BlockGraveStone.NAME);
        setCreativeTab(CreativeTabs.tabDecorations);
        setTextureName(Main.MODID + ":" + BlockGraveStone.NAME);

    }
}
