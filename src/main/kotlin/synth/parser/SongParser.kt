package synth.parser

import synth.audio.EffectDecoratorFactory
import synth.audio.SoundSource
import synth.audio.WaveformGeneratorFactory
import synth.audio.WaveformSource
import synth.model.Channel
import synth.model.Measure
import synth.model.Note
import synth.model.PitchUtils
import synth.model.Song
import java.io.File

object SongParser {
    fun parseFile(path: String): Song {
        val file = File(path)
        if (!file.exists()) {
            throw SongFormatException("Could not find song file \"$path\"")
        }

        val lines = file.readLines().filter { it.isNotBlank() }
        if (lines.isEmpty()) {
            throw SongFormatException("Song file \"$path\" is empty")
        }

        val (sampleRate, beatsPerMeasure, tempo) = parseHeader(lines[0])
        val channels = lines.drop(1).map { parseChannelLine(it, beatsPerMeasure) }

        return Song(sampleRate, tempo, channels)
    }

    private fun parseHeader(line: String): Triple<Int, Int, Int> {
        val tokens = line.trim().split(Regex("\\s+"))
        if (tokens.size != 3) {
            throw SongFormatException(
                "Header must have 3 values (sampleRate beatsPerMeasure tempo), got \"$line\""
            )
        }
        val sampleRate = tokens[0].toIntOrNull()
            ?: throw SongFormatException("Invalid sample rate \"${tokens[0]}\"")
        val beatsPerMeasure = tokens[1].toIntOrNull()
            ?: throw SongFormatException("Invalid beats per measure \"${tokens[1]}\"")
        val tempo = tokens[2].toIntOrNull()
            ?: throw SongFormatException("Invalid tempo \"${tokens[2]}\"")
        return Triple(sampleRate, beatsPerMeasure, tempo)
    }

    private fun parseChannelLine(line: String, beatsPerMeasure: Int): Channel {
        val segments = line.split("|")
        if (segments.isEmpty() || segments[0].isBlank()) {
            throw SongFormatException("Channel line is missing settings: \"$line\"")
        }

        val soundSource = parseSettings(segments[0])
        val measures = segments.drop(1)
            .filter { it.isNotBlank() } // a trailing "|" produces an empty segment, not a real measure
            .map { parseMeasure(it, beatsPerMeasure) }

        return Channel(soundSource, measures)
    }

    private fun parseSettings(settings: String): SoundSource {
        val tokens = settings.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        if (tokens.isEmpty()) {
            throw SongFormatException("Channel settings cannot be empty")
        }

        var source: SoundSource = WaveformSource(WaveformGeneratorFactory.create(tokens[0]))
        for (effectSpec in tokens.drop(1)) {
            source = EffectDecoratorFactory.wrap(source, effectSpec)
        }
        return source
    }

    private fun parseMeasure(measureText: String, beatsPerMeasure: Int): Measure {
        val tokens = measureText.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        if (tokens.size % 2 != 0) {
            throw SongFormatException("Measure must have note/duration pairs: \"$measureText\"")
        }

        val notes = tokens.chunked(2).map { (noteToken, durationToken) ->
            val pitch = if (noteToken == "-") null else noteToken
            if (pitch != null) {
                try {
                    PitchUtils.frequencyOf(pitch) // validate now so bad files fail at parse time
                } catch (e: IllegalArgumentException) {
                    throw SongFormatException("Invalid note \"$pitch\": ${e.message}")
                }
            }
            val duration = durationToken.toDoubleOrNull()
                ?: throw SongFormatException("Invalid note duration \"$durationToken\"")
            Note(pitch, duration)
        }

        val totalBeats = notes.sumOf { it.durationBeats }
        if (kotlin.math.abs(totalBeats - beatsPerMeasure) > 0.01) {
            throw SongFormatException(
                "Measure \"$measureText\" totals $totalBeats beats, expected $beatsPerMeasure"
            )
        }

        return Measure(notes)
    }
}
