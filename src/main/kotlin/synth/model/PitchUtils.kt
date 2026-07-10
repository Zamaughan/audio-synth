package synth.model

import kotlin.math.pow

/**
 * Converts scientific pitch notation (e.g. "C4", "F#4", "Bb3") into a frequency in Hz.
 * Standard tuning: A4 = 440 Hz.
 */
object PitchUtils {
    private val semitoneFromC = mapOf(
        'C' to 0, 'D' to 2, 'E' to 4, 'F' to 5, 'G' to 7, 'A' to 9, 'B' to 11
    )

    fun frequencyOf(pitch: String): Double {
        require(pitch.isNotEmpty()) { "Empty pitch string" }

        val letter = pitch[0].uppercaseChar()
        val base = semitoneFromC[letter]
            ?: throw IllegalArgumentException("unrecognized note letter '$letter'")

        // Accidentals: zero or more '#' (sharp) or 'b' (flat) right after the letter.
        var index = 1
        var accidental = 0
        while (index < pitch.length && (pitch[index] == '#' || pitch[index] == 'b')) {
            accidental += if (pitch[index] == '#') 1 else -1
            index++
        }

        val octave = pitch.substring(index).toIntOrNull()
            ?: throw IllegalArgumentException("missing or invalid octave")

        // MIDI note numbering: C4 = 60, A4 = 69.
        val midiNumber = 12 * (octave + 1) + base + accidental
        return 440.0 * 2.0.pow((midiNumber - 69) / 12.0)
    }
}
