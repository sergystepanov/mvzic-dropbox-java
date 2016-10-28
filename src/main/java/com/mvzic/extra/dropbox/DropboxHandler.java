package com.mvzic.extra.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.mvzic.extra.property.Entry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DropboxHandler {
    private final DbxClientV2 client;

    public DropboxHandler(final String accessToken) {
        // Create Dropbox client
        DbxRequestConfig config = DbxRequestConfig
                .newBuilder("mvzic-dropbox-java.client/0.0.1")
                .withUserLocale("ru-RU")
                .build();
        client = new DbxClientV2(config, accessToken);
    }

    public List<Entry> getFiles(final String path) throws DbxException, IOException {
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
}
