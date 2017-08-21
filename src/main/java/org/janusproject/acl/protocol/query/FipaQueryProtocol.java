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
package org.janusproject.acl.protocol.query;

import org.arakhne.afc.vmutil.locale.Locale;
import org.janusproject.acl.ACLAgent;
import org.janusproject.acl.ACLMessage;
import org.janusproject.acl.protocol.AbstractFipaProtocol;
import org.janusproject.kernel.address.AgentAddress;

import io.sarl.acl.message.Performative;
import io.sarl.acl.protocol.EnumFipaProtocol;
import io.sarl.acl.protocol.request.RequestProtocolState;

/**
 * Query Interaction Protocol (IP).
 * 
 * @see <a href="http://fipa.org/specs/fipa00027/SC00027H.html">FIPA Query Interaction Protocol Specification</a>
 * 
 * @author $Author: flacreus$
 * @author $Author: sroth$
 * @author $Author: cstentz$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */
public class FipaQueryProtocol extends FipaProtocol {
	private ACLMessage query;
	private ACLMessage answer;
	private ACLMessage result;
	
	private ACLMessage notUnderstood;
	
	/**
	 * Creates a new Query Interaction Protocol (IP) for a given agent.
	 * The maximum number of participants is set to 2.
	 * 
	 * @param agent the RefAclAgent
	 */
	public FipaQueryProtocol(ACLAgent agent) {
		super(agent);
		setState(QueryProtocolState.NOT_STARTED);
		setMaximumParticipants((short) 2);
	}
	
	/**
	 * Sends a query to the unique participant.
	 * The unique participant has to be set.
	 * This method can only be called by the initiator.
	 * 
	 * @param query the ACLMessage of the query
	 * 
	 * @see #queryIf(ACLMessage)
	 * @see #queryRef(ACLMessage)
	 * @see #query(Object, Performative)
	 */
	public void query(ACLMessage query) {
		if (isParticipant()) {
			print(Locale.getString("WrongAgent", this.getRefAclAgent().getAddress(), getRoleInIP(), "query()")); //$NON-NLS-1$ //$NON-NLS-2$
			return;
		}
		
		if (getState() != QueryProtocolState.NOT_STARTED) {
			print(Locale.getString("WrongState", this.getRefAclAgent().getAddress(), getRoleInIP(), getState(), "query()")); //$NON-NLS-1$ //$NON-NLS-2$
			return;
		}
		
		if (query.getPerformative() == Performative.QUERY_IF) {
			queryIf(query);
		} else if (query.getPerformative() == Performative.QUERY_REF) {
			queryRef(query);
		} else {
			print(Locale.getString("WrongPerformative", this.getRefAclAgent().getAddress(), getRoleInIP(), query.getPerformative())); //$NON-NLS-1$
		}
	}
	
	/**
	 * Sends a query to the unique participant.
	 * The unique participant has to be set.
	 * This method can only be called by the initiator.
	 * 
	 * @param content the content of the ACLMessage
	 * @param performative the performative of the ACLMessage
	 * 
	 * @see #query(ACLMessage)
	 * @see #queryIf(ACLMessage)
	 * @see #queryRef(ACLMessage)
	 */
	public void query(Object content, Performative performative) {
		query(new ACLMessage(content, performative));
	}
	
	/**
	 * Sends a query to the unique participant.
	 * The unique participant has to be set.
	 * This method can only be called by the initiator.
	 * 
	 * @param query the ACLMessage of the query
	 * 
	 * @see #query(ACLMessage)
	 * @see #query(Object, Performative)
	 */
	private void queryIf(ACLMessage query) {		
		if (isInitiator() && (getState() == QueryProtocolState.NOT_STARTED)) {
			query.setPerformative(Performative.QUERY_IF);
			sendMessage(query, getParticipants().get(0));
			this.query = query;
			setState(QueryProtocolState.WAITING_ANSWER);
		}
	}
	
	/**
	 * Sends a query to the unique participant.
	 * The unique participant has to be set.
	 * This method can only be called by the initiator.
	 * 
	 * @param query the ACLMessage of the query
	 * 
	 * @see QueryProtocolState
	 */
	private void queryRef(ACLMessage query) {
		if (isInitiator() && (getState() == QueryProtocolState.NOT_STARTED)) {
			query.setPerformative(Performative.QUERY_REF);
			sendMessage(query, getParticipants().get(0));
			this.query = query;
			setState(QueryProtocolState.WAITING_ANSWER);
		}
	}
	
	/**
	 * This method can be called by both agents (initiator and participant)
	 * If it's from the initiator, return the query sent.
	 * If it's from the participant, return the query received or check for a pending query.
	 * 
	 * @return the query sent (received) by the initiator (participant)
	 */
	public ACLMessage getQuery() {
		if (isInitiator()) {
			return this.query;
		}
		if (isParticipant()) {
			if (getState() == QueryProtocolState.NOT_STARTED) {
				return checkPendingQuery();
			}
			return this.query;
		}
		
		return null;
	}
	
	/**
	 * Check in the mailbox of the participant if there is an ACLMessage for the query protocol (query-if or query-ref)
	 * In this case, the participant will have to give an answer to the initiator.
	 * This method can only be called by the participant.
	 * 
	 * @return a Query ACLMessage (if exists)
	 */
	private ACLMessage checkPendingQuery() {
		if (isParticipant() && (getState() == QueryProtocolState.NOT_STARTED)) {
			this.query = getRefAclAgent().getACLMessage(EnumFipaProtocol.FIPA_QUERY, Performative.QUERY_IF, Performative.QUERY_REF);
			
			if (this.query != null) {
				initiate(this.query.getSender(), getRefAclAgent().getAddress());
				
				setConversationID(this.query.getConversationId());
				setState(QueryProtocolState.SENDING_ANSWER);
			}
		} 
		
		return this.query;
	}
	
	/**
	 * Sends a refuse ACLMessage as answer to the initiator.
	 * This method can only be called by the participant.
	 * 
	 * @param answer the refuse ACLMessage to sent to the initiator
	 * 
	 * @see #refuse(Object)
	 * @see #agree(ACLMessage)
	 * @see #agree(Object)
	 * @see #getQuery()
	 */
	public void refuse(ACLMessage answer) {
		if (isParticipant() && (getState() == QueryProtocolState.SENDING_ANSWER)) {
			answer.setPerformative(Performative.REFUSE);
			this.answer = answer;
			sendMessage(answer, getInitiator());
			setFinalStep();
		} else if (isInitiator()) {
			print(Locale.getString("WrongAgent", this.getRefAclAgent().getAddress(), getRoleInIP(), "refuse()")); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (getState() != QueryProtocolState.SENDING_ANSWER) {
			print(Locale.getString("WrongState", this.getRefAclAgent().getAddress(), getRoleInIP(), getState(), "refuse()")); //$NON-NLS-1$ //$NON-NLS-2$
		} 
	}
	
	/**
	 * Sends a refuse ACLMessage as answer to the initiator.
	 * This method can only be called by the participant.
	 * 
	 * @param content the content of the refuse ACLMessage
	 * 
	 * @see #refuse(ACLMessage)
	 * @see #agree(ACLMessage)
	 * @see #agree(Object)
	 * @see #getQuery()
	 */
	public void refuse(Object content) {
		refuse(new ACLMessage(content, Performative.REFUSE));
	}

	/**
	 * Sends an agree ACLMessage as answer to the initiator.
	 * This method can only be called by the participant.
	 * 
	 * @param answer the answer ACLMessage to sent to the initiator
	 * 
	 * @see #agree(Object)
	 * @see #refuse(ACLMessage)
	 * @see #refuse(Object)
	 * @see #getQuery()
	 */
	public void agree(ACLMessage answer) {
		if (isParticipant() && (getState() == QueryProtocolState.SENDING_ANSWER)) {
			answer.setPerformative(Performative.AGREE);
			this.answer = answer;
			sendMessage(answer, getInitiator());
			setState(QueryProtocolState.SENDING_RESULT);
		} else if (isInitiator()) {
			print(Locale.getString("WrongAgent", this.getRefAclAgent().getAddress(), getRoleInIP(), "agree()")); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (getState() != QueryProtocolState.SENDING_ANSWER) {
			print(Locale.getString("WrongState", this.getRefAclAgent().getAddress(), getRoleInIP(), getState(), "agree()")); //$NON-NLS-1$ //$NON-NLS-2$
		} 
	}

	/**
	 * Sends an agree ACLMessage as answer to the initiator.
	 * This method can only be called by the participant.
	 * 
	 * @param content the content of the agree ACLMessage
	 * 
	 * @see #agree(ACLMessage)
	 * @see #refuse(ACLMessage)
	 * @see #refuse(Object)
	 * @see #getQuery()
	 */
	public void agree(Object content) {
		agree(new ACLMessage(content, Performative.AGREE));
	}
	
	/**
	 * This method can be called by both agents (initiator and participant)
	 * If it's from the initiator,  return the answer received or check for a pending answer.
	 * If it's from the participant, return the answer sent.
	 * 
	 * @return the answer received (sent) by the initiator (participant)
	 * 
	 * @see #agree(ACLMessage)
	 * @see #agree(Object)
	 * @see #refuse(ACLMessage)
	 * @see #refuse(Object)
	 */
	public ACLMessage getAnswer() {
		if (isParticipant()) {
			return this.answer;
		}
		if (isInitiator()) {
			if (this.answer != null) {
				return this.answer;
			}
			return checkPendingAnswer();
		}
		
		return null;
	}
	
	/**
	 * Check in the mailbox of the initiator if there is an answer for its query (query-if or query-ref)
	 * In this case, the initiator will wait for a result.
	 * This method can only be called by the initiator.
	 * 
	 * @return a answer ACLMessage (if exists)
	 */
	private ACLMessage checkPendingAnswer() {
		if (isInitiator() && (getState() == QueryProtocolState.WAITING_ANSWER)) {
			this.answer  = getRefAclAgent().getACLMessageFromProtocol(getConversationId(), Performative.AGREE, Performative.REFUSE);
			
			if (this.answer != null) {	
				if (this.answer.getPerformative() == Performative.NOT_UNDERSTOOD) {
					setFinalStep();
				} else if (this.answer.getPerformative() == Performative.REFUSE) {
					setFinalStep();
				} else if (this.answer.getPerformative() == Performative.AGREE) {
					setState(QueryProtocolState.WAITING_RESULT);
				}
			}
		}
		
		return this.answer;
	}

	/**
	 * Sends an failure ACLMessage as result to the initiator.
	 * This method can only be called by the participant.
	 * 
	 * @param result the failure ACLMessage to sent to the initiator
	 * 
	 * @see #failure(Object)
	 */
	public void failure(ACLMessage result) {
		if (isParticipant() && (getState() == QueryProtocolState.SENDING_RESULT)) {
			result.setPerformative(Performative.FAILURE);
			this.result = result;
			sendMessage(result, getInitiator());
			setFinalStep();
		} else if (isInitiator()) {
			print(Locale.getString("WrongAgent", this.getRefAclAgent().getAddress(), getRoleInIP(), "failure()")); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (getState() != QueryProtocolState.SENDING_RESULT) {
			print(Locale.getString("WrongState", this.getRefAclAgent().getAddress(), getRoleInIP(), getState(), "failure()")); //$NON-NLS-1$ //$NON-NLS-2$
		} 
	}
	
	/**
	 * Sends an failure ACLMessage as result to the initiator.
	 * This method can only be called by the participant.
	 * 
	 * @param content the content of the failure ACLMessage
	 * 
	 * @see #failure(ACLMessage)
	 */
	public void failure(Object content) {
		failure(new ACLMessage(content, Performative.FAILURE));
	}
	
	/**
	 * Sends an inform ACLMessage as result to the initiator.
	 * This method can only be called by the participant.
	 * 
	 * @param result the result ACLMessage to sent to the initiator
	 * 
	 * @see #inform(Object)
	 * @see #informTrueFalse(ACLMessage)
	 * @see #informResult(ACLMessage)
	 */
	public void inform(ACLMessage result) {
		if (isParticipant() && (getState() == QueryProtocolState.SENDING_RESULT)) {
			if (this.query.getPerformative() == Performative.QUERY_IF) {
				informTrueFalse(result);
			} else if (this.query.getPerformative() == Performative.QUERY_REF) {
				informResult(result);
			}
		} else if (isInitiator()) {
			print(Locale.getString("WrongAgent", this.getRefAclAgent().getAddress(), getRoleInIP(), "inform()")); //$NON-NLS-1$ //$NON-NLS-2$
		} else if (getState() != QueryProtocolState.SENDING_RESULT) {
			print(Locale.getString("WrongState", this.getRefAclAgent().getAddress(), getRoleInIP(), getState(), "inform()")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * Sends an inform ACLMessage as result to the initiator.
	 * This method can only be called by the participant.
	 * 
	 * @param content the content of the ACLMessage
	 * 
	 * @see #inform(ACLMessage)
	 * @see #informTrueFalse(ACLMessage)
	 * @see #informResult(ACLMessage)
	 */
	public void inform(Object content) {
		inform(new ACLMessage(content, Performative.INFORM));
	}
	

	/**
	 * Sends an inform ACLMessage as result for a query-if to the initiator.
	 * This method can only be called by the participant.
	 * 
	 * @param result the result ACLMessage to sent to the initiator
	 * 
	 * @see #inform(ACLMessage)
	 * @see #inform(Object)
	 */
	private void informTrueFalse(ACLMessage result){
		if ((this.query.getPerformative() == Performative.QUERY_IF) && isParticipant() && (getState() == QueryProtocolState.SENDING_RESULT)) {
			sendMessage(result, getInitiator());
			this.result = result;
			setFinalStep();
		}
	}
	
	/**
	 * Sends an inform ACLMessage as result for a query-ref to the initiator.
	 * This method can only be called by the participant.
	 * 
	 * @param result the result ACLMessage to sent to the initiator
	 * 
	 * @see #inform(ACLMessage)
	 * @see #inform(Object)
	 */
	private void informResult(ACLMessage result){
		if ((this.query.getPerformative() == Performative.QUERY_REF) && isParticipant() && (getState() == QueryProtocolState.SENDING_RESULT)) {
			sendMessage(result, getInitiator());
			this.result = result;
			setFinalStep();
		}
	}
	
	/**
	 * This method can be called by both agents (initiator and participant)
	 * If it's from the initiator,  return the result received or check for a pending result.
	 * If it's from the participant, return the result sent.
	 * 
	 * @return the result received (sent) by the initiator (participant)
	 */
	public ACLMessage getResult() {
		if (isParticipant()) {
			return this.result;
		}
		if (isInitiator()) {
			if (this.result != null) {
				return this.result;
			}
			return checkPendingResult();
		}
		
		return null;
	}
	
	/**
	 * Check in the mailbox of the initiator if there is an answer for its query (query-if or query-ref)
	 * In this case, the initiator will wait for a result.
	 * This method can only be called by the initiator.
	 * 
	 * @return a result ACLMessage (if exists)
	 */
	private ACLMessage checkPendingResult() {
		if (isInitiator() && (getState() == QueryProtocolState.WAITING_RESULT) || getState() == QueryProtocolState.CANCELING) {  
			this.result = getRefAclAgent().getACLMessageFromProtocol(getConversationId(), Performative.INFORM, Performative.FAILURE);
			
			if (this.result != null) {
				setFinalStep();
			}
		}
		
		return this.result;
	}
	
	/**
	 * At any point in the IP, the receiver of a communication can inform the sender that he did not understand what was communicated.
	 * 
	 * Send an not understood ACLMessage.
	 * This method can by called by both initiator and participant.
	 * 
	 * @param notUnderstood 
	 */
	public void notUnderstood(ACLMessage notUnderstood) {
		if ((getState() != RequestProtocolState.DONE) && (getState() != RequestProtocolState.CANCELED) 
				&& (getState() != RequestProtocolState.NOT_STARTED)) {
			
			AgentAddress dest = isParticipant() ? getInitiator() : getParticipants().get(0);
			
			if (dest != null) {
				notUnderstood.setPerformative(Performative.NOT_UNDERSTOOD);
				sendMessage(notUnderstood, getInitiator());
				this.notUnderstood = notUnderstood;
				setFinalStep();
			}
		} else {
			print(Locale.getString("WrongState", this.getRefAclAgent().getAddress(), getRoleInIP(), getState(), "notUnderstood()")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	/**
	 * This method can be called by both agents (initiator and participant)
	 * Return the not understood received/sent or check for a pending result.
	 * 
	 * @return the result received (sent) by the initiator (participant)
	 */
	public ACLMessage getNotUnderstood() {
		if (this.notUnderstood != null) {
			return this.notUnderstood;
		}
		return checkPendingNotUnderstood();
	}
	
	/**
	 * Check in the mailbox of the agent if there is an not understood message
	 * In this case, the initiator will wait for a result.
	 * This method can be called by both initiator and participant.
	 * 
	 * @return a not understood ACLMessage (if exists)
	 */
	private ACLMessage checkPendingNotUnderstood() {
		if ((getState() != RequestProtocolState.DONE) && (getState() != RequestProtocolState.CANCELED) 
				&& (getState() != RequestProtocolState.NOT_STARTED)) {
			
			this.notUnderstood = getRefAclAgent().getACLMessageFromProtocol(getConversationId(), Performative.NOT_UNDERSTOOD);
			
			if (this.notUnderstood != null) {
				setFinalStep();
			}
		}
		
		return this.notUnderstood;
	}
	
	/**
	 * Sends an ACLMessage to the given agent
	 * 
	 * @param message the message to sent
	 * @param to the receiver
	 * 
	 * @see #sendMessage(Object, Performative, AgentAddress)
	 */
//	@Override
	protected void sendMessage(ACLMessage message, AgentAddress to) {
		message.setProtocol(EnumFipaProtocol.FIPA_QUERY);
		message.setConversationID(getConversationId());
		getRefAclAgent().sendACLMessage(message, to);
	}
	
	/**
	 * Sends an ACLMessage to the given agent
	 * 
	 * @param content the content of the ACLMessage
	 * @param performative the performative of the ACLMessage
	 * @param to the receiver
	 * 
	 * @see #sendMessage(ACLMessage, AgentAddress)
	 */
	@Override
	protected void sendMessage(Object content, Performative performative, AgentAddress to) {
		sendMessage(new ACLMessage(content, performative), to);
	}
	
	/**
	 * Set the state of the protocol to done.
	 */
	@Override
	protected void setFinalStep() {
		this.setState(QueryProtocolState.DONE);
	}
	
	/**
	 * Returns the String value of the role of the agent in the protocol.
	 * Useful for logs.
	 * 
	 * @return the String value of the role (initiator/participant) of the agent
	 */
	private String getRoleInIP() {
		if (isInitiator()) {
			return Locale.getString("InitiatorRole"); //$NON-NLS-1$
		} else if (isParticipant()) {
			return Locale.getString("ParticipantRole"); //$NON-NLS-1$
		} else {
			return ""; //$NON-NLS-1$
		}
	}
}
