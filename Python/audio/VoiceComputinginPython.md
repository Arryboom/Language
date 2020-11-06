>https://maelfabien.github.io/machinelearning/Speech8/#i-history

These notes are a summary of “An Introduction to Voice Computing in Python” by Jim Schwoebel, crossed with some personal notes and external resources. This books covers the key concepts of Voice Computing, recording, playing, storing and converting audio, extracting features, creating ML models on top, generating data…

# I. History

> A **voice computer** is any computerized system that can process voice inputs.

In 1876, Alexander Graham Bell invented an acoustic telegraph to transmit audio frequencies through electrical wires. Thomas Edison invented the phonograph in 1877, first machine to record a sound and play it back.

One of the first speech recognition software was written in 1952 by Bells Labs and could only recognize digits. In 1985, IBM releases a software using Hidden Markov Models to recognize over 1000 words.

In 1970, Sony invented the Digital Sound Encoding (DSE) to record an analog signal through a condenser microphone and convert it into a ditial representation (series of numbers).

We talk about Analog-to-digital converters (A/D) and Digital-to-analog (D/A). This led to a range of compressed formats, including WAV, invented in 1993. In 2001, lossless speech codecs (.FLAC) were invented, being much smaller and high quality.

Today, several tools such as Python, Tensorflow, Keras, Librosa, Kaldi, and speech-to-text APIs make voice computing easier.

# II. Fundamentals

A _sound wave_ is a vibration which causes a local pressure deviation from the ambient (average or equilibrium) atmospheric pressure, called the sound pressure.

A microphone is a transducer that converts this sound wave into an electrical signal. The electrical signal is measured in Amperes. The ampere is the electric current corresponding to the flow of 1/(1.602 176 634 × 10-19) elementary charges per second. It can also be expressed as Coulombs per seconds.

```
1A=1C/s
```
We record sound through a microphone. It uses a diaphragm to produce changes in distance over capacitor plates to produce electrical current.

The most common types of microphones are:

- condenser: Uses two capacitor plates and a diaphragm to produce electric current.
- dynamic: Uses induction coils attached to a diaphragm to produce sound via electromagnetic induction.

In most computers, a sound card receives an analog signal, converts it into a digital format using A/D converter and plays it back using D/A converters on speakers. The digital format file is generated through an audio codec software.

An audio transcoding is the conversion of one file format to another. The most common audio coding formats are:

- MP3: high compression, signal loss
- WAV: larger file sizes
- AAC: proprietary format, better than MP3
- FLAC: small size, little loss, but must be converted before analysis
- OPUS: human voice range files, losses can be problematic

1 minute of .WAV file is 3.5MB. Once converted to FLAC, it only takes 1.5MB. This is really useful for storage.

Audio channels is the number of audio inputs and outputs of a recorded audio signal:

- mono: 1 channel, 1 microphone, 1 output on speakers
- stereo: 2 channels, 2 microphones (left and right), 2 speakers

We can convert stereo into mono by averaging the amplitude of both channels into a sigle signal channel. We can also convert mono into stereo by duplicating the information on the other channel.

Speakers work by making a magnet move to produce pressure waves. A loudspeaker is a speaker made of several sub-speakers, called drivers, each in charge of different parts of the audio spectrum:

- tweeters: 2’000 - 20’000 Hz
- mid-range: 250 - 2’000 Hz
- woofers: 40 - 500 Hz

# III. Basic audio processing in Python

## III.1. Read Files

There are several key libraries in Python to read and manipulate audio files each having specific advantages:

- Pydub: Simple features to manipulate file

```
from pydub import AudioSegment
data = AudioSegment.from_wav("test.wav")
```

- SoX: Remove silence, remove noise, add chorus… Can be done in command line or using Pysox.
- Wave: adding filters

```
import wave
data=wave.open('test.wav', mode='rb')
```

- LibROSA: audio engineering and featurization

```
import librosa
y, sr = librosa.load('test.wav')
```

- SciPy: connecting to Matlab, Fortran made easy

```
from scipy.io import wavfile
fs, data = wavfile.read('test.wav')
```

## III.2. Manipulate files

We can either use SoX or Pydub for audio file manipulation. Here are a series of commands using SoX:

- Combine files: take in one.wav and two.wav to make three.wav
    
    ```
    sox one.wav two.wav three.wav
    ```
    
- Trim: take first second of one.wav and output to output.wav
    
    ```
    sox one.wav output.wav trim 0 1
    ```
    
- Increase volume: make volume 2x in one.wav and output to volup.wav
    
    ```
    sox -v 2.0 one.wav volup.wav
    ```
    
- Reverse: reverse one.wav and output to reverse.wav
    
    ```
    sox one.wav reverse.wav reverse
    ```
    
- Change sampling rate: change sample rate of one.wav to 16000 Hz
    
    ```
    sox one.wav -r 16000 sr.wav
    ```
    
- Changing audio quality: change audio file to 16 bit quality
    
    ```
    sox -b 16 one.wav 16bit.wav
    ```
    
- Convert mono file to stereo: convert mono file to stereo by cloning channels
    
    ```
    sox one.wav -c 2 stereo.wav
    ```
    
- Convert stereo file to mono: make stereo file mono by averaging out the channels
    
    ```
    sox stereo.wav -c 1 mono.wav
    ```
    
- Change audio file speed: double speed of file
    
    ```
    sox one.wav 2x.wav speed 2.0
    ```
    

Here are some commands to manipulate audio files using Pydub:

- Load audio file:
    
    ```
    from pydub import AudioSegment
    song = AudioSegment.from_wav("song.wav")
    ```
    
- Slice audio:
    
    ```
    # pydub does things in milliseconds
    ten_seconds = 10 * 1000 
    first_10_seconds = song[:ten_seconds] 
    last_5_seconds = song[-5000:]
    ```
    
- Make beginning louder and end quieter:
    
    ```
    # boost volume by 6dB
    beginning = first_10_seconds + 6 
    # reduce volume by 3dB
    end = last_5_seconds - 3
    ```
    
- Crossfade:
    
    ```
    # 1.5 second crossfade
    with_style = beginning.append(end, crossfade=1500)
    ```
    
- Repeat:
    
    ```
    # repeat the clip twice
    do_it_over = with_style * 2
    ```
    
- Fading in and out:
    
    ```
    # 2 sec fade in, 3 sec fade out
    awesome = do_it_over.fade_in(2000).fade_out(3000)
    ```
    
- Output results as .MP3:
    
    ```
    # export .mp3
    awesome.export("mashup.mp3", format="mp3")
    ```
    

## III.3. Playing audio

There are two ways to play audio:

- _synchronous_: playing the audio file is the only code executed at the moment
- _asynchronous_: playing the audio file as a background and keep executing code

For _synchronous_ playing, we can use Pygame.

```
import pygame
pygame.mixer.init() 
pygame.mixer.music.load('test.wav') 
pygame.mixer.music.play()
```

For asynchronous playing, one can use Sounddevice:

```
import sounddevice as sd
import soundfile as sf
import time

data, fs = sf.read('test.wav') 
sd.play(data, fs)

print("Execute this")
time.sleep(5)

sd.stop()
```

## III.4. Recording audio

You can check the configuration of your microphone using Sounddevice. This will display the information of the input and the output.

```
import sounddevice as sd
mics=sd.query_devices() 
for i in range(len(mics)): 
	print(mics[i])
```

To actually record a sound, we can use the sd.rec function provided by sounddevice.

```
duration = 10
fs = 10000
channels = 1
myrecording = sd.rec(int(duration * fs), samplerate=fs, channels=channels) 
sd.wait()
sf.write('sync_record.wav', myrecording, fs)
```

## III.5. Transcoding

To transcode from one format to another, one can use FFmpeg. It has a wrapper called ffmpy in Python. To run a conversion from one format to another, simply specify the input file name, and the expected output extension.

```
filename='test.mp3'
ff = ffmpy.FFmpeg(inputs={filename:None}, outputs={filename[0:-4]+'.wav': None})
ff.run()
```

We can also use ffmpeg directly in command lines.

- Convert audio from one file format to another:
    
    ```
    ffmpeg -i input.mp3 output.ogg
    ```
    
- Extract audio from a video:
    
    ```
    ffmpeg -i video.mp4 -vn -ab 256 audio.mp3
    ```
    
- Merge audio and video files:
    
    ```
    ffmpeg -i video.mp4 -i audio.mp3 -c:v copy -c:a aac -strict experimental output.mp4
    ```
    
- Add a cover image to audio file:
    
    ```
    ffmpeg -loop 1 -i image.jpg -i audio.mp3 -c:v libx264 -c:a aac -strict experimental -b:a 192k -shortest output.mp4
    ```
    
- Crop an audio file (second 90 to second 120):
    
    ```
    ffmpeg -ss 00:01:30 -t 30 -acodec copy -i inputfile.mp3 outputfile.mp3
    ```
    

## III.6. Speech-to-text (Transcribing)

There are three alternatives for this approach:

- use open-source pre-trained models such as PocketSphinx
- use proprietary models such as Google Speech-to-text
- train your own model with Kaldi or from scratch

### PocketSphinx

```
import speech_recognition as sr_audio 

r = sr_audio.Recognizer()
audio = r.record(sr_audio.AudioFile(filename)) 
text=r.recognize_sphinx(audio) 
print('transcript: '+text) 
```

### Google API

You need to have your credentials set in your bash profile.

```
text=r.recognize_google_cloud(audio)
print('transcript: '+text) 
```

## III.7. Text-to-speech

Text-to-speech systems are the other side of voice assistants. Once you understood and processed the query, you need to pronounce the answer, give the temperature…

The most popular open-source library to do that is pyttsx3.

```
import pyttsx3

engine = pyttsx3.init() 
engine.say("Test 1 2 3") 
engine.runAndWait()
```

You can also go for the Google Cloud API solution, free for up to 1 million characters per month.

```
from google.cloud import texttospeech

client = texttospeech.TextToSpeechClient()
input_text = texttospeech.types.SynthesisInput(text=text)

# Names of voices can be retrieved with client.list_voices(). 
voice = texttospeech.types.VoiceSelectionParams(language_code='en-US', ssml_gender=texttospeech.enums.SsmlVoiceGender.FEMALE, name='en-US-Wavenet-A')

audio_config = texttospeech.types.AudioConfig(audio_encoding=texttospeech.enums.AudioEncoding.MP3)
response = client.synthesize_speech("Test 1 2 3", voice, audio_config)

# The response's audio_content is binary.
with open(filename, 'wb') as out: 
    out.write(response.audio_content)
    print('Audio content written to file %s'%(filename))
```

# IV. Sound Collection

## IV.1. Mistakes to avoid

When collecting sound, major mistakes are:

- bad microphone
- too much distance
- noisy environment
- bad sample rate
- bad number of channels
- bad transcoding methodology

To make it comparable, all these variables should be kept constant over the recording.

Mono channels have a typical sampling rate of 16’000 Hz, whereas stereo channels have a sampling rate of 44’100 Hz. Accordin to the Nyquist rate, a sound should be sampled at twice the maximum frequency. The human ear hears sounds up to 20’000 Hz, therefore 44’100 > 20’000 \* 2 satisfies this requirement.

_Beamforming_ is a signal processing technique that allows for directional reception of audio signals to enhance audio quality. Acoustic echo suppression (AES) and acoustic echo cancellation (AEC) algorithms improve voice quality by suppressing acoustic echo (e.g. sounds played back through loudspeaker) and line echo (e.g. electrical impulses in wires and impedance mismatches). Together, these techniques greatly enhance audio files to achieve a higher signal-to-noise (SNR) ratio.

The collection can be passive (no response expected from the program, in the background), or active. Here is an example of a voice-first application, falling under the active-synchronous (AS) mode category, that launches a wheather website if the person says “weather” within the first 2 seconds after launching the code:

```
import sounddevice as sd
import soundfile as sf
import speech_recognition as sr_audio 
import os, pyttsx3, pygame, time

def sync_record(filename, duration, fs, channels):
    print('recording')
    myrecording = sd.rec(int(duration * fs), samplerate=fs, channels=channels) 
    sd.wait()
    sf.write(filename, myrecording, fs)
    print('done recording')
    
def sync_playback(filename):
    # takes in a file and plays it back 
    pygame.mixer.init() 
    pygame.mixer.music.load(filename) 
    pygame.mixer.music.play()
    
def speak_text(text): 
    engine=pyttsx3.init() 
    engine.setProperty('voice', 'com.apple.speech.synthesis.voice.Alex')
    engine.say(text) 
    engine.runAndWait()
    
def transcribe_audio_sphinx(filename):
# transcribe the audio (note this is only done if a voice sample) r=sr_audio.Recognizer()
    with sr_audio.AudioFile(filename) as source:
        audio = r.record(source)
        
    text=r.recognize_sphinx(audio) 
    print('transcript: '+text)
    return text

def fetch_weather():
    os.system('open https://www.yahoo.com/news/weather')
    
speak_text('What do you want to search?')
sync_record('response.wav',2,16000,1) 
transcript=transcribe_audio_sphinx('response.wav')

if transcript.lower().find('weather') >= 0:
    fetch_weather()
```

## IV.2. Cleaning audio files

### IV.2.a. Noise reduction

There are many ways to remove the noise from a given audio recording. All it requires is a small sample where there is only a background noise, and then automatically delete this noise from the rest of the sample.

The steps of the algorithm are usually the following:

- An FFT is calculated over the noise audio clip Statistics are calculated over FFT of the the noise (in frequency)
- A threshold is calculated based upon the statistics of the noise (and the desired sensitivity of the algorithm)
- An FFT is calculated over the signal
- A mask is determined by comparing the signal FFT to the threshold
- The mask is smoothed with a filter over frequency and time
- The mask is appled to the FFT of the signal, and is inverted

We can use the noisereduce package in Python:

```
import noisereduce as nr
# load data
rate, data = wavfile.read("mywav.wav")
# select section of data that is noise
noisy_part = data[10000:15000]
# perform noise reduction
reduced_noise = nr.reduce_noise(audio_clip=data, noise_clip=noisy_part, verbose=True)
```

### IV.2.b. Change volume

Changing volume is another way to clean the audio file.

```
from pydub import AudioSegment
song = AudioSegment.from_wav("song.wav")
# boost volume by 5 dB
more_volume = song + 5
```

### IV.2.c. Change sampling rate

By changing the sampling rate, one can modify the quality of the audio. This can be done using SoX:

```
sox 'test.wav' -r 48000k 'new_samle_rate.wav'
```

## IV.3. Speaker Diarization

Separating multiple speakers in a conversation is called Speaker Diarization. This is usually done using Fisher Linear Semi-Discriminant Analysis.