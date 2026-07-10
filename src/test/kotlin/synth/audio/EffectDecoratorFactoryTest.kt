package synth.audio

import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import synth.audio.decorators.AdsDecorator
import synth.audio.decorators.ClipDecorator
import synth.audio.decorators.TanhDecorator
import synth.audio.decorators.VolumeDecorator
import synth.audio.generators.SineWaveGenerator
import synth.parser.SongFormatException

class EffectDecoratorFactoryTest {
    private val base = WaveformSource(SineWaveGenerator())

    @Test
    fun `vol creates a VolumeDecorator`() {
        assertTrue(EffectDecoratorFactory.wrap(base, "vol$.8") is VolumeDecorator)
    }

    @Test
    fun `ads creates an AdsDecorator`() {
        assertTrue(EffectDecoratorFactory.wrap(base, "ads$.01$.2$.1") is AdsDecorator)
    }

    @Test
    fun `tanh creates a TanhDecorator`() {
        assertTrue(EffectDecoratorFactory.wrap(base, "tanh$5") is TanhDecorator)
    }

    @Test
    fun `clip creates a ClipDecorator`() {
        assertTrue(EffectDecoratorFactory.wrap(base, "clip$.8") is ClipDecorator)
    }

    @Test
    fun `unknown effect name fails gracefully`() {
        assertFailsWith<SongFormatException> { EffectDecoratorFactory.wrap(base, "reverb$.5") }
    }

    @Test
    fun `missing arguments fails gracefully`() {
        assertFailsWith<SongFormatException> { EffectDecoratorFactory.wrap(base, "vol") }
    }

    @Test
    fun `non-numeric argument fails gracefully`() {
        assertFailsWith<SongFormatException> { EffectDecoratorFactory.wrap(base, "vol\$loud") }
    }
}
