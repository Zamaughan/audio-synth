package synth.audio.decorators

import synth.audio.SoundSource

/** Test double: always renders the same value, ignoring the frequency/duration math. */
internal class ConstantSource(private val value: Double) : SoundSource {
    override fun render(frequencyHz: Double?, durationSeconds: Double, sampleRate: Int): DoubleArray =
        DoubleArray((durationSeconds * sampleRate).toInt()) { value }
}
