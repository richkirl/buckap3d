package Music;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;


import javax.sound.sampled.AudioFileFormat;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.Scanner;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.EXTEfx.*;
import static org.lwjgl.openal.EXTEfx.AL_MAX_DIRECT_FILTER_GAINHF_AUTO;
import java.util.*;


public class WavFormS {
    /*outFile.writeBytes("RIFF");                 // 00 - RIFF
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

    outFile.write(ByteBuffer.allocate(bufferSize*formatSize).order(ByteOrder.BIG_ENDIAN).put(buf).array());//44
    outFile.close();*/


    //IntBuffer buffers = BufferUtils.createIntBuffer(1);
    //IntBuffer sources = BufferUtils.createIntBuffer(1);
    public static void play(String s) throws IOException {
        String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
        long device = alcOpenDevice(defaultDeviceName);
        System.out.println(defaultDeviceName);
        int[] attributes = {0};
        int[] iSends = {0};
        if (!alcIsExtensionPresent(device, "ALC_EXT_EFX")) {
            return;
        }
        System.out.println("EFX Extension found!\n");
        long context = alcCreateContext(device, attributes);
        alcMakeContextCurrent(context);
        alcProcessContext(context);

        alcGetIntegerv(device, ALC_MAX_AUXILIARY_SENDS, iSends);
        ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
        ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);
        int uiEffectSlot[] = {0, 0, 0, 0};
        int uiEffect[] = {0, 0, 0};
        int uiFilter[] = {0, 0};
        //AL_EFFECT_EAXREVERB;

        int uiLoop;
        //for(uiLoop=0;uiLoop<4;uiLoop++) {
        alGenAuxiliaryEffectSlots(uiEffectSlot);
        //}
        System.out.printf("Generated %d Aux Effect Slots\n", uiEffectSlot.length);
        alGenEffects(uiEffect);
        System.out.printf("Generated %d Effects\n", uiEffect.length);
        alGetError();
        if (alIsEffect(uiEffect[0])) {
            alEffecti(uiEffect[0], AL_EFFECT_TYPE, AL_EFFECT_REVERB);
            if (alGetError() != ALC_NO_ERROR)
                System.out.println("Reverb Effect not supported\n");
            else
                alEffectf(uiEffect[0], AL_REVERB_DECAY_TIME, 10.0f);
        }
        /*alGetError();
        if (alIsEffect(uiEffect[1])) {
            alEffecti(uiEffect[1], AL_EFFECT_TYPE, AL_EFFECT_COMPRESSOR);
            if (alGetError() != ALC_NO_ERROR)
                System.out.println("CompressorEffect not supported\n");
            //else
            //alEffectf(uiEffect[0],AL_REVERB_DECAY_TIME,4.0f);
        }*/
        alGetError();
        //if (alIsEffect(uiEffect[2])) {
        //    alEffecti(uiEffect[2], AL_EFFECT_TYPE, AL_EFFECT_FLANGER);
        //    if (alGetError() != ALC_NO_ERROR)
        //        System.out.println("Flanger Effect not supported\n");
        //    else
                alEffectf(uiEffect[2], AL_FLANGER_PHASE, 360);
        //}
        alGetError();
        alGenFilters(uiFilter);
        if (alGetError() == AL_NO_ERROR)
            System.out.printf("Generated a Filter\n");
        if (alIsFilter(uiFilter[0])) {
            /* Set Filter type to Low-Pass and set parameters */
            alFilteri(uiFilter[0], AL_FILTER_TYPE, AL_FILTER_LOWPASS);
            if (alGetError() != AL_NO_ERROR)
                System.out.printf("Low Pass Filter not supported\n");
            else {
                alFilterf(uiFilter[0], AL_LOWPASS_GAIN, 0.6f);
                alFilterf(uiFilter[0], AL_LOWPASS_GAINHF, 0.6f);
            }
        }

        if (!alCapabilities.OpenAL10) {
            assert false : "Audio library not support";
        }
        //ShortBuffer rawAudioBuffer;

        //int sampleRate;

        //IntBuffer channelsBuffer = stackMallocInt(1);

        //IntBuffer sampleRateBuffer = stackMallocInt(1);

        //rawAudioBuffer = stb_vorbis_decode_filename("Apocalyptica_-_Aqua_Balalaika_66119855.ogg", channelsBuffer, sampleRateBuffer);
        String wav = s;
        FileInputStream inFile = new FileInputStream(wav);
        byte[] Riff = new byte[4];//00
        byte[] chekRif = new byte[4];//4
        byte[] WAVE = new byte[4];//8
        byte[] fmt = new byte[4];//12

        byte[] sizeChunk = new byte[4];//16
        byte[] format = new byte[2];//20
        byte[] channel = new byte[2];//22
        byte[] freq = new byte[4];// 24 - samples per second (numbers per second)
        byte[] byteseq = new byte[4];// 28 - bytes per second
        byte[] bytesample = new byte[2];// 32 - # of bytes in one sample, for all channels
        byte[] bitdepth = new byte[2];// 34 - how many bits in a sample(number)?  usually 16 or 24

        byte[] namedata = new byte[4]; //data//38
        byte[] datasize = new byte[4];// 40 - how big is this data chunk



        inFile.read(Riff,0,4);
        inFile.read(chekRif,0,4);
        inFile.read(WAVE,0,4);
        inFile.read(fmt,0,4);

        inFile.read(sizeChunk,0,4);
        inFile.read(format,0,2);
        inFile.read(channel,0,2);
        inFile.read(freq,0,4);

        inFile.read(byteseq,0,4);//byteseq
        inFile.read(bytesample,0,2);//bytesample
        inFile.read(bitdepth,0,2);//bitdepth
        inFile.read(namedata,0,4);//namedata
        inFile.read(datasize,0,4);//datasize
        //inFile.read(buffer);//buffer
        //String sr = new String(Riff);
        //String schr = new String(chekRif);
        //String sWav = new String(WAVE);
        //String sfmt = new String(fmt);


        //String ssz = new String(sizeChunk);
        //ByteBuffer ssz = ByteBuffer.wrap(sizeChunk);
        //String sform = new String(format);//format
        //ByteBuffer sform = ByteBuffer.wrap(format);
        //ByteBuffer schanl = ByteBuffer.wrap(channel);//channel

        ByteBuffer sfreq = ByteBuffer.wrap(freq);
        int ssfreq = sfreq.order(ByteOrder.LITTLE_ENDIAN).getInt();

        //ByteBuffer sbyteseq = ByteBuffer.wrap(byteseq);
        //ByteBuffer sbytesample = ByteBuffer.wrap(bytesample);
        //ByteBuffer sbitdepth = ByteBuffer.wrap(bitdepth);

        //byte[] namedata = new byte[4]; //data
        //String snamedata = new String(namedata);
        //byte[] datasize = new byte[4];// 40 - how big is this data chunk
        //ByteBuffer sdatasize = ByteBuffer.wrap(datasize);//datasize 44
        //int sssdatasize = sdatasize.order(ByteOrder.LITTLE_ENDIAN).getInt();
        //byte[] buffer = new byte[inFile.available()];

        //ByteBuffer sbuffer = ByteBuffer.wrap(buffer);


        byte[] byteInput = new byte[(int)inFile.available()];
        short[] input = new short[(int)(byteInput.length / 2f)];
        inFile.read(byteInput, 44, byteInput.length - 45);//data audio
        ByteBuffer.wrap(byteInput).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(input);

        //int sSi = sizeChunk;
        /*System.out.println(sr);
        System.out.println(schr);
        System.out.println(sWav);
        System.out.println(sfmt);
        System.out.println(ssz.order(ByteOrder.LITTLE_ENDIAN).getInt());
        System.out.println(sform.order(ByteOrder.LITTLE_ENDIAN).getShort());
        System.out.println(schanl.order(ByteOrder.LITTLE_ENDIAN).getShort());
        System.out.println(ssfreq);
        System.out.println(sbyteseq.order(ByteOrder.LITTLE_ENDIAN).getInt());
        System.out.println(sbytesample.order(ByteOrder.LITTLE_ENDIAN).getShort());
        System.out.println(sbitdepth.order(ByteOrder.LITTLE_ENDIAN).getShort());
        System.out.println(snamedata);
        System.out.println(sssdatasize/1024/1024);*/


//Request space for the buffer
        IntBuffer bufferPointer = BufferUtils.createIntBuffer(1);
        IntBuffer sourcePointer = BufferUtils.createIntBuffer(1);
        alGenBuffers(bufferPointer);

//Send the data to OpenAL
        //assert rawAudioBuffer != null;
        //assert !(rawAudioBuffer == null);
        //alBufferData(sbuffer, AL_FORMAT_STEREO16, sbuffer, sfreq);
        alBufferData(bufferPointer.get(),AL_FORMAT_STEREO16,input,ssfreq);
        //byteInput;
        inFile.close();
//Free the memory allocated by STB
        //free(input);
        //input.clear();
        sfreq.clear();
        ssfreq=0;
//Request a source
        alGenSources(sourcePointer);

//Assign the sound we just loaded to the source
        alSourcef(sourcePointer.get(), AL_PITCH, 1.0f);
        alSourcef(sourcePointer.get(0), AL_GAIN, 1.0f);
        alSource3f(sourcePointer.get(0), AL_POSITION, 0, 0, 1);
        alSource3f(sourcePointer.get(0), AL_VELOCITY, 0, 0, 1);
        alSourcei(sourcePointer.get(0), AL_LOOPING, AL_FALSE);
        alSourcei(sourcePointer.get(0), AL_BUFFER, bufferPointer.get(0));
        alAuxiliaryEffectSloti(uiEffectSlot[0], AL_EFFECTSLOT_EFFECT, uiEffect[0]);
        AL11.alSource3i(sourcePointer.get(0), AL_AUXILIARY_SEND_FILTER, uiEffectSlot[0], 0, 0);
        AL11.alSource3i(sourcePointer.get(0), AL_AUXILIARY_SEND_FILTER, uiEffectSlot[0], 0, uiFilter[0]);
        AL11.alSource3i(sourcePointer.get(0), AL_AUXILIARY_SEND_FILTER, uiEffectSlot[1], 1, uiFilter[0]);
        AL11.alSource3i(sourcePointer.get(0), AL_AUXILIARY_SEND_FILTER, uiEffectSlot[2], 2, uiFilter[0]);
        alSourcei(sourcePointer.get(0), AL_DIRECT_FILTER, uiFilter[0]);
        AL11.alSource3i(sourcePointer.get(0), AL_AUXILIARY_SEND_FILTER, uiEffectSlot[0], 0, uiFilter[0]);
        alSourcef(sourcePointer.get(0), AL_CONE_OUTER_GAINHF, 1.0f);
        alListenerf(AL_METERS_PER_UNIT, 0.3f);
        alSourcef(sourcePointer.get(0), AL_CONE_OUTER_GAINHF, 1.3f);
        alSourcef(sourcePointer.get(0), AL_MAX_DIRECT_FILTER_GAINHF_AUTO, 1);
//Play the sound
        int state = AL_PLAYING;
        //int prin = 0;
        Scanner statestr = new Scanner(System.in);
        System.out.println("1. Пауза");
        System.out.print("2. Стоп.\n");
        //System.out.print("3. Продолжить трек.\n");
        System.out.print("3. Перемотка вперед.\n");
        alSourcePlay(sourcePointer.get(0));
        while (state==AL_PLAYING) {
            //prin=alGetEffecti(AL_EFFECT_REVERB,prin);
            //System.out.print(prin+"\r");
            int x = statestr.nextInt();
            switch (x){
                case 1:
                {
                    alSourcePause(sourcePointer.get(0));
                    break;
                }
                case 2:
                {
                    alSourceStop(sourcePointer.get(0));
                    break;
                }
                /*case 3:
                {
                    alSourcePlay(sourcePointer.get(0));
                    break;
                }*/
                case 3:
                {
                    alSourceRewind(sourcePointer.get(0));
                    break;
                }
            }
            state = alGetSourcei(sourcePointer.get(0), AL_SOURCE_STATE);


        }
        System.out.println("Done playing.");
//Terminate OpenAL
        alDeleteSources(sourcePointer);
        alDeleteBuffers(bufferPointer);
        alcDestroyContext(context);
        alcCloseDevice(device);
    }
    //public static void main(String[] args) throws NullPointerException, IOException {
        //WavFormS.play();

    //}
}
