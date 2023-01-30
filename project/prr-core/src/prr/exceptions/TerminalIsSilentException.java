package prr.exceptions;

public class TerminalIsSilentException extends NetworkExceptions{
    private final String _key;


    public TerminalIsSilentException(String key) {
        _key = key;
    }

    public String getKey(){
        return _key;
    }
}
