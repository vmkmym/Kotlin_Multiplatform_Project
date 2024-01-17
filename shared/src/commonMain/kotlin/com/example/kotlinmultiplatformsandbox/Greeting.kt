package com.example.kotlinmultiplatformsandbox

import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds
// 이게 그 바로 유명한 state?

class Greeting {
    private val platform: Platform = getPlatform()
    private val rocketComponent = RocketComponent()

    // 이거는 일시정지용 코드
//    fun greet(): List<String> = buildList {
//        add(if (Random.nextBoolean()) "Hi!" else "Hello!")
//        add("Guess what it is! > ${platform.name.reversed()}!")
//        add(daysPhrase())
//    }

    // stateFlow
    fun greet(): Flow<String> = flow {
        emit(if (Random.nextBoolean()) "Hi!" else "Hello!")
        delay(1.seconds)
        emit("Guess what this is! > ${platform.name.reversed()}")
        delay(1.seconds)
        emit(daysPhrase())
        emit(rocketComponent.launchPhrase())
    }
}