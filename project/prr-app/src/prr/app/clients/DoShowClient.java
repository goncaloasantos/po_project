package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.ClientDoesntExistException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show specific client: also show previous notifications.
 */
class DoShowClient extends Command<Network> {

	DoShowClient(Network receiver) {
		super(Label.SHOW_CLIENT, receiver);
		addStringField("id", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
		try{
			_display.popup(_receiver.getClient(stringField("id"))
					.renderClientsWNotifications());
		}
		catch (ClientDoesntExistException e){ 
			throw new UnknownClientKeyException(stringField("id"));
		}
				
	}
}
