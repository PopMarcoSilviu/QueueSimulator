package simulation;

import java.util.concurrent.atomic.AtomicInteger;

public class ValuesINeed
{
    public AtomicInteger totalWaitingTime = new AtomicInteger(0);
    public AtomicInteger nbOfProcessedClients = new AtomicInteger(0);
    public AtomicInteger totalServiceTime = new AtomicInteger(0);
}
