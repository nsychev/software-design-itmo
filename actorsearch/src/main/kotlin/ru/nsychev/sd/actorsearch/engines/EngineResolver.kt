package ru.nsychev.sd.actorsearch.engines

fun EngineType.resolve(): Class<out BaseEngineActor> = when (this) {
    EngineType.APORT -> AportEngineActor::class.java
    EngineType.RAMBLER -> RamblerEngineActor::class.java
    EngineType.SPUTNIK -> SputnikEngineActor::class.java
}
