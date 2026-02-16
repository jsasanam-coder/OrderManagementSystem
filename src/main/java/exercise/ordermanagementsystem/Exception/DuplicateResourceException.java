package exercise.ordermanagementsystem.Exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(){
    }
    public DuplicateResourceException(String message){
        super(message);
    }
}
