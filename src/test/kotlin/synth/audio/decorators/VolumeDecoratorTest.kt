package synth.audio.decorators

import kotlin.test.Test
import synth.assertApproxEquals

class VolumeDecoratorTest {
    @Test
    fun `scales every sample by the level`() {
        val decorator = VolumeDecorator(ConstantSource(1.0), level = 0.5)
        val samples = decorator.render(440.0, 0.001, 1000)
        assertApproxEquals(0.5, samples[0], 0.0001)
    }

    @Test
    fun `silence stays silent`() {
        val decorator = VolumeDecorator(ConstantSource(0.0), level = 0.8)
        val samples = decorator.render(null, 0.001, 1000)
        assertApproxEquals(0.0, samples[0], 0.0001)
    }
}
