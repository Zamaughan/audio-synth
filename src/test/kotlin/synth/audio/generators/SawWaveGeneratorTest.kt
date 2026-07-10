package synth.audio.generators

import kotlin.test.Test
import synth.assertApproxEquals

class SawWaveGeneratorTest {
    private val generator = SawWaveGenerator()

    @Test
    fun `starts at the bottom of the ramp`() {
        assertApproxEquals(-1.0, generator.sample(0.0), 0.0001)
    }

    @Test
    fun `midpoint is centered`() {
        assertApproxEquals(0.0, generator.sample(0.5), 0.0001)
    }

    @Test
    fun `approaches the top just before wrapping back down`() {
        assertApproxEquals(0.98, generator.sample(0.99), 0.0001)
    }
}
