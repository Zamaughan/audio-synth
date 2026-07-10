package synth.audio.generators

import kotlin.test.Test
import synth.assertApproxEquals

class SineWaveGeneratorTest {
    private val generator = SineWaveGenerator()

    @Test
    fun `phase 0 is silence`() {
        assertApproxEquals(0.0, generator.sample(0.0), 0.0001)
    }

    @Test
    fun `phase quarter is the peak`() {
        assertApproxEquals(1.0, generator.sample(0.25), 0.0001)
    }

    @Test
    fun `phase half is back to zero`() {
        assertApproxEquals(0.0, generator.sample(0.5), 0.0001)
    }

    @Test
    fun `phase three quarters is the trough`() {
        assertApproxEquals(-1.0, generator.sample(0.75), 0.0001)
    }
}
