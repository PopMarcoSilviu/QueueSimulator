package policy;

import simulation.Server;
import simulation.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy
{
    @Override
    public void addTask(List<Server> servers, Task t)
    {
        Server best = null;

        for (Server server : servers)
        {
            if (best == null || server.getWaitingPeriod() < best.getWaitingPeriod())
            {
                best = server;
            }
        }

        if (best != null)
        {
            best.addTask(t);
        }
    }
}
