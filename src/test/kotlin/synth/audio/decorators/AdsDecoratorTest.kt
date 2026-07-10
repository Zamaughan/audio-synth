package synth.audio.decorators

import kotlin.test.Test
import synth.assertApproxEquals

class AdsDecoratorTest {
    // attack ends at .01s, decay ends at .02s, sustain .5 - 1000 Hz so each sample is 1ms.
    private val decorator = AdsDecorator(ConstantSource(1.0), attackEnd = 0.01, decayEnd = 0.02, sustain = 0.5)
    private val samples = decorator.render(440.0, 0.03, 1000)

    @Test
    fun `ramps up linearly during attack`() {
        assertApproxEquals(0.5, samples[5], 0.0001) // halfway through attack
    }

    @Test
    fun `peaks at full volume right when attack ends`() {
        assertApproxEquals(1.0, samples[10], 0.0001)
    }

    @Test
    fun `eases down through decay`() {
        assertApproxEquals(0.75, samples[15], 0.0001) // halfway through decay
    }

    @Test
    fun `holds at sustain level after decay ends`() {
        assertApproxEquals(0.5, samples[20], 0.0001)
        assertApproxEquals(0.5, samples[29], 0.0001)
    }

    @Test
    fun `zero attack starts immediately at full volume`() {
        val zeroAttack = AdsDecorator(ConstantSource(1.0), attackEnd = 0.0, decayEnd = 0.01, sustain = 0.3)
        val result = zeroAttack.render(440.0, 0.02, 1000)
        assertApproxEquals(1.0, result[0], 0.0001)
        assertApproxEquals(0.3, result[10], 0.0001)
    }
}
