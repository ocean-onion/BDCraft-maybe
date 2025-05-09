package com.bdcraft.plugin.events;

/**
 * Base class for all BDCraft events.
 * These are internal events used for module communication
 * and are completely separate from Bukkit's event system.
 */
public abstract class BDEvent {
    private boolean cancelled = false;
    private final boolean cancellable;
    
    /**
     * Creates a new BD event.
     * 
     * @param cancellable Whether this event can be cancelled
     */
    public BDEvent(boolean cancellable) {
        this.cancellable = cancellable;
    }
    
    /**
     * Checks if this event is cancellable.
     * 
     * @return Whether this event is cancellable
     */
    public boolean isCancellable() {
        return cancellable;
    }
    
    /**
     * Checks if this event is cancelled.
     * 
     * @return Whether this event is cancelled
     */
    public boolean isCancelled() {
        return cancelled;
    }
    
    /**
     * Sets whether this event is cancelled.
     * 
     * @param cancelled Whether this event is cancelled
     * @throws IllegalStateException If this event is not cancellable
     */
    public void setCancelled(boolean cancelled) {
        if (!cancellable) {
            throw new IllegalStateException("This event cannot be cancelled");
        }
        
        this.cancelled = cancelled;
    }
    
    /**
     * Gets the name of this event.
     * 
     * @return The name of this event
     */
    public String getEventName() {
        return getClass().getSimpleName();
    }
}