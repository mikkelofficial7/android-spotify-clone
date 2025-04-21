package com.view.musicplayer.spotifyclone.network.response

import com.google.gson.annotations.SerializedName

data class OpenRouterResponse (
    val id: String,
    val provider: String,
    val model: String,
    @SerializedName("object")
    val welcome1Object: String,
    val created: Long,
    val choices: List<Choice>,
    val usage: Usage
)

data class Choice (
    val logprobs: Any? = null,
    @SerializedName("finish_reason")
    val finishReason: String,
    @SerializedName("native_finish_reason")
    val nativeFinishReason: String,
    val index: Long,
    val message: Message
)

data class Message (
    val role: String,
    val content: String,
    val refusal: Any? = null,
    val reasoning: Any? = null
)

data class Usage (
    @SerializedName("prompt_tokens")
    val promptTokens: Long,
    @SerializedName("completion_tokens")
    val completionTokens: Long,
    @SerializedName("total_tokens")
    val totalTokens: Long
)
