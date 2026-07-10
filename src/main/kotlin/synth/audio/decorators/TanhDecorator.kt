package synth.audio.decorators

import synth.audio.SoundSource
import kotlin.math.tanh

class TanhDecorator(wrapped: SoundSource, private val drive: Double) : SoundEffectDecorator(wrapped) {
    override fun process(samples: DoubleArray, sampleRate: Int): DoubleArray =
        DoubleArray(samples.size) { i -> tanh(samples[i] * drive) }
}
