package com.bdcraft.plugin.util;

import org.bukkit.Location;
import org.bukkit.Particle;

/**
 * Utility class to handle particles across different Minecraft versions.
 * This provides compatibility between API versions.
 */
public class ParticleHelper {
    
    /**
     * Spawns witch spell particles at a location.
     * 
     * @param location The location to spawn particles at
     * @param count The number of particles to spawn
     * @param offsetX The X offset of the particles
     * @param offsetY The Y offset of the particles
     * @param offsetZ The Z offset of the particles
     * @param speed The speed of the particles
     */
    public static void spawnWitchParticles(Location location, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        // Use basic particles that exist in all Minecraft versions
        location.getWorld().spawnParticle(Particle.SMOKE, location, count, offsetX, offsetY, offsetZ, speed);
    }
    
    /**
     * Spawns enchantment particles at a location.
     * 
     * @param location The location to spawn particles at
     * @param count The number of particles to spawn
     * @param offsetX The X offset of the particles
     * @param offsetY The Y offset of the particles
     * @param offsetZ The Z offset of the particles
     * @param speed The speed of the particles
     */
    public static void spawnEnchantmentParticles(Location location, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        // Use a safe particle type that's available in all Minecraft versions
        location.getWorld().spawnParticle(Particle.PORTAL, location, count, offsetX, offsetY, offsetZ, speed);
    }
    
    /**
     * Spawns totem particles at a location.
     * 
     * @param location The location to spawn particles at
     * @param count The number of particles to spawn
     * @param offsetX The X offset of the particles
     * @param offsetY The Y offset of the particles
     * @param offsetZ The Z offset of the particles
     * @param speed The speed of the particles
     */
    public static void spawnTotemParticles(Location location, int count, double offsetX, double offsetY, double offsetZ, double speed) {
        // Use a safe particle type that's available in all Minecraft versions
        location.getWorld().spawnParticle(Particle.FLAME, location, count, offsetX, offsetY, offsetZ, speed);
    }
}