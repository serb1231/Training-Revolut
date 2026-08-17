package org.example.requests;

import java.time.Clock;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Client {
    private final ConcurrentLinkedDeque<Request> requests;
    private final int id;
    private int requestID;
    private int requestCounter;
    Clock clock;

    public Client(int id) {
        this.id = id;
        requests = new ConcurrentLinkedDeque<>();
        clock = Clock.systemDefaultZone();
        requestID = 1;
        requestCounter = 0;
//      a client cannot exist without also having a request
        addRequest();

    }

    public int NrRequests(){
        return requestCounter;
    }

//  tests: verify that a new inserted request will be located at the end
    public void addRequest() {
        Request request = new Request(requestID);
        requestID += 1;
        requestCounter++;
        requests.addLast(request);
        // modify the clock of the client as it has a new request rn
        clock = Clock.systemDefaultZone();
    }

//    tests: none as pooFirst() cannot throw exception even if deque empty
//    should I make a test to verify that after this operation, we peek at the second element?
//    in case somebody later modifies the eliminate request function??
    public void eliminateRequest() {
        requests.pollFirst();
    }
//  tests: none as peekFirst() cannot throw exception even if empty
    public Request peekFirst() {
        return requests.peekFirst();
    }
}
