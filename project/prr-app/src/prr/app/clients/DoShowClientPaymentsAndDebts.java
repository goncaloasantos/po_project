package prr.app.clients;

import prr.Network;
import prr.app.exceptions.UnknownClientKeyException;
import prr.exceptions.ClientDoesntExistException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show the payments and debts of a client.
 */
class DoShowClientPaymentsAndDebts extends Command<Network> {

	DoShowClientPaymentsAndDebts(Network receiver) {
		super(Label.SHOW_CLIENT_BALANCE, receiver);
		addStringField("id", Prompt.key());
	}

	@Override
	protected final void execute() throws CommandException {
        try {
			_display.popup(Message.
				clientPaymentsAndDebts(stringField("id"),
				_receiver.getClient(stringField("id")).getPayment(),
				_receiver.getClient(stringField("id")).getDebt()));
		} catch (ClientDoesntExistException e) {
			throw new UnknownClientKeyException(stringField("id"));
		}
	}
}
