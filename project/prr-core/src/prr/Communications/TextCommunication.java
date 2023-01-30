package prr.Communications;

import prr.Network;
import prr.terminals.Terminal;

public class TextCommunication extends Communication{
    private String _text;

    public TextCommunication(Terminal og, Terminal dest, String text, Network network){
        super(og, dest, text, network);
        _text = text;
    }

    public String getMessage(){
        return _text;
    }

    public String communicationName(){
        return "TEXT";
    }
}
