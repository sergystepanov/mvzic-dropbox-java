package com.mvzic.extra.property;

import com.mvzic.extra.file.Path;

public class DotDotDotEntry extends Entry {
    public DotDotDotEntry() {
        super(Path.PARENT, Path.PARENT, true);
        setSize(-1);
    }
}
