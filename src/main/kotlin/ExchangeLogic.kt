data class ExchangeLogic(val initialCurrency: String, val demandedCurrency: String) {

    override fun equals(other: Any?): Boolean {

        if(this === other) return true

        val otherExchange = other as? ExchangeLogic ?: return false

        val lineCompare: Boolean = initialCurrency == otherExchange.initialCurrency && demandedCurrency == otherExchange.demandedCurrency
        val reverseCompare: Boolean = initialCurrency == otherExchange.demandedCurrency && demandedCurrency == otherExchange.initialCurrency

        val compareLogic: Boolean = lineCompare || reverseCompare

        return compareLogic
    }

    override fun hashCode(): Int {

        val hashData: Int = setOf(initialCurrency, demandedCurrency).hashCode()

        return hashData
    }
}