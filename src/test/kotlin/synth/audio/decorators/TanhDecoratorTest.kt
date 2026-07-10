package synth.audio.decorators

import kotlin.math.tanh
import kotlin.test.Test
import kotlin.test.assertTrue
import synth.assertApproxEquals

class TanhDecoratorTest {
    @Test
    fun `applies tanh scaled by the drive`() {
        val decorator = TanhDecorator(ConstantSource(0.5), drive = 3.0)
        val samples = decorator.render(440.0, 0.001, 1000)
        assertApproxEquals(tanh(1.5), samples[0], 0.0001)
    }

    @Test
    fun `zero stays at zero`() {
        val decorator = TanhDecorator(ConstantSource(0.0), drive = 5.0)
        val samples = decorator.render(440.0, 0.001, 1000)
        assertApproxEquals(0.0, samples[0], 0.0001)
    }

    @Test
    fun `output stays within the bounded range even with a very high drive`() {
        val decorator = TanhDecorator(ConstantSource(1.0), drive = 50.0)
        val samples = decorator.render(440.0, 0.001, 1000)
        assertTrue(samples[0] <= 1.0 && samples[0] >= -1.0)
    }
}
