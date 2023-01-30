package prr.Notifications;

import prr.terminals.Terminal;

public class NotificationSilentToIdle extends Notifications{
    
    public NotificationSilentToIdle(Terminal t){
        super(t);
    }
    
    @Override
    public String notificationType(){
        return "S2I";
    };
}
