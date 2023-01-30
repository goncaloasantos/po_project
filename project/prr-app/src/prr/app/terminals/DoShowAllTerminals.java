package prr.app.terminals;

import java.util.Collections;

import prr.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show all terminals.
 */
class DoShowAllTerminals extends Command<Network> {

	DoShowAllTerminals(Network receiver) {
		super(Label.SHOW_ALL_TERMINALS, receiver);
	}

	@Override
	protected final void execute() throws CommandException {
		_receiver.getAllTerminals()
					.stream()
					.map(t -> t.renderFields())
					.forEach(s -> _display.popup(s));
	}
}
