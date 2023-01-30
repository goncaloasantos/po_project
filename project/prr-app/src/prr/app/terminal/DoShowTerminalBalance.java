package prr.app.terminal;

import javax.swing.Popup;

import prr.Network;
import prr.terminals.Terminal;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show balance.
 */
class DoShowTerminalBalance extends TerminalCommand {

	DoShowTerminalBalance(Network context, Terminal terminal) {
		super(Label.SHOW_BALANCE, context, terminal);
	}

	@Override
	protected final void execute() throws CommandException {
        _display.popup(Message.terminalPaymentsAndDebts(_receiver.getTerminalID(),
					_receiver.getTerminalBalance().getPaid(),
					_receiver.getTerminalBalance().getDebt()));   
	}
}
