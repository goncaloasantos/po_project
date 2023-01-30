package prr.exceptions;

public class ClientDoesntExistException extends NetworkExceptions{
    private final String _key;


    public ClientDoesntExistException(String key) {
        _key = key;
    }

    public String getKey(){
        return _key;
    }
}
