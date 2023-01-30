package prr.exceptions;

public class CommunicationNotSupportedAtDestinationException extends NetworkExceptions{
    
    private final String _key;
    
    private final String _type;
   
    public CommunicationNotSupportedAtDestinationException(String key, String type){
        _key = key;
        _type = type;
    }

    public String getKey(){return _key;}
    
    public String getType(){return _type;}
}
