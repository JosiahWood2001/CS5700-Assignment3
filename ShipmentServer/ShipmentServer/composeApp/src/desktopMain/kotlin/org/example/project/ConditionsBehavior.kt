package org.example.project

interface ConditionsBehavior {
    fun checkUpdateConditions(update: ShippingUpdate, shipment: Shipment)
}

object ExpressConditions : ConditionsBehavior {
    override fun checkUpdateConditions(update: ShippingUpdate, shipment: Shipment){

    }
}

object OvernightConditions : ConditionsBehavior {
    override fun checkUpdateConditions(update: ShippingUpdate, shipment: Shipment){

    }
}

object BulkConditions : ConditionsBehavior {
    override fun checkUpdateConditions(update: ShippingUpdate, shipment: Shipment){

    }
}

object StandardConditions : ConditionsBehavior {
    override fun checkUpdateConditions(update: ShippingUpdate, shipment: Shipment){

    }
}