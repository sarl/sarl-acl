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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.arakhne.afc.vmutil.locale.Locale;
import org.janusproject.acl.ACLAgent;
import org.janusproject.acl.ACLMessage;
import org.janusproject.acl.protocol.AbstractFipaProtocol;
import org.janusproject.acl.protocol.ProtocolResult;
import org.janusproject.kernel.address.AgentAddress;

import io.sarl.acl.message.Performative;
import io.sarl.acl.protocol.EnumFipaProtocol;

/**
 * Contract Net Interaction Protocol.
 * 
 * @see <a href="http://www.fipa.org/specs/fipa00029/SC00029H.html">FIPA Contract Net Interaction Protocol Specification</a>
 * 
 * @author $Author: madeline$
 * @author $Author: kleroy$
 * @author $Author: ptalagrand$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */
public class FipaContractNetProtocol extends FipaProtocol {
	private boolean checkTimeOut;
	
	/**
	 * Creates a new Contract Net Protocol for a given agent.
	 * The timeouts are testing during the protocol execution. 
	 * 
	 * @param agent
	 */
	public FipaContractNetProtocol(ACLAgent agent){
		this(agent, true);
	}
	
	/**
	 * Creates a new Contract Net Protocol for a given agent.
	 * 
	 * @param agent
	 * @param checkTimeOut indicates if the timeouts are tested during the
	 * protocol execution. 
	 */
	public FipaContractNetProtocol(ACLAgent agent, boolean checkTimeOut){
		super(agent);
		setMaximumParticipants(Short.MAX_VALUE);
		this.checkTimeOut = checkTimeOut;
	}
	
	/**
	 * The proposals received from the participants
	 */
	protected List<ProtocolResult> proposals;
	
	/**
	 * The participants that were selected to perform the task 
	 */
	protected List<AgentAddress> selectedParticipants;
	
	/**
	 * The results of the tasks
	 */
	protected List<ProtocolResult> results;
	
	/**
	 * The number of proposal that the agent is expected to receive
	 */
	protected int nbExpectedProposal;
	
	/**
	 * The number of results that the agent is expected to receive
	 */
	protected int nbExpectedResults;
	
	/**
	 * Send a call for proposal to the participants.
	 * This method must be called by the initiator.
	 * 
	 * @param content
	 */
	public void callForProposal(Object content) {
		
		if( isInitiator() && (getState() == ContractNetProtocolState.NOT_STARTED) ){
			for (AgentAddress participant : getParticipants()) {
				sendMessage(content, Performative.CFP, participant);
			}			
			setState(ContractNetProtocolState.WAITING_ALL_PROPOSALS);
		}
		else if( isParticipant() ){
			this.logger.log(Level.SEVERE, Locale.getString("FipaContractNetProtocol.0")); //$NON-NLS-1$
		}
		else{
			this.logger.log(Level.SEVERE, Locale.getString("FipaContractNetProtocol.1")); //$NON-NLS-1$
		}
	}
	
	/**
	 * Looks if there is a pending call for proposal in the mailbox
	 * 
	 * @return ProtocolResult
	 */
	public ProtocolResult getCallForProposal() {
		
		if( this.checkTimeOut && hasReachedTimeout() ){
			addError(Locale.getString("FipaContractNetProtocol.2")); //$NON-NLS-1$
		}
		else{
			if (isParticipant() && (getState() == ContractNetProtocolState.NOT_STARTED)) {
				
				ACLMessage message = getRefAclAgent().getACLMessage(EnumFipaProtocol.FIPA_CONTRACT_NET, Performative.CFP);
				
				if (message != null) {
					this.getParticipants().clear();
					initiate( message.getSender(), getRefAclAgent().getAddress() );
					
					setConversationID( message.getConversationId() );
					setState( ContractNetProtocolState.SENDING_PROPOSAL );
					
					return new ProtocolResult(message.getSender(), Performative.REQUEST, message.getContent().getContent().toString());
				}
			}
			else if (isInitiator()) {
				addError(Locale.getString("FipaContractNetProtocol.3")); //$NON-NLS-1$
			}
			else {
				addError(Locale.getString("FipaContractNetProtocol.4")); //$NON-NLS-1$
			}
		}
		return null;
	}
	
	/**
	 * Refuses the call for proposal made by the initiator.
	 * This method must be called by the participant.
	 * 
	 * @param content
	 * 
	 * @see #propose(Object)
	 */
	public void refuse(Object content){

		if (isParticipant() && (getState() == ContractNetProtocolState.SENDING_PROPOSAL)) {
			sendMessage(content, Performative.REFUSE, getInitiator());
			setFinalStep();
		}
		else if( isInitiator() ){
			addError(Locale.getString("FipaContractNetProtocol.5")); //$NON-NLS-1$
		}
		else{
			addError(Locale.getString("FipaContractNetProtocol.6")); //$NON-NLS-1$
		}
	}
	
	/**
	 * Accepts the request made by the initiator.
	 * This method must be called by the participant.
	 * <p>
	 * Once the request has been agreed upon, 
	 * then the Participant must send one of the following notifications:
	 * failure(), informDone(), informResult().
	 * </p>
	 * 
	 * @param content
	 * 
	 * @see #refuse(Object)
	 * @see #failure(Object)
	 * @see #informDone(Object)
	 */
	public void propose(Object content){

		if( isParticipant() && (getState() == ContractNetProtocolState.SENDING_PROPOSAL)){
			sendMessage(content, Performative.PROPOSE, getInitiator());
			setState(ContractNetProtocolState.WAITING_PROPOSAL_ANSWER);
		}
		else if( isInitiator() ){
			addError(Locale.getString("FipaContractNetProtocol.7")); //$NON-NLS-1$
		}
		else{
			addError(Locale.getString("FipaContractNetProtocol.8")); //$NON-NLS-1$
		}
	}
	
	/**
	 * Receives all the proposals from the participants
	 * 
	 * @return a list of ProtocolProposal (Author + Performative + Content) 
	 * or null if all the proposals are not received.
	 * 
	 */
	public List<ProtocolResult> getAllProposals(){
		
		if( this.checkTimeOut && hasReachedTimeout() ){
			addError(Locale.getString("FipaContractNetProtocol.9")); //$NON-NLS-1$
		}
		else{
			if (isInitiator() && (getState() == ContractNetProtocolState.WAITING_ALL_PROPOSALS)) {
				
				if (this.proposals == null) {
					this.proposals = new ArrayList<ProtocolResult>();
					this.nbExpectedProposal = getParticipants().size();
				}
				
				ACLMessage aMsg = getRefAclAgent().getACLMessageForConversationId(getConversationId());
				
				if( aMsg != null )
				{		
					if (aMsg.getPerformative().compareTo(Performative.NOT_UNDERSTOOD) == 0
							|| aMsg.getPerformative().compareTo(Performative.REFUSE) == 0) {
						this.nbExpectedProposal--;
					}
					else if (aMsg.getPerformative().compareTo(Performative.PROPOSE) == 0) {
						ProtocolResult proposal = new ProtocolResult();
						proposal.setAuthor(aMsg.getSender());
						proposal.setPerformative(Performative.PROPOSE);
						proposal.setContent(aMsg.getContent().getContent().toString());
						this.proposals.add(proposal);
						this.nbExpectedProposal--;
					}
					
					if (this.nbExpectedProposal == 0) {
						setState(ContractNetProtocolState.SENDING_ALL_PROPOSAL_ANSWERS);
						resetStartedTime();
						
						return this.proposals;
					}
				}

				return null;
			}
			else if( isParticipant() ){
				addError(Locale.getString("FipaContractNetProtocol.10")); //$NON-NLS-1$
			}
			else{
				addError(Locale.getString("FipaContractNetProtocol.11")); //$NON-NLS-1$
			}
		}
		return null;
	}
	
	/**
	 * Accepts the proposal of one of the participants
	 * Automatically refuse the others proposals
	 * 
	 * @param selectedAuthor the author of the selected proposal 
	 * @param content
	 */
	public void acceptProposal(AgentAddress selectedAuthor, Object content) {
		
		if (isInitiator() && (getState() == ContractNetProtocolState.SENDING_ALL_PROPOSAL_ANSWERS)) {
			
			this.selectedParticipants = new ArrayList<AgentAddress>();
			this.selectedParticipants.add(selectedAuthor);
			this.nbExpectedResults = 1;
			
			sendMessage(content, Performative.ACCEPT_PROPOSAL, selectedAuthor);
			
			// sending reject-proposal to all the participants that answers the call for proposal
			for (ProtocolResult proposal : this.proposals) {
				if (proposal.getAuthor() != selectedAuthor) {
					sendMessage(Locale.getString("FipaContractNetProtocol.12"), Performative.REJECT_PROPOSAL, proposal.getAuthor()); //$NON-NLS-1$
				}
			}
			
			setState(ContractNetProtocolState.WAITING_ALL_RESULTS);
		}
		else if (isParticipant()) {
			this.logger.log(Level.SEVERE, Locale.getString("FipaContractNetProtocol.13")); //$NON-NLS-1$
		}
		else {
			this.logger.log(Level.SEVERE, Locale.getString("FipaContractNetProtocol.14")); //$NON-NLS-1$
		}
	}
	
	/**
	 * Accepts the proposal of several participants
	 * Automatically refuse the others proposals
	 * 
	 * @param selectedParticipants the participants of the selected proposal 
	 * @param content
	 */
	public void acceptProposals(List<AgentAddress> selectedParticipants, Object content) {
		
		if (isInitiator() && (getState() == ContractNetProtocolState.SENDING_ALL_PROPOSAL_ANSWERS)) {
			
			this.selectedParticipants = selectedParticipants;
			this.nbExpectedResults = selectedParticipants.size();
			
			for (AgentAddress selectedAuthor : selectedParticipants) {
				sendMessage(content, Performative.ACCEPT_PROPOSAL, selectedAuthor);
			}
			
			// sending reject-proposal to all the participants who have answered to the call for proposal
			for (ProtocolResult proposal : this.proposals) {
				if (!selectedParticipants.contains(proposal.getAuthor())) {
					sendMessage(Locale.getString("FipaContractNetProtocol.15"), Performative.REJECT_PROPOSAL, proposal.getAuthor()); //$NON-NLS-1$
				}
			}
			
			setState(ContractNetProtocolState.WAITING_ALL_RESULTS);
		}
		else if (isParticipant()) {
			this.logger.log(Level.SEVERE, Locale.getString("FipaContractNetProtocol.16")); //$NON-NLS-1$
		}
		else {
			this.logger.log(Level.SEVERE, Locale.getString("FipaContractNetProtocol.17")); //$NON-NLS-1$
		}
	}
	
	/**
	 * Refuses all the proposal made
	 * 
	 * @param content
	 */
	public void rejectAllProposals(Object content) {
		
		if (isInitiator() && (getState() == ContractNetProtocolState.SENDING_ALL_PROPOSAL_ANSWERS)) {	
			for (ProtocolResult proposal : this.proposals) {
				sendMessage(Locale.getString("FipaContractNetProtocol.18"), Performative.REJECT_PROPOSAL, proposal.getAuthor()); //$NON-NLS-1$
			}		
			setFinalStep();
		}
		else if (isParticipant()) {
			this.logger.log(Level.SEVERE, Locale.getString("FipaContractNetProtocol.19")); //$NON-NLS-1$
		}
		else {
			this.logger.log(Level.SEVERE, Locale.getString("FipaContractNetProtocol.20")); //$NON-NLS-1$
		}
	}
	
	/**
	 * Check if the proposal has been accepted or not
	 * 
	 * @return the response of the initiator
	 */
	public ProtocolResult getAnswerToCallForProposal() {
		
		if( this.checkTimeOut && hasReachedTimeout() ){
			addError(Locale.getString("FipaContractNetProtocol.21")); //$NON-NLS-1$
		}
		else{
			if (isParticipant() && (getState() == ContractNetProtocolState.WAITING_PROPOSAL_ANSWER)) {
				
				ACLMessage message = getRefAclAgent().getACLMessageForConversationId(getConversationId());
				
				if (message != null) {
					
					String content = message.getContent().getContent().toString();
					
					if (message.getPerformative().compareTo(Performative.ACCEPT_PROPOSAL) == 0) {
						
						setState(ContractNetProtocolState.SENDING_RESULT);
						resetStartedTime();
						
						return new ProtocolResult(message.getSender(), Performative.ACCEPT_PROPOSAL, content);
					}
					else if (message.getPerformative().compareTo(Performative.REJECT_PROPOSAL) == 0) {
						
						setFinalStep();
						resetStartedTime();
						
						return new ProtocolResult(message.getSender(), Performative.REJECT_PROPOSAL, content);
					}
					else{
						addError(Locale.getString("FipaContractNetProtocol.22")); //$NON-NLS-1$
					}
				}
			}
			else if (isInitiator()) {
				addError(Locale.getString("FipaContractNetProtocol.23")); //$NON-NLS-1$
			}
			else {
				addError(Locale.getString("FipaContractNetProtocol.24")); //$NON-NLS-1$
			}
		}
		return null;
	}
	
	/**
	 * Once the request has been agreed upon, 
	 * then the Participant can communicate a failure 
	 * if it fails in its attempt to fill the request
	 * 
	 * @param content
	 */
	public void failure(Object content){

		if (isParticipant() && (getState() == ContractNetProtocolState.SENDING_RESULT)) {
			sendMessage(content, Performative.FAILURE, getInitiator());
			setFinalStep();
		}
		else if( isInitiator() ){
			addError(Locale.getString("FipaContractNetProtocol.25")); //$NON-NLS-1$
		}
		else{
			addError(Locale.getString("FipaContractNetProtocol.26")); //$NON-NLS-1$
		}
	}
	
	/**
	 * Once the request has been agreed upon, 
	 * then the Participant can communicate an inform-done 
	 * if it successfully completes the request and only wishes to indicate that it is done.
	 * 
	 * @param content
	 */
	public void informDone(Object content){

		if( isParticipant() && ( getState() == ContractNetProtocolState.SENDING_RESULT ) ){
			sendMessage(content, Performative.INFORM, getInitiator());
			setFinalStep();
		}
		else if( isInitiator() ){
			addError(Locale.getString("FipaContractNetProtocol.27")); //$NON-NLS-1$
		}
		else{
			addError( Locale.getString("FipaContractNetProtocol.28") + getState() ); //$NON-NLS-1$
			addError(Locale.getString("FipaContractNetProtocol.29")); //$NON-NLS-1$
		}
	}
	
	/**
	 * Once the request has been agreed upon, 
	 * then the Participant can communicate an inform-result 
	 * if it wishes to indicate both that it is done and notify the initiator of the results.
	 * 
	 * @param content
	 */
	public void informResult(Object content){

		if( isParticipant() && ( getState() == ContractNetProtocolState.SENDING_RESULT ) ){
			sendMessage(content, Performative.INFORM, getInitiator());
			setFinalStep();
		}
		else if( isInitiator() ){
			addError(Locale.getString("FipaContractNetProtocol.30")); //$NON-NLS-1$
		}
		else{
			addError(Locale.getString("FipaContractNetProtocol.31")); //$NON-NLS-1$
		}
	}
	
	/**
	 * 
	 * @return the notification (Performative + Content)
	 * 
	 * @see #failure(Object)
	 * @see #informDone(Object)
	 */
	public List<ProtocolResult> getResults() {

		if( this.checkTimeOut && hasReachedTimeout() ){
			addError(Locale.getString("FipaContractNetProtocol.32")); //$NON-NLS-1$
		}
		else {
			if( isInitiator() && (getState() == ContractNetProtocolState.WAITING_ALL_RESULTS) || getState() == ContractNetProtocolState.CANCELING) {  
	
				if (this.results == null) {
					this.results = new ArrayList<ProtocolResult>();
				}
				
				ACLMessage aMsg = getRefAclAgent().getACLMessageForConversationId( getConversationId() );
				
				if (aMsg != null) {
					ProtocolResult result = new ProtocolResult();
					result.setPerformative( aMsg.getPerformative() );
					result.setContent( aMsg.getContent().getContent() );
					result.setAuthor(aMsg.getSender());
					this.results.add(result);
					this.nbExpectedResults--;					
				}
				
				if (this.nbExpectedResults == 0) {
					setFinalStep();
					resetStartedTime();
					return this.results;
				}
				
				return null;
			}
			else if(isParticipant()) {
				addError(Locale.getString("FipaContractNetProtocol.33")); //$NON-NLS-1$
			}
			else{
				addError(Locale.getString("FipaContractNetProtocol.34")); //$NON-NLS-1$
			}
		}
		
		return null;
	}
	
	// ----------------------------
	// Exceptions to Protocol Flow
	// ----------------------------
	
	/**
	 * At any point in the IP, the initiator of the IP may cancel the interaction protocol.
	 * 
	 * The semantics of cancel should roughly be interpreted 
	 * as meaning that the initiator is no longer interested in continuing the interaction 
	 * and that it should be terminated in a manner acceptable to both the Initiator and the Participant. 
	 * 
	 * The Participant either informs the Initiator that the interaction 
	 * is done using an inform-done or indicates the failure of the cancellation using a failure.
	 * 
	 * @param content
	 */
	public void cancel(Object content){
		
		if (isInitiator() && (getState() != ContractNetProtocolState.DONE) && (getState() != ContractNetProtocolState.CANCELED)
				&& (getState() != ContractNetProtocolState.NOT_STARTED)) 
		{
			sendMessage(content, Performative.CANCEL, getParticipants().get(0));
			setState(ContractNetProtocolState.CANCELING);
		}
		else if (isParticipant()) {
			addError(Locale.getString("FipaContractNetProtocol.35")); //$NON-NLS-1$
		}
		else{
			addError(Locale.getString("FipaContractNetProtocol.36")); //$NON-NLS-1$
		}
	}
	
	/**
	 * At any point in the IP, the receiver of a communication 
	 * can inform the sender that he did not understand what was communicated.
	 * 
	 * The communication of a not-understood within an interaction protocol may terminate the entire IP 
	 * and termination of the interaction may imply that any commitments made during the interaction 
	 * are null and void.
	 * 
	 * @param content
	 */
	public void notUnderstood(Object content) {
		
		if( isParticipant() && (getState() != ContractNetProtocolState.DONE) && (getState() != ContractNetProtocolState.CANCELED)
				&& (getState() != ContractNetProtocolState.NOT_STARTED)) 
		{
			sendMessage(content, Performative.NOT_UNDERSTOOD, getInitiator());
			setFinalStep();
		}
		else if( isInitiator() ){
			addError(Locale.getString("FipaContractNetProtocol.37")); //$NON-NLS-1$
		}
		else{
			addError(Locale.getString("FipaContractNetProtocol.38")); //$NON-NLS-1$
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
	protected final void sendMessage(Object content, Performative performative, AgentAddress to) {

		ACLMessage message = new ACLMessage(content, performative);
		
		message.setProtocol(EnumFipaProtocol.FIPA_CONTRACT_NET);
		message.setConversationID( getConversationId() );
		
		getRefAclAgent().sendACLMessage(message, to);
		
		//System.out.println("\n=> MESSAGE envoyé via CNP PROTOCOL par " + getRefAclAgent().getName() + ": \n" + message.toString() );
	}

	@Override
	protected void setFinalStep() {
		this.setState(ContractNetProtocolState.DONE);
	}

	/** Replies if the timeouts are testing during the procotol execution.
	 * 
	 * @return <code>true</code> if the timeouts are checked;
	 * <code>false</code> otherwise.
	 */
	public boolean isCheckTimeOut() {
		return this.checkTimeOut;
	}

	/** Enable or disable the checking of the timeouts during
	 * the protocol execution.
	 * 
	 * @param checkTimeOut is <code>true</code> for enabling;
	 * <code>false</code> for disabling.
	 */
	public void setCheckTimeOut(boolean checkTimeOut) {
		this.checkTimeOut = checkTimeOut;
	}
}

