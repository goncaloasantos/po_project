package prr.exceptions;

public class TerminalDoesntExistException extends NetworkExceptions{
    
    private final String _key;

    public TerminalDoesntExistException(String key) {
        _key = key;
    }

    public String getKey(){
        return _key;
    }
    
}
