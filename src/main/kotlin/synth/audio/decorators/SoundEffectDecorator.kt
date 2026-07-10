package synth.audio.decorators

import synth.audio.SoundSource

/**
 * Base decorator: wraps another SoundSource, asks it to render first, then
 * post-processes the result. Subclasses only need to implement [process] -
 * the wrapping and delegation live here so they don't repeat it four times.
 */
abstract class SoundEffectDecorator(private val wrapped: SoundSource) : SoundSource {
    override fun render(frequencyHz: Double?, durationSeconds: Double, sampleRate: Int): DoubleArray {
        val samples = wrapped.render(frequencyHz, durationSeconds, sampleRate)
        return process(samples, sampleRate)
    }

    protected abstract fun process(samples: DoubleArray, sampleRate: Int): DoubleArray
}
