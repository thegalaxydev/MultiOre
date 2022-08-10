package mod.galaxy.multiore;

import com.mojang.logging.LogUtils;
import mod.galaxy.multiore.lootable.conditions.HasSilkTouch;
import mod.galaxy.multiore.lootable.conditions.RandomChanceWithLuck;
import mod.galaxy.multiore.lootable.conditions.ToolHasTier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import mod.galaxy.multiore.init.BlockInit;
import mod.galaxy.multiore.init.ItemInit;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("multiore")
public class MultiOre
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final String MOD_ID = "multiore";


    public static final CreativeModeTab MULTI_ORE_TAB = new CreativeModeTab(MOD_ID) {
        @Override
        @OnlyIn(Dist.CLIENT)
        public ItemStack makeIcon() {
            return new ItemStack(BlockInit.MULTI_ORE.get());
        }
    };



    public static class ConditionRegistry {
        public static void init() {}

        public static final LootItemConditionType RANDOM_CHANCE_WITH_LUCK = register("random_chance_with_luck", new RandomChanceWithLuck.LootSerializer());
        public static final LootItemConditionType ToolHasTier = register("tool_has_tier", new ToolHasTier.LootSerializer());
        public static final LootItemConditionType HAS_SILK_TOUCH = register("has_silk_touch", new HasSilkTouch.LootSerializer());

        private static LootItemConditionType register(String id, Serializer<? extends LootItemCondition> serializer) {
            return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(MOD_ID, id), new LootItemConditionType(serializer));
        }
    }

    public MultiOre()
    {



        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemInit.ITEMS.register(bus);
        BlockInit.BLOCKS.register(bus);

        bus.addListener(this::setup);

        bus.addListener(this::enqueueIMC);

        bus.addListener(this::processIMC);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {

    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        // Some example code to dispatch IMC to another mod
        InterModComms.sendTo("multiore", "imcdispatch", () -> { LOGGER.info("MultiOre IMC Dispatch"); return "MultiOre IMC Dispatch";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        // Some example code to receive and process InterModComms from other mods
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.messageSupplier().get()).
                collect(Collectors.toList()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("MultiOre Server Start Detected");
    }

    // You can use EventBusSubscriber to automatically subscribe events on the contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents
    {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent)
        {
        }
    }
}
