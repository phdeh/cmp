interface Val {
    val value: Int

    operator fun plus(other: Val): Val
    operator fun minus(other: Val): Val
    operator fun times(other: Val): Val
    operator fun div(other: Val): Val
}