package dev.slne.surf.combat.gradle

import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project

fun DependencyHandlerScope.combatModule(
    module: CombatModule,
    moduleScope: CombatModuleScope = CombatModuleScope.API,
    scope: String = "api"
) {
    if (!module.availableScopes.contains(moduleScope)) {
        throw IllegalArgumentException("Module ${module.moduleName} does not have scope ${moduleScope.scopeName}")
    }

    add(
        scope,
        project(":surf-combat-modules:surf-combat-module-${module.moduleName}:surf-combat-module-${module.moduleName}-${moduleScope.scopeName}")
    )
}