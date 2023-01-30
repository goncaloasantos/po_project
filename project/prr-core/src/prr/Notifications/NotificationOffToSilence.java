package prr.Notifications;

import prr.terminals.Terminal;

public class NotificationOffToSilence extends Notifications{
    
    public NotificationOffToSilence(Terminal t){
        super(t);
    }
    
    @Override
    public String notificationType(){
        return "O2S";
    };
}

