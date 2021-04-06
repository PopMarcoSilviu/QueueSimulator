package simulation;

import view.SimulationWindow;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable
{
    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;
    private SimulationWindow simulationWindow;
    private ValuesINeed valuesINeed;

    public Server(SimulationWindow simulationWindow)
    {
        waitingPeriod = new AtomicInteger(0);
        tasks = new LinkedBlockingQueue<>();
        this.simulationWindow = simulationWindow;
        valuesINeed = new ValuesINeed();
    }

    public void addTask(Task task)
    {
        try
        {
            tasks.put(task);
            waitingPeriod.getAndAdd(task.getProcessingTime());
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public int getWaitingPeriod()
    {
        return waitingPeriod.get();
    }

    public int getNbOfWaitingTasks()
    {
        return tasks.size();
    }

    public BlockingQueue<Task> getTasks()
    {
        return tasks;
    }

    public ValuesINeed getValuesINeed()
    {
        return valuesINeed;
    }

    @Override
    public void run()
    {
        while (!Thread.interrupted())
        {
            Task currentTask;

            if (tasks.peek() != null)
            {
                currentTask = tasks.peek();
                int processingTime = currentTask.getProcessingTime();

                while (currentTask.getProcessingTime() > 0)
                {
                    try
                    {
                        Thread.sleep(1000);

                    } catch (InterruptedException e)
                    {
                    }
                    currentTask.setProcessingTime(currentTask.getProcessingTime() - 1);
                }
                valuesINeed.nbOfProcessedClients.incrementAndGet();
                valuesINeed.totalWaitingTime.getAndAdd(tasks.size() - 1);
                valuesINeed.totalServiceTime.getAndAdd(processingTime);
                tasks.remove();
            }
        }
    }
}
