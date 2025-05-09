package com.bdcraft.plugin.events;

import com.bdcraft.plugin.BDCraft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manages the internal event system for BDCraft.
 * This system is completely self-contained and does not rely on any external event systems.
 */
public class BDEventManager {
    private final BDCraft plugin;
    private final Logger logger;
    private final Map<Class<? extends BDEvent>, List<Consumer<? extends BDEvent>>> eventListeners = new HashMap<>();
    
    /**
     * Creates a new event manager.
     * 
     * @param plugin The plugin instance
     */
    public BDEventManager(BDCraft plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
    }
    
    /**
     * Registers a listener for an event.
     * 
     * @param <T> The event type
     * @param eventClass The event class
     * @param listener The listener
     */
    public <T extends BDEvent> void registerListener(Class<T> eventClass, Consumer<T> listener) {
        eventListeners.computeIfAbsent(eventClass, k -> new CopyOnWriteArrayList<>()).add(listener);
    }
    
    /**
     * Unregisters a listener for an event.
     * 
     * @param <T> The event type
     * @param eventClass The event class
     * @param listener The listener
     */
    @SuppressWarnings("unchecked")
    public <T extends BDEvent> void unregisterListener(Class<T> eventClass, Consumer<T> listener) {
        List<Consumer<? extends BDEvent>> listeners = eventListeners.get(eventClass);
        
        if (listeners != null) {
            listeners.remove(listener);
        }
    }
    
    /**
     * Fires an event.
     * 
     * @param <T> The event type
     * @param event The event
     * @return The event
     */
    @SuppressWarnings("unchecked")
    public <T extends BDEvent> T fireEvent(T event) {
        List<Consumer<? extends BDEvent>> listeners = eventListeners.get(event.getClass());
        
        if (listeners == null || listeners.isEmpty()) {
            return event;
        }
        
        for (Consumer<? extends BDEvent> listener : listeners) {
            try {
                Consumer<T> typedListener = (Consumer<T>) listener;
                typedListener.accept(event);
                
                if (event.isCancelled()) {
                    break;
                }
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error processing event " + event.getEventName(), e);
            }
        }
        
        return event;
    }
    
    /**
     * Clears all event listeners.
     */
    public void clearListeners() {
        eventListeners.clear();
    }
    
    /**
     * Gets the number of registered listeners for an event.
     * 
     * @param eventClass The event class
     * @return The number of listeners
     */
    public int getListenerCount(Class<? extends BDEvent> eventClass) {
        List<Consumer<? extends BDEvent>> listeners = eventListeners.get(eventClass);
        return listeners != null ? listeners.size() : 0;
    }
}