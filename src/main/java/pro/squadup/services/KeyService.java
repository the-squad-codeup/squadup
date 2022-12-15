package pro.squadup.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeyService {

    @Value("${igdb.client.id}")
    private String IGDB_CLIENT_ID;

    @Value("${igdb.access.token}")
    private String IGDB_ACCESS_TOKEN;

    @Value("${igdb.proxy.key}")
    private String IGDB_PROXY_KEY;

    @Value("${igdb.proxy.url}")
    private String IGDB_PROXY_URL;

    public KeyService() {
    }

    public String getIGDB_CLIENT_ID() {
        return IGDB_CLIENT_ID;
    }

    public void setIGDB_CLIENT_ID(String IGDB_CLIENT_ID) {
        this.IGDB_CLIENT_ID = IGDB_CLIENT_ID;
    }

    public String getIGDB_ACCESS_TOKEN() {
        return IGDB_ACCESS_TOKEN;
    }

    public void setIGDB_ACCESS_TOKEN(String IGDB_ACCESS_TOKEN) {
        this.IGDB_ACCESS_TOKEN = IGDB_ACCESS_TOKEN;
    }

    public String getIGDB_PROXY_KEY() {
        return IGDB_PROXY_KEY;
    }

    public void setIGDB_PROXY_KEY(String IGDB_PROXY_KEY) {
        this.IGDB_PROXY_KEY = IGDB_PROXY_KEY;
    }

    public String getIGDB_PROXY_URL() {
        return IGDB_PROXY_URL;
    }

    public void setIGDB_PROXY_URL(String IGDB_PROXY_URL) {
        this.IGDB_PROXY_URL = IGDB_PROXY_URL;
    }
}
