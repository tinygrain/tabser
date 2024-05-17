package app.tabser.view;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.util.Log;

import java.util.Map;
import java.util.Objects;

import app.tabser.model.Bar;
import app.tabser.model.Note;
import app.tabser.model.Sequence;
import app.tabser.model.Song;

public class ToneGenerator {
    private final int sampleRate = 44100/5;
    private final AudioAttributes audioAttributes;
    private final AudioFormat audioFormat;
    private final Handler handler = new Handler();
    private final String rate;

    public ToneGenerator(Context context) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.rate = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);
        this.audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        this.audioFormat = new AudioFormat.Builder()
                .setSampleRate(Integer.parseInt(rate))
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                .build();
    }

    public void play(double freqOfTone, double duration) {
        playSound(genTone(freqOfTone, duration), (int) (duration * sampleRate));
    }

    public void playAsync(double freqOfTone, int duration) {
        // Use a new tread as this can take a while
        final Thread thread = new Thread(new Runnable() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        playSound(genTone(freqOfTone, duration), (int) (duration * sampleRate));
                    }
                });
            }
        });
        thread.start();
    }

    private byte[] genTone(double freqOfTone, double duration) {
        // fill out the array
        int numSamples = (int) (duration * sampleRate);
        double[] sample = new double[numSamples];
        double effFreq = freqOfTone / sampleRate;
        double lastPoint = effFreq * numSamples;
        double lastDecimals = lastPoint - (int) lastPoint;
        int overflow = (int) (lastDecimals / effFreq);
        for (int i = 0; i < numSamples-overflow; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate / freqOfTone));
        }
        Log.d("TAG", "genTone: "+sample[sample.length-1]);

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;

        byte generatedSnd[] = new byte[2 * numSamples];
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
        return generatedSnd;
    }

    private void playSound(byte[] generatedSnd, int numSamples) {
        AudioTrack audioTrack = new AudioTrack(audioAttributes,
                audioFormat,
                numSamples,
                AudioTrack.MODE_STREAM,
                0);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
        //audioTrack.stop();
    }

    public void play(Song model) {
        for (Bar bar : model.getBars(Sequence.DEFAULT_HIDDEN_SEQUENCE_NAME)) {
            for (Map<Integer, Note> notes : bar.getNotes()) {
                for (Integer key: notes.keySet()) {
                    Note n = notes.get(key);
                    if (Objects.nonNull(n)) {
                        play(n.getPitch().getFrequency(), 2);
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
/*
    private static int sampleRate = 4410void0;
    private final int duration = 1; // seconds
    private final int numSamples = duration * sampleRate;

    private byte[] generateCombinedWaveBuffer(double[] frequencies) {
        byte[] buffer = new byte[numSamples * 2]; // 16-bit samples

        for (int i = 0; i < numSamples; i++) {
            double amplitude = 0.0; // Initialize amplitude to zero
            for (double frequency : frequencies) {
                amplitude += Math.sin(2 * Math.PI * i * frequency / sampleRate);
            }
            double maxAmplitude = 0.7 * Short.MAX_VALUE;
            if (Math.abs(amplitude) > maxAmplitude) {
                double scalingFactor = maxAmplitude / Math.abs(amplitude);
                amplitude *= scalingFactor;
                for (int j = 0; j < frequencies.length; j++) {
                    frequencies[j] *= scalingFactor; // Scale all frequencies
                }
            }

            short sample = (short) (amplitude * Short.MAX_VALUE);
            //buffer[2 * i] = (byte) (sample & 0xFF);
            buffer[2 * i + 1] = (byte) ((sample >> 8) & 0xFF);

        }

        return buffer;
    }

    void testSound() {

        AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, numSamples,
                AudioTrack.MODE_STATIC);
        track.write(generateCombinedWaveBuffer(freqs), 0, numSamples);
        track.play();

        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        String rate = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE);

        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        AudioFormat audioFormat = new AudioFormat.Builder()
                .setSampleRate(Integer.parseInt(rate))
                .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                .build();

// then, initialize with new constructor
        AudioTrack audioTrack = new AudioTrack(audioAttributes,
                audioFormat,
                numSamples,
                AudioTrack.MODE_STREAM,
                0);
        double[] freqs = {300};
        audioTrack.write(generateCombinedWaveBuffer(freqs), 0, numSamples);
        audioTrack.play();
    }
                */
}
