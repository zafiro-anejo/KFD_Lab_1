data class MoneyRepresentation(val moneyQuantity: Long, val currency: String) {

    override fun toString(): String {
        val divisor = CurrencyLogic.getDivisor(currency)
        val integerPart = moneyQuantity / divisor
        val fractionalPart = moneyQuantity % divisor

        val moneyDisplay = "$integerPart.${fractionalPart.toString().padStart(2, '0')}$currency"
        return moneyDisplay
    }
}