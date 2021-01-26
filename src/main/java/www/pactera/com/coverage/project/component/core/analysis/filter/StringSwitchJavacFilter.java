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
import www.pactera.com.coverage.project.component.core.analysis.IFilter;
import www.pactera.com.coverage.project.component.core.analysis.IFilterContext;
import www.pactera.com.coverage.project.component.core.analysis.IFilterOutput;

public final class StringSwitchJavacFilter implements IFilter {

	public void filter(final MethodNode methodNode,
					   final IFilterContext context, final IFilterOutput output) {
		for (final AbstractInsnNode i : methodNode.instructions) {
			filter(i, output);
		}
	}

	private void filter(final AbstractInsnNode start,
			final IFilterOutput output) {
		final LabelNode dflt;
		if (start.getOpcode() == Opcodes.LOOKUPSWITCH) {
			dflt = ((LookupSwitchInsnNode) start).dflt;
		} else if (start.getOpcode() == Opcodes.TABLESWITCH) {
			dflt = ((TableSwitchInsnNode) start).dflt;
		} else {
			return;
		}
		if (new Matcher().match(start, dflt)) {
			output.ignore(start, dflt);
		}
	}

	private static class Matcher extends AbstractMatcher {
		boolean match(final AbstractInsnNode start,
				final AbstractInsnNode secondSwitchLabel) {
			cursor = start;
			for (int i = 0; cursor != null && i < 4; i++) {
				cursor = cursor.getPrevious();
			}
			if (cursor == null || cursor.getOpcode() != Opcodes.ICONST_M1) {
				return false;
			}
			nextIsVar(Opcodes.ISTORE, "c");
			// Even if expression is not a variable, its result will be
			// precomputed before the previous two instructions:
			nextIsVar(Opcodes.ALOAD, "s");
			nextIsInvoke(Opcodes.INVOKEVIRTUAL, "java/lang/String", "hashCode",
					"()I");
			next();
			while (true) {
				nextIsVar(Opcodes.ALOAD, "s");
				nextIs(Opcodes.LDC);
				nextIsInvoke(Opcodes.INVOKEVIRTUAL, "java/lang/String",
						"equals", "(Ljava/lang/Object;)Z");
				// jump to next comparison or second switch
				nextIs(Opcodes.IFEQ);
				// ICONST, BIPUSH or SIPUSH
				next();
				nextIsVar(Opcodes.ISTORE, "c");
				if (cursor == null) {
					return false;
				}
				if (cursor.getNext() == secondSwitchLabel) {
					break;
				}
				nextIs(Opcodes.GOTO);
				if (((JumpInsnNode) cursor).label != secondSwitchLabel) {
					return false;
				}
			}
			nextIsVar(Opcodes.ILOAD, "c");
			// Can be TABLESWITCH or LOOKUPSWITCH depending on number of cases
			nextIsSwitch();
			return cursor != null;
		}
	}

}
