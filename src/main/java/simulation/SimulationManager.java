package simulation;

import policy.SelectionPolicy;
import utils.GenerateRandomNumberInRange;
import utils.Parameters;
import utils.Status;
import utils.WriteToFile;
import view.ParametersSelectionWindow;
import view.SimulationWindow;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class SimulationManager implements Runnable
{
    private static SimulationWindow simulationWindow;
    private static Thread t;
    public static int timeLimit = 100;
    public static int maxProcessingTime = 5;
    public static int minProcessingTime = 1;
    public static int maxArrivalTime = 10;
    public static int minArrivalTime = 0;
    public static int numberOfServers = 3;
    public static int numberOfClients = 10;
    public static int maxNbOfClientsPerQueue = 20;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;
    private Scheduler scheduler;
    private List<Task> generatedTasks;
    private static ParametersSelectionWindow parametersSelectionWindow;

    public SimulationManager()
    {
        generatedTasks = new ArrayList<>();
        scheduler = new Scheduler();
        parametersSelectionWindow = new ParametersSelectionWindow();
        parametersSelectionWindow.addActionListener(e -> start(t));
    }

    private static void start(Thread t)
    {
        simulationWindow = new SimulationWindow(parametersSelectionWindow.getValue(Parameters.numberOfClients),
                parametersSelectionWindow.getValue(Parameters.numberOfServers));
        t.start();
    }

    public static void main(String[] args)
    {
        SimulationManager gen = new SimulationManager();
        t = new Thread(gen);

    }

    private void generateNRandomTasks()
    {
        for (int i = 1; i <= numberOfClients; i++)
        {
            int arrivalTime = GenerateRandomNumberInRange.getRandomNumber(minArrivalTime, maxArrivalTime);
            int processingTime = GenerateRandomNumberInRange.getRandomNumber(minProcessingTime, maxProcessingTime);
            Task newTask = new Task(arrivalTime, processingTime, i);
            generatedTasks.add(newTask);
        }

        generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
    }

    @Override
    public void run()
    {
        timeLimit = parametersSelectionWindow.getValue(Parameters.timeLimit);
        maxArrivalTime = parametersSelectionWindow.getValue(Parameters.maxArrivalTime);
        minArrivalTime = parametersSelectionWindow.getValue(Parameters.minArrivalTime);
        maxNbOfClientsPerQueue = parametersSelectionWindow.getValue(Parameters.maxNbOfClientsPerQueue);
        maxProcessingTime = parametersSelectionWindow.getValue(Parameters.maxProcessingTime);
        minProcessingTime = parametersSelectionWindow.getValue(Parameters.minProcessingTime);
        numberOfClients = parametersSelectionWindow.getValue(Parameters.numberOfClients);
        numberOfServers = parametersSelectionWindow.getValue(Parameters.numberOfServers);
        selectionPolicy = parametersSelectionWindow.getSelectionPolicy();

        generateNRandomTasks();

        simulationWindow.setStatus(Status.ON);

        scheduler.initializeServers(numberOfServers, maxNbOfClientsPerQueue, simulationWindow);
        scheduler.changeStrategy(selectionPolicy);


        int currentTime = 0;
        int peakTime = 0;
        int peakValue = 0;

        while (currentTime <= timeLimit)
        {
            int clientsLeft = 0;

            for (int i = 0; i < generatedTasks.size(); i++)
            {
                if (generatedTasks.get(i).getArrivalTime() == currentTime)
                {
                    scheduler.dispatchTask(generatedTasks.get(i));
                    generatedTasks.remove(i);
                    i--;
                }
            }


            ValuesINeed valuesINeed = new ValuesINeed();
            int sum = 0;


            for (Server server : scheduler.getServers())
            {
                sum += server.getNbOfWaitingTasks();
                valuesINeed.totalServiceTime.addAndGet(server.getValuesINeed().totalServiceTime.get());
                valuesINeed.totalWaitingTime.addAndGet(server.getValuesINeed().totalWaitingTime.get());
                valuesINeed.nbOfProcessedClients.addAndGet(server.getValuesINeed().nbOfProcessedClients.get());
            }

            double averageWaitingTime = (double) valuesINeed.totalWaitingTime.get() /
                    (double) (numberOfClients - generatedTasks.size() - valuesINeed.nbOfProcessedClients.get());
            double averageServiceTime = (double) valuesINeed.totalServiceTime.get() /
                    (double) valuesINeed.nbOfProcessedClients.get();

            simulationWindow.setAverageWaitingTime(averageWaitingTime);
            simulationWindow.setAverageServiceTime(averageServiceTime);

            if (sum > peakValue)
            {
                peakValue = sum;
                peakTime = currentTime;
            }

            simulationWindow.setPeakTime(peakTime);
            simulationWindow.updateQueues(scheduler.getServers());

            try
            {
                writeLog(currentTime);
                Thread.sleep(1000);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            clientsLeft += generatedTasks.size();

            for (Server server : scheduler.getServers())
            {
                clientsLeft += server.getNbOfWaitingTasks();
            }

            if (clientsLeft == 0)
            {
                break;
            }
            currentTime++;
        }

        if(currentTime!=timeLimit)
        {
            writeLog(currentTime +1);
        }

        simulationWindow.updateQueues(scheduler.getServers());
        simulationWindow.setStatus(Status.OFF);
        scheduler.closeThreads();
    }

    private void writeLog(int currentTime)
    {

        WriteToFile.write("Time: " + currentTime + "\n");
        WriteToFile.write("Waiting List: ");

        for (Task task : generatedTasks)
        {
            WriteToFile.write(task.toString() + " ");
        }

        WriteToFile.write("\n");

        int i = 0;

        for (Server server : scheduler.getServers())
        {
            WriteToFile.write("Queue " + ++i + ": ");

            for (Task task : server.getTasks())
            {
                WriteToFile.write(task.toString() + " ");
            }
            WriteToFile.write("\n");
        }
        WriteToFile.write("\n");
    }
}
