package com.sjet.auracascade.client.particles.nodeconnectionparticle;

import net.minecraft.client.particle.*;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class NodeConnectionParticleType extends ParticleType<NodeConnectionParticleData> {

    public NodeConnectionParticleType() {
        super(false, NodeConnectionParticleData.DESERIALIZER);
    }

    @OnlyIn(Dist.CLIENT)
    public static class FACTORY  implements IParticleFactory<NodeConnectionParticleData> {
        public final IAnimatedSprite spriteSet;

        public FACTORY (IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(NodeConnectionParticleData data, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            NodeConnectionParticle particle = new NodeConnectionParticle(world, x, y, z, xSpeed, ySpeed, zSpeed, data.size, data.maxAge, spriteSet);
            particle.selectSpriteRandomly(this.spriteSet);
            return particle;
        }
    }
}
