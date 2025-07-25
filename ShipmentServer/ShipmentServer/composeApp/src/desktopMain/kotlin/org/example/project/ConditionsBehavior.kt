package org.example.project

private const val ONE_DAY_MS = 86_400_000L
private const val THREE_DAYS_MS = ONE_DAY_MS * 3

interface ConditionsBehavior {
    fun checkUpdateConditions(shipment: Shipment)
}

object ExpressConditions : ConditionsBehavior {
    override fun checkUpdateConditions(shipment: Shipment){
        shipment.expectedDeliveryDateTimestamp?.minus(shipment.createdTimeStamp)?.let {
            if (it > THREE_DAYS_MS) {
                shipment.setStatus("!Something went wrong!")
                shipment.addNote("An Express Shipment was updated to include a delivery date later than 3 days after it was created.")
            }
        }
    }
}

object OvernightConditions : ConditionsBehavior {
    override fun checkUpdateConditions(shipment: Shipment){
        shipment.expectedDeliveryDateTimestamp?.minus(shipment.createdTimeStamp)?.let {
            if (it > ONE_DAY_MS){
                shipment.setStatus("!Something went wrong!")
                shipment.addNote("An Overnight Shipment was updated to include a delivery date later than 1 day after it was created.")
            }
        }
    }
}

object BulkConditions : ConditionsBehavior {
    override fun checkUpdateConditions(shipment: Shipment){
        shipment.expectedDeliveryDateTimestamp?.minus(shipment.createdTimeStamp)?.let {
            println(it)
            if (it < THREE_DAYS_MS){
                shipment.setStatus("!Something went wrong!")
                shipment.addNote("A Bulk Shipment was updated to include a delivery date sooner than 3 days after it was created.")
            }
        }
    }
}

object StandardConditions : ConditionsBehavior {
    override fun checkUpdateConditions(shipment: Shipment){
    }
}