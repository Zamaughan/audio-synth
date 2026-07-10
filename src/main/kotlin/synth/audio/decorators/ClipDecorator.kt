package synth.audio.decorators

import synth.audio.SoundSource
import kotlin.math.max
import kotlin.math.min

class ClipDecorator(wrapped: SoundSource, private val threshold: Double) : SoundEffectDecorator(wrapped) {
    override fun process(samples: DoubleArray, sampleRate: Int): DoubleArray =
        DoubleArray(samples.size) { i -> max(-threshold, min(threshold, samples[i])) }
}
