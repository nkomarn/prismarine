package xyz.nkomarn.prismarine.protocol.encoding

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialInfo

@SerialInfo
@Target(AnnotationTarget.PROPERTY)
@OptIn(ExperimentalSerializationApi::class)
annotation class JsonString
