package com.mvzic.extra.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.mvzic.extra.file.Path;
import com.mvzic.extra.property.Entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Dropbox API V2 handler.
 *
 * @since 1.0.0
 */
public final class DropboxHandler {
    private final DbxClientV2 client;
    private String path;

    public DropboxHandler(final String accessToken) {
        this.path = Path.ROOT;

        // Create Dropbox client
        DbxRequestConfig config = DbxRequestConfig
                .newBuilder("mvzic-dropbox-java.client/1.0.0")
                .withUserLocale("ru-RU")
                .build();
        client = new DbxClientV2(config, accessToken);
    }

    public List<Entry> getFiles(final String path) throws DbxException {
        // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = client.files().listFolderBuilder(path)
                .withIncludeMediaInfo(true)
                .start();

        List<Entry> entries = new ArrayList<>();
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                entries.add(new Entry(metadata.getName(), metadata.getPathLower(), metadata instanceof FolderMetadata));
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }

        return entries;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
