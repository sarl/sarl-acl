/* 
 * $Id$
 * 
 * SARL is an general-purpose agent programming language.
 * More details on http://www.sarl.io
 * 
 * Copyright (C) 2014-2021 the original authors or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.sarl.acl.encoding.bitefficient.constant;

/**
 * This enumeration describes all available constant for ExprEnd as defined by FIPA for Bit-Efficient encoding, 
 * and their setter (used for decoding process - java reflection tips)
 * 
 * @see <a href="http://www.fipa.org/specs/fipa00069/SC00069G.html">FIPA ACL Message Representation in Bit-Efficient Specification</a> 
 * 
 * @author $Author: flacreus$
 * @author $Author: sroth$
 * @author $Author: cstentz$
 * @version $FullVersion$
 * @mavengroupid $Groupid$
 * @mavenartifactid $ArtifactId$
 */

public enum ExprEnd {

	/** Level up.
	 * <p>
	 * Code: {@code 0x40}.
	 */
	LEVEL_UP((byte) 0x40),
	/** Begin a word.
	 * <p>
	 * Code: {@code 0x50}.
	 */
	WORD_BEGIN((byte) 0x50),
	/** End a word.
	 * <p>
	 * Code: {@code 0x00}.
	 */
	WORD_END((byte) 0x00),
	/** Begin a code for word index.
	 * <p>
	 * Code: {@code 0x51}.
	 */
	INDEX_WORD_CODE_BEGIN((byte) 0x51),
	/** Begin a number.
	 * <p>
	 * Code: {@code 0x52}.
	 */
	NUMBER_BEGIN((byte) 0x52),
	/** Begin an hexadecimal number.
	 * <p>
	 * Code: {@code 0x53}.
	 */
	HEXA_NUMBER_BEGIN((byte) 0x53),
	/** Begin a string.
	 * <p>
	 * Code: {@code 0x54}.
	 */
	STRING_BEGIN((byte) 0x54),
	/** End a word.
	 * <p>
	 * Code: {@code 0x00}.
	 */
	STRING_END((byte) 0x00),
	/** Begin a index string.
	 * <p>
	 * Code: {@code 0x55}.
	 */
	INDEX_STRING_BEGIN((byte) 0x55),
	/** Begin a len8 string.
	 * <p>
	 * Code: {@code 0x56}.
	 */
	LEN8_STRING_BEGIN((byte) 0x56),
	/** Begin a len16 string.
	 * <p>
	 * Code: {@code 0x57}.
	 */
	LEN16_STRING_BEGIN((byte) 0x57),
	/** Begin a len32 string.
	 * <p>
	 * Code: {@code 0x58}.
	 */
	LEN32_STRING_BEGIN((byte) 0x58),
	/** Begin a string for index byte.
	 * <p>
	 * Code: {@code 0x56}.
	 */
	INDEX_BYTE_STRING_BEGIN((byte) 0x56);
	
	private final byte code;
	
	private ExprEnd(byte code){
		this.code = code;
	}
	
	/** Replies the code from the FIPA specification.
	 * 
	 * @return the code.
	 */
	public byte getCode() {
		return this.code;
	}
	

}
