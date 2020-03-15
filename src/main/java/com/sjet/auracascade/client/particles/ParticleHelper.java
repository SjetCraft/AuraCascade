package com.sjet.auracascade.client.particles;

import com.sjet.auracascade.client.particles.auratransferparticle.AuraTransferParticle;
import com.sjet.auracascade.client.particles.auratransferparticle.AuraTransferParticleData;
import com.sjet.auracascade.client.particles.nodeconnectionparticle.NodeConnectionParticleData;
import com.sjet.auracascade.common.api.IAuraColor;
import com.sjet.auracascade.common.util.Common;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

public class ParticleHelper {

    /**
     * This helper function will spawn particles from the source position to the target position.
     * Intended to be used as an indicator to what other nodes the placed node connected to.
     *
     * @param world
     * @param source source block from where the particles will spawn
     * @param target target block where the particles will travel to
     */
    public static void sendConnectionParticlesToTarget(World world, BlockPos source, BlockPos target) {
        Random rand = new Random();
        //delta is the area where the particles will travel through
        double delta = Common.getDistance(source, target) * 3;
        //the deltaX,Y,Z is the control to extract the direction
        double deltaX = (target.getX() - source.getX()) / delta;
        double deltaY = (target.getY() - source.getY()) / delta;
        double deltaZ = (target.getZ() - source.getZ()) / delta;
        //0.5 is the center of the block
        double sourceX = source.getX() + 0.5;
        double sourceY = source.getY() + 0.5;
        double sourceZ = source.getZ() + 0.5;

        int density = 1;// + (auraAmount / 500);

        //spawns particles through delta-1 areas
        for (int i = 0; i < delta; i++) {

            float randomSize = 0.1f + (0.105f - 0.1f) * rand.nextFloat();

            int randomAge = 20 + (rand.nextInt(6));

            NodeConnectionParticleData particle = NodeConnectionParticleData.nodeConnectionParticle( 0, 0, 0, randomSize, randomAge);
            world.addParticle(particle, sourceX, sourceY, sourceZ, deltaX * rand.nextDouble(), deltaY * rand.nextDouble(), deltaZ * rand.nextDouble());

            //moves the particle generation by delta distance
            if (deltaX != 0) {
                sourceX += deltaX;
            } else if (deltaY != 0) {
                sourceY += deltaY;
            } else if (deltaZ != 0) {
                sourceZ += deltaZ;
            }
        }
    }

    public static void transferAura(World world, BlockPos source, BlockPos target, IAuraColor color, int auraAmount) {

        Random rand = new Random();
        //delta is the area where the particles will travel through
        double delta = Common.getDistance(source, target) * 6;
        //the deltaX,Y,Z is the control to extract the direction
        double deltaX = (target.getX() - source.getX()) / delta;
        double deltaY = (target.getY() - source.getY()) / delta;
        double deltaZ = (target.getZ() - source.getZ()) / delta;
        //0.5 is the center of the block
        double sourceX = source.getX() + 0.5;
        double sourceY = source.getY() + 0.5;
        double sourceZ = source.getZ() + 0.5;

        //Aura Color
        float r = color.getRed();
        float g = color.getGreen();
        float b = color.getBlue();

        int density = 1;// + (auraAmount / 500);

        //spawns particles through delta-1 areas
        for (int i = 0; i < delta; i++) {

            float randomSize = 0.05f + (0.085f - 0.13f) * rand.nextFloat();

            int randomAge = 14 + (rand.nextInt(5));

            AuraTransferParticleData particle = AuraTransferParticleData.auraTransferParticle( 0, 0, 0, randomSize, r, g, b, randomAge);
            world.addParticle(particle, sourceX, sourceY, sourceZ, deltaX * rand.nextDouble(), deltaY * rand.nextDouble(), deltaZ * rand.nextDouble());

            //moves the particle generation by delta distance
            if (deltaX != 0) {
                sourceX += deltaX;
            } else if (deltaY != 0) {
                sourceY += deltaY;
            } else if (deltaZ != 0) {
                sourceZ += deltaZ;
            }
        }

        /*
        Vec3d velocity = new Vec3d(target.subtract(source));
        velocity = velocity.normalize();
        double dist = Math.sqrt(target.distanceSq(source));

        int density = 5;
        for (int count = 0; count < dist * density; count++) {
            double i = ((double) count) / density;
            double xp = source.getX() + (velocity.x * i) + .5;
            double yp = source.getY() + (velocity.y * i) + .5;
            double zp = source.getZ() + (velocity.z * i) + .5;
            AuraTransferParticleData particle = AuraTransferParticleData.auraTransferParticle(target.getX() + .5, target.getY() + .5, target.getZ() + .5, 0.02F, color.getRed(), color.getGreen(), color.getBlue(), 20);
            world.addParticle(particle, xp, yp, zp, velocity.x * .1, velocity.y * .1, velocity.z * .1);
        }
        */
    }
}