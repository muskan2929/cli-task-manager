import java.time.LocalDateTime;

public class Task {
    private final int id;
    private String title;
    private String description;
    private Status status;
    private final LocalDateTime createdAt;

    public Task(int id,String title,String description){
        this.id=id;
        this.title=title;
        this.description=description;
        this.status=Status.PENDING;
        this.createdAt=LocalDateTime.now();
    }

public int getId(){
    return id;
}
public String getTitle(){
    return title;
}
public String getDescription(){
    return description;
}
public Status geStatus(){
    return status;
}
public LocalDateTime getCreatedAt(){
    return createdAt;
}
   public void setTitle(String title){
    this.title=title;
   } 
   public void setDescription(String description){
    this.description=description;
   }
   public void setStatus(Status status){
    this.status=status;
   }

}