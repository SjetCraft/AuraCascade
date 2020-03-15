package com.sjet.auracascade.client.particles.auratransferparticle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class AuraTransferParticleType extends ParticleType<AuraTransferParticleData> {

    public AuraTransferParticleType() {
        super(false, AuraTransferParticleData.DESERIALIZER);
    }

    @OnlyIn(Dist.CLIENT)
    public static class FACTORY implements IParticleFactory<AuraTransferParticleData> {
        public final IAnimatedSprite spriteSet;

        public FACTORY(IAnimatedSprite spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Override
        public Particle makeParticle(AuraTransferParticleData data, World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            AuraTransferParticle particle = new AuraTransferParticle(world, x, y, z, data.targetX, data.targetY, data.targetZ, xSpeed, ySpeed, zSpeed, data.size, data.r, data.g, data.b, data.maxAge, this.spriteSet);
            particle.selectSpriteRandomly(this.spriteSet);
            return particle;
        }
    }
}
