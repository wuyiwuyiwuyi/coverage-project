/*******************************************************************************
 * Copyright (c) 2009, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Evgeny Mandrikov - initial API and implementation
 *
 *******************************************************************************/
package www.pactera.com.coverage.project.component.core.analysis.filter;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractMatcher {

	final Map<String, VarInsnNode> vars = new HashMap<String, VarInsnNode>();

	AbstractInsnNode cursor;

	final void firstIsALoad0(final MethodNode methodNode) {
		cursor = methodNode.instructions.getFirst();
		skipNonOpcodes();
		if (cursor != null && cursor.getOpcode() == Opcodes.ALOAD
				&& ((VarInsnNode) cursor).var == 0) {
			return;
		}
		cursor = null;
	}

	final void nextIsType(final int opcode, final String desc) {
		nextIs(opcode);
		if (cursor == null) {
			return;
		}
		if (((TypeInsnNode) cursor).desc.equals(desc)) {
			return;
		}
		cursor = null;
	}

	final void nextIsInvoke(final int opcode, final String owner,
			final String name, final String descriptor) {
		nextIs(opcode);
		if (cursor == null) {
			return;
		}
		final MethodInsnNode m = (MethodInsnNode) cursor;
		if (owner.equals(m.owner) && name.equals(m.name)
				&& descriptor.equals(m.desc)) {
			return;
		}
		cursor = null;
	}

	final void nextIsVar(final int opcode, final String name) {
		nextIs(opcode);
		if (cursor == null) {
			return;
		}
		final VarInsnNode actual = (VarInsnNode) cursor;
		final VarInsnNode expected = vars.get(name);
		if (expected == null) {
			vars.put(name, actual);
		} else if (expected.var != actual.var) {
			cursor = null;
		}
	}

	final void nextIsSwitch() {
		next();
		if (cursor == null) {
			return;
		}
		switch (cursor.getOpcode()) {
		case Opcodes.TABLESWITCH:
		case Opcodes.LOOKUPSWITCH:
			return;
		default:
			cursor = null;
		}
	}

	final void nextIs(final int opcode) {
		next();
		if (cursor == null) {
			return;
		}
		if (cursor.getOpcode() != opcode) {
			cursor = null;
		}
	}

	final void next() {
		if (cursor == null) {
			return;
		}
		cursor = cursor.getNext();
		skipNonOpcodes();
	}

	final void skipNonOpcodes() {
		cursor = skipNonOpcodes(cursor);
	}

	static AbstractInsnNode skipNonOpcodes(AbstractInsnNode cursor) {
		while (cursor != null && (cursor.getType() == AbstractInsnNode.FRAME
				|| cursor.getType() == AbstractInsnNode.LABEL
				|| cursor.getType() == AbstractInsnNode.LINE)) {
			cursor = cursor.getNext();
		}
		return cursor;
	}

}
