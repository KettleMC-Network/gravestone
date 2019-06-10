package de.maxhenkel.gravestone.entity;

import java.util.UUID;
import javax.annotation.Nullable;

import de.maxhenkel.gravestone.Config;
import de.maxhenkel.gravestone.Main;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class EntityGhostPlayer extends MonsterEntity {

    private static final DataParameter<String> PLAYER_UUID = EntityDataManager.createKey(EntityGhostPlayer.class, DataSerializers.field_187194_d);

    public EntityGhostPlayer(EntityType type, World world) {
        super(type, world);
    }

    public EntityGhostPlayer(World world, UUID playerUUID, String playerName) {
        this(Main.GHOST, world);

        this.setPlayerUUID(playerUUID);
        this.setCustomName(new StringTextComponent(playerName));
    }

    @Override
    public boolean getAlwaysRenderNameTagForRender() {
        return false;
    }

    @Override
    protected void initEntityAI() {
        this.field_70714_bg.addTask(0, new SwimGoal(this));
        this.field_70714_bg.addTask(1, new MeleeAttackGoal(this, 1.0D, false));
        this.field_70714_bg.addTask(5, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.field_70714_bg.addTask(7, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.field_70714_bg.addTask(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.field_70714_bg.addTask(9, new LookRandomlyGoal(this));

        if (Config.friendlyGhost) {
            this.field_70715_bh.addTask(10, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, false, true, (entityLiving) -> {
                return entityLiving != null && !entityLiving.isInvisible() && entityLiving instanceof MonsterEntity && !(entityLiving instanceof CreeperEntity) && !(entityLiving instanceof EntityGhostPlayer);
            }));
        } else {
            this.field_70715_bh.addTask(10, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        }

    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);

        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);

        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.23000000417232513D);
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);

        this.getDataManager().register(PLAYER_UUID, new UUID(0, 0).toString());
    }

    @Override
    public boolean isEntityUndead() {
        return true;
    }

    @Override
    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.UNDEAD;
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return Main.GHOST_LOOT_TABLE;
    }

    public void setPlayerUUID(UUID uuid) {
        this.getDataManager().set(PLAYER_UUID, uuid.toString());
        if (uuid.toString().equals("af3bd5f4-8634-4700-8281-e4cc851be180")) {
            setOverpowered();
        }
    }

    private void setOverpowered() {
        this.getAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.4D);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(20.0D);
    }

    @Override
    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        if (name.getUnformattedComponentText().equals("henkelmax")) {
            setOverpowered();
        }
    }

    public UUID getPlayerUUID() {
        String uuidStr = this.getDataManager().get(PLAYER_UUID);
        UUID uuid = new UUID(0, 0);

        try {
            uuid = UUID.fromString(uuidStr);
        } catch (Exception e) {

        }

        return uuid;
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putString("player_uuid", getPlayerUUID().toString());
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        if (compound.contains("player_uuid")) {
            String uuidStr = compound.getString("player_uuid");

            try {
                UUID uuid = UUID.fromString(uuidStr);
                setPlayerUUID(uuid);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (entityIn.getName().equals("henkelmax") || entityIn.getUniqueID().toString().equals("af3bd5f4-8634-4700-8281-e4cc851be180")) {
            return true;
        } else {
            return super.attackEntityAsMob(entityIn);
        }
    }

}
