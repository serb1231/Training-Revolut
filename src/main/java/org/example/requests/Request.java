package org.example.requests;

import java.time.Clock;

public class Request {
    public int requestID;
    public Clock clock;
    Request(int requestID) {
        this.requestID = requestID;
        clock = Clock.systemDefaultZone();
    }
}
