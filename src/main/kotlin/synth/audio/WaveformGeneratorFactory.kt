package synth.audio

import synth.audio.generators.SawWaveGenerator
import synth.audio.generators.SineWaveGenerator
import synth.audio.generators.SquareWaveGenerator
import synth.audio.generators.WhiteNoiseGenerator
import synth.parser.SongFormatException

object WaveformGeneratorFactory {
    fun create(name: String): WaveformGenerator = when (name) {
        "sin" -> SineWaveGenerator()
        "square" -> SquareWaveGenerator()
        "saw" -> SawWaveGenerator()
        "whitenoise" -> WhiteNoiseGenerator()
        else -> throw SongFormatException("Unknown waveform \"$name\"")
    }
}
