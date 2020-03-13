package com.sjet.auracascade.client.particles;

import com.sjet.auracascade.AuraCascade;
import com.sjet.auracascade.client.particles.auratransferparticle.AuraTransferParticleData;
import com.sjet.auracascade.client.particles.auratransferparticle.AuraTransferParticleType;
import com.sjet.auracascade.client.particles.nodeconnectionparticle.NodeConnectionParticleData;
import com.sjet.auracascade.client.particles.nodeconnectionparticle.NodeConnectionParticleType;
import net.minecraft.client.Minecraft;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

import static com.sjet.auracascade.setup.CommonSetup.register;

@Mod.EventBusSubscriber(modid = AuraCascade.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModParticles {

    @ObjectHolder(AuraCascade.MODID + ":node_connect") public static ParticleType<NodeConnectionParticleData> NODE_CONNECT;
    @ObjectHolder(AuraCascade.MODID + ":aura_transfer") public static ParticleType<AuraTransferParticleData> AURA_TRANSFER;

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> evt) {
        register(evt.getRegistry(), new NodeConnectionParticleType(), "node_connect");
        register(evt.getRegistry(), new AuraTransferParticleType(), "aura_transfer");
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = AuraCascade.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class FactoryHandler {
        @SubscribeEvent
        public static void registerFactories(ParticleFactoryRegisterEvent evt) {
            Minecraft.getInstance().particles.registerFactory(ModParticles.NODE_CONNECT, NodeConnectionParticleType.FACTORY::new);
            Minecraft.getInstance().particles.registerFactory(ModParticles.AURA_TRANSFER, AuraTransferParticleType.FACTORY::new);
        }
    }
}