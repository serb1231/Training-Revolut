package org.example.requests;
import java.util.HashMap;

public class RequestManager {
    private final HashMap<Integer, Client> map_id_account;
    private final int maxNrRequests;

    public RequestManager(int maxReq) {
        map_id_account = new HashMap<>();
        maxNrRequests = maxReq;
    }

    // test the cases: a new client exist in the map
    // a client that exists will have a new request
    // a client that constantly receives requests will not have mroe than n
    public boolean allow(int ClientID) {
        // verify that the client exists. If he doesn't exist, create them
        if (!map_id_account.containsKey(ClientID)) {
            Client client = new Client(ClientID);
            map_id_account.put(ClientID, client);
            return true;
        } else {
            Client client = map_id_account.get(ClientID);
            if (client.NrRequests() <= maxNrRequests) {
                client.addRequest();
                return true;
            }
        }
        return false;
    }
}
