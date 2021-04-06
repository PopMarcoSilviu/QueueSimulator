package policy;

import simulation.Server;
import simulation.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy
{

    @Override
    public void addTask(List<Server> servers, Task t)
    {
        Server best = null;

        for (Server server : servers)
        {
            if (best == null || server.getNbOfWaitingTasks() < best.getNbOfWaitingTasks())
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
