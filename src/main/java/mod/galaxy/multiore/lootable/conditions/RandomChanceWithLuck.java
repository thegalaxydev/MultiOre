package mod.galaxy.multiore.lootable.conditions;

import mod.galaxy.multiore.MultiOre;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

import java.util.function.IntPredicate;

public class RandomChanceWithLuck implements LootItemCondition {
    private final float luckModifier;
    private final float baseChance;
    private final LootContext.EntityTarget entity;


    public RandomChanceWithLuck(LootContext.EntityTarget entity, float baseChance, float luckModifier) {
        this.entity = entity;
        this.luckModifier = luckModifier;
        this.baseChance = baseChance;
    }

    @Override
    public LootItemConditionType getType() {
        return MultiOre.ConditionRegistry.RANDOM_CHANCE_WITH_LUCK;
    }

    @Override
    public boolean test(LootContext lootContext) {
        Entity target = lootContext.getParamOrNull(entity.getParam());
        float chance = this.baseChance;

        if(target instanceof LivingEntity) {
            if(((LivingEntity) target).hasEffect(MobEffects.LUCK)) {
                return lootContext.getRandom().nextFloat() < chance * this.luckModifier;
            }
            else {
                return lootContext.getRandom().nextFloat() < chance;
            }
        }

        return lootContext.getRandom().nextFloat() < chance;
    }

    public static class LootSerializer implements Serializer<RandomChanceWithLuck> {
        @Override
        public void serialize(JsonObject JSONObject, RandomChanceWithLuck randomChanceWithLuck, JsonSerializationContext JSONSerializationContext) {
            JSONObject.addProperty("entity", String.valueOf(JSONSerializationContext.serialize(randomChanceWithLuck.entity)));
            JSONObject.addProperty("luckModifier", randomChanceWithLuck.luckModifier);
            JSONObject.addProperty("baseChance", randomChanceWithLuck.baseChance);
        }

        public RandomChanceWithLuck deserialize(JsonObject JSONObject, JsonDeserializationContext jsonDeserializationContext) {
            return new RandomChanceWithLuck(GsonHelper.getAsObject(JSONObject, "entity", jsonDeserializationContext, LootContext.EntityTarget.class), GsonHelper.getAsFloat(JSONObject, "baseChance"), GsonHelper.getAsFloat(JSONObject, "luckModifier"));
        }
    }
}
