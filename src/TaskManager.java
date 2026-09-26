import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskManager{
    private final Map<Integer,Task>tasks;
    private int nextId;

    public TaskManager(){
        this.tasks=new HashMap<>();
        this.nextId=1;
    }
    public Task createTask(String title,String description){
        Task task=new Task(nextId,title,description);
        tasks.put(nextId,task);
        nextId++;
        return task;
    }
    private void validateTitle(String title){
        if(title==null||title.trim().isEmpty()){
            throw new IllegalArgumentException("Title cannot be emoty");
        }
        
    }
    public Task getTask(int id){
        Task task=tasks.get(id);
        if(task==null){
            throw new IllegalArgumentException("Task not found with id:"+id);
        }
        return task;
    }
    public Task updateTask(int id,String title,String description){
        Task task=getTask(id);
        validateTitle(title);
        task.setTitle(title);
        if(description!=null){
            task.setDescription(description);
        }
        return task;
    }
    public void deleteTask(int id){
        getTask(id);
        tasks.remove(id);
    }
    public Task completeTask(int id){
        Task task=getTask(id);

        task.setStatus(Status.COMPLETED);

        return task;
    }
    public Collection<Task> listTasks(){
        return tasks.values();
    }
    public List<Task> searchTasks(String keyword){
        List<Task>results=new ArrayList<>();
        String lower=keyword.toLowerCase();
        for(Task task:tasks.values()){
            if(task.getTitle().toLowerCase().contains(lower)){
                results.add(task);
            }
        }
        return results;
    }
}