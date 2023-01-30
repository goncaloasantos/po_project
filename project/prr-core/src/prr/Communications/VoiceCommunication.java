package prr.Communications;

import prr.Network;
import prr.terminals.Terminal;

public class VoiceCommunication extends Communication{
    public VoiceCommunication(Terminal og, Terminal dest, int duration, Network network){
        super(og, dest, duration, network, false);
    }

    @Override
    public String communicationName(){
        return "VOICE";
    }
}
