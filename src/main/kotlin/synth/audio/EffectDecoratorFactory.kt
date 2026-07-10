package synth.audio

import synth.audio.decorators.AdsDecorator
import synth.audio.decorators.ClipDecorator
import synth.audio.decorators.TanhDecorator
import synth.audio.decorators.VolumeDecorator
import synth.parser.SongFormatException

object EffectDecoratorFactory {
    /** [spec] looks like "vol$.8" or "ads$.01$.2$.1" - name followed by $-separated args. */
    fun wrap(base: SoundSource, spec: String): SoundSource {
        val parts = spec.split("$")
        val name = parts[0]
        val args = parts.drop(1).map {
            it.toDoubleOrNull() ?: throw SongFormatException("Invalid argument in effect \"$spec\"")
        }

        try {
            return when (name) {
                "vol" -> VolumeDecorator(base, args[0])
                "ads" -> AdsDecorator(base, args[0], args[1], args[2])
                "tanh" -> TanhDecorator(base, args[0])
                "clip" -> ClipDecorator(base, args[0])
                else -> throw SongFormatException("Unknown effect \"$name\"")
            }
        } catch (e: IndexOutOfBoundsException) {
            throw SongFormatException("Effect \"$name\" is missing arguments")
        }
    }
}
