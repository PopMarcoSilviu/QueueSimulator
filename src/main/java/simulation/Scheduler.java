package simulation;

import policy.ConcreteStrategyQueue;
import policy.ConcreteStrategyTime;
import policy.SelectionPolicy;
import policy.Strategy;
import view.SimulationWindow;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Scheduler
{
    private List<Server> servers;
    private List<Thread> threads;
    private int maxNoOfServers;
    private int maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(int maxNoOfServers, int maxTasksPerServer, SimulationWindow simulationWindow)
    {
        initializeServers(maxNoOfServers, maxTasksPerServer, simulationWindow);
    }

    public Scheduler()
    {

    }

    public void initializeServers(int maxNoOfServers, int maxTasksPerServer, SimulationWindow simulationWindow)
    {
        servers = new CopyOnWriteArrayList<>();
        threads = new CopyOnWriteArrayList<>();

        for (int i = 0; i < maxNoOfServers; i++)
        {
            Server server = new Server(simulationWindow);
            Thread thread = new Thread(server);
            servers.add(server);
            threads.add(thread);
            thread.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy)
    {
        switch (policy)
        {
            case SHORTEST_QUEUE:
                strategy = new ConcreteStrategyQueue();
                break;
            case SHORTEST_TIME:
                strategy = new ConcreteStrategyTime();
                break;
            default:
                break;
        }
    }

    public void dispatchTask(Task t)
    {
        strategy.addTask(servers, t);
    }

    public void closeThreads()
    {
        for (Thread thread : threads)
        {
            thread.interrupt();
        }
    }

    public List<Server> getServers()
    {
        return servers;
    }
}
