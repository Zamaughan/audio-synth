package synth.audio.generators

import synth.audio.WaveformGenerator
import kotlin.random.Random

class WhiteNoiseGenerator : WaveformGenerator {
    override fun sample(phase: Double): Double = Random.nextDouble(-1.0, 1.0)
}
