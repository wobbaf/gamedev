package org.example;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class Util implements LineListener{
    boolean isPlaybackRunning = false;

    private Clip audioClip;
    public void playSound(String path){
        try {
            if(audioClip != null){
                audioClip.stop();
            }
            InputStream is = getClass().getClassLoader().getResourceAsStream(path);
            AudioInputStream ais = AudioSystem.getAudioInputStream(is);
            AudioFormat audioFormat = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, audioFormat);
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.addLineListener(this);
            audioClip.open(ais);
            audioClip.setMicrosecondPosition(1000);
            audioClip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(LineEvent event) {
        if (LineEvent.Type.START == event.getType()) {
//            if(isPlaybackRunning){
//                audioClip.stop();
//            }
            isPlaybackRunning = true;
        } else if (LineEvent.Type.STOP == event.getType()) {
            isPlaybackRunning = false;
        }
    }
}
