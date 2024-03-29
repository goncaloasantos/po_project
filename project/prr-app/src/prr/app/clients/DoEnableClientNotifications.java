package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.ClientDoesntExistException;
import prr.exceptions.NotificationsAlreadyEnabled;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;


/**
 * Enable client notifications.
 */
class DoEnableClientNotifications extends Command<Network> {

	DoEnableClientNotifications(Network receiver) {
		super(Label.ENABLE_CLIENT_NOTIFICATIONS, receiver);
		addStringField("id", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException{
            try{
				_receiver.getClient(stringField("id"))
					 .setNotifStateTrue();
			}
			catch(ClientDoesntExistException e){
				throw new UnknownClientKeyException(stringField("id"));
			}
			catch(NotificationsAlreadyEnabled e){
				_display.popup(Message.clientNotificationsAlreadyEnabled());
			}
	}
}
