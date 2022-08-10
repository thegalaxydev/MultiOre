package mod.galaxy.multiore.lootable.conditions;

import mod.galaxy.multiore.MultiOre;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.HashMap;
import java.util.Map;

public class ToolHasTier implements LootItemCondition {
    private final Tier tier;

    public ToolHasTier(Tier tier) {
        this.tier = tier;
    }


    public LootItemConditionType getType() {
        return MultiOre.ConditionRegistry.ToolHasTier;
    }

    public boolean test(LootContext lootContext) {


        ItemStack itemStack = lootContext.getParamOrNull(LootContextParams.TOOL);
        Item item = itemStack.getItem();


        if (item instanceof TieredItem) {
            TieredItem tieredItem  = (TieredItem) item;
            var tieredItemTier = tieredItem.getTier();

            System.out.println("TIER OF ITEM: " + tieredItemTier);
            System.out.println("GIVEN TIER: " + this.tier);




            var lowers = TierSortingRegistry.getTiersLowerThan(this.tier);
            if (lowers.contains(tieredItemTier)) {
                return false;
            }

            else {
                return true;
            }
        }
        return false;
    }

    public static class LootSerializer implements Serializer<ToolHasTier> {
        public void serialize(JsonObject JSONObject, ToolHasTier toolHasTier, JsonSerializationContext JSONSerializationContext) {
            JSONObject.addProperty("tier", TierSortingRegistry.getName(toolHasTier.tier).getNamespace());
        }

        public ToolHasTier deserialize(JsonObject JSONObject, JsonDeserializationContext jsonDeserializationContext) {
            final String tierString = GsonHelper.getAsString(JSONObject, "tier");

            return new ToolHasTier(TierSortingRegistry.byName(new ResourceLocation(tierString)));
        }
    }
}
