package com.mvzic.extra.audio;

import java.io.File;

public interface AudioMetadataReader {
    AudioTag getTag(final File file);
}
