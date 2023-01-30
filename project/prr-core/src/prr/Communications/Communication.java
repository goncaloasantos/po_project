package prr.Communications;

import prr.Network;
import prr.terminals.Terminal;

public abstract class  Communication{
    private int _communicationID;
    private Terminal _sender, _reciver;
    private boolean _communicationON = true;
    private String _text;
    private double _price;
    private int _units;
    private boolean _wasPaid = false;
    private boolean _isVideo = false;

    public Communication(Terminal og, Terminal dest, int duration, Network network, boolean isVideo){
        _communicationID = network.getGommunicationID();
        _sender = og;
        _reciver = dest;
        _units = duration;
        _isVideo = isVideo;
    }
    public Communication(Terminal og, Terminal dest, String text, Network network){
        _communicationID = network.getGommunicationID();
        _sender = og;
        _reciver = dest;
        _text = text;
        _communicationON = false;
        _units = text.length();
    }

    public boolean isVideo(){return _isVideo;}

    public String getIDSender(){return _sender.getTerminalID();}

    public String getIDReciver(){return _reciver.getTerminalID();}

    public Terminal getSenderTerminal(){return _sender;}

    public Terminal getReciverTerminal(){return _reciver;}

    public String getText(){return _text;}

    public int getcomID(){return _communicationID;}

    public int getUnits(){return _units;}

    public double getPrice(){return _price;}

    public void pay(){_wasPaid = true;}

    public boolean hasBeenPayed(){return _wasPaid;}

    public boolean isOn(){return _communicationON;}

    public String getStatus(){
        if(_communicationON){
            return "ONGOING";
        }
        return "FINISHED";
    }

    public void setPrice(double price){
        _price = price;
    }

    public void setUnits(int units){
        _units = units;
    }

    public void endCommunication(){
        _communicationON = false;
    }

    public String renderCommunications(){
        return communicationName() + "|" + getcomID() + "|" + getIDSender() + "|" + 
        getIDReciver() + "|" + getUnits() + "|" + Math.round(getPrice()) + "|" + getStatus(); 
    }

    public abstract String communicationName();

    public boolean isCommunicationActive(){
        return _communicationON;
    }

}
