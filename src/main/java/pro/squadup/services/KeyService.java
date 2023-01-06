package pro.squadup.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeyService {

    @Value("${igdb.client.id}")
    private String IGDB_CLIENT_ID;

    @Value("${igdb.access.token}")
    private String IGDB_ACCESS_TOKEN;

    @Value("${filestack.key}")
    private String FILESTACK_KEY;



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

    public String getFILESTACK_KEY() {
        return FILESTACK_KEY;
    }

    public void setFILESTACK_KEY(String FILESTACK_KEY) {
        this.FILESTACK_KEY = FILESTACK_KEY;
    }
}
