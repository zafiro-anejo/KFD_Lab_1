class ATM : UserManager() {
    private val exchangeRates = mutableMapOf<ExchangeLogic, Long>()

    init {
        currenciesInitialization()
    }

    private fun currenciesInitialization() {

        val currencies = listOf(
            ExchangeLogic("RUB", "USD") to 8140L,
            ExchangeLogic("RUB", "EUR") to 9494L,
            ExchangeLogic("USD", "EUR") to 117L,
            ExchangeLogic("RUB", "BTC") to 905361581L,
            ExchangeLogic("RUB", "ETH") to 33071731L,
            ExchangeLogic("USD", "BTC") to 11123100L,
            ExchangeLogic("USD", "ETH") to 405754L,
            ExchangeLogic("EUR", "BTC") to 9537206L,
            ExchangeLogic("EUR", "ETH") to 348385L,
            ExchangeLogic("BTC", "ETH") to 3646000L
        )

        exchangeRates.putAll(currencies)
    }

    private fun checkBalancesForMoney(balances: Map<String, Long>, initialCurrency: String, moneyQuantity: Long) {
        if ((balances[initialCurrency] ?: 0L) < moneyQuantity) {
            throw IllegalArgumentException("[ОШИБКА]Недостаточно средств для того, чтобы произвести обмен!")
        }
    }

    fun currenciesExchange(userName: String, initialCurrency: String, demandedCurrency: String, moneyQuantity: Long): Boolean {
        validateUserExists(userName)

        val balances = receiveUserBalance(userName)
        val exchangeCurrenciesPair = ExchangeLogic(initialCurrency, demandedCurrency)
        val rateExchangeKey = exchangeRates.keys.find { it == exchangeCurrenciesPair }
            ?: throw IllegalArgumentException("[ОШИБКА]Курс обмена для пары $initialCurrency/$demandedCurrency не существует!")

        checkBalancesForMoney(balances, initialCurrency, moneyQuantity)

        val exchangeRate = exchangeRates[rateExchangeKey]
            ?: throw IllegalStateException("[ОШИБКА]Курс обмена для пары $initialCurrency/$demandedCurrency не найден!")

        val receivedMoneyQuantity = if (rateExchangeKey.initialCurrency == initialCurrency) {
            (moneyQuantity * CurrencyLogic.getDivisor(demandedCurrency)) / exchangeRate
        } else {
            (moneyQuantity * exchangeRate) / CurrencyLogic.getDivisor(initialCurrency)
        }

        val newBalances = balances.toMutableMap()
        val currentInitialBalance = newBalances[initialCurrency]
            ?: throw IllegalStateException("[ОШИБКА]Баланс по валюте $initialCurrency не найден!")

        newBalances[initialCurrency] = currentInitialBalance - moneyQuantity
        newBalances[demandedCurrency] = (newBalances[demandedCurrency] ?: 0L) + receivedMoneyQuantity

        updateUserBalance(userName, newBalances)

        updateExchangeRate(rateExchangeKey)

        return true
    }

    private fun updateExchangeRate(exchangeCurrenciesPair: ExchangeLogic) {
        val currentExchangeRate = exchangeRates[exchangeCurrenciesPair]
            ?: throw IllegalStateException("[ОШИБКА]Курс обмена для пары ${exchangeCurrenciesPair.initialCurrency}/${exchangeCurrenciesPair.demandedCurrency} не найден!")
        val percentageChange = (-5..5).random()
        val newExchangeRate = currentExchangeRate + (currentExchangeRate * percentageChange) / 100

        exchangeRates[exchangeCurrenciesPair] = maxOf(newExchangeRate, 1L)
    }

    fun exchangeRatesDisplay(): Map<ExchangeLogic, String> {
        val exchangeRatesData = exchangeRates.mapValues { (exchangeCurrenciesPair, rateExchange) ->
            val divisor = CurrencyLogic.getDivisor(exchangeCurrenciesPair.initialCurrency)
            val integerPart = rateExchange / divisor
            val fractionalValue = rateExchange % divisor
            val fractionalPart = (fractionalValue * 100) / divisor

            "1 ${exchangeCurrenciesPair.demandedCurrency} = $integerPart.${fractionalPart.toString().padStart(2, '0')} ${exchangeCurrenciesPair.initialCurrency}"
        }

        return exchangeRatesData
    }
}