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
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import www.pactera.com.coverage.project.component.core.analysis.IFilter;
import www.pactera.com.coverage.project.component.core.analysis.IFilterContext;
import www.pactera.com.coverage.project.component.core.analysis.IFilterOutput;

import java.util.HashSet;
import java.util.Set;

public final class KotlinDefaultArgumentsFilter implements IFilter {

	static boolean isDefaultArgumentsMethod(final MethodNode methodNode) {
		return methodNode.name.endsWith("$default");
	}

	static boolean isDefaultArgumentsConstructor(final MethodNode methodNode) {
		if (!"<init>".equals(methodNode.name)) {
			return false;
		}
		final Type[] argumentTypes = Type.getMethodType(methodNode.desc)
				.getArgumentTypes();
		if (argumentTypes.length < 2) {
			return false;
		}
		return "kotlin.jvm.internal.DefaultConstructorMarker"
				.equals(argumentTypes[argumentTypes.length - 1].getClassName());
	}

	public void filter(final MethodNode methodNode,
					   final IFilterContext context, final IFilterOutput output) {
		if ((methodNode.access & Opcodes.ACC_SYNTHETIC) == 0) {
			return;
		}
		if (!KotlinGeneratedFilter.isKotlinClass(context)) {
			return;
		}

		if (isDefaultArgumentsMethod(methodNode)) {
			new Matcher().match(methodNode, output, false);
		} else if (isDefaultArgumentsConstructor(methodNode)) {
			new Matcher().match(methodNode, output, true);
		}
	}

	private static class Matcher extends AbstractMatcher {
		public void match(final MethodNode methodNode,
				final IFilterOutput output, final boolean constructor) {
			cursor = methodNode.instructions.getFirst();

			nextIs(Opcodes.IFNULL);
			nextIsType(Opcodes.NEW, "java/lang/UnsupportedOperationException");
			nextIs(Opcodes.DUP);
			nextIs(Opcodes.LDC);
			if (cursor == null
					|| !(((LdcInsnNode) cursor).cst instanceof String)
					|| !(((String) ((LdcInsnNode) cursor).cst).startsWith(
							"Super calls with default arguments not supported in this target"))) {
				cursor = null;
			}
			nextIsInvoke(Opcodes.INVOKESPECIAL,
					"java/lang/UnsupportedOperationException", "<init>",
					"(Ljava/lang/String;)V");
			nextIs(Opcodes.ATHROW);
			if (cursor != null) {
				output.ignore(methodNode.instructions.getFirst(), cursor);
				next();
			} else {
				cursor = methodNode.instructions.getFirst();
			}

			final Set<AbstractInsnNode> ignore = new HashSet<AbstractInsnNode>();
			final int maskVar = maskVar(methodNode.desc, constructor);
			while (true) {
				if (cursor.getOpcode() != Opcodes.ILOAD) {
					break;
				}
				if (((VarInsnNode) cursor).var != maskVar) {
					break;
				}
				next();
				nextIs(Opcodes.IAND);
				nextIs(Opcodes.IFEQ);
				if (cursor == null) {
					return;
				}
				ignore.add(cursor);
				cursor = ((JumpInsnNode) cursor).label;
				skipNonOpcodes();
			}

			for (AbstractInsnNode i : ignore) {
				output.ignore(i, i);
			}
		}

		private static int maskVar(final String desc,
				final boolean constructor) {
			int slot = 0;
			if (constructor) {
				// one slot for reference to current object
				slot++;
			}
			final Type[] argumentTypes = Type.getMethodType(desc)
					.getArgumentTypes();
			final int penultimateArgument = argumentTypes.length - 2;
			for (int i = 0; i < penultimateArgument; i++) {
				slot += argumentTypes[i].getSize();
			}
			return slot;
		}
	}

}
