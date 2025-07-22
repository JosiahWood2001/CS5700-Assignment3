package org.example.project

import junit.framework.TestCase.assertTrue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ObserverPatternTest {

    @Test
    fun testObserverIsRegisteredAndNotified() {
        val shipment = Shipment("123")
        var wasUpdated = false

        val observer = object : Observer {
            override fun update() {
                wasUpdated = true
            }
        }
        shipment.registerObserver(observer)
        shipment.notifyObserver()

        assertTrue(wasUpdated)
    }

    @Test
    fun testObserverIsRemovedAndNotNotified() {
        val shipment = Shipment("456")
        var wasUpdated = false

        val observer = object : Observer {
            override fun update() {
                wasUpdated = true
            }
        }

        shipment.registerObserver(observer)
        shipment.removeObserver(observer)
        shipment.notifyObserver()

        assertFalse(wasUpdated)
    }

    @Test
    fun testDuplicateObserverIsNotAdded() {
        val shipment = Shipment("789")
        var count = 0
        val observer = object : Observer {
            override fun update() {
                count++
            }
        }

        shipment.registerObserver(observer)
        shipment.registerObserver(observer)
        shipment.notifyObserver()
        assertEquals(1, count)
    }
}