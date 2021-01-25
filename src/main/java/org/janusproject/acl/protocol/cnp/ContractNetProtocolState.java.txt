/* 
 * $Id$
 * 
 * Janus platform is an open-source multiagent platform.
 * More details on <http://www.janus-project.org>
 * Copyright (C) 2012 Janus Core Developers
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.janusproject.acl.protocol.cnp;

import org.janusproject.acl.protocol.ProtocolState;

/**
 * This enumeration describes all available states of the Contract Net Protocol (CNP).
 * 
 * @see FipaContractNetProtocol
 * @see QueryProtocolState
 * 
 * @author $Author: madeline$
 * @author $Author: kleroy$
 * @author $Author: ptalagrand$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */
public enum ContractNetProtocolState implements QueryProtocolState {
	/**
	 */
	NOT_STARTED,
	/**
	 */
	SENDING_CALL_FOR_PROPOSAL,
	/**
	 */
	WAITING_CALL_FOR_PROPOSAL,
	/**
	 */
	SENDING_PROPOSAL,
	/**
	 */
	WAITING_ALL_PROPOSALS,
	/**
	 */
	SENDING_ALL_PROPOSAL_ANSWERS,
	/**
	 */
	WAITING_PROPOSAL_ANSWER,
	/**
	 */
	SENDING_RESULT,
	/**
	 */
	WAITING_ALL_RESULTS,
	/**
	 */
	CANCELING,
	/**
	 */
	CANCELED,
	/**
	 */
	DONE;
}
