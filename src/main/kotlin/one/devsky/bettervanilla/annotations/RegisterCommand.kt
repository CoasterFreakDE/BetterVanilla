package one.devsky.bettervanilla.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RegisterCommand(
    val name: String,
    val description: String = "",
    val requiredPermission: String = "melion.default",
    val availableAliases: Array<out String> = []
)
