package synth.parser

import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import synth.assertApproxEquals

class SongParserTest {

    private fun tempSongFile(content: String): String {
        val file = File.createTempFile("song", ".synth")
        file.writeText(content)
        file.deleteOnExit()
        return file.path
    }

    @Test
    fun `parses the header values`() {
        val path = tempSongFile(
            """
            44100 4 120
            sin|C4 1 D4 1 E4 1 F4 1
            """.trimIndent()
        )
        assertEquals(44100, SongParser.parseFile(path).sampleRate())
    }

    @Test
    fun `note durations control the rendered length`() {
        // 120 bpm = .5s/beat - one measure of four one-beat notes is 2 seconds.
        val path = tempSongFile(
            """
            44100 4 120
            sin|C4 1 D4 1 E4 1 F4 1
            """.trimIndent()
        )
        val audio = SongParser.parseFile(path).synthesize()
        assertApproxEquals(2.0, audio.size / 44100.0, 0.001)
    }

    @Test
    fun `a dash is a rest and renders silence`() {
        val path = tempSongFile(
            """
            1000 1 60
            sin|- 1
            """.trimIndent()
        )
        val audio = SongParser.parseFile(path).synthesize()
        assertTrue(audio.all { it == 0.0 })
    }

    @Test
    fun `multiple channels parse and mix without error`() {
        val path = tempSongFile(
            """
            44100 1 120
            sin|C4 1
            square|C4 1
            """.trimIndent()
        )
        val audio = SongParser.parseFile(path).synthesize()
        assertApproxEquals(0.5, audio.size / 44100.0, 0.001) // .5s/beat at 120bpm
    }

    @Test
    fun `channel effects get attached to the rendered audio`() {
        val path = tempSongFile(
            """
            1000 1 60
            sin vol$.5|C4 1
            """.trimIndent()
        )
        val audio = SongParser.parseFile(path).synthesize()
        assertTrue(audio.maxOrNull()!! <= 0.5 + 0.0001)
    }

    @Test
    fun `missing file fails gracefully`() {
        assertFailsWith<SongFormatException> { SongParser.parseFile("/no/such/file.synth") }
    }

    @Test
    fun `empty file fails gracefully`() {
        assertFailsWith<SongFormatException> { SongParser.parseFile(tempSongFile("")) }
    }

    @Test
    fun `malformed header fails gracefully`() {
        val path = tempSongFile(
            """
            44100 120
            sin|C4 1
            """.trimIndent()
        )
        assertFailsWith<SongFormatException> { SongParser.parseFile(path) }
    }

    @Test
    fun `non-numeric header value fails gracefully`() {
        val path = tempSongFile(
            """
            fast 4 120
            sin|C4 1
            """.trimIndent()
        )
        assertFailsWith<SongFormatException> { SongParser.parseFile(path) }
    }

    @Test
    fun `unknown waveform fails gracefully`() {
        val path = tempSongFile(
            """
            44100 4 120
            triangle|C4 1
            """.trimIndent()
        )
        assertFailsWith<SongFormatException> { SongParser.parseFile(path) }
    }

    @Test
    fun `unrecognized note letter fails gracefully`() {
        val path = tempSongFile(
            """
            44100 4 120
            sin|H4 1
            """.trimIndent()
        )
        assertFailsWith<SongFormatException> { SongParser.parseFile(path) }
    }

    @Test
    fun `odd number of note-duration tokens fails gracefully`() {
        val path = tempSongFile(
            """
            44100 4 120
            sin|C4 1 D4
            """.trimIndent()
        )
        assertFailsWith<SongFormatException> { SongParser.parseFile(path) }
    }

    @Test
    fun `unknown effect fails gracefully`() {
        val path = tempSongFile(
            """
            44100 4 120
            sin reverb$.5|C4 1
            """.trimIndent()
        )
        assertFailsWith<SongFormatException> { SongParser.parseFile(path) }
    }

    @Test
    fun `a measure whose durations don't add up to beatsPerMeasure fails gracefully`() {
        val path = tempSongFile(
            """
            44100 4 120
            sin|C4 1 D4 1
            """.trimIndent()
        ) // only 2 beats in a measure the header says should have 4
        assertFailsWith<SongFormatException> { SongParser.parseFile(path) }
    }

    @Test
    fun `a measure with fractional durations that add up correctly is accepted`() {
        val path = tempSongFile(
            """
            44100 4 120
            sin|C4 2.67 C4 .33 C4 .33 C4 .34 D4 .33
            """.trimIndent()
        ) // 2.67 + .33 + .33 + .34 + .33 = 4.0, within floating-point tolerance
        val audio = SongParser.parseFile(path).synthesize()
        assertApproxEquals(4.0 * 0.5, audio.size / 44100.0, 0.01) // 4 beats at 120bpm
    }

    @Test
    fun `a trailing pipe on a channel line doesn't create a phantom empty measure`() {
        val path = tempSongFile(
            """
            44100 4 120
            sin|C4 1 D4 1 E4 1 F4 1|
            """.trimIndent()
        )
        val audio = SongParser.parseFile(path).synthesize()
        assertApproxEquals(2.0, audio.size / 44100.0, 0.001) // just the one real measure, .5s/beat
    }
}
