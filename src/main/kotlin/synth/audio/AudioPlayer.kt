package synth.audio

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem

object AudioPlayer {
    fun play(samples: DoubleArray, sampleRate: Int) {
        val format = AudioFormat(sampleRate.toFloat(), 16, 1, true, true)
        val line = AudioSystem.getSourceDataLine(format)
        line.open(format)
        line.start()

        val bytes = ByteArray(samples.size * 2)
        for (i in samples.indices) {
            val sample16 = (samples[i].coerceIn(-1.0, 1.0) * Short.MAX_VALUE).toInt().toShort()
            bytes[i * 2] = (sample16.toInt() shr 8).toByte()
            bytes[i * 2 + 1] = sample16.toByte()
        }

        line.write(bytes, 0, bytes.size)
        line.drain()
        line.close()
    }
}
