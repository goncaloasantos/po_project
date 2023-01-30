package prr.exceptions;

public class ClientExistsException extends NetworkExceptions{
    
    private final String _key;


    public ClientExistsException(String key) {
        _key = key;
    }

    public String getKey(){
        return _key;
    }
}
