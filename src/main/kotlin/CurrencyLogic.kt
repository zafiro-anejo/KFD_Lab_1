class CurrencyLogic {
    companion object {

        private val currencyDivisors = mapOf(
            "RUB" to 1_00L,
            "USD" to 1_00L,
            "EUR" to 1_00L,
            "BTC" to 1_000_000_00L,
            "ETH" to 10_000_000_000_000_000_00L
        )

        fun getDivisor(currency: String): Long {
            return currencyDivisors[currency] ?: 100L
        }
    }
}