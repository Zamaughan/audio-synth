package synth.audio.decorators

import synth.audio.SoundSource

class VolumeDecorator(wrapped: SoundSource, private val level: Double) : SoundEffectDecorator(wrapped) {
    override fun process(samples: DoubleArray, sampleRate: Int): DoubleArray =
        DoubleArray(samples.size) { i -> samples[i] * level }
}
