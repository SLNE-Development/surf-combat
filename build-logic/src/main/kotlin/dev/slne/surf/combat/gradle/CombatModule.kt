package dev.slne.surf.combat.gradle

enum class CombatModule(
    val moduleName: String,
    val availableScopes: List<CombatModuleScope> = listOf(
        CombatModuleScope.API,
        CombatModuleScope.IMPL
    ),
    val implementationScope: CombatModuleScope = CombatModuleScope.IMPL
) {
    COMBO("combo"),
    COMBATLOG("combatlog")
}