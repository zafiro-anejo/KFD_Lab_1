data class MoneyRepresentation(val moneyQuantity: Long, val currency: String) {

    override fun toString(): String {

        val moneyDisplay: String = "%.2f%s".format(moneyQuantity / 100.00, currency)

        return moneyDisplay
    }
}