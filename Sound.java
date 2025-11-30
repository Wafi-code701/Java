package assignment2.ver2;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import java.awt.*;

public class Sound
{
    public static void runVoice1() {
        Toolkit.getDefaultToolkit().beep();
    }

    public static void runVoice2() {
        float sampleRate = 44100;
        byte[] buf = new byte[1];
        AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
        try (SourceDataLine sdl = AudioSystem.getSourceDataLine(af)){
            sdl.open(af);
            sdl.start();
            int hz = 440; // frequency (A4 note)
            int durationMs = 1000;
            for (int i = 0; i < durationMs * (sampleRate / 1000); i++) {
                double angle = i / (sampleRate / hz) * 2.0 * Math.PI;
                buf[0] = (byte)(Math.sin(angle) * 127.0);
                sdl.write(buf, 0, 1);
            }
            sdl.drain();
        } catch (LineUnavailableException e) {
            System.err.println("Cannot open audio stream for Voice 2"+e.getMessage());
        }


    }

}
