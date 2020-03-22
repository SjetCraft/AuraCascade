package com.sjet.auracascade.client.particles;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.particles.auratransferparticle.AuraTransferParticleData;
import com.sjet.auracascade.client.particles.auratransferparticle.AuraTransferParticleType;
import com.sjet.auracascade.client.particles.nodeconnectionparticle.NodeConnectionParticleData;
import com.sjet.auracascade.client.particles.nodeconnectionparticle.NodeConnectionParticleType;
import com.sjet.auracascade.client.particles.pumptransferparticle.PumpTransferParticleData;
import com.sjet.auracascade.client.particles.pumptransferparticle.PumpTransferParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.ObjectHolder;

@Mod.EventBusSubscriber(modid = AuraCascade.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRegistry {

    @ObjectHolder(AuraCascade.MODID + ":node_connect") public static ParticleType<NodeConnectionParticleData> NODE_CONNECT;
    @ObjectHolder(AuraCascade.MODID + ":aura_transfer") public static ParticleType<AuraTransferParticleData> AURA_TRANSFER;
    @ObjectHolder(AuraCascade.MODID + ":pump_transfer") public static ParticleType<PumpTransferParticleData> PUMP_TRANSFER;

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> evt) {
        register(evt.getRegistry(), new NodeConnectionParticleType(), "node_connect");
        register(evt.getRegistry(), new AuraTransferParticleType(), "aura_transfer");
        register(evt.getRegistry(), new PumpTransferParticleType(), "pump_transfer");
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AuraCascade.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class FactoryHandler {
        @SubscribeEvent
        public static void registerFactories(ParticleFactoryRegisterEvent evt) {
            Minecraft.getInstance().particles.registerFactory(ParticleRegistry.NODE_CONNECT, NodeConnectionParticleType.FACTORY::new);
            Minecraft.getInstance().particles.registerFactory(ParticleRegistry.AURA_TRANSFER, AuraTransferParticleType.FACTORY::new);
            Minecraft.getInstance().particles.registerFactory(ParticleRegistry.PUMP_TRANSFER, PumpTransferParticleType.FACTORY::new);
        }
    }

    public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> reg, IForgeRegistryEntry<T> thing, ResourceLocation name) {
        reg.register(thing.setRegistryName(name));
    }

    public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> reg, IForgeRegistryEntry<T> thing, String name) {
        register(reg, thing, new ResourceLocation(AuraCascade.MODID, name));
    }
}