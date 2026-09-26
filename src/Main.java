import java.util.Collection;
import java.util.Scanner;
public class Main{
    public static void main(String args[]){
        TaskManager manager=new TaskManager();
        Scanner scanner=new Scanner(System.in);
        boolean running=true;

        while(running){
            printMenu();
            String choice=scanner.nextLine();
            switch(choice){
                case "1":
                    createTaskFlow(manager,scanner);
                    break;
                    case "2":
                        listTasksFlow(manager);
                        break;
                        case "3":
                            getTaskFlow(manager,scanner);
                            break;
                            case "4":
                                updateTaskFlow(manager,scanner);
                                break;
                                case "5":
                                    deleteTaskFlow(manager,scanner);
                                    break;
                                    case "6":
                                        completeTaskFlow(manager,scanner);
                                        break;
                    case "8":
                        running=false;
                        System.out.println("GoodBye!");
                        break;
                         default:
                    System.out.println("Not implemented yet.");
            }
        }

        scanner.close();
    }
            
    
    private static void printMenu(){
        System.out.println("\n--- Task Manager ---");
        System.out.println("1. Create Task");
        System.out.println("2. List Tasks");
        System.out.println("3. Get Task");
        System.out.println("4. Update Task");
        System.out.println("5. Delete Task");
        System.out.println("6. Complete Task");
        System.out.println("7.Search Tasks");
        System.out.print("Choose an option:");
    
   

        
    }
    private static void createTaskFlow(TaskManager manager, Scanner scanner) {
        System.out.print("Enter title: ");
        String title = scanner.nextLine();
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        try {
            Task task = manager.createTask(title, description);
            System.out.println("Created task #" + task.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void listTasksFlow(TaskManager manager){
        Collection<Task> tasks=manager.listTasks();
        if(tasks.isEmpty()){
            System.out.println("No tasks yet");
            return;
        }
        for(Task task:tasks){
            System.out.println("#"+task.getId()+"["+task.geStatus()+"]"+task.getTitle());
        }
    }
    private static void getTaskFlow(TaskManager manager,Scanner scanner){
        System.out.print("Enter task ID:");
        String input=scanner.nextLine();

        try {
            int id=Integer.parseInt(input);
            Task task=manager.getTask(id);
           System.out.println("#"+task.getId()+"["+task.geStatus()+"]"+task.getTitle());
            System.out.println("Description:"+task.getDescription());
            System.out.println("Created:"+task.getCreatedAt());
        } catch (NumberFormatException e) {
             System.out.println("Error: '" + input + "' is not a valid number.");
        }
        catch(IllegalArgumentException e){
            System.out.println("Error:"+e.getMessage());
        }
    }
    private static void updateTaskFlow(TaskManager manager,Scanner scanner){
        System.out.print("Enter task ID:");
        String input=scanner.nextLine();

        try {
            int id=Integer.parseInt(input);
            System.out.print("Enter new title:");
            String title=scanner.nextLine();
            System.out.print("Enter new description (leave blank to keep current):");
            String description=scanner.nextLine();

            if(description.isEmpty()){
                description=null;
            }
            Task task=manager.updateTask(id,title,description);
            System.out.println("Updated task #"+task.getId());
        } catch (NumberFormatException e) {
            System.out.println("Error: '" + input + "' is not a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void deleteTaskFlow(TaskManager manager,Scanner scanner){
        System.out.print("Enter task ID:");
        String input=scanner.nextLine();

        try{
            int id=Integer.parseInt(input);
            manager.deleteTask(id);
            System.out.println("Deleted task#"+id);
        }
        catch (NumberFormatException e) {
            System.out.println("Error: '" + input + "' is not a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private static void completeTaskFlow(TaskManager manager,Scanner scanner){
        System.out.println("Enter task ID:");
        String input=scanner.nextLine();

        try {
            int id=Integer.parseInt(input);
            Task task=manager.completeTask(id);
          System.out.println("Completed task #" + task.getId());
        } catch (NumberFormatException e) {
            System.out.println("Error: '" + input + "' is not a valid number.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
        }
    
