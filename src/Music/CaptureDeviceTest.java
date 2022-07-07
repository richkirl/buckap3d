package Music;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;
import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import static org.lwjgl.openal.EXTCapture.*;
public class CaptureDeviceTest {


    public static void captureTest() throws IOException {

        int state = AL_PLAYING;

        int format = AL_FORMAT_STEREO16;
        int formatSize = 32/8;
        int freq = 44800;
        int time = 140;
        //4 24
        //270
        int bufferSize = (freq * time);

        IntBuffer sampleCount = BufferUtils.createIntBuffer(1);
        ByteBuffer buf = BufferUtils.createByteBuffer(bufferSize * formatSize);

        long context = alcGetCurrentContext();
        String defaultDeviceName = alcGetString(context, ALC_DEFAULT_DEVICE_SPECIFIER);
        long device1 = ALC10.alcOpenDevice(defaultDeviceName);
        ALCCapabilities deviceCaps = ALC.createCapabilities(device1);
        context = ALC10.alcCreateContext(device1, (IntBuffer) null);
        ALC10.alcMakeContextCurrent(context);
        AL.createCapabilities(deviceCaps);

        long pDevice = alcGetContextsDevice(context);


        if (!alcIsExtensionPresent(pDevice, "ALC_EXT_CAPTURE")) {
            return;
        }

        String[] capDevices = alcGetString(pDevice, ALC_CAPTURE_DEVICE_SPECIFIER).split("\0");
        System.out.println("Available Capture Devices: ");
        for (int i = 0; i < capDevices.length; i++) {
            System.out.println(i + ": " + capDevices[i]);
        }

        String defCap = alcGetString(pDevice, ALC_CAPTURE_DEFAULT_DEVICE_SPECIFIER);
        System.out.println("Default capture Device: " + defCap);

        long device = alcCaptureOpenDevice(defCap, freq, format, bufferSize);
        if (device != 0) {
            //CAPTURE
            System.out.println("Recordin using " + ALC10.alcGetString(device, ALC11.ALC_CAPTURE_DEVICE_SPECIFIER) + " ..." );
            ALC11.alcCaptureStart(device);

            while (sampleCount.get(0) < bufferSize) {
                alcGetIntegerv(device, ALC_CAPTURE_SAMPLES, sampleCount);

            }
            EXTCapture.alcCaptureSamples(device, buf, bufferSize);
            System.out.println(sampleCount.get());
            System.out.println(buf);
            System.out.println("Done recording.");
            ALC11.alcCaptureStop(device);

            IntBuffer buffers = BufferUtils.createIntBuffer(1);
            IntBuffer sources = BufferUtils.createIntBuffer(1);

            //PLAYBACK

            System.out.println("Playing ...");

            buffers.position(0).limit(1);
            AL10.alGenBuffers(buffers.position(0).limit(1));

            sources.position(0).limit(1);
            AL10.alGenSources(sources.position(0).limit(1));

            File wav = new File("test.wav");
            if (!wav.exists()) {
                wav.createNewFile();
            }
            DataOutputStream outFile = new DataOutputStream(new FileOutputStream(wav));

            outFile.writeBytes("RIFF");                 // 00 - RIFF
            outFile.write(String.valueOf("----").getBytes(), 0, 4);     // 04 - how big is the rest of this file?
            outFile.writeBytes("WAVE");                 // 08 - WAVE
            outFile.writeBytes("fmt ");                 // 12 - fmt
            outFile.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(16).array()); // 16 - size of this chunk
            outFile.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) 1).array());        // 20 - what is the audio format? 1 for PCM = Pulse Code Modulation
            outFile.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) 2).array());  // 22 - mono or stereo? 1 or 2?  (or 5 or ???)
            outFile.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(44800).array());        // 24 - samples per second (numbers per second)
            outFile.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(44800*2).array());      // 28 - bytes per second
            outFile.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) 2).array());    // 32 - # of bytes in one sample, for all channels
            outFile.write(ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).putShort((short) 16).array()); // 34 - how many bits in a sample(number)?  usually 16 or 24
            outFile.writeBytes("data");                 // 36 - data
            outFile.write(ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(bufferSize*formatSize).array());      // 40 - how big is this data chunk

            AL10.alBufferData(buffers.get(), format, buf, freq);

            outFile.write(ByteBuffer.allocate(bufferSize*formatSize).order(ByteOrder.BIG_ENDIAN).put(buf).array());
            outFile.close();
            AL10.alSourcei(sources.get(), AL10.AL_BUFFER, buffers.get(0));
            AL10.alSourcei(sources.get(0), AL10.AL_LOOPING, AL10.AL_FALSE);
            AL10.alSourcePlay(sources.get(0));

            while (state == AL10.AL_PLAYING) {
                state = AL10.alGetSourcei(sources.get(0), AL10.AL_SOURCE_STATE);
            }

            System.out.println("Done playing.");
            ALC11.alcCaptureCloseDevice(device);
            alcDestroyContext(context);
            ALC11.alcCloseDevice(device1);
            AL10.alDeleteBuffers(buffers);
            AL10.alDeleteSources(sources);
        }
    }
}
