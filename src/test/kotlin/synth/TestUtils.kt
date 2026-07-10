package synth

import kotlin.math.abs
import kotlin.test.assertTrue

/** Compares two doubles within a tolerance - audio math never lines up to the last bit. */
fun assertApproxEquals(expected: Double, actual: Double, tolerance: Double = 0.0001) {
    assertTrue(
        abs(expected - actual) <= tolerance,
        "Expected $expected but was $actual (tolerance $tolerance)"
    )
}
