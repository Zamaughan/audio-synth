package synth.model

import synth.audio.AudioMixer

/** The whole song: tempo/sample-rate info plus every channel that makes it up. */
class Song(
    private val sampleRate: Int,
    private val tempo: Int,
    private val channels: List<Channel>
) {
    fun sampleRate(): Int = sampleRate

    /** Renders and mixes every channel into one final sample array. */
    fun synthesize(): DoubleArray {
        val channelAudios = channels.map { it.renderAudio(sampleRate, tempo) }
        return AudioMixer.mix(channelAudios)
    }
}
