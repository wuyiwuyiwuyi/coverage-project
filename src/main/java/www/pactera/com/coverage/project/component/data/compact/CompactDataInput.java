/*******************************************************************************
 * Copyright (c) 2009, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package www.pactera.com.coverage.project.component.data.compact;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;


public class CompactDataInput extends DataInputStream {

	public CompactDataInput(final InputStream in) {
		super(in);
	}


	public int readVarInt() throws IOException {
		final int value = 0xFF & readByte();
		if ((value & 0x80) == 0) {
			return value;
		}
		return (value & 0x7F) | (readVarInt() << 7);
	}

	public boolean[] readBooleanArray() throws IOException {
		final boolean[] value = new boolean[readVarInt()];
		int buffer = 0;
		for (int i = 0; i < value.length; i++) {
			if ((i % 8) == 0) {
				buffer = readByte();
			}
			value[i] = (buffer & 0x01) != 0;
			buffer >>>= 1;
		}
		return value;
	}

}
