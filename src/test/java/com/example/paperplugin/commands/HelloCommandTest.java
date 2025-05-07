package com.example.paperplugin.commands;

import com.example.paperplugin.PaperPlugin;
import com.example.paperplugin.config.PluginConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

/**
 * Test class for the HelloCommand.
 */
@ExtendWith(MockitoExtension.class)
public class HelloCommandTest {

    @Mock
    private PaperPlugin plugin;
    
    @Mock
    private Logger logger;
    
    @Mock
    private PluginConfig pluginConfig;
    
    @Mock
    private CommandSender consoleSender;
    
    @Mock
    private Player player;
    
    @Mock
    private Command command;
    
    private HelloCommand helloCommand;

    @BeforeEach
    void setUp() {
        // Set up mocks
        when(plugin.getLogger()).thenReturn(logger);
        when(plugin.getPluginConfig()).thenReturn(pluginConfig);
        when(pluginConfig.getGreetingFormat()).thenReturn("Hello, {player}! Welcome to your Paper 1.21 plugin!");
        when(player.getName()).thenReturn("TestPlayer");
        
        // Create the command instance
        helloCommand = new HelloCommand(plugin);
    }

    @Test
    @DisplayName("Test hello command execution from console")
    void testExecuteFromConsole() {
        // Execute the command as console
        boolean result = helloCommand.onCommand(consoleSender, command, "hello", new String[]{});
        
        // Verify the result and interactions
        assertTrue(result, "Command should execute successfully");
        verify(consoleSender).sendMessage(anyString());
        verify(logger).info("Console executed the hello command");
    }

    @Test
    @DisplayName("Test hello command execution from player")
    void testExecuteFromPlayer() {
        // Execute the command as a player
        boolean result = helloCommand.onCommand(player, command, "hello", new String[]{});
        
        // Verify the result and interactions
        assertTrue(result, "Command should execute successfully");
        verify(player).sendMessage(any());
        verify(logger).info("Player TestPlayer executed the hello command");
    }
}
