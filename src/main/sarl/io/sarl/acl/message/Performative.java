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
package io.sarl.acl.message;

/**
 * This enumeration describes all available performatives as defined by FIPA
 * 
 * @see <a href="http://www.fipa.org/specs/fipa00037/SC00037J.html">FIPA
 *      Communicative Act Library Specification</a>
 * 
 * @author $Author: ngaud$
 * @author $Author: sgalland$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */
public enum Performative {
	/**
	 */
	NONE("none"), //$NON-NLS-1$
	/**
	 */
	ACCEPT_PROPOSAL("accept-proposal"), //$NON-NLS-1$
	/**
	 */
	AGREE("agree"), //$NON-NLS-1$
	/**
	 */
	CANCEL("cancel"), //$NON-NLS-1$
	/**
	 */
	CFP("cfp"), //$NON-NLS-1$
	/**
	 */
	CONFIRM("confirm"), //$NON-NLS-1$
	/**
	 */
	DISCONFIRM("disconfirm"), //$NON-NLS-1$
	/**
	 */
	FAILURE("failure"), //$NON-NLS-1$
	/**
	 */
	INFORM("inform"), //$NON-NLS-1$
	/**
	 */
	INFORM_IF("inform-if"), //$NON-NLS-1$
	/**
	 */
	INFORM_REF("inform-ref"), //$NON-NLS-1$
	/**
	 */
	NOT_UNDERSTOOD("not-understood"), //$NON-NLS-1$
	/**
	 */
	PROPOSE("propose"), //$NON-NLS-1$  
	/**
	 */
	QUERY_IF("query-if"), //$NON-NLS-1$
	/**
	 */
	QUERY_REF("query-ref"), //$NON-NLS-1$ 
	/**
	 */
	REFUSE("refuse"), //$NON-NLS-1$
	/**
	 */
	REJECT_PROPOSAL("reject-proposal"), //$NON-NLS-1$
	/**
	 */
	REQUEST("request"), //$NON-NLS-1$ 
	/**
	 */
	REQUEST_WHEN("request-when"), //$NON-NLS-1$
	/**
	 */
	REQUEST_WHENEVER("request-whenever"), //$NON-NLS-1$
	/**
	 */
	SUBSCRIBE("subscribe"), //$NON-NLS-1$
	/**
	 */
	PROXY("proxy"), //$NON-NLS-1$ 
	/**
	 */
	PROPAGATE("propagate"); //$NON-NLS-1$

	private final String name;

	Performative(String name) {
		this.name = name;
	}

	/**
	 * @return the string name of the performative
	 */
	public String getFipaName() {
		return this.name;
	}

	/**
	 * @param name
	 * @return the field of the performative enum corresponding to the specified
	 *         string
	 * @throws IllegalArgumentException
	 */
	public static Performative valueOfByName(String name) {
		for (Performative value : values()) {
			if (value.getFipaName().equalsIgnoreCase(name)) {
				return value;
			}
		}
		return Performative.NONE;
	}

	/**
	 * @param ordinal
	 * @return the field of the performative enum corresponding to the specified
	 *         ordinal
	 * @throws IllegalArgumentException
	 */
	public static Performative valueOfByOrdinal(Integer ordinal) {
		if (ordinal != null) {
			int index = ordinal.intValue();
			return values()[index];
		}
		return Performative.NONE;
	}

}
