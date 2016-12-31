package com.mvzic.extra.audio;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;

public class JaudiotagReader implements AudioMetadataReader {
    @Override
    public AudioTag getTag(final File file) {
        AudioFile f;
        AudioTag audioTag = null;
        try {
            f = AudioFileIO.read(file);
            Tag tag = f.getTag();

            if (tag != null) {
                audioTag = new AudioTag(tag.getFirst(FieldKey.ARTIST), tag.getFirst(FieldKey.ALBUM));
                System.out.println(audioTag);
            }
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException |
                InvalidAudioFrameException e) {
           // e.printStackTrace();
        }

        return audioTag;
    }
}
