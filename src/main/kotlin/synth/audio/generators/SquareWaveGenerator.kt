package synth.audio.generators

import synth.audio.WaveformGenerator

class SquareWaveGenerator : WaveformGenerator {
    override fun sample(phase: Double): Double = if (phase < 0.5) 1.0 else -1.0
}
