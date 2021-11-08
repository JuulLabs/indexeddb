object kotlinx {
    fun coroutines(
        module: String = "core",
        version: String = "1.5.2"
    ): String = "org.jetbrains.kotlinx:kotlinx-coroutines-$module:$version"
}

object wrappers {
    fun extensions(
        version: String = "1.0.1-pre.264-kotlin-1.5.31"
    ) = "org.jetbrains.kotlin-wrappers:kotlin-extensions:$version"
}
