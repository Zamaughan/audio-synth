package synth.audio

import kotlin.math.abs

object AudioMixer {
    /** Sums channels sample-by-sample (shorter ones implicitly padded with silence), then
     *  normalizes if the combined signal would clip. */
    fun mix(channelAudios: List<DoubleArray>): DoubleArray {
        if (channelAudios.isEmpty()) return DoubleArray(0)

        val length = channelAudios.maxOf { it.size }
        val mixed = DoubleArray(length)

        for (channel in channelAudios) {
            for (i in channel.indices) {
                mixed[i] += channel[i]
            }
        }

        val peak = mixed.maxOf { abs(it) }
        if (peak > 1.0) {
            for (i in mixed.indices) {
                mixed[i] /= peak
            }
        }
        return mixed
    }
}
