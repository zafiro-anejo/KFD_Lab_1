fun main() {
    val atm = ATM()


    atm.registerUser("Юзер")

    println("Изначальный баланс:")
    atm.userBalanceDisplay("Юзер").forEach { (currency, money) ->
        println("$currency: $money")
    }

    println("Изначальный курс валют:")
    atm.exchangeRatesDisplay().forEach { (exchange, rate) ->
        println("${exchange.initialCurrency} -> ${exchange.demandedCurrency}: $rate")
    }

    var count = 0
    try {
        while (true) {
            atm.currenciesExchange("Юзер", "RUB", "USD", 100000000L)
            count++
        }
    } catch (e: Exception) {
        println("Ошибка после $count операций: ${e.message}")
    }


    println("Финальный баланс:")
    atm.userBalanceDisplay("Юзер").forEach { (currency, money) ->
        println("$currency: $money")
    }

    println("Финальный Курс валют:")
    atm.exchangeRatesDisplay().forEach { (exchange, rate) ->
        println("${exchange.initialCurrency} -> ${exchange.demandedCurrency}: $rate")
    }
}
