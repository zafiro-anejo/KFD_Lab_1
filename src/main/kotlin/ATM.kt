class ATM : UserManager() {
    private val exchangeRates = mutableMapOf<ExchangeLogic, Long>()

    init {
        currenciesInitialization()
    }

    private fun currenciesInitialization(): Unit {

        val currencies = listOf(
            ExchangeLogic("RUB", "USD") to 8082L,
            ExchangeLogic("RUB", "EUR") to 9371L,
            ExchangeLogic("USD", "EUR") to 116L
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

        val exchangeRate = exchangeRates[rateExchangeKey]!!

        val receivedMoneyQuantity = if (rateExchangeKey.initialCurrency == initialCurrency) {
            (moneyQuantity * 100) / exchangeRate
        } else {
            (moneyQuantity * exchangeRate) / 100
        }

        val newBalances = balances.toMutableMap()

        newBalances[initialCurrency] = newBalances[initialCurrency]!! - moneyQuantity
        newBalances[demandedCurrency] = (newBalances[demandedCurrency] ?: 0L) + receivedMoneyQuantity

        updateUserBalance(userName, newBalances)

        updateExchangeRate(rateExchangeKey)

        return true
    }

    private fun updateExchangeRate(exchangeCurrenciesPair: ExchangeLogic) {
        val currentExchangeRate = exchangeRates[exchangeCurrenciesPair]!!
        val percentageChange = (-5..6).random()
        val newExchangeRate = currentExchangeRate + (currentExchangeRate * percentageChange) / 100

        exchangeRates[exchangeCurrenciesPair] = maxOf(newExchangeRate, 1L)
    }

    fun exchangeRatesDisplay(): Map<ExchangeLogic, String> {
        val exchangeRatesData = exchangeRates.mapValues { (exchangeCurrenciesPair, rateExchange) ->
            "1 ${exchangeCurrenciesPair.demandedCurrency} = ${"%.2f".format(rateExchange / 100.0)} ${exchangeCurrenciesPair.initialCurrency}"
        }

        return exchangeRatesData
    }

}