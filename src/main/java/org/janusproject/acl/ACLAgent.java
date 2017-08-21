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
package org.janusproject.acl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.janusproject.kernel.address.AgentAddress;
import org.janusproject.kernel.agent.Agent;

import io.sarl.acl.encoding.PayloadEncoding;
import io.sarl.acl.message.ACLRepresentation;
import io.sarl.acl.message.Performative;
import io.sarl.acl.protocol.EnumFipaProtocol;

/**
 * Implements the agent concept of the Janus Metamodel
 * with the ability to manage ACL Messages.
 * 
 * @author $Author: madeline$
 * @author $Author: kleroy$
 * @author $Author: ptalagrand$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */
public class ACLAgent extends Agent {

	private static final long serialVersionUID = -8831445952166271312L;
	
	/**
	 * The ACL Message Handler of this agent
	 */
	private ACLMessageHandler aclMessageHandler;

	/**
	 * The text encoding of the ACLTransportMessage
	 * Default UTF-8
	 */
	private PayloadEncoding payloadEncoding;

	/**
	 * The ACL representation of the ACLTransportMessage (Bit-efficient, String, JSON or XML)
	 * Default Bit-efficient
	 */
	private ACLRepresentation aclRepresentation;

	/**
	 * Creates a new ACL Agent with default payload encoding (UTF8) and default acl representation (string).
	 */
	public ACLAgent() {
		super();
		this.payloadEncoding = PayloadEncoding.UTF8;
		this.aclRepresentation = ACLRepresentation.STRING;
	}

	/**
	 * Creates a new ACL Agent.
	 * 
	 * @param aclRepresentation
	 * @param payloadEncoding
	 * 
	 * @see ACLRepresentation
	 * @see PayloadEncoding
	 */
	public ACLAgent(ACLRepresentation aclRepresentation, PayloadEncoding payloadEncoding) {
		super();
		this.payloadEncoding = payloadEncoding;
		this.aclRepresentation = aclRepresentation;
	}

	/**
	 * Sends the specified <code>ACL Message</code> to one randomly selected agent.
	 * <p>
	 * This function force the emitter of the message to be this agent.
	 * </p>
	 * 
	 * @param message is the ACL Message to send
	 * @param agents is the collection of receivers.
	 * @return the address of the receiver of the freshly sent message if it
	 *         was found, or <code>null</code> otherwise.
	 */
	public final AgentAddress sendACLMessage(ACLMessage message, AgentAddress... agents) {

		initACLMessage(message);
		ACLTransportMessage transportMessage = getAclMessageHandler().prepareOutgoingACLMessage(message, agents);

		return sendMessage(transportMessage, agents);
	}

	/**
	 * Replies the first available ACL Message in the agent mail box and remove it from the mailbox.
	 * 
	 * @return the first available ACL Message, or <code>null</code> if the mailbox is empty.
	 * 
	 * @see #peekACLMessage()
	 * @see #getACLMessages()
	 * @see #peekACLMessages()
	 * @see #hasACLMessage()
	 */
	protected final ACLMessage getACLMessage() {
		for(ACLTransportMessage msg : peekMessages(ACLTransportMessage.class)) {
			ACLMessage aMsg = getAclMessageFromTransportMessage( msg ) ;
			if(aMsg != null) {
				getMailbox().remove( msg );
				return aMsg;
			}
		}
		return null;
	}

//	/**
//	 * Replies the first available ACL Message
//	 * in the agent mail box  
//	 * with the given performative 
//	 * and the given protocol type 
//	 * and, then, removes it from the mailbox.
//	 * @param protocolType 
//	 * @param performative 
//	 * 
//	 * @return the first available ACL Message, or <code>null</code> if the mailbox is empty.
//	 * @see #peekACLMessage()
//	 * @see #getACLMessages()
//	 * @see #peekACLMessages()
//	 * @see #hasACLMessage()
//	 */
//	public final ACLMessage getACLMessage(EnumFipaProtocol protocolType, Performative performative) {
//		for(ACLTransportMessage msg : peekMessages(ACLTransportMessage.class)) {
//			ACLMessage aMsg = getAclMessageFromTransportMessage( msg ) ;
//			if(aMsg != null 
//					&& ( aMsg.getPerformative() == performative )
//					&& ( aMsg.getProtocol() == protocolType)
//					)
//			{
//				getMailbox().remove( msg );
//				return aMsg;
//			}
//		}
//		return null;
//	}
	
	/**
	 * Replies the first available ACL Message
	 * in the agent mail box  
	 * with one of the given performative 
	 * and the given protocol type 
	 * and, then, removes it from the mailbox.
	 * @param protocolType 
	 * @param performative 
	 * 
	 * @return the first available ACL Message, or <code>null</code> if the mailbox is empty.
	 * @see #peekACLMessage()
	 * @see #getACLMessages()
	 * @see #peekACLMessages()
	 * @see #hasACLMessage()
	 */
	public final ACLMessage getACLMessage(EnumFipaProtocol protocolType, Performative... performative) {
		List<Performative> performatives = Arrays.asList(performative);
		
		for(ACLTransportMessage msg : peekMessages(ACLTransportMessage.class)) {
			ACLMessage aMsg = getAclMessageFromTransportMessage( msg ) ;
			if(aMsg != null 
					&& ( performatives.contains(aMsg.getPerformative()) )
					&& ( aMsg.getProtocol() == protocolType)
					)
			{
				getMailbox().remove( msg );
				return aMsg;
			}
		}
		return null;
	}

	/**
	 * Replies the first available ACL Message 
	 * for a specific conversation 
	 * and, then, removes it from the mailbox.
	 * <p>
	 * This method is essentially used in protocols.
	 * 
	 * @param conversationId
	 * 
	 * @return the first available ACL Message, or <code>null</code>
	 */
	public final ACLMessage getACLMessageForConversationId(UUID conversationId) {
		for(ACLTransportMessage msg : peekMessages(ACLTransportMessage.class)) {
			ACLMessage aMsg = getAclMessageFromTransportMessage( msg ) ;
			if( ( aMsg != null ) && ( aMsg.getConversationId().compareTo(conversationId) == 0 ) ){
				getMailbox().remove( msg );
				return aMsg;
			}
		}
		return null;
	}
	
	/**
	 * Replies the first available ACL Message 
	 * for a specific conversation and performative(s)
	 * and, then, removes it from the mailbox.
	 * This method is essentially used in protocols.
	 * 
	 * @param conversationId
	 * @param performative
	 * 
	 * @return the first available ACL Message, or <code>null</code>
	 */
	public final ACLMessage getACLMessageFromProtocol(UUID conversationId, Performative... performative) {
		List<Performative> performatives = Arrays.asList(performative);
		
		for(ACLTransportMessage msg : peekMessages(ACLTransportMessage.class)) {
			ACLMessage aMsg = getAclMessageFromTransportMessage( msg ) ;
			if( ( aMsg != null ) && ( aMsg.getConversationId().compareTo(conversationId) == 0 ) 
					&& performatives.contains(aMsg.getPerformative())) {
				getMailbox().remove( msg );
				return aMsg;
			}
		}
		return null;
	}

	/**
	 * Replies all the ACL Messages in the agent mailbox.
	 * <p>
	 * Each time a message is consumed
	 * from the replied iterable object,
	 * the corresponding ACL Message is removed
	 * from the mailbox.
	 * 
	 * @return all the ACL Messages, never <code>null</code>.
	 * @see #getACLMessage()
	 * @see #peekACLMessage()
	 * @see #peekACLMessages()
	 * @see #hasACLMessage()
	 */
	protected final Iterable<ACLMessage> getACLMessages() {

		LinkedList<ACLMessage> resultList = new LinkedList<ACLMessage>();

		for(ACLTransportMessage msg : peekMessages(ACLTransportMessage.class)) {
			ACLMessage aMsg = getAclMessageFromTransportMessage( msg ) ;
			if(aMsg != null){
				getMailbox().remove( msg );
				resultList.add( aMsg );
			}
		}
		
		return resultList;
	}

	/**
	 * Replies all the ACL Messages in the agent mailbox with the specified performative.
	 * Each time a message is consumed
	 * from the replied iterable object,
	 * the corresponding ACL Message is removed
	 * from the mailbox.
	 * @param performative 
	 * 
	 * @return all the ACL Messages, never <code>null</code>.
	 * @see #getACLMessage()
	 * @see #peekACLMessage()
	 * @see #peekACLMessages()
	 * @see #hasACLMessage()
	 */
	protected final Iterator<ACLMessage> getACLMessages(Performative performative) {

		LinkedList<ACLMessage> resultList = new LinkedList<ACLMessage>();

		for(ACLTransportMessage msg : peekMessages(ACLTransportMessage.class)) {
			ACLMessage aMsg = getAclMessageFromTransportMessage( msg ) ;
			if(aMsg != null && aMsg.getPerformative() == performative){
				getMailbox().remove( msg );
				resultList.add( aMsg );
			}
		}

		return resultList.iterator();
	}

	/**
	 * Replies the first available ACL Message in the agent mail box
	 * and leave it inside the mailbox.
	 * 
	 * @return the first available ACL Message, or <code>null</code> if
	 * the mailbox is empty.
	 * @see #getACLMessage()
	 * @see #getACLMessages()
	 * @see #peekACLMessages()
	 * @see #hasACLMessage()
	 */
	protected final ACLMessage peekACLMessage() {
		for(ACLTransportMessage msg : peekMessages(ACLTransportMessage.class)) {
			ACLMessage aMsg = getAclMessageFromTransportMessage( msg ) ;
			if(aMsg != null) return aMsg;
		}
		return null;
	}

	/**
	 * Replies the ACL Messages in the agent mailbox.
	 * Each time a message is consumed
	 * from the replied iterable object,
	 * the corresponding message is NOT removed
	 * from the mailbox.
	 * 
	 * @return all the ACL Messages, never <code>null</code>.
	 * @see #getACLMessage()
	 * @see #peekACLMessage()
	 * @see #getACLMessages()
	 * @see #hasACLMessage()
	 */
	protected final Iterator<ACLMessage> peekACLMessages() {

		LinkedList<ACLMessage> resultList = new LinkedList<ACLMessage>();

		for(ACLTransportMessage msg : peekMessages(ACLTransportMessage.class)) {
			ACLMessage aMsg = getAclMessageFromTransportMessage( msg ) ;

			if(aMsg != null){
				resultList.add( aMsg );
			}
		}
		
		return resultList.iterator();
	}

	/** 
	 * Indicates if the agent mailbox contains at least one ACL Message or not.
	 * 
	 * @return <code>true</code> if the mailbox contains at least one ACL Message,
	 * otherwise <code>false</code>
	 */
	protected final boolean hasACLMessage(){
		Iterator<?> iterator = peekMessages(ACLTransportMessage.class).iterator();
		return iterator.hasNext();
	}
	
	/** 
	 * Indicates if the agent mailbox contains at least one ACL Message for the given protocol and performatives or not.
	 * @param protocol 
	 * @param performative 
	 * 
	 * @return <code>true</code> if the mailbox contains at least one ACL Message 
	 * for the given protocol and performatives, otherwise <code>false</code>
	 */
	public final boolean hasACLMessages(EnumFipaProtocol protocol, Performative... performative) {
		List<Performative> performatives = Arrays.asList(performative);
		
		for(ACLTransportMessage msg : peekMessages(ACLTransportMessage.class)) {
			ACLMessage aMsg = getAclMessageFromTransportMessage( msg ) ;
			if(aMsg != null 
					&& ( performatives.contains(aMsg.getPerformative()) )
					&& ( aMsg.getProtocol() == protocol)
					)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Gets ACL Message from ACL Transport Message 
	 * after verification of the content of this message.
	 * @param tMsg 
	 * 
	 * @return the ACL Message
	 */
	protected final ACLMessage getAclMessageFromTransportMessage( ACLTransportMessage tMsg ){

		if( ( tMsg.getContent() != null ) && (tMsg.getContent() instanceof byte[] ) )
		{
			try{
				ACLMessage aMsg = getAclMessageHandler().prepareIncomingMessage( tMsg ) ;
				return aMsg;
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}

		return null;
	}

	/**
	 * Gets the ACL Message Handler for the agent
	 * 
	 * @return the aclMessageHandler
	 */
	public ACLMessageHandler getAclMessageHandler() {
		if (this.aclMessageHandler == null) {
			this.aclMessageHandler = new ACLMessageHandler();
		}
		return this.aclMessageHandler;
	}

	/**
	 * Sets the ACL Message Handler for the agent
	 * 
	 * @param aclMessageHandler the aclMessageHandler to set
	 */
	public void setAclMessageHandler(ACLMessageHandler aclMessageHandler) {
		if (aclMessageHandler != null) {
			this.aclMessageHandler = aclMessageHandler;
		}
	}

	/**
	 * Initiates and completes a given ACL Message before sending it.
	 * 
	 * @param message
	 */
	private void initACLMessage(ACLMessage message) {
		message.setAclRepresentation(this.aclRepresentation.getValue());
		message.setEncoding(this.payloadEncoding.getValue());
		message.setSender(getAddress());
	}
}