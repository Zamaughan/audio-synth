package synth.audio.generators

import synth.audio.WaveformGenerator

class SawWaveGenerator : WaveformGenerator {
    override fun sample(phase: Double): Double = 2.0 * phase - 1.0
}
