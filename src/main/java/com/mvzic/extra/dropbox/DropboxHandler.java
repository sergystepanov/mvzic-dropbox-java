package com.mvzic.extra.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

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

    public List<String> getFiles(final String path) throws DbxException, IOException {
        // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = client.files().listFolder(path);
        List<String> entries = new ArrayList<>();
        while (true) {
            entries.addAll(result.getEntries().stream().map(Metadata::getPathLower).collect(Collectors.toList()));

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());
        }

        return entries;
    }
}
