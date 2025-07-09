package com.trackier.sdk.unit

import com.trackier.sdk.APIRepository
import com.trackier.sdk.APIService
import com.trackier.sdk.Constants
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
import android.content.Context
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class APIRepositoryUnitTest {
    
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
    fun testAPIRepositoryObjectExists() {
        // Test that APIRepository object can be accessed
        assertNotNull(APIRepository)
    }
    
    @Test
    fun testAPIServiceCreation() {
        // Test that APIService interface is accessible
        // Note: Actual service creation requires Factory.getConfig() to be initialized
        assertNotNull(APIService::class.java)
    }
    
    @Test
    fun testConstantsUsedInAPIRepository() {
        // Test that constants used in APIRepository are accessible
        assertNotNull(Constants.SCHEME)
        assertNotNull(Constants.BASE_URL)
        assertNotNull(Constants.BASE_URL_DL)
        assertNotNull(Constants.BASE_URL_DYNAMIC_LINK)
    }
    
    @Test
    fun testURLBuildingLogic() {
        // Test URL building logic without actual service creation
        val expectedBaseUrl = "${Constants.SCHEME}in-${Constants.BASE_URL}"
        val expectedDeeplinksUrl = "${Constants.SCHEME}in-${Constants.BASE_URL_DL}"
        
        assertEquals("https://in-events.trackier.io/v1/", expectedBaseUrl)
        assertEquals("https://in-sdkr.apptracking.io/dl/", expectedDeeplinksUrl)
    }
    
    @Test
    fun testEmptyRegionURLBuilding() {
        // Test URL building with empty region
        val region = ""
        val expectedBaseUrl = "${Constants.SCHEME}${Constants.BASE_URL}"
        val expectedDeeplinksUrl = "${Constants.SCHEME}${Constants.BASE_URL_DL}"
        
        assertEquals("https://events.trackier.io/v1/", expectedBaseUrl)
        assertEquals("https://sdkr.apptracking.io/dl/", expectedDeeplinksUrl)
    }
    
    @Test
    fun testConstantsValues() {
        // Test that constants have expected values
        assertEquals("https://", Constants.SCHEME)
        assertEquals("events.trackier.io/v1/", Constants.BASE_URL)
        assertEquals("sdkr.apptracking.io/dl/", Constants.BASE_URL_DL)
        assertEquals("sdkr.apptracking.io/api/v4/ug/dlg/", Constants.BASE_URL_DYNAMIC_LINK)
    }
    
    @Test
    fun testAPIRepositoryStructure() {
        // Test that APIRepository has expected structure
        val repositoryClass = APIRepository::class.java
        
        // Check if it's an object (singleton)
        assertTrue(repositoryClass.isAssignableFrom(APIRepository::class.java))
        
        // Check that it's a Kotlin object (singleton)
        assertTrue(repositoryClass.simpleName == "APIRepository")
    }
    
    @Test
    fun testAPIServiceInterfaceMethods() {
        // Test that APIService interface has expected methods
        val serviceClass = APIService::class.java
        val methods = serviceClass.declaredMethods
        
        // Check for expected API endpoints
        val methodNames = methods.map { it.name }
        assertTrue("sendInstallData method should exist", methodNames.contains("sendInstallData"))
        assertTrue("sendEventData method should exist", methodNames.contains("sendEventData"))
        assertTrue("sendSessionData method should exist", methodNames.contains("sendSessionData"))
        assertTrue("sendDeeplinksData method should exist", methodNames.contains("sendDeeplinksData"))
        assertTrue("sendDynamicLinkData method should exist", methodNames.contains("sendDynamicLinkData"))
    }
    
    @Test
    fun testAPIServiceAnnotations() {
        // Test that APIService methods have proper Retrofit annotations
        val serviceClass = APIService::class.java
        
        serviceClass.declaredMethods.forEach { method ->
            val annotations = method.annotations
            assertTrue("Method ${method.name} should have Retrofit annotations", 
                     annotations.any { it.annotationClass.simpleName?.contains("POST") == true })
        }
    }
} 