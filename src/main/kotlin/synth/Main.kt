package synth

fun main(args: Array<String>) {
    if (args.isEmpty()) {
        println("Usage: synth <path-to-song-file>")
        return
    }
    SynthesizerApp().run(args[0])
}
