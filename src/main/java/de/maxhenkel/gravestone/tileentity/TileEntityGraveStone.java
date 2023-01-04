package de.maxhenkel.gravestone.tileentity;

import de.maxhenkel.gravestone.util.Tools;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityGraveStone extends TileEntity implements IInventory {

    private InventoryBasic inventory;
    private String playerName;
    private String playerUUID;
    private long deathTime;
    private boolean renderHead;
    private static final String TAG_NAME = "ItemStacks";
    private static final String INV_NAME = "GraveInventory";
    private static final String PLAYER_NAME = "PlayerName";
    private static final String PLAYER_UUID = "PlayerUUID";
    private static final String DEATH_TIME = "DeathTime";
    private static final String RENDER_HEAD = "RenderHead";

    public TileEntityGraveStone() {
        this.inventory = new InventoryBasic(INV_NAME, false, 127);
        this.playerName = "";
        this.deathTime = 0L;
        this.playerUUID = "";
        this.renderHead = true;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setString(PLAYER_NAME, playerName);
        compound.setLong(DEATH_TIME, deathTime);
        compound.setString(PLAYER_UUID, playerUUID);
        compound.setBoolean(RENDER_HEAD, renderHead);


        NBTTagList list = new NBTTagList();

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            if (inventory.getStackInSlot(i) != null) {
                NBTTagCompound tag = new NBTTagCompound();
                inventory.getStackInSlot(i).writeToNBT(tag);
                list.appendTag(tag);
            }
        }

        compound.setTag(TAG_NAME, list);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagList list = compound.getTagList(TAG_NAME, 10);
        this.inventory = new InventoryBasic(INV_NAME, false, list.tagCount());

        for (int i = 0; i < list.tagCount(); i++) {
            NBTTagCompound tag = list.getCompoundTagAt(i);
            inventory.setInventorySlotContents(i, ItemStack.loadItemStackFromNBT(tag));
        }

        this.playerName = compound.getString(PLAYER_NAME);
        this.playerUUID = compound.getString(PLAYER_UUID);
        this.deathTime = compound.getLong(DEATH_TIME);

        if (compound.hasKey(RENDER_HEAD)) {
            this.renderHead = compound.getBoolean(RENDER_HEAD);
        }

    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.func_148857_g());
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound c = new NBTTagCompound();
        writeToNBT(c);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, getBlockMetadata(), c);
    }

    @Override
    public int getSizeInventory() {
        return inventory.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return inventory.decrStackSize(index, count);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.inventory.setInventorySlotContents(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return inventory.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return inventory.isUseableByPlayer(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return inventory.isItemValidForSlot(index, stack);
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
        markDirty();
        if (worldObj != null) {
            worldObj.notifyBlockChange(xCoord, yCoord, zCoord, blockType);
        }
    }

    public long getDeathTime() {
        return this.deathTime;
    }

    public void setDeathTime(long time) {
        this.deathTime = time;
    }

    public void setPlayerName(long time) {
        this.deathTime = time;
    }

    public String getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(String playerUUID) {
        this.playerUUID = playerUUID;
    }

    public boolean renderHead() {
        return renderHead;
    }

    public void setRenderHead(boolean renderHead) {
        this.renderHead = renderHead;
    }

    public String getTimeString() {
        return Tools.timeToString(deathTime);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        return inventory.getStackInSlotOnClosing(index);
    }

    @Override
    public String getInventoryName() {
        return inventory.getInventoryName();
    }

    @Override
    public boolean hasCustomInventoryName() {
        return inventory.hasCustomInventoryName();
    }

    @Override
    public void openInventory() {
        inventory.openInventory();
    }

    @Override
    public void closeInventory() {
        inventory.closeInventory();
    }
}
