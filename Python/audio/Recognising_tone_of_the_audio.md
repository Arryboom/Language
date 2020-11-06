>https://stackoverflow.com/questions/1797631/recognising-tone-of-the-audio


To recognize the frequency of an audio signal, you would use the FFT ([fast Fourier transform](http://en.wikipedia.org/wiki/Fast_Fourier_transform)) algorithm. As far as I can tell, PyGame has no means to record audio, nor does it support the FFT transform.

First, you need to capture the raw sampled data from the sound card; this kind of data is called PCM (Pulse Code Modulation). The simplest way to capture audio in Python is using the [PyAudio library](http://people.csail.mit.edu/hubert/pyaudio/) (Python bindings to PortAudio). [GStreamer](http://gstreamer.freedesktop.org/) can also do it, it's probably an overkill for your purposes. Capturing 16-bit samples at a rate of 48000 Hz is pretty typical and probably the best a normal sound card will give you.

Once you have raw PCM audio data, you can use the `fftpack` module from the [scipy library](http://www.scipy.org/) to run the samples through the FFT transform. This will give you a _frequency distribution_ of the analysed audio signal, i.e., how strong is the signal in certain frequency bands. Then, it's a matter of finding the frequency that has the strongest signal.

You _might_ need some additional filtering to avoid [harmonic frequencies](http://en.wikipedia.org/wiki/Harmonic) I am not sure.



---



I once wrote a utility that does exactly that - it analyses what sounds are being played.

You can look at the code [here](https://web.archive.org/web/20120112041203/http://trac.assembla.com/guitarist/browser/src/soundAnalyzer/AudioRecorder.py) (or you can download the whole project. its integrated with Frets On Fire, a guitar hero open source clone to create a real guitar hero). It was tested using a guitar, an harmonica and whistles :) The code is ugly, but it works :)

I used pymedia to record, and scipy for the FFT.

Except for the basics that others already noted, I can give you some tips:

1. If you record from mic, there is a lot of noise. You'll have to use a lot of trial-and-error to set thresholds and sound clean up methods to get it working. One possible solution is to use an electric guitar, and plug its output to the audio-in. This worked best for me.
2. Specifically, there is a lot of noise around 50Hz. That's not so bad, but its overtones (see below) are at 100 Hz and 150 Hz, and that's close to guitar's G2 and D3.... As I said my solution was to switch to an electric guitar.
3. There is a tradeoff between speed of detection, and accuracy. The more samples you take, the longer it will take you to detect sounds, but you'll be more accurate detecting the exact pitch. If you really want to make a project out of this, you probably need to use several time scales.
4. When a tones is played, it has [overtones](http://en.wikipedia.org/wiki/Overtone). Sometimes, after a few seconds, the overtones might even be more powerful than the base tone. If you don't deal with this, your program with think it heard E2 for a few seconds, and then E3. To overcome this, I used a list of currently playing sounds, and then as long as this note, or one of its overtones had energy in it, I assumed its the same note being played....
5. It is specifically hard to detect when someone plays the same note 2 (or more) times in a row, because it's hard to distinguish between that, and random fluctuations of sound level. You'll see in my code that I had to use a constant that had to be configured to match the guitar used (apparently every guitar has its own pattern of power fluctuations).



---

You will need to use an audio library such as the built-in [**audioop**](http://docs.python.org/library/audioop.html).

Analyzing the specific note being played is not trivial, but can be done using those APIs.

Also could be of use: [http://wiki.python.org/moin/PythonInMusic](http://wiki.python.org/moin/PythonInMusic)




---


Very similar questions:

- [Audio Processing - Tone Recognition](https://stackoverflow.com/questions/1499932/audio-processing-tone-recognition)
- [Real time pitch detection](https://stackoverflow.com/questions/1354084/real-time-pitch-detection)
- [Real-time pitch detection using FFT](https://stackoverflow.com/questions/1466968/real-time-pitch-detection-using-fft)

Turning sound into a sequence of notes is not an easy thing to do, especially with multiple notes at once. Read through Google results for "frequency estimation" and "note recognition".

I have some [Python frequency estimation examples](http://gist.github.com/255291), but this is only a portion of what you need to solve to get notes from guitar recordings.




---


This [link](http://www.codeproject.com/KB/audio-video/FftGuitarTuner.aspx) shows some one doing it in VB.NET but the basics of what need to be done to achieve your goal is captured in these links below.

- [STFT](http://en.wikipedia.org/wiki/STFT)
- [Colley Tukey](http://en.wikipedia.org/wiki/Cooley-Tukey_FFT_algorithm)
- [FFT](http://en.wikipedia.org/wiki/FFT)