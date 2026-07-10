package synth.audio

/**
 * The base SoundSource: generates a raw waveform by delegating to a WaveformGenerator
 * strategy. This class plays two roles at once - it's the Strategy pattern's context,
 * and it's the ConcreteComponent that SoundEffectDecorators wrap.
 */
class WaveformSource(private val generator: WaveformGenerator) : SoundSource {
    override fun render(frequencyHz: Double?, durationSeconds: Double, sampleRate: Int): DoubleArray {
        val numSamples = (durationSeconds * sampleRate).toInt()
        val samples = DoubleArray(numSamples)

        if (frequencyHz == null) {
            return samples // rest - stays all zeros
        }

        var phase = 0.0
        val phaseIncrement = frequencyHz / sampleRate
        for (i in 0 until numSamples) {
            samples[i] = generator.sample(phase)
            phase = (phase + phaseIncrement) % 1.0
        }
        return samples
    }
}
