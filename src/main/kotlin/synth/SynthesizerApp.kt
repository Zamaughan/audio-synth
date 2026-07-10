package synth

import synth.audio.AudioPlayer
import synth.parser.SongFormatException
import synth.parser.SongParser

class SynthesizerApp(private val player: AudioPlayer = AudioPlayer) {
    fun run(filePath: String) {
        try {
            val song = SongParser.parseFile(filePath)
            val audio = song.synthesize()
            player.play(audio, song.sampleRate())
        } catch (e: SongFormatException) {
            println("Could not play that song: ${e.message}")
        } catch (e: Exception) {
            println("Something went wrong while synthesizing the song: ${e.message}")
        }
    }
}
