package de.maxhenkel.gravestone;

import de.maxhenkel.gravestone.tileentity.TileEntityGraveStone;
import de.maxhenkel.gravestone.util.BlockPos;
import de.maxhenkel.gravestone.util.GraveUtils;
import de.maxhenkel.gravestone.util.Tools;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class GraveProcessor {

    private final EntityLivingBase entity;
    private final World world;
    private final BlockPos deathPosition;
    private BlockPos gravePosition;
    private static List<Block> replaceableBlocks;
    private final List<ItemStack> drops;
    private final long time;

    public GraveProcessor(EntityLivingBase entity) {
        this.entity = entity;
        this.world = entity.worldObj;
        this.deathPosition = new BlockPos(entity.posX, entity.posY, entity.posZ);
        this.gravePosition = deathPosition;
        this.drops = new ArrayList<ItemStack>();
        this.time = System.currentTimeMillis();

        if (replaceableBlocks == null) {
            replaceableBlocks = Config.instance().replaceableBlocks;
        }
    }

    public boolean placeGraveStone(List<EntityItem> drops) {

        for (EntityItem ei : drops) {
            this.drops.add(ei.getEntityItem());
        }

        this.gravePosition = GraveUtils.getGraveStoneLocation(world, deathPosition);

        if (this.gravePosition == null) {
            this.gravePosition = deathPosition;
            Log.i("Grave from '" + entity.getCommandSenderName() + "' cant be created (No space)");
            return false;
        }

        try {
            int l = Tools.oppositeSite(entity);
            world.setBlock(gravePosition.getX(), gravePosition.getY(), gravePosition.getZ(), ModBlocks.GRAVESTONE, l, 2);

            if (GraveUtils.isReplaceable(world, gravePosition.down())) {
                world.setBlock(gravePosition.down().getX(), gravePosition.down().getY(), gravePosition.down().getZ(), Blocks.dirt);
            }


        } catch (Exception e) {
            return false;
        }

        TileEntity tileentity = world.getTileEntity(gravePosition.getX(), gravePosition.getY(), gravePosition.getZ());

        if (tileentity == null || !(tileentity instanceof TileEntityGraveStone)) {
            return false;
        }

        try {
            TileEntityGraveStone graveTileEntity = (TileEntityGraveStone) tileentity;

            graveTileEntity.setPlayerName(entity.getCommandSenderName());
            graveTileEntity.setPlayerUUID(entity.getUniqueID().toString());
            graveTileEntity.setDeathTime(time);

            graveTileEntity.setRenderHead(entity instanceof EntityPlayer);

            addItems(graveTileEntity, drops);

        } catch (Exception e) {
            Log.w("Failed to fill gravestone with data");
        }

        return true;
    }

    private void addItems(TileEntityGraveStone graveStone, List<EntityItem> items) {
        try {
            for (int i = 0; i < items.size(); i++) {
                EntityItem item = items.get(i);
                try {
                    ItemStack stack = item.getEntityItem();
                    graveStone.setInventorySlotContents(i, stack);
                } catch (Exception e) {
                    Log.w("Failed to add Item '" + item.getEntityItem().getUnlocalizedName() + "' to gravestone");
                }
            }
        } catch (Exception e) {
            Log.w("Failed to add Items to gravestone");
        }
    }

    @Deprecated
    public boolean givePlayerNote() {
        return GraveUtils.givePlayerNote(entity, drops, gravePosition, time);
    }

    public EntityLivingBase getEntity() {
        return entity;
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getDeathPosition() {
        return deathPosition;
    }

    public static List<Block> getReplaceableBlocks() {
        return replaceableBlocks;
    }

    public List<ItemStack> getDrops() {
        return drops;
    }

    public long getTime() {
        return time;
    }

    public BlockPos getGravePosition() {
        return gravePosition;
    }

}
