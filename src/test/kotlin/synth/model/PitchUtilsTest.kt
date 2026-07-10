package synth.model

import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertFailsWith
import synth.assertApproxEquals

class PitchUtilsTest {
    @Test
    fun `A4 is 440 Hz`() {
        assertApproxEquals(440.0, PitchUtils.frequencyOf("A4"), 0.001)
    }

    @Test
    fun `A5 is one octave above A4`() {
        assertApproxEquals(880.0, PitchUtils.frequencyOf("A5"), 0.001)
    }

    @Test
    fun `A3 is one octave below A4`() {
        assertApproxEquals(220.0, PitchUtils.frequencyOf("A3"), 0.001)
    }

    @Test
    fun `C4 is middle C`() {
        assertApproxEquals(261.626, PitchUtils.frequencyOf("C4"), 0.01)
    }

    @Test
    fun `a sharp raises pitch by exactly one semitone`() {
        val ratio = PitchUtils.frequencyOf("C#4") / PitchUtils.frequencyOf("C4")
        assertApproxEquals(2.0.pow(1.0 / 12.0), ratio, 0.0001)
    }

    @Test
    fun `flat and sharp give enharmonic equivalents`() {
        assertApproxEquals(PitchUtils.frequencyOf("C#4"), PitchUtils.frequencyOf("Db4"), 0.0001)
    }

    @Test
    fun `lowercase letters are accepted`() {
        assertApproxEquals(440.0, PitchUtils.frequencyOf("a4"), 0.001)
    }

    @Test
    fun `unrecognized note letter throws`() {
        assertFailsWith<IllegalArgumentException> { PitchUtils.frequencyOf("H4") }
    }

    @Test
    fun `missing octave throws`() {
        assertFailsWith<IllegalArgumentException> { PitchUtils.frequencyOf("C") }
    }

    @Test
    fun `empty string throws`() {
        assertFailsWith<IllegalArgumentException> { PitchUtils.frequencyOf("") }
    }
}
