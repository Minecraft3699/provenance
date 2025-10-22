plugins {
    id("idea")
    id("java-library")
    id("maven-publish")
    id("net.neoforged.moddev") version "2.0.107"
}

version = "mod_version"()
group = "mod_group_id"()

repositories {
    mavenLocal()
    maven("https://cursemaven.com")
}

base.archivesName = "mod_id"()
java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = "neo_version"()

    parchment {
        mappingsVersion = "parchment_mappings_version"()
        minecraftVersion = "parchment_minecraft_version"()
    }

    accessTransformers.from(files("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        maybeCreate("client").apply {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", "mod_id"())
            devLogin = true
        }

        maybeCreate("server").apply {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", "mod_id"())
        }

        maybeCreate("gameTestServer").apply {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", "mod_id"())
        }

        maybeCreate("data").apply {
            data()
            gameDirectory = project.file("run-data")

            programArguments.addAll(
                "--mod",
                "mod_id"(),
                "--all",
                "--output",
                file("src/generated/resources/").absolutePath,
                "--existing",
                file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        maybeCreate("mod_id"()).apply {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main.get().resources { srcDir("src/generated/resources") }

dependencies {
    implementation("curse.maven:customizable-player-models-439870:6787917")
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to "minecraft_version"(),
        "minecraft_version_range" to "minecraft_version_range"(),
        "neo_version" to "neo_version"(),
        "neo_version_range" to "neo_version_range"(),
        "loader_version_range" to "loader_version_range"(),
        "mod_id" to "mod_id"(),
        "mod_name" to "mod_name"(),
        "mod_license" to "mod_license"(),
        "mod_version" to "mod_version"(),
        "mod_authors" to "mod_authors"(),
        "mod_description" to "mod_description"(),
    )

    inputs.properties(replaceProperties)
    expand(replaceProperties)
    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

sourceSets.main.get().resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = project.base.archivesName.get()

            artifact(tasks.jar)
        }
    }

    repositories {
        mavenLocal()

        val userP = properties["maven.user"] as String?
        val passP = properties["maven.pass"] as String?

        if (userP != null && passP != null) {
            maven {
                name = "TheBrokenScript"
                url = uri("https://maven.thebrokenscript.net/snapshots/")

                credentials {
                    username = userP
                    password = passP
                }
            }
        }
    }
}

idea {
    module {
        isDownloadSources = true
        isDownloadJavadoc = true
    }
}

operator fun String.invoke(): String = rootProject.ext[this] as? String ?: error("No property \"$this\"")