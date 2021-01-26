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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class CompactDataOutput extends DataOutputStream {


	public CompactDataOutput(final OutputStream out) {
		super(out);
	}

	public void writeVarInt(final int value) throws IOException {
		if ((value & 0xFFFFFF80) == 0) {
			writeByte(value);
		} else {
			writeByte(0x80 | (value & 0x7F));
			writeVarInt(value >>> 7);
		}
	}

	public void writeBooleanArray(final boolean[] value) throws IOException {
		writeVarInt(value.length);
		int buffer = 0;
		int bufferSize = 0;
		for (final boolean b : value) {
			if (b) {
				buffer |= 0x01 << bufferSize;
			}
			if (++bufferSize == 8) {
				writeByte(buffer);
				buffer = 0;
				bufferSize = 0;
			}
		}
		if (bufferSize > 0) {
			writeByte(buffer);
		}
	}

}
