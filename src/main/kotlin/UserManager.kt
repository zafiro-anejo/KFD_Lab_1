open class UserManager {
    private val userBalances = mutableMapOf<String, MutableMap<String, Long>>()

    protected fun validateUserNotExists(userName: String) {
        if (userExistenceStatus(userName)) {
            throw IllegalArgumentException("[ОШИБКА]Пользователь ${userName} уже существует!")
        }
    }

    protected fun validateUserExists(userName: String) {
        if (!userExistenceStatus(userName)) {
            throw IllegalArgumentException("[ОШИБКА]Пользователь ${userName} не существует!")
        }
    }

    fun userExistenceStatus(userName: String): Boolean {
        return userName in userBalances
    }


    fun registerUser(userName: String): Unit {
        validateUserNotExists(userName)

        userBalances[userName] = mutableMapOf(
            "RUB" to 10000000000L,
            "USD" to 10000000000L,
            "EUR" to 10000000000L
        )
    }

    fun receiveUserBalance(userName: String): MutableMap<String, Long> {
        validateUserExists(userName)

        val moneyData = userBalances[userName]!!

        return moneyData

    }

    fun userBalanceDisplay(userName: String): Map<String, MoneyRepresentation> {
        validateUserExists(userName)

        val moneyDataForDisplay = userBalances[userName]!!.mapValues { (currency, moneyQuantity) -> MoneyRepresentation(moneyQuantity, currency) }

        return moneyDataForDisplay
    }

    fun updateUserBalance(userName: String, newBalances: Map<String, Long>): Unit {
        validateUserExists(userName)

        userBalances[userName]!!.putAll(newBalances)

    }

}