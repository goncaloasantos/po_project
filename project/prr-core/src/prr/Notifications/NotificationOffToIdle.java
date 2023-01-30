package prr.Notifications;

import prr.terminals.Terminal;

public class NotificationOffToIdle extends Notifications{
    
    public NotificationOffToIdle(Terminal t){
        super(t);
    }
    
    @Override
    public String notificationType(){
        return "O2I";
    };
}
