package prr.exceptions;

public class TerminalIsOffException extends NetworkExceptions{
    private final String _key;


    public TerminalIsOffException(String key) {
        _key = key;
    }

    public String getKey(){
        return _key;
    }
}
