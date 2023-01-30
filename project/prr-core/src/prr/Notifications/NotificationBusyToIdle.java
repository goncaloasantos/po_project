package prr.Notifications;

import prr.terminals.Terminal;

public class NotificationBusyToIdle extends Notifications{
    
    public NotificationBusyToIdle(Terminal t){
        super(t);
    }
   
    @Override
    public String notificationType(){
        return "B2I";
    };    
}
