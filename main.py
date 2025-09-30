# main.py
# A very simple Kivy app to test the build process.

from kivy.app import App
from kivy.uix.label import Label

class SignalIntelApp(App):
    def build(self):
        # This will display "Signal Intel" in the center of the screen.
        return Label(text='Signal Intel')

if __name__ == '__main__':
    SignalIntelApp().run()
