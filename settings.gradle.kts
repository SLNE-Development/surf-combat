@file:Suppress("UNCHECKED_CAST")

import dev.slne.surf.combat.gradle.CombatModule

pluginManagement {
    includeBuild("build-logic")
}

plugins {
    id("dev.slne.surf.combat.settings")
}

rootProject.name = "surf-combat"

include("surf-combat-api")
include("surf-combat-server")

// Modules
include("surf-combat-modules:surf-combat-module-loader")

CombatModule.entries.forEach { module ->
    val moduleName = module.moduleName

    module.availableScopes.forEach { scope ->
        include("surf-combat-modules:surf-combat-module-$moduleName:surf-combat-module-$moduleName-${scope.scopeName}")
    }
}