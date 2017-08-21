/*
 * $Id$
 *
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 *
 * Copyright (C) 2014-2017 the original authors or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.sarl.acl.encoding;

/**
 * This enumeration corresponds to the different type of encoding (UTF-8, ISO, ASCII...)
 * 
 * @author $Author: sgalland$
 * @author $Author: ngaud$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */
public enum PayloadEncoding {
	
	/**
	 * UTF8 Enconding, the defualt one
	 */
	UTF8("UTF-8"); //$NON-NLS-1$
	
	private final String value;
	
	PayloadEncoding(String value) {
		this.value = value;
	}

	/**
	 * @return the string value associated to an enum case
	 */
	public String getValue() {
		return this.value;
	}
	
	
}
