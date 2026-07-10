package synth.audio.generators

import kotlin.test.Test
import kotlin.test.assertEquals

class SquareWaveGeneratorTest {
    private val generator = SquareWaveGenerator()

    @Test
    fun `first half of the cycle is high`() {
        assertEquals(1.0, generator.sample(0.0))
        assertEquals(1.0, generator.sample(0.49))
    }

    @Test
    fun `second half of the cycle is low`() {
        assertEquals(-1.0, generator.sample(0.5))
        assertEquals(-1.0, generator.sample(0.99))
    }
}
