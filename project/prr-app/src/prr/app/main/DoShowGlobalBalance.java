package prr.app.main;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.util.ArrayList;
import java.util.List;

//FIXME add more imports if needed

/**
 * Show global balance.
 */
class DoShowGlobalBalance extends Command<Network> {

	DoShowGlobalBalance(Network receiver) {
		super(Label.SHOW_GLOBAL_BALANCE, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		List<Long> lista = _receiver.getAllPaymentsAndDebst();
		_display.popup(Message.globalPaymentsAndDebts(lista.get(1), lista.get(0)));
	}
}
