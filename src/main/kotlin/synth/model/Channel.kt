package synth.model

import synth.audio.SoundSource

/**
 * One audio channel: a waveform + effect chain (built once by the parser) plus the
 * sequence of measures it plays.
 */
class Channel(
    private val soundSource: SoundSource,
    private val measures: List<Measure>
) {
    /** Renders every note in every measure, back to back, into one sample array. */
    fun renderAudio(sampleRate: Int, tempo: Int): DoubleArray {
        val secondsPerBeat = 60.0 / tempo
        val chunks = mutableListOf<DoubleArray>()

        for (measure in measures) {
            for (note in measure.notes) {
                val durationSeconds = note.durationBeats * secondsPerBeat
                chunks += soundSource.render(note.frequencyHz(), durationSeconds, sampleRate)
            }
        }

        return concatenate(chunks)
    }

    private fun concatenate(chunks: List<DoubleArray>): DoubleArray {
        val result = DoubleArray(chunks.sumOf { it.size })
        var offset = 0
        for (chunk in chunks) {
            chunk.copyInto(result, offset)
            offset += chunk.size
        }
        return result
    }
}
