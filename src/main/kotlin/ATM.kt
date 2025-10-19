class ATM : UserManager() {
    private val exchangeRates = mutableMapOf<ExchangeLogic, Long>()

    init {
        currenciesInitialization()
    }

    private fun currenciesInitialization() {

        val currencies = listOf(
            ExchangeLogic("RUB", "USD") to 8082L,
            ExchangeLogic("RUB", "EUR") to 9371L,
            ExchangeLogic("USD", "EUR") to 116L,
            ExchangeLogic("RUB", "BTC") to 3_500_000_000L,
            ExchangeLogic("RUB", "ETH") to 200_000_000L,
            ExchangeLogic("USD", "BTC") to 43_000_000L,
            ExchangeLogic("USD", "ETH") to 2_500_000L,
            ExchangeLogic("EUR", "BTC") to 39_000_000L,
            ExchangeLogic("EUR", "ETH") to 2_200_000L,
            ExchangeLogic("BTC", "ETH") to 58_000_000L
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