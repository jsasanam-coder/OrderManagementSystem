package exercise.ordermanagementsystem.Exception;

public class OrderDeletionException extends RuntimeException{

    public OrderDeletionException(){}
    public OrderDeletionException(String message){
        super(message);
    }
}
