package com.mvzic.extra.dropbox;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FolderMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.sharing.ListSharedLinksResult;
import com.dropbox.core.v2.sharing.SharedLinkMetadata;
import com.mvzic.extra.file.Path;
import com.mvzic.extra.property.Entry;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Dropbox API V2 handler.
 *
 * @since 1.0.0
 */
@Slf4j
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

    private boolean isAudio(final Entry entry) {
        return false;//!entry.folderProperty().get() && (entry.getName().endsWith(".mp3") || entry.getName().endsWith(".flac"));
    }

    public List<Entry> getFiles(final String path) throws DbxException {
        final String dir = buildPath(path);
        setPath(dir);

        // Get files and folder metadata from Dropbox root directory
        ListFolderResult result = client.files().listFolderBuilder(dir)
                .withIncludeMediaInfo(true)
                .withIncludeHasExplicitSharedMembers(true)
                .start();

        List<Entry> entries = new ArrayList<>();
        while (true) {
            for (Metadata metadata : result.getEntries()) {
                final Entry entry = new Entry(metadata.getName(), metadata.getPathLower(), metadata instanceof FolderMetadata);
                entries.add(entry);

                if (isAudio(entry)) {
                    final long start = System.nanoTime();

                    log.info("[dropbox] getting share info for {}", entry.getName());
                    ListSharedLinksResult sharedResults =
                            client.sharing()
                                    .listSharedLinksBuilder()
                                    .withDirectOnly(true)
                                    .withPath(entry.getPath())
                                    .start();
                    for (SharedLinkMetadata meta : sharedResults.getLinks()) {
                        log.info(meta.toString());
                    }

                    log.info("[dropbox] [{}] got info for {}", (System.nanoTime() - start) / 1000000, entry.getName());
                }
            }

            if (!result.getHasMore()) {
                break;
            }

            result = client.files().listFolderContinue(result.getCursor());


        }
//
//        if (!dir.equals(Path.ROOT)) {
//        ListSharedLinksResult sharedResults = client.sharing().listSharedLinksBuilder().withCursor()
//                .withPath(dir)//"/music/cool/mvzic/2013/2013 j/17. tommy heavenly6 - pray (tv version).mp3")
//                //.withDirectOnly(true)
//                .start();
//
//        while (true) {
//            for (SharedLinkMetadata meta : sharedResults.getLinks()) {
//                LOGGER.info(meta.toString());
//            }
//
//            if (!sharedResults.getHasMore()) {
//                break;
//            }
//
//            sharedResults = client.sharing().listSharedLinksBuilder().withCursor(sharedResults.getCursor()).start();
//        }
//    }

        return entries;
    }

    /**
     * Converts magic paths into absolute ones.
     *
     * @param path The path to convert.
     * @return An absolute path.
     * @since 1.0.0
     */
    private String buildPath(final String path) {
        return path.equals(Path.PARENT) ? getPath().substring(0, getPath().lastIndexOf('/')) : path;
    }

    public String getPath() {
        return path;
    }

    private void setPath(String path) {
        this.path = path;
    }
}
