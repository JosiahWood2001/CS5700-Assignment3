package org.example.project

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.*
import java.io.File
import kotlin.test.DefaultAsserter.assertNotNull
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SimulationTest {
    private val trackingId = "TEST123"
    private val baseTime = System.currentTimeMillis()

    @BeforeEach
    fun clearShipments() {
        // Reset the internal shipment set via reflection or restart app state between tests if necessary.
        val field = TrackingSimulator::class.java.getDeclaredField("shipments")
        field.isAccessible = true
        val emptySet = mutableSetOf<Shipment>()
        field.set(TrackingSimulator, emptySet)
    }

    @Test
    fun `test add and find shipment`() {
        val shipment = Shipment(trackingId)
        TrackingSimulator.addShipment(shipment)
        val result = TrackingSimulator.findShipment(trackingId)
        assertEquals(trackingId, result?.getId())
    }

    @Test
    fun `test handlers map handles created update`() {
        val timestamp = baseTime
        val updateString = listOf("created", trackingId, timestamp.toString())
        handlers["created"]?.handleUpdate(updateString)
        val shipment = TrackingSimulator.findShipment(trackingId)
        assertEquals("created", shipment?.status)
        assertEquals(1, shipment?.updateHistory?.size)
    }

    @Test
    fun `test runSimulation from file`() { runBlocking {
        val testFile = File("test.txt")
        val testLines = listOf(
            "created,$trackingId,${baseTime}",
            "shipped,$trackingId,${baseTime + 10000},${baseTime + 30000}"
        )
        testFile.writeText(testLines.joinToString("\n"))

        TrackingSimulator.runSimulation()

        val shipment = TrackingSimulator.findShipment(trackingId)
        assertEquals("shipped", shipment?.status)
        assertEquals(2, shipment?.updateHistory?.size)

        // Clean up
        testFile.delete()
    }}

    @Test
    fun `test runSimulation with unknown update type does not crash`() { runBlocking {
        val testFile = File("test.txt")
        val testLines = listOf(
            "created,$trackingId,${baseTime}",
            "unknownType,$trackingId,${baseTime + 10000}"
        )
        testFile.writeText(testLines.joinToString("\n"))

        // Should run without exception
        assertDoesNotThrow {
            runBlocking {
                TrackingSimulator.runSimulation()
            }
        }

        val shipment = TrackingSimulator.findShipment(trackingId)
        assertEquals("created", shipment?.status)

        // Clean up
        testFile.delete()
    }}
}