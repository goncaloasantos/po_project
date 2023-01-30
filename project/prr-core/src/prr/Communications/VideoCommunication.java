package prr.Communications;

import prr.Network;
import prr.terminals.Terminal;

public class VideoCommunication extends Communication{
    public VideoCommunication(Terminal og, Terminal dest, int duration, Network network){
        super(og, dest, duration, network, true);
    }

    @Override
    public String communicationName(){
        return "VIDEO";
    }
}
