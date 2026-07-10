package synth.model

import kotlin.test.Test
import kotlin.test.assertEquals
import synth.audio.SoundSource

/** Test double: 1.0 for a note, 0.0 for a rest - just enough to track where each note landed. */
private class FixedLevelSource : SoundSource {
    override fun render(frequencyHz: Double?, durationSeconds: Double, sampleRate: Int): DoubleArray {
        val level = if (frequencyHz == null) 0.0 else 1.0
        return DoubleArray((durationSeconds * sampleRate).toInt()) { level }
    }
}

class ChannelTest {
    @Test
    fun `concatenates every note across every measure`() {
        val measure1 = Measure(listOf(Note("C4", 1.0), Note("D4", 1.0)))
        val measure2 = Measure(listOf(Note("E4", 2.0)))
        val channel = Channel(FixedLevelSource(), listOf(measure1, measure2))

        // 120 bpm = .5s/beat, total beats = 1 + 1 + 2 = 4 -> 2 seconds at 1000 Hz
        val audio = channel.renderAudio(1000, 120)
        assertEquals(2000, audio.size)
    }

    @Test
    fun `a rest contributes silence in the middle of a channel`() {
        val measure = Measure(listOf(Note("C4", 1.0), Note(null, 1.0)))
        val channel = Channel(FixedLevelSource(), listOf(measure))

        val audio = channel.renderAudio(1000, 120) // .5s/beat -> 500 samples per note
        assertEquals(1.0, audio[0])
        assertEquals(0.0, audio[500])
    }
}
