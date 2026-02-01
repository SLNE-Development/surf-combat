plugins {
    id("dev.slne.surf.surfapi.gradle.paper-plugin")
}

surfPaperPluginApi {
    mainClass("dev.slne.surf.combat.server.CombatPaperPlugin")
    foliaSupported(true)
    generateLibraryLoader(false)
}

dependencies {
    api(project(":surf-combat-modules:surf-combat-module-loader"))
}