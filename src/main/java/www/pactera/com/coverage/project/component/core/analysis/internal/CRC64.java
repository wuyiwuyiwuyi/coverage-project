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
package www.pactera.com.coverage.project.component.core.analysis.internal;

import org.objectweb.asm.Opcodes;


public final class CRC64 {

	private static final long POLY64REV = 0xd800000000000000L;

	private static final long[] LOOKUPTABLE;

	static {
		LOOKUPTABLE = new long[0x100];
		for (int i = 0; i < 0x100; i++) {
			long v = i;
			for (int j = 0; j < 8; j++) {
				if ((v & 1) == 1) {
					v = (v >>> 1) ^ POLY64REV;
				} else {
					v = (v >>> 1);
				}
			}
			LOOKUPTABLE[i] = v;
		}
	}


	private static long update(final long sum, final byte b) {
		final int lookupidx = ((int) sum ^ b) & 0xff;
		return (sum >>> 8) ^ LOOKUPTABLE[lookupidx];
	}


	private static long update(long sum, final byte[] bytes,
			final int fromIndexInclusive, final int toIndexExclusive) {
		for (int i = fromIndexInclusive; i < toIndexExclusive; i++) {
			sum = update(sum, bytes[i]);
		}
		return sum;
	}


	public static long classId(final byte[] bytes) {
		if (bytes.length > 7 && bytes[6] == 0x00 && bytes[7] == Opcodes.V9) {
			// To support early versions of Java 9 we did a trick - change of
			// Java 9 class files version on Java 8. Unfortunately this also
			// affected class identifiers.
			long sum = update(0, bytes, 0, 7);
			sum = update(sum, (byte) Opcodes.V1_8);
			return update(sum, bytes, 8, bytes.length);
		}
		return update(0, bytes, 0, bytes.length);
	}

	private CRC64() {
	}

}
