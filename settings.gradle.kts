rootProject.name = "surf-combat"

include("surf-combat-api")
include("surf-combat-server")

// Modules
include("surf-combat-modules:surf-combat-module-loader")
listOf(
    "combatlog"
).forEach { module ->
    include("surf-combat-modules:surf-combat-module-$module")
}