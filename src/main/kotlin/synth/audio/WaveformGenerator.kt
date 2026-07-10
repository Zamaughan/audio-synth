package synth.audio

/**
 * Strategy interface: produces one amplitude sample at a given point in a wave's cycle.
 * Swapping implementations is exactly the strategy pattern - WaveformSource is the
 * context that holds one of these.
 */
interface WaveformGenerator {
    /**
     * @param phase position within the cycle, in [0, 1)
     * @return amplitude in [-1, 1]
     */
    fun sample(phase: Double): Double
}
