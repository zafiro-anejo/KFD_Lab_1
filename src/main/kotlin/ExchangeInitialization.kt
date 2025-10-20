class ExchangeInitialization {
    private val atm = ATM()
    private val userName: String = "Юзер"

    init {
        atm.registerUser(userName)
    }

    private fun currencyExchangeProcess() {
        val initialCurrencyString: String = enterStringValues("Введите валюту на обмен:")
        val demandedCurrencyString: String = enterStringValues("Введите желаемую валюту:")

        val currencyAmountInString: Long = try {
            enterStringValues("Введите сумму с точность до дробной части:").toLong()
        } catch (_: Exception) {
            0
        }

        try {
            atm.currenciesExchange(userName, initialCurrencyString, demandedCurrencyString, currencyAmountInString)
        } catch (e: Exception) {
            println("[ОШИБКА] НЕПРАВИЛЬНО УКАЗАНЫ ВАЛЮТНЫЕ ПАРЫ И/ИЛИ ПРЕВЫШЕНО КОЛИЧЕСТВО ВАЛЮТЫ НА ОБМЕН: ${e.message}")
        }


    }

    private fun enterStringValues(enteringString: String): String {
        println(enteringString)
        print("> ")

        val enteredString: String? = readlnOrNull()

        return enteredString ?: "miss"
    }

    fun currencyMenuDisplay() {



        while (true) {
            println("1. Показать баланс пользователя")
            println("2. Показать курсы валют")
            println("3. Обмен валют")
            println("0. Выход")

            print("> ")
            val menuOption = readln().toInt()

            when (menuOption) {
                1 -> atm.userBalanceDisplay("Юзер").forEach { (currency, money) -> println("$currency: $money") }
                2 -> atm.exchangeRatesDisplay().forEach { (exchange, rate) -> println("${exchange.initialCurrency} -> ${exchange.demandedCurrency}: $rate") }
                3 -> currencyExchangeProcess()
                0 -> return
                else -> println("[ОШИБКА] ВЫБРАННОЙ ОПЦИИ НЕ СУЩЕСТВУЕТ!")
            }
        }
    }


}