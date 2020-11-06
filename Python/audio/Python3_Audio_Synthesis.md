>https://pythonaudiosynthesisbasics.com/


![](/pics/screencapture-pythonaudiosynthesisbasics-2020-11-06-13_20_46.png)


Python 3 sound synthesis, easy to learn, code then listen to straight away, right now. Everyone loves synthesizers and messing with sound. Python is more than capable of shaping and skewing sound into any weird and wonderful whatever... In any key...Mapped to your keyboard. So get stuck in. Go on. DO IT!

## Starter

Your going to need numpy, and sounddevice ( for listening ), matplotlib ( for plotting ) and scipy ( for reading and writing .wav files and stuff ). pip3 install numpy pip3 install matplotlib pip3 install scipy pip3 install sounddevice

The numpy linspace array is like a list or range of numbers with a start value, stop value and step.

x = np.linspace( start, stop, step ).

Step is how many elements with equaly spaced values the range is chopped up into. This can be used as the 'x' axis or 'time' axis in a sin(x) function. The most common sample rate for an audio file like in a .wav file is 44100 Hz. Here the sample rate has been set at 44100 Hz so that means the resulting sin(x) array will be played at a rate of 44100 elements per second for a given duration.

x = np.linspace( 0, duration, int( 44100 \* duration ))

Here you can see the range of the 'x' axis linspace array is start = 0, stop = duration, and the step = 44100 (sample\_rate) multiplied by the duration. Remember it will be played at 44100 elements per second so multiply that by 3,and you got three seconds of sound right? Thats 3 divided into 3 x 44100 equaly spaced values.

The numpy sin function uses radians. To get a complete cycle of a sine wave using sin(x) for 1 second you need to multiply the stop value of x by 2π right?.. For 0 to 3 (stop = 3 x 2 x π) you get 3 cycles. Now all you have to do to get a desired frequency is multiply the sin of x by the frequency. sin( frequency \* x ).

x = np.linspace( 0, duration \* 2 \* np.pi, int(duration \* sample\_rate))

y = np.sin(frequency \* x)

Next some code to listen to it. Sounddevice is a fantastic package, dead easy:- **sd.play( data, sample\_rate )**. Then use time.sleep( number of seconds duration ) to pause your program while sounddevice executes in the background playing your data through your speakers, then **sd.stop()** to stop the sounddevice playback execution. Have your speakers on a low volume on the first play of a new wave.

![tremolo plot](https://d33wubrfki0l68.cloudfront.net/791b42bd08881c7733ceb37a58c2d3723f401ef5/d12e7/media/trem_2000.jpg)

[Next Page](https://pythonaudiosynthesisbasics.com/two.html)

![Showing individual samples](https://d33wubrfki0l68.cloudfront.net/6cd60a15233851d93230dd50f0426102c97b24a0/553cf/media/guitar_plot.jpg)

            `import numpy as np
          import sounddevice as sd
          import time


          sample_rate = 44100
          duration = 3.0
          frequency = 440.0

          x = np.linspace(0, duration * 2 * np.pi, int(duration * sample_rate))
          sinewave_data = np.sin(frequency * x)

          # best to attenuate it before playing, they can get very loud
          sinewave_data = sinewave_data * 0.3

          sd.play(sinewave_data, sample_rate)
          time.sleep(duration)
          sd.stop()` 
          

Now you have heard it, you can plot a slice to see what it looks like by adding this code using matplotlib.

![Sinewave plot](https://d33wubrfki0l68.cloudfront.net/c0da68de337b5d3b8e9322427187e82ef4f93d59/c107c/media/sin_wave.jpg)

##### Stick this in your editor....Click

 [![FM chord plot](https://d33wubrfki0l68.cloudfront.net/de6bedd24aaab38bd71a7decca5aa197800689d8/ac1d2/media/proper_job.jpg)](https://pythonaudiosynthesisbasics.com/#together) 

            `import matplotlib.pyplot as plt

          # slicing 500 samples off of your wavfile should show about 5 cycles
          plot_data = sinewave_data[:500]

          fig, ax = plt.subplots()
          ax.plot(plot_data, linewidth=3)
          plt.xlabel('Sample Number')
          plt.ylabel('Amplitude')
          plt.title('Sine Wave')
          plt.show()` 
          

If you wish, you could write it as a .wav file by adding this code using scipy. First, convert our array into (2's complement) signed 16bit integers. np.int16(sinewave\_data \* 32767). This ranges from -32768 to 32767. Now it can be written with the **scipy.io.wavfile.write( 'filename.wav', sample\_rate, data )**

            `import scipy.io.wavfile as wf

          write_data = np.int16(sinewave_data * 32767)
          wf.write('440Hz_sinewave.wav', sample_rate, write_data)` 
          

Add waves together to make chords. below is E4 major guitar style chord. It does not sound very impressive yet, theres a bit more work to do.

            `sinewave_data = np.sin(440.0 * x) + np.sin(
                              329.6276 * x) + np.sin(659.2551 * x)` 
          

Adding different shaped waves gives the sound more color, depth etc. But First try a bit of frequency modulation, it is the key to some really awesome sounds further on.

##### Make a Triangle Wave

 [![Triangle wave plot](https://d33wubrfki0l68.cloudfront.net/e1077464255fe4fc9fc729168a6cfef9dc3e8b84/818f8/media/triangle_wave.jpg)](https://pythonaudiosynthesisbasics.com/#triangle-wave) 

##### Make Sawtooth and Square Waves

 [![Triangle and square waves plot](https://d33wubrfki0l68.cloudfront.net/f406c709881b897ed60ebadc298789a9d33721e0/2afaa/media/tri_squ.jpg)](https://pythonaudiosynthesisbasics.com/#tri-squ) 

## Frequency Modulation

This is where the fun starts. Here the frequency of the sinewave is being varied by another sinewave. The first sine wave is called the carrier, the second is the modulator. It is possible to adjust the range the tone is bent up and down by multiplying the second sine function by some factor ( np.sin(fm\_0 \* x ) \* factor ). Try it! A factor of about 13 and reduce fm\_0 to about 5 Hz and it sounds like some siren off of an old 1950s scifi or something. The graph shows the varying frequency as a function of time.

            `import numpy as np
        import sounddevice as sd
        import time


        sample_rate = 44100
        duration = 6.0
        frequency_0 = 440.0   # Carrier. Experiment with different frequencies.
        fm_0 = 439.0          # Modulation frequency, mess with this figure too .
        factor = 2.0

        x = np.linspace(0, duration * 2 * np.pi, int(duration * sample_rate))
        wave_data = np.sin(frequency_0 * x + (np.sin(fm_0 * x) * factor))

        # Don't forget to attenuate: wave_data times the attenuation=0.3 below
        wave_data = wave_data * 0.2

        sd.play(wave_data, sample_rate)
        time.sleep(duration)
        sd.stop()` 
          

![Frequency modulation plot](https://d33wubrfki0l68.cloudfront.net/401d7a12aee92c32772aed59d28c105640fe87aa/ffc0f/media/fm2_big.jpg)

A log ramp can be used to change the carrier phase and modulating frequency simultaneously as a funtion of time. Add it to the carrier function before multiplying by the modulator function. It sounds awesome.

            `import numpy as np
        import sounddevice as sd
        import time


        sample_rate = 44100
        duration = 6.0
        frequency_0 = 880.0    # Carrier.
        fm_0 = 73.3333            # modulator.

        ramp_0 = np.logspace(0, 1, int(duration * sample_rate))

        x = np.linspace(0, duration * 2 * np.pi, int(duration * sample_rate))

        wave_data = np.sin(frequency_0 * x + ramp_0 * np.sin(fm_0 * x))

        wave_data = wave_data * 0.2

        sd.play(wave_data, sample_rate)
        time.sleep(duration)
        sd.stop()` 
          

Ok. Maybe modulate the modulator as well then. wave\_data = np.sin(frequency\_0 \* x + ramp\_0 \* np.sin(fm\_0 \* x + np.sin(x \* 586.666))) Now it sounds Awesome.

 [![FM with triangle wave plot](https://d33wubrfki0l68.cloudfront.net/cd88e9d43309b2939eb9a02dc4b9201cac36bfdb/4c0f1/media/fm_wiv_tr1.jpg)](https://pythonaudiosynthesisbasics.com/#fm-tri) 

## tremolo

The graph opposite shows the amplitude being attenuated by multiplying the wave\_data by another sinewave. When sin(x) = 1 theres no attenuation. when sin(x) = 0 the amplitude is multiplied by 0, if sin(x) = 0.4, amplitude is multiplied by 0.4. You get the picture.

You can just simply multiply the wave\_data by the tremolo\_osc. The output sounds interesting, infact it sounds good, but this means the wave\_data is also being multiplied by negetive numbers. (ring modulators do this, check the plot at the top of the page!) This is not what you want if your appying tremolo to a music file array say. Try it. The tremolo wave must swing between 0 and 1. Wave\_data \* tremolo\_osc / 2 + 0.5 will work, but if you want to adjust the tremolo amount by some factor you must add another line instead:-

tremolo\_osc = (tremolo\_osc \* factor / 2 + (1 - factor / 2))

This will make the tremolo\_osc oscillate by some factor less than or equal to 1 while keeping the maximum value at 1 and at no time go below 0. Now with this code added, multiply the wave\_data by the tremolo\_osc to shape the wave as usual.

I have also added the log ramp. The ramp amount can also be adjusted by multiplying by some factor. I have set ramp\_amount to 2.1 in this example.

            `import matplotlib.pyplot as plt
        import numpy as np
        import sounddevice as sd
        import time


        sample_rate = 44100
        duration = 6.0
        frequency_0 = 440.0
        fm_0 = 430.0
        tremolo_frequency = 8.0   # experiment with this number
        ramp_amount = 2.1         # experiment with this number too.
        factor = 0.9              # tremolo amount between 0 and 1

        ramp_0 = np.logspace(0, 1, int(duration * sample_rate)) * ramp_amount
        x = np.linspace(0, duration * 2 * np.pi, int(duration * sample_rate))
        x_lfo = np.linspace(0, duration  * 2 * np.pi, int(duration * sample_rate))

        tremolo_osc = np.sin(tremolo_frequency * x)
        tremolo_osc = (tremolo_osc * factor / 2 + (1 - factor / 2))

        print(np.max(tremolo_osc))
        print(np.min(tremolo_osc))
        wave_data = np.sin(frequency_0 * x + ramp_0 * np.sin(
                           fm_0 * x)) * tremolo_osc

        wave_data = wave_data * 0.2

        sd.play(wave_data, sample_rate)
        time.sleep(duration)
        sd.stop()


        plot_data = wave_data[:10000]
        fig, ax = plt.subplots()
        ax.plot(plot_data)
        plt.title('tremolo')
        plt.xlabel('Sample Number')
        plt.ylabel('Amplitude')
        plt.show()` 
          

![tremolo plot](https://d33wubrfki0l68.cloudfront.net/d3f1ac8e50283f95e72173fa60b57b60bf9ac2a2/f3aa0/media/trem_negless.jpg)

##### Add tremolo To A .wav Sample

 [![tremolo to music file plot](https://d33wubrfki0l68.cloudfront.net/5b91da4fe5d2a9b8ee0adc55be418f7a0468a3ee/3d0b5/media/trem_to_wav.jpg)](https://pythonaudiosynthesisbasics.com/#trem-wav) 

To shape the modulating frequency you can also have reverse log ramps of course:- np.logspace( 1, 0, int(duration \* sample\_rate) ), or linear ramps, np.linspace( 0, 1, int(duration \* sample\_rate) ) Or even a low frequency oscilator instead ( another sine wave ) Start mixing and matching, endless fun. And as for shaping waves with oscilators, you can use these ramps too.

Ramps can also be used the control the amplitude (vol) of the resulting wave. This is envelope shaping. The graph opposite shows two ramps concatenated together with another array. Click on it to skip to the code example.

##### Envelope Shaping

 [![Shaped envelope plot](https://d33wubrfki0l68.cloudfront.net/4963b1e3597e7cfbcc596dcfb26adebbceef71f6/97eb0/media/env3.jpg)](https://pythonaudiosynthesisbasics.com/#shaper) 

## Gui To Adjust Variables

This example uses tkinter. If your not familiar with this package, you may have to pip install tkinter if it did not come with your python 3 instalation or in linux :-

###### sudo apt-get install python-tk

Define a function called play and paste the whole wave making and playing program in it. After importing tkinter as tk, make an instance of a window:- root = tk.Tk() Set window size if you wish:- root.geometry() The sliders will replace the assigned variables. A slider widget object in tkinter is:- tk.Scale( root, from\_= , to=, resolution=, orient=tk.HORIZONTAL )

- from\_ : minimum variable value goes here
- to : maximum variable value goes here
- resolution : size of steps
- orient : wether a tk.HORIZONTAL or tk.VERTICAL slider is required

Get the value from the sliders by using the .get() method as a float, as your variable:- duration = float( duration\_scale.get() ) for example. The play button is:- tk.Button( root, text='Play', command=play ) The command argument assigns it to the play funtion. Remember not to include the perenthisis so it does not get called until the button is clicked. Arrange it all in a grid with .grid( row=, column=, ) or do the .pack() thingy. Lastly:- root.mainloop() to keep the window open. There is no time.sleep() or sd.stop() here so the function can return while sounddevice completes playback in its own thread. This leaves you free to play with the sliders ready for the next call of the play function.

            `import numpy as np
        import sounddevice as sd
        import tkinter as tk


        def play():
            play_button.config(text='Wait', state='disabled')
            play_button.update()      # Disable button until functions finnished
            duration = float(duration_scale.get())
            frequency_0 = float(frequency_0_scale.get())
            fm_0 = float(fm_0_scale.get())
            ramp_amount = float(ramp_amount_scale.get())

            ramp_0 = np.logspace(0, 1, int(duration * sample_rate)) * ramp_amount
            x = np.linspace(0, duration  * 2 * np.pi, int(duration * sample_rate))
            wave_data = np.sin(frequency_0 * x + ramp_0 * np.sin(
                               fm_0 * x))

            wave_data = wave_data * 0.25

            sd.play(wave_data, sample_rate)

            # Enable play button again
            play_button.update()
            play_button.config(text='Play', state='normal')


        sample_rate = 44100

        root = tk.Tk()
        root.geometry("400x300")

        duration_label = tk.Label(root, text='Duration')
        frequency_0_label = tk.Label(root, text='Frequency')
        fm_0_label = tk.Label(root, text='Fm')
        ramp_amount_label = tk.Label(root, text='Ramp Amount')

        duration_scale = tk.Scale(root, from_=1, to=20,
                                  resolution=1, orient=tk.HORIZONTAL)
        frequency_0_scale = tk.Scale(root, from_=220, to=880,
                                     resolution=10, orient=tk.HORIZONTAL)
        fm_0_scale = tk.Scale(root, from_=60, to=660,
                              resolution=10, orient=tk.HORIZONTAL)
        ramp_amount_scale = tk.Scale(root, from_=0.4, to=6.0,
                                     resolution=0.2, orient=tk.HORIZONTAL)
        play_button = tk.Button(root, text='Play', command=play)

        duration_label.grid(row=0, column=0)
        frequency_0_label.grid(row=1, column=0)
        fm_0_label.grid(row=2, column=0)
        ramp_amount_label.grid(row=3, column=0)
        duration_scale.grid(row=0, column=1)
        frequency_0_scale.grid(row=1, column=1)
        fm_0_scale.grid(row=2, column=1)
        ramp_amount_scale.grid(row=3, column=1)
        play_button.grid(row=0, column=2)

        root.mainloop()` 
          

On the next page bind your computer keyboard keys to different notes so you can bash out a tune and play with the sliders :)

![Gui screenshot](https://d33wubrfki0l68.cloudfront.net/8216f4a00daab90a2a2d712e69337f055850d877/97b8c/media/gui.jpg)

## Triangle Wave

This is a triangle wave formula I found on wikipedia then factored out the 2π that is already multiplied into the x array. This one can be written straight into python code.

![Triangle wave formula](https://d33wubrfki0l68.cloudfront.net/d46a6fbd1ecead3d0109163699143640c35b41e9/8bc57/media/triangle_formula.jpg)

Matplotlib has many figure styles. Here I have used the 'dark\_background'. Directly underneath the imports select the style you want with:- plt.style.use() for a style other than the deafault. The different styles can be viewed with:- print( plt.style.available ).

            `import numpy as np
        import sounddevice as sd
        import time
        import matplotlib.pyplot as plt
        plt.style.use('dark_background')


        sample_rate = 44100
        duration = 2.0
        frequency = 440

        x = np.linspace(0, duration * 2 * np.pi, int(duration * sample_rate))
        wave_data = 2 / np.pi * np.arcsin(np.sin(frequency * x)) * 0.5

        sd.play(wave_data, sample_rate)
        time.sleep(duration)
        sd.stop()

        fig, ax = plt.subplots()
        ax.plot(wave_data[:400], linewidth=3.0)
        plt.title('Triangle Wave', fontsize=20, y=1.03)
        plt.xlabel('Sample Number', fontsize=15)
        plt.ylabel('Amplitude', fontsize=15)
        plt.show()` 
          

![Triangle wave](https://d33wubrfki0l68.cloudfront.net/e1077464255fe4fc9fc729168a6cfef9dc3e8b84/818f8/media/triangle_wave.jpg)

##### Make a Sawtooth with sinewaves

 [![Reverse sawtooth plot](https://d33wubrfki0l68.cloudfront.net/b14ec547ee9f2a7c0504c48e6a1ab6ebb24b61d6/ae0c0/media/reverse_sawtooth.jpg)](https://pythonaudiosynthesisbasics.com/#sawtooth) 

## Frequency Modulating a Triange Wave

Waves are easier to mess with if their put into functions, then you can add, subtract, modulate, whatever, with the whole function as shown below. Here the triangle wave is being modulated with the sin\_wave function.

            `import numpy as np
        import sounddevice as sd
        import time


        def sin_wave():
            return np.sin(fm_0 * x)


        def wave_data():
            return 2 / np.pi * np.arcsin(
                   np.sin(frequency_0 * x + ramp_0 * sin_wave())) * 0.2


        sample_rate = 44100
        duration = 8.0
        frequency_0 = 440
        fm_0 = 100

        x = np.linspace(0, duration * 2 *np.pi, int(duration * sample_rate))
        ramp_0 = np.logspace(0, 1, int(duration * sample_rate)) * 7

        result = wave_data()

        sd.play(result, sample_rate)
        time.sleep(duration)
        sd.stop()` 
          

![FM with triangle wave plot](https://d33wubrfki0l68.cloudfront.net/cd88e9d43309b2939eb9a02dc4b9201cac36bfdb/4c0f1/media/fm_wiv_tr1.jpg)

## Put It All Together

Here I have picked out some frequencies to make a chord. A list of 'Piano key frequencies' can be found on Wikipedia. An assortment of waves and modulating waves and the like have been defined as functions then added together to make the chord. Now it is starting to sound proper.

            `import numpy as np
        import sounddevice as sd
        import time


        def sine(f):
            y = np.sin(f * x) * 0.2
            return y


        def triangle(f1, f2):
            y = 2 / np.pi * np.arcsin(np.sin(f1 * x + ramp_1 * sine(f2))) * 0.2
            return y


        def triangle2(f):
            y = 2 / np.pi * np.arcsin(np.sin(f * x)) * 0.2
            return y


        def triangle_mod(f):
            y = 2 / np.pi * np.arcsin(np.sin(A4 * x + ramp_0 * np.sin(f * x))) * 0.2
            return y


        def lfo():
            y = np.sin(lfo_f * x)
            y = (y * lf0_amount / 2 + (1 - lf0_amount / 2))
            return y


        sample_rate = 44100
        duration = 20.0
        freq1 = 400.0
        freq2 = 300.0
        A1 = 55.0
        A4 = 440.0
        Bb5 = 932.3275
        Bb6 = 1864.655
        Eb5 = 622.2540
        E2 = 82.40689
        lfo_f = 10.0
        lf0_amount = 0.1

        x = np.linspace(0, duration * 2 * np.pi, int(duration * sample_rate))

        ramp_0 = np.logspace(0, 1, int(duration * sample_rate)) * 4
        ramp_1 = np.logspace(1, 0, int(duration * sample_rate)) * 5

        wave_data = ((triangle_mod(freq1) + sine(freq2) + triangle(Bb6, Eb5)
                      + triangle2(Bb5) + sine(E2) + sine(A1)) * lfo()) * 0.2

        sd.play(wave_data, sample_rate)
        time.sleep(duration)
        sd.stop()` 
          

![Modulated chord plot](https://d33wubrfki0l68.cloudfront.net/de6bedd24aaab38bd71a7decca5aa197800689d8/ac1d2/media/proper_job.jpg)

## Sawtooth and Square Waves

![Sawtooth wave formula](https://d33wubrfki0l68.cloudfront.net/81ac2a3c9a3a96abd90e216cc564cdc7e37f0be3/0eeeb/media/sawtooth_formula.jpg)

A sawtooth formula to write into python code. Not forgetting to multiply the frequency f in this formula by the 2 \* π to factor out the 2 \* π in the x array.

This square wave is just a sine wave rounded off to the nearest integer, after dividing by 2 and adding 0.5. This makes it oscillate between 0 and 1 instead of -1 and 1. numpy.round() is perfect for the job, now the nearest integer is 0 and 1. Now subtract 0.5 to make it oscillate around the zero axis at 0.5 and -0.5, this keeps the volume down to the level of the sawtooth.

            `import numpy as np
        import sounddevice as sd
        import time
        import matplotlib.pyplot as plt
        plt.style.use('dark_background')


        def sawtooth():
            return -2 / np.pi * np.arctan(
                    np.tan(np.pi / 2 - (x * np.pi / (1 / frequency_0 * 2 * np.pi))))


        def square():
            a = np.sin(frequency_0 * x) / 2 + 0.5
            d = np.round(a) - 0.5
            return d


        sample_rate = 44100
        duration = 1.0
        frequency_0 = 440

        x = np.linspace(0, duration * 2 * np.pi, int(duration * sample_rate))

        # Concatenate into one array.
        sawtooth_and_square_play = np.concatenate((sawtooth(), square())) * 0.3

        # set sleep long enough for sd to play both waves.
        sd.play(sawtooth_and_square_play, sample_rate)
        time.sleep(duration * 2)
        sd.stop()

        # Plot with no atenuation.
        sawtooth_plot = sawtooth()
        square_plot = square()

        fig, (ax0, ax1) = plt.subplots(nrows=2)
        ax0.plot(sawtooth_plot[:400], linewidth=2.0)
        ax1.plot(square_plot[:400], linewidth=2.0)

        plt.show()` 
          

Alternatively you could always use scipy.signal.sawtooth(x, width=1) and scipy.signal.square(x, duty=0.5). The duty arg = the proportion of mark to the space. The sawtooth width arg = the rising ramp as a proportion of the total cycle. A width of 0.5 would make it a triangle wave.

            `import scipy.signal as sig

          def sawtooth():
              return sig.sawtooth(x * 440, 0.8) * 0.5


          def square():
              return sig.square(x * 330, 0.8) * 0.2` 
          

![Triangle an square waves plot](https://d33wubrfki0l68.cloudfront.net/f406c709881b897ed60ebadc298789a9d33721e0/2afaa/media/tri_squ.jpg)

## Python Dictionary Of Notes

Construct a python dictionary of notes as the keys and frequencies for values. Use the get method in whatever wave function with the key value as the argument. Theres the option of adding a default value as a second argument which will be returned if the key argument given don't exist in the dictionary.

name\_of\_dictionary.get( key, default\_value )

            `def sine_wave(note):
            f = notes.get(note, 220.0)    # The get method.
            y = np.sin(f * x)
            return y * 0.2

        # The dictionary.
        notes = {'E4': 329.6276, 'F4': 349.2282, 'G4b': 369.9944, 'G4': 391.9954,
                 'A4b': 415.3047, 'A4': 440.0, 'B4b': 466.1638, 'B4': 493.8833,
                 'C5': 523.2511, 'D5b': 554.3653, 'D5': 587.3295}


        x = np.linspace(0, duration * 2 * np.pi, int(duration * sample_rate))

        wave_data = sine_wave(note='E4')      # E4 for example` 
          

Yep. Now, your thinking about iterating over a python list of notes to make a tune.

## Reverse Sawtooth

A reverse sawtooth can be easily made by adding sinewaves. There is a pattern in the formula :-

![Reverse Sawtooth wave formula](https://d33wubrfki0l68.cloudfront.net/e301cfdb92518377f78d79608b522c39863437ca/93bc1/media/rev_form.jpg)

..... The more waves you add, the more it approaches a proper sawtooth.

Incidentally adjusting the phase of a wave can be done by adding the amount of required phase shift to our sinewave\_data function:-

np.sin( (frequency \* x ) + phase\_radians )

By putting your wave\_data in a function you can make the phase argument optional by setting a default value with the equals sign:- def a\_wave( x, freq, phase=0 ): If no argument is given for phase when the function is called, phase will default to zero.

Make the Sawtooth, then start messing with phase and stuff. No?. Well, it kept my small brain occupied for a while. This is how additive synthesis used to work. Back when i were a lad. Probably. These days they have gui sliders for the variables. You know, most of the stuff on this page is just a result of messing around to see what happens.

            `import numpy as np
        import matplotlib.pyplot as plt
        import sounddevice as sd
        import time
        plt.style.use('dark_background')


        # a skeleton function to stuff full of meaty sawtooth values.
        # play with phase at your leisure, phase arg is optional.

        def a_wave(x, freq, phase=0):
            return np.sin((freq * x) + phase) * 0.2

        # build up ya reverse sawtooth


        def make_wave_2(x):
            wave0 = a_wave(x, 220.0)
            wave1 = a_wave(x, 440.0)/2
            wave2 = a_wave(x, 660.0)/3
            wave3 = a_wave(x, 880.0)/4
            wave4 = a_wave(x, 1100.0)/5
            wave5 = a_wave(x, 1320.0)/6
            wave6 = a_wave(x, 1540.0)/7
            wave7 = a_wave(x, 1760.0)/8

            return wave0 + wave1 + wave2 + wave3 + wave4 + wave5 + wave6 + wave7


        # see what it looks like
        def plotting():
            y = make_wave_2(x)
            y_plot = y[: 500]

            fig, ax = plt.subplots()
            ax.plot(y_plot)

            plt.xlabel('Sample Number')
            plt.title('Reverse Sawtooth')
            plt.show()


        # hear what it sounds like
        def hearing():
            waveform = make_wave_2(x)
            atten_waveform = waveform * 0.8
            sd.play(atten_waveform, sample_rate)
            time.sleep(duration)
            sd.stop()


        sample_rate = 44100
        duration = 3.0
        x = np.linspace(0, duration  * 2 * np.pi, int(duration * sample_rate))
        hearing()
        plotting()` 
          

Make a for loop for this or stick it in a gui.

![Reverse sawtooth plot](https://d33wubrfki0l68.cloudfront.net/b14ec547ee9f2a7c0504c48e6a1ab6ebb24b61d6/ae0c0/media/reverse_sawtooth.jpg)

## Envelope Shaping

Here the amplitude of the wave\_data is being controlled by three arrays, np.linspace( start\_value, stop\_value, step ), and np.ones( shape ). Shape, in this single channel array will be just the number of elements. The start and stop values in the linspace array can be any value between 0 and 1 you like. Here the end values of each array is equal to the start of the next. The sustain\_value variable is used as the stop value of decay array so they line up. These arrays are concatenated together and the resulting array filled with values between 0 and 1 will be multiplied together with the wave\_data. There is no limit to the amount of arrays that can be used:- attack, decay, sustain and release for example.

              `import sounddevice as sd
      import numpy as np
      import time


      sample_rate = 44100
      duration = 4.0
      sustain_value = 0.3   # Release amplitude between 0 and  1

      x = np.linspace(0, duration * 2 * np.pi, int(duration * sample_rate))

      # Dividing x legnth value into three parts:- 1/10, 1/2, 4/10.
      attack_length = len(x) // 10
      decay_length = len(x) // 2
      sustain_legnth = len(x) - (attack_length + decay_length)

      # Setting array size and length.
      attack = np.linspace(0, 1, num=attack_length)
      decay = np.linspace(1, sustain_value, num=decay_length)
      sustain = np.ones(sustain_legnth) * sustain_value
      attack_decay_sustain = np.concatenate((attack, decay, sustain))

      # Creating some waveform
      wave_data = np.sin(220.0 * x)

      # attack_decay_sustain will control the amplitudes of the waveform,
      wave_data = wave_data * attack_decay_sustain

      sd.play(wave_data, sample_rate)
      time.sleep(duration)
      sd.stop()` 
            

![Envelope shape plot](https://d33wubrfki0l68.cloudfront.net/4963b1e3597e7cfbcc596dcfb26adebbceef71f6/97eb0/media/env3.jpg)

## Change Key

There are 12 notes in the western musical scale each seperated by a semi-tone. The difference in frequency between notes is not on a linear scale, but can be calculated by multiplying an intitial frequency by a factor of 2n/12 where n is the number of semitone steps you need. Easy. Lob it into python code.

            `def sine_wave(f):
        y = np.sin(f * x)
        return y

      def change_note(frequency, steps):
        f = frequency * (2**(steps / 12.0))   # frequency multiplied by 2^n/12
        return f

      freq = 220.0                                  # 220.0 = A3.
      wave_data = sine_wave(change_note(freq, 2))   # Returns B3.` 
          

## Add tremolo To A .wav Sample

Read in the audio with **scipy.io.wavfile.read()**. This will return a tuple:- the sample rate (44100 Hz), and a 2d array. The channels can be seperated by slicing. Left channel slice is data\[ :, 0 \], and the right channel slice is data\[ :, 1 \]. Here i have only used one channel for readability but you can easily do both. Prosses the channels individualy ie:- data\_0 = data\[ :, 0 \] , data\_1 = data\[ :, 1 \]. sounddevice will take the 2d array as an argument for play(). Remember to convert the array to int16 before playing otherwise there will be a lot of distortion.

            `import numpy as np
        import scipy.io.wavfile as wf
        import sounddevice as sd
        import time
        import matplotlib.pyplot as plt
        plt.style.use('dark_background')


        sample_rate, data = wf.read("Path_to/file.wav")
        data_0 = data[:, 0]
        print('array length:- {}'.format(len(data)))
        factor = 0.8

        sound = data_0[:190000]
        # Note data type, here it is int16
        print('data type:- {}'.format(data_0.dtype))
        print('array shape:- {}'.format(data.shape))

        duration = len(sound) / sample_rate

        x = np.linspace(0, duration * 2 * np.pi, len(sound))
        tremolo_osc = np.sin(10 * x)
        tremolo_osc = (tremolo_osc * factor / 2 + (1 - factor / 2))

        result = sound * tremolo_osc * 0.5
        # Note data type has changed to float64.
        print('result dtype:- {}'.format(result.dtype))
        # Change it back to int16 with numpy.
        result_int = np.int16(result)

        sd.play(result_int, sample_rate)
        time.sleep(duration)
        sd.stop()

        plot_result = result[: 8000]

        fig, ax = plt.subplots()
        ax.plot(plot_result)
        plt.title('tremolo Applied To .wav File', fontsize=20, y=1.03)
        plt.xlabel('sample Number', fontsize=15)
        plt.ylabel('Amplitude', fontsize=15)
        plt.show()` 
          

![tremolo to music file plot](https://d33wubrfki0l68.cloudfront.net/5b91da4fe5d2a9b8ee0adc55be418f7a0468a3ee/3d0b5/media/trem_to_wav.jpg)

## Crappy Delay

Looks ruff, sounds great. Sounddevice should pick up your default microphone. Make sure you have it enabled because this is gonna do groovy stuff to whatever you scream into your mic!

Numpy arrays are excellent. You can splice sound arrays in wherever you want, then merge (add) them together. Not only that you can flip them backwards with np.flip() and it plays your sound backwards, and, with echo or delay if you stick more than one flip in there, things sound really interesting.

It is possible to record audio data straight into a numpy array with sonnddevice.rec(). data\_0 = sd.rec(int(duration \* sample\_rate), samplerate=sample\_rate, channels=1, dtype='float64')

Here I have created an np.zeros() 2d array with one row for each delay (including 0 for the first undelayed sound). The number of elements in the rows will equal the sound array plus the delay time elements multiplied by the number of repeats. These columns are iterated over, and with indexing, inserting a copy of the sound array, offset more each iteration by the delay time 'blox'. The resulting array's 7 rows are added together to form a single array. The fade is how much the delayed sound array is attenuated on each iteration.

      import numpy as np
      import sounddevice as sd
      import time


      sample_rate = 44100
      duration = 4.0
      repeats = 7          # Play with these numbers.
      delay_time = 0.2
      fade = 2             # Lower = less fade, 1 = no fade.
      volume = 1.5         # Adjust to suit your mic.

      # Record.
      data_0 = sd.rec(int(duration * sample_rate),
                      samplerate=sample_rate, channels=1, dtype='float64')
      time.sleep(duration)
      sd.stop()

      blox = round(delay_time * sample_rate)   # size of delay 0s

      # Reshape From (n, 1) to (n,)
      data_0 = data_0.reshape((len(data_0),)) * volume

      arr = np.zeros((repeats, (len(data_0)) + blox * repeats))
      print(arr.shape)   # See what ya got.

      for i in range(0, repeats):
          arr[i, i * blox:i * blox + len(data_0)] = data_0 * 1 / (fade**i)

      result = np.sum(arr, axis=0)
      # result = np.flip(result)        # Uncomment to play backwards

      sd.play(result, sample_rate)
      time.sleep(len(result) / sample_rate)
      sd.stop()` 
          

You can definatly improve on this one, I bet you got ideas already, GO!

![Sound pasted on zeros](https://d33wubrfki0l68.cloudfront.net/da502dd69611141bc654fcd6716aae7c6bea87c1/f13d6/media/zero_s.jpg)

If you have problems or if you want to use a different sound card, usb audio interface or something, your input/output devices can be viewed with:-

print(sd.query\_devices())

This will print a list of your devices prefixed with an ID number. You can select an alternative device to the defaults by adding the following to your sd.rec() or sd.play() functions

device=\[ input-ID-number, output-ID-number \]

For example sd.rec( int( duration \* sample\_rate ), samplerate=sample\_rate, channels=1, dtype='float64', device=\[4, 4\] )

Here is a link to the excellent sounddevice docs :- [https://python-sounddevice.readthedocs.io/en/0.3.7/index.html](https://python-sounddevice.readthedocs.io/en/0.3.7/index.html)

If like me your microphone is to quiet, on linux :- sudo apt-get install pulseaudio-module-jack. Then adjust the volume with the 'PulseAudio Volume Control' gui that appears in your applications menu. On windows just click on the sound icon then recording devices.