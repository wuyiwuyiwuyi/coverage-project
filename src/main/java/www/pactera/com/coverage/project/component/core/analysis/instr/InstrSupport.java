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
package www.pactera.com.coverage.project.component.core.analysis.instr;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static java.lang.String.format;


public final class InstrSupport {

	private InstrSupport() {
	}


	public static final int ASM_API_VERSION = Opcodes.ASM8;

	// === Data Field ===


	public static final String DATAFIELD_NAME = "$jacocoData";


	public static final int DATAFIELD_ACC = Opcodes.ACC_SYNTHETIC
			| Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_TRANSIENT;


	public static final int DATAFIELD_INTF_ACC = Opcodes.ACC_SYNTHETIC
			| Opcodes.ACC_PUBLIC | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL;


	public static final String DATAFIELD_DESC = "[Z";

	// === Init Method ===


	public static final String INITMETHOD_NAME = "$jacocoInit";


	public static final String INITMETHOD_DESC = "()[Z";


	public static final int INITMETHOD_ACC = Opcodes.ACC_SYNTHETIC
			| Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC;


	static final String CLINIT_NAME = "<clinit>";


	static final String CLINIT_DESC = "()V";


	static final int CLINIT_ACC = Opcodes.ACC_SYNTHETIC | Opcodes.ACC_STATIC;


	public static int getMajorVersion(final byte[] b) {
		return ((b[6] & 0xFF) << 8) | (b[7] & 0xFF);
	}


	public static void setMajorVersion(final int majorVersion, final byte[] b) {
		b[6] = (byte) (majorVersion >>> 8);
		b[7] = (byte) majorVersion;
	}


	public static int getMajorVersion(final ClassReader reader) {
		// relative to the beginning of constant pool because ASM provides API
		// to construct ClassReader which reads from the middle of array
		final int firstConstantPoolEntryOffset = reader.getItem(1) - 1;
		return reader.readUnsignedShort(firstConstantPoolEntryOffset - 4);
	}


	public static boolean needsFrames(final int version) {
		// consider major version only (due to 1.1 anomaly)
		return (version & 0xFFFF) >= Opcodes.V1_6;
	}


	public static void assertNotInstrumented(final String member,
			final String owner) throws IllegalStateException {
		if (member.equals(DATAFIELD_NAME) || member.equals(INITMETHOD_NAME)) {
			throw new IllegalStateException(format(
					"Cannot process instrumented class %s. Please supply original non-instrumented classes.",
					owner));
		}
	}


	public static void push(final MethodVisitor mv, final int value) {
		if (value >= -1 && value <= 5) {
			mv.visitInsn(Opcodes.ICONST_0 + value);
		} else if (value >= Byte.MIN_VALUE && value <= Byte.MAX_VALUE) {
			mv.visitIntInsn(Opcodes.BIPUSH, value);
		} else if (value >= Short.MIN_VALUE && value <= Short.MAX_VALUE) {
			mv.visitIntInsn(Opcodes.SIPUSH, value);
		} else {
			mv.visitLdcInsn(Integer.valueOf(value));
		}
	}


	public static ClassReader classReaderFor(final byte[] b) {
		final int originalVersion = getMajorVersion(b);
		if (originalVersion == Opcodes.V15 + 1) {
			// temporarily downgrade version to bypass check in ASM
			setMajorVersion(Opcodes.V15, b);
		}
		final ClassReader classReader = new ClassReader(b);
		setMajorVersion(originalVersion, b);
		return classReader;
	}

}
