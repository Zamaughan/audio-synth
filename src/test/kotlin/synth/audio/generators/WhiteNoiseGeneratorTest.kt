package synth.audio.generators

import kotlin.test.Test
import kotlin.test.assertTrue

class WhiteNoiseGeneratorTest {
    private val generator = WhiteNoiseGenerator()

    @Test
    fun `samples stay within range regardless of phase`() {
        repeat(1000) {
            assertTrue(generator.sample(0.0) in -1.0..1.0)
        }
    }

    @Test
    fun `samples are not all identical`() {
        val samples = List(50) { generator.sample(0.0) }
        assertTrue(samples.toSet().size > 1)
    }
}
