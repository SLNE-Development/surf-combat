plugins {
    id("dev.slne.surf.surfapi.gradle.paper-raw")
}

dependencies {
    compileOnly(project(":surf-combat-api"))
    
    listOf(
        "combatlog"
    ).forEach { module ->
        api(project(":surf-combat-modules:surf-combat-module-$module"))
    }
}