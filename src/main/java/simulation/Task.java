package simulation;

public class Task
{
    private int arrivalTime;
    private int processingTime;
    private int id;

    public Task(int arrivalTime, int processingTime, int id)
    {
        this.arrivalTime = arrivalTime;
        this.processingTime = processingTime;
        this.id = id;
    }

    public int getArrivalTime()
    {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime)
    {
        this.arrivalTime = arrivalTime;
    }

    public int getProcessingTime()
    {
        return processingTime;
    }

    public void setProcessingTime(int processingTime)
    {
        this.processingTime = processingTime;
    }

    @Override
    public String toString()
    {
        return "("+id+","+arrivalTime+","+processingTime  + ")";
    }
}
