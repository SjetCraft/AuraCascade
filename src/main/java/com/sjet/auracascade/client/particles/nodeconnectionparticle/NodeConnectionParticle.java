package com.sjet.auracascade.client.particles.nodeconnectionparticle;

import net.minecraft.client.particle.IAnimatedSprite;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.world.World;

public class NodeConnectionParticle extends SpriteTexturedParticle {
    private double sourceX;
    private double sourceY;
    private double sourceZ;
    private IAnimatedSprite spriteSet;

    public NodeConnectionParticle(World world, double sourceX, double sourceY, double sourceZ,
                                  double xSpeed, double ySpeed, double zSpeed, float size, float maxAge,
                                  IAnimatedSprite spriteSet) {
        super(world, sourceX, sourceY, sourceZ);

        motionX = xSpeed;
        motionY = ySpeed;
        motionZ = zSpeed;

        //color is rgb float from 0 to 1
        setColor(1F, 1F, 1F);

        particleGravity = 0;
        this.setGravity(0f);

        this.maxAge = Math.round(maxAge);

        this.particleScale = size;
        setSize(size, size);

        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;

        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
        this.canCollide = false;

        this.spriteSet = spriteSet;
        selectSpriteRandomly(spriteSet);

        this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += motionX * 0.4D;
        this.motionY += motionY * 0.4D;
        this.motionZ += motionZ * 0.4D;

        //random movement
        motionX += 0.02 * rand.nextFloat() * (rand.nextFloat() > 0.5 ? 1 : -1);
        motionY += 0.02 * rand.nextFloat() * (rand.nextFloat() > 0.3 ? 1 : -1);
        motionZ += 0.02 * rand.nextFloat() * (rand.nextFloat() > 0.5 ? 1 : -1);
    }

    @Override
        public IParticleRenderType getRenderType() {
            return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
        }

    public void setGravity(float value) {
        particleGravity = value;
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.age++ >= this.maxAge) {
            this.setExpired();
        } else {
            this.move(this.motionX, this.motionY, this.motionZ);

            //random movement
            motionX += 0.002 * rand.nextFloat() * (rand.nextFloat() > 0.5 ? 1 : -1);
            motionY += 0.002 * rand.nextFloat() * (rand.nextFloat() > 0.3 ? 1 : -1);
            motionZ += 0.002 * rand.nextFloat() * (rand.nextFloat() > 0.5 ? 1 : -1);

            //randomly age the particle
            this.age += (rand.nextFloat() > 0.5 ? 0 : 2);
            selectSpriteWithAge(spriteSet);
        }
    }
}
