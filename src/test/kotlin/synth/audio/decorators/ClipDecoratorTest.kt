package synth.audio.decorators

import kotlin.test.Test
import synth.assertApproxEquals

class ClipDecoratorTest {
    @Test
    fun `values within the threshold pass through unchanged`() {
        val decorator = ClipDecorator(ConstantSource(0.3), threshold = 0.8)
        assertApproxEquals(0.3, decorator.render(440.0, 0.001, 1000)[0], 0.0001)
    }

    @Test
    fun `values above the threshold get clamped`() {
        val decorator = ClipDecorator(ConstantSource(0.95), threshold = 0.8)
        assertApproxEquals(0.8, decorator.render(440.0, 0.001, 1000)[0], 0.0001)
    }

    @Test
    fun `values below the negative threshold get clamped`() {
        val decorator = ClipDecorator(ConstantSource(-0.95), threshold = 0.8)
        assertApproxEquals(-0.8, decorator.render(440.0, 0.001, 1000)[0], 0.0001)
    }
}
