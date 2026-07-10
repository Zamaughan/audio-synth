package synth.model

/** A single measure within a channel: just a sequence of notes/rests played back to back. */
data class Measure(val notes: List<Note>)
