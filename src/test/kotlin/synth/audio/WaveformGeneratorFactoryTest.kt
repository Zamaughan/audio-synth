package synth.audio

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import synth.audio.generators.SawWaveGenerator
import synth.audio.generators.SineWaveGenerator
import synth.audio.generators.SquareWaveGenerator
import synth.audio.generators.WhiteNoiseGenerator
import synth.parser.SongFormatException

class WaveformGeneratorFactoryTest {
    @Test
    fun `creates the right generator for each known name`() {
        assertTrue(WaveformGeneratorFactory.create("sin") is SineWaveGenerator)
        assertTrue(WaveformGeneratorFactory.create("square") is SquareWaveGenerator)
        assertTrue(WaveformGeneratorFactory.create("saw") is SawWaveGenerator)
        assertTrue(WaveformGeneratorFactory.create("whitenoise") is WhiteNoiseGenerator)
    }

    @Test
    fun `unknown waveform name fails gracefully`() {
        assertFailsWith<SongFormatException> { WaveformGeneratorFactory.create("triangle") }
    }
}
