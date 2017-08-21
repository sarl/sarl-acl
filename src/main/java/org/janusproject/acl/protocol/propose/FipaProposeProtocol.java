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
package org.janusproject.acl.protocol.propose;

import java.util.logging.Level;

import org.arakhne.afc.vmutil.locale.Locale;
import org.janusproject.acl.ACLAgent;
import org.janusproject.acl.ACLMessage;
import org.janusproject.acl.protocol.AbstractFipaProtocol;
import org.janusproject.acl.protocol.ProtocolResult;
import org.janusproject.kernel.address.AgentAddress;

import io.sarl.acl.message.Performative;
import io.sarl.acl.protocol.EnumFipaProtocol;
import io.sarl.acl.protocol.request.RequestProtocolState;

/**
 * Propose Interaction protocol.
 * 
 * @see <a href="http://www.fipa.org/specs/fipa00036/SC00036H.html">FIPA Propose
 *      Interaction Protocol Specification</a>
 * 
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */
public class FipaProposeProtocol extends FipaProtocol {

	/**
	 * Creates a new Propose Protocol for a given agent.
	 * <p>
	 * The maximum number of participants is set to 2.
	 * 
	 * @param agent
	 */
	public FipaProposeProtocol(ACLAgent agent) {
		super(agent);
		setMaximumParticipants((short) 2);
	}

	/**
	 * Send a proposal to the participant. This method must be called by the
	 * initiator.
	 * 
	 * @param content
	 */
	public void propose(Object content) {
		if (isInitiator() && (getState() == ProposeProtocolState.NOT_STARTED)) {
			sendMessage(content, Performative.PROPOSE, getParticipants().get(0));
			setState(ProposeProtocolState.WAITING_ANSWER);
		} else if (isParticipant()) {
			this.logger.log(Level.SEVERE,
					Locale.getString("FipaProposeProtocol.0")); //$NON-NLS-1$
		} else {
			this.logger.log(Level.SEVERE,
					Locale.getString("FipaProposeProtocol.1")); //$NON-NLS-1$
		}
	}

	/**
	 * Looks if there is a pending proposal in the mailbox
	 * 
	 * @return ProtocolResult the result
	 */
	public ProtocolResult getPropose() {

		if (hasReachedTimeout()) {
			addError(Locale.getString("FipaProposeProtocol.2")); //$NON-NLS-1$
		} else {
			if (isParticipant()
					&& (getState() == ProposeProtocolState.NOT_STARTED)) {

				ACLMessage message = getRefAclAgent().getACLMessage(
						EnumFipaProtocol.FIPA_PROPOSE, Performative.PROPOSE);

				if (message != null) {
					initiate(message.getSender(), getRefAclAgent().getAddress());

					setConversationID(message.getConversationId());
					setState(ProposeProtocolState.WAITING_PROPOSE);

					resetStartedTime();

					return new ProtocolResult(message.getSender(),
							Performative.PROPOSE, message.getContent()
									.getContent().toString());
				}
			} else if (isInitiator()) {
				addError(Locale.getString("FipaProposeProtocol.3")); //$NON-NLS-1$
			} else {
				addError(Locale.getString("FipaProposeProtocol.4")); //$NON-NLS-1$
			}
		}
		return null;
	}

	/**
	 * Reject the propose made by the initiator. This method must be called by
	 * the participant.
	 * 
	 * @param content
	 * 
	 * @see #accept(Object)
	 */
	public void reject(Object content) {

		if (isParticipant()
				&& (getState() == ProposeProtocolState.WAITING_PROPOSE)) {
			sendMessage(content, Performative.REJECT_PROPOSAL, getInitiator());
			setFinalStep();
		} else if (isInitiator()) {
			addError(Locale.getString("FipaProposeProtocol.5")); //$NON-NLS-1$
		} else {
			addError(Locale.getString("FipaProposeProtocol.6")); //$NON-NLS-1$
		}
	}

	/**
	 * Accepts the propose made by the initiator. This method must be called by
	 * the participant.
	 * 
	 * @param content
	 * 
	 * @see #reject(Object)
	 */
	public void accept(Object content) {

		if (isParticipant()
				&& (getState() == ProposeProtocolState.WAITING_PROPOSE)) {
			sendMessage(content, Performative.ACCEPT_PROPOSAL, getInitiator());
		} else if (isInitiator()) {
			addError(Locale.getString("FipaProposeProtocol.7")); //$NON-NLS-1$
		} else {
			addError(Locale.getString("FipaProposeProtocol.8")); //$NON-NLS-1$
		}
	}

	/**
	 * Replies the answer of the participant to the request. (ACCEPT or REJECT)
	 * 
	 * This notification can be obtained by the initiator thanks to
	 * getNotification().
	 * 
	 * @return the answer (Performative + Content) or null if no answer.
	 */
	public ProtocolResult getAnswer() {

		if (hasReachedTimeout()) {
			addError(Locale.getString("FipaProposeProtocol.9")); //$NON-NLS-1$
		} else {
			if (isInitiator()
					&& (getState() == ProposeProtocolState.WAITING_ANSWER)) {

				ProtocolResult result = null;
				ACLMessage aMsg = getRefAclAgent()
						.getACLMessageForConversationId(getConversationId());

				if (aMsg != null) {
					result = new ProtocolResult();
					result.setPerformative(aMsg.getPerformative());
					result.setContent(aMsg.getContent().getContent());
					setFinalStep();

					resetStartedTime();
				}

				return result;
			} else if (isParticipant()) {
				addError(Locale.getString("FipaProposeProtocol.10")); //$NON-NLS-1$
			} else {
				addError(Locale.getString("FipaProposeProtocol.11")); //$NON-NLS-1$
			}
		}
		return null;
	}

	// ----------------------------
	// Exceptions to Protocol Flow
	// ----------------------------

	/**
	 * At any point in the IP, the initiator of the IP may cancel the
	 * interaction protocol.
	 * 
	 * The semantics of cancel should roughly be interpreted as meaning that the
	 * initiator is no longer interested in continuing the interaction and that
	 * it should be terminated in a manner acceptable to both the Initiator and
	 * the Participant.
	 * 
	 * The Participant either informs the Initiator that the interaction is done
	 * using an inform-done or indicates the failure of the cancellation using a
	 * failure.
	 * 
	 * @param content
	 */
	public void cancel(Object content) {

		if (isInitiator() && (getState() != ProposeProtocolState.DONE)
				&& (getState() != ProposeProtocolState.CANCELED)
				&& (getState() != ProposeProtocolState.NOT_STARTED)) {
			sendMessage(content, Performative.CANCEL, getParticipants().get(0));
			setState(ProposeProtocolState.CANCELING);
		} else if (isParticipant()) {
			addError(Locale.getString("FipaProposeProtocol.21")); //$NON-NLS-1$
		} else {
			addError(Locale.getString("FipaProposeProtocol.22")); //$NON-NLS-1$
		}
	}

	/**
	 * Once the propose has been canceled, then the Participant can informs
	 * the Initiator that the interaction is done using an inform-done
	 * 
	 * @param content
	 */
	public void informDone(Object content) {

		if (isParticipant()) {
			sendMessage(content, Performative.INFORM, getInitiator());
			setFinalStep();
		} else if (isInitiator()) {
			addError(Locale.getString("FipaProposeProtocol.14")); //$NON-NLS-1$
		} else {
			addError(Locale.getString("FipaProposeProtocol.15")); //$NON-NLS-1$
		}
	}

	/**
	 * Once the propose has been canceled, then the Participant can indicates
	 * the failure of the cancellation using a failure.
	 * 
	 * @param content
	 */
	public void failure(Object content) {

		if (isParticipant()) {
			sendMessage(content, Performative.FAILURE, getInitiator());
			setFinalStep();
		} else if (isInitiator()) {
			addError(Locale.getString("FipaRequestProtocol.12")); //$NON-NLS-1$
		} else {
			addError(Locale.getString("FipaRequestProtocol.13")); //$NON-NLS-1$
		}
	}

	/**
	 * 
	 * @return the cancel result (Performative + Content)
	 * 
	 * @see #failure(Object)
	 * @see #informDone(Object)
	 */
	public ProtocolResult getResult() {

		if (hasReachedTimeout()) {
			addError(Locale.getString("FipaProposeProtocol.18")); //$NON-NLS-1$
		} else {
			if (isInitiator() && getState() == RequestProtocolState.CANCELING) {

				ProtocolResult result = null;
				ACLMessage aMsg = getRefAclAgent()
						.getACLMessageForConversationId(getConversationId());

				if (aMsg != null) {
					result = new ProtocolResult();
					result.setPerformative(aMsg.getPerformative());
					result.setContent(aMsg.getContent().getContent());

					setFinalStep();

					resetStartedTime();
				}

				return result;
			} else if (isParticipant()) {
				addError(Locale.getString("FipaRequestProtocol.19")); //$NON-NLS-1$
			} else {
				addError(Locale.getString("FipaRequestProtocol.20")); //$NON-NLS-1$
			}
		}

		return null;
	}

	/**
	 * At any point in the IP, the receiver of a communication can inform the
	 * sender that he did not understand what was communicated.
	 * 
	 * The communication of a not-understood within an interaction protocol may
	 * terminate the entire IP and termination of the interaction may imply that
	 * any commitments made during the interaction are null and void.
	 * 
	 * @param content
	 */
	public void notUnderstood(Object content) {

		if (isParticipant() && (getState() != ProposeProtocolState.DONE)
				&& (getState() != ProposeProtocolState.CANCELED)
				&& (getState() != ProposeProtocolState.NOT_STARTED)) {
			sendMessage(content, Performative.NOT_UNDERSTOOD, getInitiator());
			setFinalStep();
		} else if (isInitiator()) {
			addError(Locale.getString("FipaProposeProtocol.23")); //$NON-NLS-1$
		} else {
			addError(Locale.getString("FipaProposeProtocol.24")); //$NON-NLS-1$
		}
	}

	// -------
	// Helpers
	// -------

	/**
	 * Helper method used to send ACL Messages within this protocol.
	 * 
	 * @param content
	 * @param performative
	 * @param to
	 */
	@Override
	protected final void sendMessage(Object content, Performative performative,
			AgentAddress to) {

		ACLMessage message = new ACLMessage(content, performative);

		message.setProtocol(EnumFipaProtocol.FIPA_PROPOSE);
		message.setConversationID(getConversationId());

		getRefAclAgent().sendACLMessage(message, to);
	}

	
	/**
	 * Set the state of the protocol to done.
	 */
	@Override
	protected void setFinalStep() {
		this.setState(ProposeProtocolState.DONE);
	}

}
