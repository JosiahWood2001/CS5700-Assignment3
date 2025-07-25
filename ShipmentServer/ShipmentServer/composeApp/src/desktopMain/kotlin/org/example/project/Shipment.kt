package org.example.project

class Shipment(
    shipmentId: String,
               shipmentType: String,
               shipmentConditional: ConditionsBehavior
) : Subject {
    //all variables are private set, setters are below
    var status: String = ""
        private set
    private val id: String = shipmentId
    var notes: MutableList<String> = mutableListOf()
        private set
    var updateHistory: MutableList<ShippingUpdate> = mutableListOf()
        private set
    var expectedDeliveryDateTimestamp: Long? = null
        private set
    var currentLocation: String = "Unknown"
        private set
    var shipmentType: String = shipmentType
    private set
    private val conditions: ConditionsBehavior = shipmentConditional

    //this will store a list of the observers that need to be notified
    private val observers: MutableSet<Observer> = mutableSetOf()

    override fun registerObserver(observer: Observer) {
        observers.add(observer)
    }

    override fun removeObserver(observer: Observer) {
        observers.remove(observer)

    }

    override fun notifyObserver() {
        for (observer in observers) {
            observer.update()
        }
    }

    fun setStatus(newStatus: String) {
        status = newStatus
        notifyObserver()
    }

    //shipment must be created with an id, kotlin restrict private set variables in the constructor
    fun getId(): String {
        return id
    }

    fun setNotes(newNotes: MutableList<String>) {
        notes = newNotes
        notifyObserver()
    }

    fun setUpdateHistory(newUpdateHistory: MutableList<ShippingUpdate>) {
        updateHistory = newUpdateHistory
        notifyObserver()
    }

    fun setExpectedDeliveryDateTimestamp(newExpectedDeliveryDateTimestamp: Long) {
        expectedDeliveryDateTimestamp = newExpectedDeliveryDateTimestamp
        notifyObserver()
    }

    fun setCurrentLocation(newCurrentLocation: String) {
        currentLocation = newCurrentLocation
        notifyObserver()
    }

    fun addNote(note: String) {
        notes.add(note)
        notifyObserver()
    }

    fun addUpdate(update: ShippingUpdate) {
        updateHistory.add(update)
        notifyObserver()
    }
}