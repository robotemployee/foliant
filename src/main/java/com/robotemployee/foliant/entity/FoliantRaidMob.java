package com.robotemployee.foliant.entity;

import com.robotemployee.foliant.FoliantRaid;
import com.robotemployee.reu.util.MobUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.UUID;

public abstract class FoliantRaidMob extends Monster {

    protected FoliantRaidMob(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    long tickCreated;
    public static final int TICKS_UNTIL_DIE_OF_OLD_AGE = 720000;

    protected FoliantRaid parentRaid;
    protected boolean needsParentRaid = true;

    // im spaced out okay
    protected final ArrayList<WeakReference<DevilEntity>> devilsProtectingMe = new ArrayList<>();
    @Nullable
    public FoliantRaid getParentRaid() {
        return parentRaid;
    }

    public void setParentRaid(FoliantRaid raid) {
        parentRaid = raid;
    }

    public boolean isInRaid() {
        return getParentRaid() != null;
    }

    public boolean needsParentRaid() {
        return needsParentRaid;
    }

    public void makeIndependentOfRaid() {
        needsParentRaid = false;
        parentRaid = null;
    }

    public void init(@NotNull FoliantRaid parentRaid) {
        setParentRaid(parentRaid);
        if (isInRaid()) {
            applyPowerBuffs(getParentRaid().getPowerFloat());
        }
        tickCreated = level().getGameTime();
    }

    @Override
    public void onRemovedFromWorld() {
        super.onRemovedFromWorld();
        if (isInRaid()) removeFromRaid();
    }

    @Override
    public boolean removeWhenFarAway(double p_21542_) {
        return !this.hasCustomName() && !this.isPersistenceRequired();
    }

    protected boolean canDieOfOldAge() {
        return true;
    }

    // with overriding removeWhenFarAway and FoliantRaid's chunkloading, it is best to automatically remove us after a long time
    protected boolean shouldDieOfOldAge() {
        return canDieOfOldAge() && level().getGameTime() - tickCreated > TICKS_UNTIL_DIE_OF_OLD_AGE;
    }

    public void removeFromRaid() {
        if (!isInRaid()) return;
        getParentRaid().decrementPopulation(getEnemyType());
        setParentRaid(null);
    }

    protected void applyPowerBuffs(float power) {
        applyPowerHealthBuff(power);
        applyPowerDamageBuff(power);
        applyPowerSpeedBuff(power);
    }

    public boolean recievesHealthScalingWithPower() {
        return true;
    }
    public boolean recievesDamageScalingWithPower() {
        return true;
    }

    public boolean recievesSpeedScalingWithPower() {
        return true;
    }

    public static final UUID HEALTH_POWER_MODIFIER_UUID = UUID.fromString("4a2063fc-418d-40e2-adcb-e41011fca417");
    protected void applyPowerHealthBuff(float power) {
        if (!recievesHealthScalingWithPower()) return;
        if (power < 1) return;
        double multiplier = Math.min(6, 1 + (power * power) * 0.001);

        float healthFraction = getHealth() / getMaxHealth();

        getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier(
                HEALTH_POWER_MODIFIER_UUID,
                "Health bonus from raid power",
                multiplier - 1,
                AttributeModifier.Operation.MULTIPLY_BASE
        ));

        setHealth(healthFraction * getMaxHealth());
    }

    public static final UUID DAMAGE_POWER_MODIFIER_UUID = UUID.fromString("7470fc32-423b-4f09-a95e-64a53426bcde");
    protected void applyPowerDamageBuff(float power) {
        if (!recievesDamageScalingWithPower()) return;
        if (power < 1) return;
        double multiplier = Math.min(6, 1 + (power * power) * 0.001);

        AttributeInstance attackDamage = getAttribute(Attributes.ATTACK_DAMAGE);

        if (attackDamage == null) return;

        attackDamage.addPermanentModifier(new AttributeModifier(
                DAMAGE_POWER_MODIFIER_UUID,
                "Damage bonus from raid power",
                multiplier - 1,
                AttributeModifier.Operation.MULTIPLY_BASE
        ));
    }

    public static final UUID SPEED_POWER_MODIFIER_UUID = UUID.fromString("fb96b6eb-13d6-4abb-9122-dde22b4566e6");
    protected void applyPowerSpeedBuff(float power) {
        if (!recievesSpeedScalingWithPower()) return;
        if (power < 1) return;

        double multiplier = Math.pow(Math.pow(power * 0.08, 0.68) * 0.05 + 1, 1.5);

        AttributeInstance groundSpeed = getAttribute(Attributes.MOVEMENT_SPEED);

        if (groundSpeed != null) {
            groundSpeed.addPermanentModifier(new AttributeModifier(
                    SPEED_POWER_MODIFIER_UUID,
                    "Ground speed bonus from raid power",
                    multiplier - 1,
                    AttributeModifier.Operation.MULTIPLY_BASE
            ));
        }

        AttributeInstance flightSpeed = getAttribute(Attributes.FLYING_SPEED);

        if (flightSpeed != null) {
            flightSpeed.addPermanentModifier(new AttributeModifier(
                    SPEED_POWER_MODIFIER_UUID,
                    "Flight speed bonus from raid power",
                    multiplier - 1,
                    AttributeModifier.Operation.MULTIPLY_BASE
            ));
        }
    }

    // Devil Protection
    public void startProtectionFrom(@NotNull DevilEntity devil) {
        addDevilProtectingMe(devil);
    }
    // VERY IMPORTANT TO CALL WHENEVER YOU ARE NO LONGER BEING PROTECTED
    // todo make this get called when the Protection thingy stops
    public void stopProtectionFrom(@NotNull DevilEntity devil) {
        removeDevilProtectingMe(devil);
    }

    private void addDevilProtectingMe(@NotNull DevilEntity devil) {
        cleanDevilsProtectingMe();
        devilsProtectingMe.add(new WeakReference<>(devil));
    }

    private void removeDevilProtectingMe(@NotNull DevilEntity devil) {
        cleanDevilsProtectingMe();
        devilsProtectingMe.removeIf(reference -> reference.refersTo(devil));
    }

    private void cleanDevilsProtectingMe() {
        devilsProtectingMe.removeIf(reference -> {
            DevilEntity devil = reference.get();
            if (devil == null) return true;
            if (!MobUtils.entityIsValidForTargeting(devil)) return true;
            return false;
        });
    }
    public ArrayList<WeakReference<DevilEntity>> getDevilsProtectingMe() {
        cleanDevilsProtectingMe();
        return devilsProtectingMe;
    }
    public boolean isBeingProtected() {
        cleanDevilsProtectingMe();
        return devilsProtectingMe.size() > 0;
    }

    public boolean canDevilProtect() {
        return !isBeingProtected() && getDevilProtectionWeight() > 0;
    }
    public boolean canDevilGrantKnockbackResistance() {
        return canDevilProtect();
    }
    public int getDevilProtectionWeight() {
        return 1;
    }

    public abstract FoliantRaid.EnemyType getEnemyType();

    // This is for determining how effectively a creature is in fulfilling their role -
    // if a creature is very badly wounded, it should report a lower fulfillment rating.
    // 0 to 1.
    public float getFulfillment() {
        return (getHealth() / (2 * getMaxHealth())) + 0.5f;
    }

    // override this if you want something to be able to survive without a raid
    int ticksWantedParentRaidButAlone = 0;
    public static final int TICKS_UNTIL_DIE_OF_LONELINESS = 20;
    public boolean shouldIDieRightNow() {
        if (needsParentRaid() && !isInRaid()) ticksWantedParentRaidButAlone++;
        else ticksWantedParentRaidButAlone = 0;
        return ticksWantedParentRaidButAlone > TICKS_UNTIL_DIE_OF_LONELINESS && needsParentRaid() && (!isInRaid() || getParentRaid().isPoop());
    }

    public void tickPowerBuffs() {
        if (tickCount % 200 > 0) return;
        if (!isInRaid()) return;
        applyPowerBuffs(getParentRaid().getPowerFloat());
    }

    @Override
    public void tick() {
        super.tick();

        if (shouldIDieRightNow()) kill();

    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData groupData, @Nullable CompoundTag tag) {
        // this is horrible logic, the raid should tell its spawned entities that they need a raid parent
        // but whatever
        if (spawnType == MobSpawnType.SPAWN_EGG ||
                spawnType == MobSpawnType.COMMAND ||
                spawnType == MobSpawnType.DISPENSER ||
                spawnType == MobSpawnType.SPAWNER) {
            makeIndependentOfRaid();
        }
        return super.finalizeSpawn(level, difficulty, spawnType, groupData, tag);
    }
}
