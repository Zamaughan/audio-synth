package synth.audio

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import synth.assertApproxEquals
import synth.audio.generators.SineWaveGenerator

class WaveformSourceTest {
    @Test
    fun `renders the right number of samples`() {
        val source = WaveformSource(SineWaveGenerator())
        assertEquals(44100, source.render(440.0, 1.0, 44100).size)
    }

    @Test
    fun `a rest renders silence`() {
        val source = WaveformSource(SineWaveGenerator())
        val samples = source.render(null, 0.5, 44100)
        assertTrue(samples.all { it == 0.0 })
    }

    @Test
    fun `delegates the actual sample shape to its strategy`() {
        // 1 Hz at 4 samples/sec = one full cycle, phase advances by .25 each sample -
        // matches the SineWaveGenerator directly.
        val source = WaveformSource(SineWaveGenerator())
        val samples = source.render(1.0, 1.0, 4)
        assertApproxEquals(0.0, samples[0], 0.0001)
        assertApproxEquals(1.0, samples[1], 0.0001)
    }
}
