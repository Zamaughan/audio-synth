package synth.audio.decorators

import kotlin.test.Test
import synth.assertApproxEquals

class DecoratorChainingTest {
    @Test
    fun `volume then clip - clip sees the already-reduced signal`() {
        val volume = VolumeDecorator(ConstantSource(1.0), level = 0.5)
        val clipped = ClipDecorator(volume, threshold = 0.3)
        assertApproxEquals(0.3, clipped.render(440.0, 0.001, 1000)[0], 0.0001)
    }

    @Test
    fun `clip then volume - order changes the result`() {
        val clip = ClipDecorator(ConstantSource(1.0), threshold = 0.3)
        val volumed = VolumeDecorator(clip, level = 0.5)
        assertApproxEquals(0.15, volumed.render(440.0, 0.001, 1000)[0], 0.0001)
    }
}
