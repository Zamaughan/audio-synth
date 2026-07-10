package synth.audio

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import synth.assertApproxEquals

class AudioMixerTest {
    @Test
    fun `sums channels sample by sample`() {
        val a = doubleArrayOf(0.1, 0.2, 0.1)
        val b = doubleArrayOf(0.1, 0.1, 0.1)
        val mixed = AudioMixer.mix(listOf(a, b))
        assertApproxEquals(0.2, mixed[0], 0.0001)
        assertApproxEquals(0.3, mixed[1], 0.0001)
        assertApproxEquals(0.2, mixed[2], 0.0001)
    }

    @Test
    fun `pads shorter channels with silence`() {
        val short = doubleArrayOf(0.5)
        val long = doubleArrayOf(0.1, 0.1, 0.1)
        val mixed = AudioMixer.mix(listOf(short, long))
        assertEquals(3, mixed.size)
        assertApproxEquals(0.6, mixed[0], 0.0001)
        assertApproxEquals(0.1, mixed[1], 0.0001)
    }

    @Test
    fun `normalizes when the combined signal would clip`() {
        val mixed = AudioMixer.mix(listOf(doubleArrayOf(0.9), doubleArrayOf(0.9)))
        assertApproxEquals(1.0, mixed[0], 0.0001) // 1.8 normalized down to a peak of 1.0
    }

    @Test
    fun `leaves quiet mixes untouched`() {
        val mixed = AudioMixer.mix(listOf(doubleArrayOf(0.2), doubleArrayOf(0.1)))
        assertApproxEquals(0.3, mixed[0], 0.0001)
    }

    @Test
    fun `empty channel list produces empty audio`() {
        assertTrue(AudioMixer.mix(emptyList()).isEmpty())
    }
}
