package com.trackier.sdk.unit

import com.trackier.sdk.Factory
import com.trackier.sdk.TrackierSDKConfig
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.lang.reflect.Field
import java.util.logging.Level
import android.content.Context
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.robolectric.RuntimeEnvironment

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class FactoryUnitTest {
    
    @Mock
    private lateinit var mockContext: Context
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        // Reset Factory singleton state
        resetFactorySingleton()
    }
    
    @After
    fun tearDown() {
        // Clean up if needed
    }
    
    private fun resetFactorySingleton() {
        try {
            val configField = Factory::class.java.getDeclaredField("config")
            configField.isAccessible = true
            configField.set(null, null)
        } catch (e: Exception) {
            // Ignore if field doesn't exist or can't be reset
        }
    }
    
    @Test
    fun testFactoryObjectExists() {
        // Test that Factory object can be accessed
        assertNotNull(Factory)
    }
    
    @Test
    fun testFactoryLoggerExists() {
        // Test that logger is accessible
        assertNotNull(Factory.logger)
    }
    
    @Test
    fun testFactoryLoggerInitialization() {
        // Test that logger is properly initialized
        val logger = Factory.logger
        assertNotNull(logger)
        assertNotNull(logger.name)
    }
    
    @Test
    fun testSetLogLevel() {
        // Test setting log level
        val originalLevel = Factory.logger.level
        Factory.setLogLevel(Level.FINEST)
        assertEquals(Level.FINEST, Factory.logger.level)
        
        // Reset to original level
        Factory.setLogLevel(originalLevel ?: Level.INFO)
    }
    
    @Test
    fun testSetConfig() {
        // Test setting config
        val context = RuntimeEnvironment.getApplication()
        val config = TrackierSDKConfig(context, "test_token", "sandbox")
        Factory.setConfig(config)
        
        // Verify config was set
        val retrievedConfig = Factory.getConfig()
        assertNotNull(retrievedConfig)
        assertEquals("test_token", retrievedConfig.appToken)
        assertEquals("sandbox", retrievedConfig.env)
    }
    
    @Test
    fun testSetConfigMultipleTimes() {
        // Test that setting config multiple times is handled properly
        val context = RuntimeEnvironment.getApplication()
        val config1 = TrackierSDKConfig(context, "token1", "sandbox")
        val config2 = TrackierSDKConfig(context, "token2", "production")
        
        Factory.setConfig(config1)
        Factory.setConfig(config2) // This should be ignored
        
        val retrievedConfig = Factory.getConfig()
        assertEquals("token1", retrievedConfig.appToken) // Should still be first config
    }
    
    @Test
    fun testGetConfigBeforeSet() {
        // Reset config to null
        resetFactorySingleton()
        
        // Test that getting config before it's set throws exception
        try {
            Factory.getConfig()
            fail("Should have thrown IllegalStateException")
        } catch (e: IllegalStateException) {
            assertEquals("TrackierSDKConfig is not initialized.", e.message)
        }
    }
    
    @Test
    fun testFactorySingletonPattern() {
        // Test that Factory is a singleton
        val factory1 = Factory
        val factory2 = Factory
        assertSame(factory1, factory2)
    }
    
    @Test
    fun testFactoryClassStructure() {
        // Test that Factory has expected structure
        val factoryClass = Factory::class.java
        
        // Check if it's an object (singleton)
        assertTrue(factoryClass.isAssignableFrom(Factory::class.java))
        
        // Check that it's a Kotlin object (singleton)
        assertTrue(factoryClass.simpleName == "Factory")
    }
    
    @Test
    fun testLoggerLevelChanges() {
        // Test different log levels
        val levels = listOf(Level.SEVERE, Level.WARNING, Level.INFO, Level.FINE, Level.FINEST)
        
        levels.forEach { level ->
            Factory.setLogLevel(level)
            assertEquals(level, Factory.logger.level)
        }
    }
    
    @Test
    fun testConfigPersistence() {
        // Test that config persists across multiple calls
        val context = RuntimeEnvironment.getApplication()
        val config = TrackierSDKConfig(context, "persistent_token", "sandbox")
        Factory.setConfig(config)
        
        // Call getConfig multiple times
        val config1 = Factory.getConfig()
        val config2 = Factory.getConfig()
        val config3 = Factory.getConfig()
        
        // All should be the same instance
        assertSame(config1, config2)
        assertSame(config2, config3)
        assertEquals("persistent_token", config1.appToken)
    }
    
    @Test
    fun testFactoryVolatileConfig() {
        // Test that config field is volatile (thread safety)
        val configField = Factory::class.java.getDeclaredField("config")
        assertTrue(java.lang.reflect.Modifier.isVolatile(configField.modifiers))
    }
} 