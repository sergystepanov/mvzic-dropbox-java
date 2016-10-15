package com.mvzic.extra.property;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DropboxProperties {
    private StringProperty accessToken = new SimpleStringProperty();

    public final String getAccessToken() {
        return accessToken.get();
    }

    public StringProperty accessTokenProperty() {
        return accessToken;
    }

    public final void setAccessToken(final String accessToken) {
        this.accessToken.set(accessToken);
    }
}
