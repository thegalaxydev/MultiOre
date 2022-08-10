package mod.galaxy.multiore.lootable.conditions;

import mod.galaxy.multiore.MultiOre;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public class HasSilkTouch implements LootItemCondition {
    private final boolean shouldHave;


    public HasSilkTouch(boolean shouldHave) {
        this.shouldHave = shouldHave;
    }

    public LootItemConditionType getType() {
        return MultiOre.ConditionRegistry.HAS_SILK_TOUCH;
    }

    public boolean test(LootContext lootContext) {
        ItemStack itemStack = lootContext.getParamOrNull(LootContextParams.TOOL);

        if(itemStack != null) {
            if(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, itemStack) > 0) {
                return shouldHave;

            }
            else {
                return !shouldHave;
            }
        }

        return false;
    }

    public static class LootSerializer implements Serializer<HasSilkTouch> {
        @Override
        public void serialize(JsonObject JSONObject, HasSilkTouch hasSilkTouch, JsonSerializationContext JSONSerializationContext) {
            JSONObject.addProperty("shouldHave", hasSilkTouch.shouldHave);
        }

        public HasSilkTouch deserialize(JsonObject JSONObject, JsonDeserializationContext jsonDeserializationContext) {
            return new HasSilkTouch(GsonHelper.getAsBoolean(JSONObject, "shouldHave"));
        }
    }
}
