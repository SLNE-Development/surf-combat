plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

repositories {
    gradlePluginPortal()
}

gradlePlugin {
    plugins {
        create("includeCombatModule") {
            id = "dev.slne.surf.combat"
            implementationClass = "dev.slne.surf.combat.gradle.IncludeCombatModulePlugin"
        }
        create("includeCombatModuleSettings") {
            id = "dev.slne.surf.combat.settings"
            implementationClass = "dev.slne.surf.combat.gradle.IncludeCombatModuleSettingsPlugin"
        }
    }
}