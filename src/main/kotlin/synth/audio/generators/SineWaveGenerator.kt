package synth.audio.generators

import synth.audio.WaveformGenerator
import kotlin.math.PI
import kotlin.math.sin

class SineWaveGenerator : WaveformGenerator {
    override fun sample(phase: Double): Double = sin(2 * PI * phase)
}
