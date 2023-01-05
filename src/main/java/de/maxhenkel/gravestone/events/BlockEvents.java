package de.maxhenkel.gravestone.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import de.maxhenkel.gravestone.*;
import de.maxhenkel.gravestone.tileentity.TileEntityGraveStone;
import de.maxhenkel.gravestone.util.BlockPos;
import de.maxhenkel.gravestone.util.GraveUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class BlockEvents {

    private final boolean removeDeathNote;
    private final boolean onlyOwnersCanBreak;

    public BlockEvents() {
        this.removeDeathNote = Config.instance().removeDeathNote;
        this.onlyOwnersCanBreak = Config.instance().onlyPlayersCanBreak;
    }

    @SubscribeEvent
    public void onBlockPlace(BlockEvent.PlaceEvent event) {
        if (event.isCanceled()) {
            return;
        }

        World world = event.world;

        if (world.isRemote) {
            return;
        }

        if (!event.placedBlock.equals(ModBlocks.GRAVESTONE)) {
            return;
        }

        TileEntity te = event.world.getTileEntity(event.x, event.y, event.z);

        if (!(te instanceof TileEntityGraveStone)) {
            return;
        }

        TileEntityGraveStone graveTileEntity = (TileEntityGraveStone) te;

        ItemStack stack = event.itemInHand;

        if (stack == null || !stack.getItem().equals(ModItems.GRAVESTONE)) {
            return;
        }

        if (!stack.hasDisplayName()) {
            return;
        }

        String name = stack.getDisplayName();

        if (name == null) {
            return;
        }

        graveTileEntity.setPlayerName(name);
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        if (event.isCanceled()) {
            return;
        }

        BlockPos deathLoc = new BlockPos(event.x, event.y, event.z);

        if (!removeDeathNote) GraveUtils.removeDeathNote(event.getPlayer(), event.world, deathLoc, event.block);
        event.setCanceled(!GraveUtils.canBreakGrave(event.world, event.getPlayer(), deathLoc));
    }

}
