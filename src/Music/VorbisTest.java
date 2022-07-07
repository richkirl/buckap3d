package Music;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.*;

import java.io.IOException;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Scanner;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL10.alDeleteBuffers;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.ALC10.alcCloseDevice;
import static org.lwjgl.openal.EXTEfx.*;
import static org.lwjgl.openal.EXTEfx.AL_MAX_DIRECT_FILTER_GAINHF_AUTO;
import static org.lwjgl.stb.STBVorbis.stb_vorbis_decode_filename;
import static org.lwjgl.system.MemoryStack.stackMallocInt;
import static org.lwjgl.system.libc.LibCStdlib.free;

public class VorbisTest {
    long device;
    long context;

    public static void play(String s) throws Exception {
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
            alEffecti(uiEffect[0], AL_EFFECT_TYPE, AL_EFFECT_EAXREVERB);
            if (alGetError() != ALC_NO_ERROR)
                System.out.println("Reverb Effect not supported\n");
            else
                alEffectf(uiEffect[0], AL_EAXREVERB_DECAY_TIME, 5.0f);
        }
        alGetError();
        if (alIsEffect(uiEffect[1])) {
            alEffecti(uiEffect[1], AL_EFFECT_TYPE, AL_EFFECT_COMPRESSOR);
            if (alGetError() != ALC_NO_ERROR)
                System.out.println("CompressorEffect not supported\n");
            //else
            //alEffectf(uiEffect[0],AL_REVERB_DECAY_TIME,4.0f);
        }
        alGetError();
        if (alIsEffect(uiEffect[2])) {
            alEffecti(uiEffect[2], AL_EFFECT_TYPE, AL_EFFECT_FLANGER);
            if (alGetError() != ALC_NO_ERROR)
                System.out.println("Flanger Effect not supported\n");
            else
                alEffectf(uiEffect[2], AL_FLANGER_PHASE, 360);
        }
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
        ShortBuffer rawAudioBuffer;

        int sampleRate;

        IntBuffer channelsBuffer = stackMallocInt(1);

        IntBuffer sampleRateBuffer = stackMallocInt(1);

        rawAudioBuffer = stb_vorbis_decode_filename(s, channelsBuffer, sampleRateBuffer);

        sampleRate = sampleRateBuffer.get();
        //}

//Request space for the buffer
        IntBuffer bufferPointer = BufferUtils.createIntBuffer(1);
        IntBuffer sourcePointer = BufferUtils.createIntBuffer(1);
        alGenBuffers(bufferPointer);

//Send the data to OpenAL
        //assert rawAudioBuffer != null;
        //assert !(rawAudioBuffer == null);
        if(rawAudioBuffer == null)
        {
            System.out.println("Добавьте папку где находится музыка");
            MenuAudio.mainLoop();
        }
        alBufferData(bufferPointer.get(), AL_FORMAT_STEREO16, rawAudioBuffer, sampleRate);

//Free the memory allocated by STB
        free(rawAudioBuffer);

//Request a source
        alGenSources(sourcePointer);

//Assign the sound we just loaded to the source
        AL10.alSourcef(sourcePointer.get(), AL_PITCH, 1.0f);
        AL10.alSourcef(sourcePointer.get(0), AL_GAIN, 1.0f);
        AL10.alSource3f(sourcePointer.get(0), AL_POSITION, 0, 0, 1);
        AL10.alSource3f(sourcePointer.get(0), AL_VELOCITY, 0, 0, 1);
        AL10.alSourcei(sourcePointer.get(0), AL_LOOPING, AL_FALSE);
        AL10.alSourcei(sourcePointer.get(0), AL_BUFFER, bufferPointer.get(0));
        alAuxiliaryEffectSloti(uiEffectSlot[0], AL_EFFECTSLOT_EFFECT, uiEffect[0]);
        AL11.alSource3i(sourcePointer.get(0), AL_AUXILIARY_SEND_FILTER, uiEffectSlot[0], 0, 0);
        AL11.alSource3i(sourcePointer.get(0), AL_AUXILIARY_SEND_FILTER, uiEffectSlot[0], 0, uiFilter[0]);
        AL11.alSource3i(sourcePointer.get(0), AL_AUXILIARY_SEND_FILTER, uiEffectSlot[1], 1, uiFilter[0]);
        AL11.alSource3i(sourcePointer.get(0), AL_AUXILIARY_SEND_FILTER, uiEffectSlot[2], 2, uiFilter[0]);
        AL10.alSourcei(sourcePointer.get(0), AL_DIRECT_FILTER, uiFilter[0]);
        AL11.alSource3i(sourcePointer.get(0), AL_AUXILIARY_SEND_FILTER, uiEffectSlot[0], 0, uiFilter[0]);
        AL10.alSourcef(sourcePointer.get(0), AL_CONE_OUTER_GAINHF, 1.0f);
        AL10.alListenerf(AL_METERS_PER_UNIT, 0.3f);
        AL10.alSourcef(sourcePointer.get(0), AL_CONE_OUTER_GAINHF, 1.3f);
        AL10.alSourcef(sourcePointer.get(0), AL_MAX_DIRECT_FILTER_GAINHF_AUTO, 5);
//Play the sound
        int state = AL_PLAYING;

        Scanner statestr = new Scanner(System.in);
        System.out.println("1. Пауза");
        System.out.print("2. Стоп.\n");
        //System.out.print("3. Продолжить трек.\n");
        System.out.print("3. Перемотка вперед.\n");
        alSourcePlay(sourcePointer.get(0));
        while (state==AL_PLAYING|state==AL_PAUSED) {

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
            state = AL10.alGetSourcei(sourcePointer.get(0), AL10.AL_SOURCE_STATE);

        }
        System.out.println("Done playing.");
//Terminate OpenAL
        alDeleteSources(sourcePointer);
        alDeleteBuffers(bufferPointer);
        alcDestroyContext(context);
        alcCloseDevice(device);
    }
}
