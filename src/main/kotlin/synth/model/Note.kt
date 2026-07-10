package synth.model

/**
 * One note (or rest) inside a measure: what to play and how long to hold it, in beats.
 * A null pitch means this is a rest.
 */
data class Note(val pitch: String?, val durationBeats: Double) {
    /** Frequency in Hz for this note's pitch, or null if it's a rest. */
    fun frequencyHz(): Double? = pitch?.let { PitchUtils.frequencyOf(it) }
}
