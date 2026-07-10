package synth.audio.decorators

import synth.audio.SoundSource

/**
 * Attack-decay-sustain envelope. Ramps up to full volume by [attackEnd] seconds,
 * eases down to [sustain] level by [decayEnd] seconds, then holds at [sustain]
 * for the rest of the note. (No release stage - the spec doesn't define one.)
 */
class AdsDecorator(
    wrapped: SoundSource,
    private val attackEnd: Double,
    private val decayEnd: Double,
    private val sustain: Double
) : SoundEffectDecorator(wrapped) {

    override fun process(samples: DoubleArray, sampleRate: Int): DoubleArray {
        val result = DoubleArray(samples.size)
        for (i in samples.indices) {
            val t = i.toDouble() / sampleRate
            val envelope = when {
                t < attackEnd -> if (attackEnd > 0) t / attackEnd else 1.0
                t < decayEnd -> {
                    val decaySpan = decayEnd - attackEnd
                    val decayProgress = if (decaySpan > 0) (t - attackEnd) / decaySpan else 1.0
                    1.0 - decayProgress * (1.0 - sustain)
                }
                else -> sustain
            }
            result[i] = samples[i] * envelope
        }
        return result
    }
}
