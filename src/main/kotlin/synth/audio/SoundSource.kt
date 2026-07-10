package synth.audio

/**
 * Anything that can render audio for a single note: a raw waveform, or a waveform with
 * effects layered on top of it. This is the interface the decorator pattern wraps.
 */
interface SoundSource {
    /**
     * Renders [durationSeconds] of audio at [sampleRate]. [frequencyHz] is null for a
     * rest, in which case the result is silence.
     */
    fun render(frequencyHz: Double?, durationSeconds: Double, sampleRate: Int): DoubleArray
}
