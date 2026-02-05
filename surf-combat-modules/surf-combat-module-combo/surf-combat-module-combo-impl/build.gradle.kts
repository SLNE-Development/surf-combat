import dev.slne.surf.combat.gradle.CombatModule
import dev.slne.surf.combat.gradle.CombatModuleScope
import dev.slne.surf.combat.gradle.combatModule

plugins {
    id("dev.slne.surf.surfapi.gradle.paper-raw")
    id("dev.slne.surf.combat")
}

dependencies {
    api(project(":surf-combat-api"))
    combatModule(CombatModule.COMBO, CombatModuleScope.API)
    combatModule(CombatModule.COMBATLOG, CombatModuleScope.API)
}