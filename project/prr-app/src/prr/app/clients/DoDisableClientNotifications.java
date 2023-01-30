package prr.app.clients;

import javax.management.Notification;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.ClientDoesntExistException;
import prr.exceptions.NotificationsAlreadyDisabled;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;


/**
 * Disable client notifications.
 */
class DoDisableClientNotifications extends Command<Network> {

	DoDisableClientNotifications(Network receiver) {
		super(Label.DISABLE_CLIENT_NOTIFICATIONS, receiver);
		addStringField("id", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			_receiver.getClient(stringField("id"))
				 .setNotifStateFalse();
		}
		catch(ClientDoesntExistException e){
			throw new UnknownClientKeyException(stringField("id"));
		}
		catch(NotificationsAlreadyDisabled e){
			_display.popup(Message.clientNotificationsAlreadyDisabled());
		}
	}
}
