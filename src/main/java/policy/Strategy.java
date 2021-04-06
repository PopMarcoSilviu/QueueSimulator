package policy;

import simulation.Server;
import simulation.Task;

import java.util.List;

public interface Strategy
{
    public void addTask(List<Server> servers, Task t);
}
