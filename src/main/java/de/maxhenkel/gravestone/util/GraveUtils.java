package de.maxhenkel.gravestone.util;

import de.maxhenkel.gravestone.Config;
import de.maxhenkel.gravestone.DeathInfo;
import de.maxhenkel.gravestone.ModBlocks;
import de.maxhenkel.gravestone.ModItems;
import de.maxhenkel.gravestone.tileentity.TileEntityGraveStone;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class GraveUtils {

    @Nullable
    public static BlockPos getGraveStoneLocation(World world, BlockPos deathPosition) {
        BlockPos location = new BlockPos(deathPosition.getX(), deathPosition.getY(), deathPosition.getZ());

        int buildLimit = world.getHeight();
        int step = 1;

        if (location.getY() < 1) {
            location = new BlockPos(location.getX(), 1, location.getZ());
        } else if (location.getY() >= buildLimit) {
            location = new BlockPos(location.getX(), buildLimit - 1, location.getZ());
            step = -1;
        }

        while (location.getY() < buildLimit && location.getY() >= 1) {
            if (isReplaceable(world, location)) {
                return location;
            }

            location.setY(location.getY() + step);
        }

        return null;
    }

    public static boolean isReplaceable(World world, BlockPos pos) {
        Block b = world.getBlock(pos.getX(), pos.getY(), pos.getZ());

        if (world.isAirBlock(pos.getX(), pos.getY(), pos.getZ())) {
            return true;
        }

        return Config.instance().replaceableBlocks.stream().anyMatch(block -> block.getUnlocalizedName().equals(b.getUnlocalizedName()));
    }

    public static boolean canBreakGrave(World world, EntityPlayer player, BlockPos pos) {
        if (!Config.instance().onlyPlayersCanBreak) {
            return true;
        }

        TileEntity te = world.getTileEntity(pos.getX(), pos.getY(), pos.getZ());

        if (!(te instanceof TileEntityGraveStone)) {
            return true;
        }

        TileEntityGraveStone grave = (TileEntityGraveStone) te;

        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP playerMp = (EntityPlayerMP) player;
            if (isOP(playerMp)) {
                return true;
            }
        }

        String uuid = grave.getPlayerUUID();

        if (uuid == null) {
            return false;
        }

        if (player.getUniqueID().toString().equals(uuid)) {
            return false;
        }

        return player.getUniqueID().equals(uuid);
    }

    public static boolean givePlayerNote(Entity entity, List<ItemStack> drops, BlockPos gravePosition, long time) {

        if (!(entity instanceof EntityPlayer)) {
            return false;
        }

        EntityPlayer player = (EntityPlayer) entity;

        DeathInfo.ItemInfo[] items = new DeathInfo.ItemInfo[drops.size()];
        for (int i = 0; i < drops.size(); i++) {
            ItemStack stack = drops.get(i);
            if (stack != null) {
                items[i] = new DeathInfo.ItemInfo(Tools.getStringFromItem(stack.getItem()), stack.stackSize, stack.getItemDamage());
            }
        }

        DeathInfo info = new DeathInfo(gravePosition, player.dimension, items, player.getDisplayName(), time, player.getUniqueID());
        ItemStack stack = new ItemStack(ModItems.DEATH_INFO);

        info.addToItemStack(stack);
        player.inventory.addItemStackToInventory(stack);
        return true;
    }

    public static void removeDeathNote(EntityPlayer player, World world, BlockPos deathPos, Block block) {

        if (world.isRemote) {
            return;
        }

        if (!block.equals(ModBlocks.GRAVESTONE)) {
            return;
        }

        InventoryPlayer inv = player.inventory;
        int dim = player.dimension;

        for (int i = 0; i < inv.mainInventory.length; i++) {
            ItemStack stack = inv.mainInventory[i];
            if (stack != null && stack.getItem().equals(ModItems.DEATH_INFO)) {
                if (stack.hasTagCompound() && stack.getTagCompound().hasKey(DeathInfo.KEY_INFO)) {
                    DeathInfo info = DeathInfo.fromNBT(stack.getTagCompound().getCompoundTag(DeathInfo.KEY_INFO));
                    if (info != null && dim == info.getDimension() && deathPos.equals(info.getDeathLocation())) {
                        inv.setInventorySlotContents(i, null);
                    }
                }
            }
        }

        for (int i = 0; i < inv.armorInventory.length; i++) {
            ItemStack stack = inv.mainInventory[i];
            if (stack != null && stack.getItem().equals(ModItems.DEATH_INFO)) {
                inv.setInventorySlotContents(i, null);
            }
        }
    }

    public static boolean isOP(EntityPlayerMP player) {
        //return player.canCommandSenderUseCommand(player.mcServer.getOpPermissionLevel(), "op");
        return MinecraftServer.getServer().getConfigurationManager().func_152603_m().func_152700_a(player.getDisplayName()) != null;
    }

}