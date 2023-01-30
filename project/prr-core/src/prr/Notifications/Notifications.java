package prr.Notifications;

import prr.terminals.Terminal;

public abstract class Notifications{
    private Terminal _terminal;

    public Notifications(Terminal terminal){
        _terminal = terminal;
    }

    public Terminal getTerminal(){return _terminal;}

    public abstract String notificationType();

    public String toString(){
        return notificationType() + "|" + _terminal.getTerminalID();
    }
}
