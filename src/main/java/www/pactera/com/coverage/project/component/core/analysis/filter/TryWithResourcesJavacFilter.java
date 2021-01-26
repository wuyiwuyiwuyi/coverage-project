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
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TryCatchBlockNode;
import www.pactera.com.coverage.project.component.core.analysis.IFilter;
import www.pactera.com.coverage.project.component.core.analysis.IFilterContext;
import www.pactera.com.coverage.project.component.core.analysis.IFilterOutput;

public final class TryWithResourcesJavacFilter implements IFilter {

	public void filter(final MethodNode methodNode,
					   final IFilterContext context, final IFilterOutput output) {
		if (methodNode.tryCatchBlocks.isEmpty()) {
			return;
		}
		final Matcher matcher = new Matcher(output);
		for (final TryCatchBlockNode t : methodNode.tryCatchBlocks) {
			if ("java/lang/Throwable".equals(t.type)) {
				for (final Matcher.JavacPattern p : Matcher.JavacPattern
						.values()) {
					matcher.start(t.handler);
					if (matcher.matchJavac(p)) {
						break;
					}
				}
			}
		}
	}


	static class Matcher extends AbstractMatcher {
		private final IFilterOutput output;

		private String expectedOwner;

		private AbstractInsnNode start;

		Matcher(final IFilterOutput output) {
			this.output = output;
		}

		private enum JavacPattern {
			OPTIMAL,
			FULL,
			OMITTED_NULL_CHECK,
			METHOD,
		}

		private void start(final AbstractInsnNode start) {
			this.start = start;
			cursor = start.getPrevious();
			vars.clear();
			expectedOwner = null;
		}

		private boolean matchJavac(final JavacPattern p) {
			// "catch (Throwable t)"
			nextIsVar(Opcodes.ASTORE, "t1");
			// "primaryExc = t"
			nextIsVar(Opcodes.ALOAD, "t1");
			nextIsVar(Opcodes.ASTORE, "primaryExc");
			// "throw t"
			nextIsVar(Opcodes.ALOAD, "t1");
			nextIs(Opcodes.ATHROW);

			// "catch (any t)"
			nextIsVar(Opcodes.ASTORE, "t2");
			nextIsJavacClose(p, "e");
			// "throw t"
			nextIsVar(Opcodes.ALOAD, "t2");
			nextIs(Opcodes.ATHROW);
			if (cursor == null) {
				return false;
			}
			final AbstractInsnNode end = cursor;

			AbstractInsnNode startOnNonExceptionalPath = start.getPrevious();
			cursor = startOnNonExceptionalPath;
			while (!nextIsJavacClose(p, "n")) {
				startOnNonExceptionalPath = startOnNonExceptionalPath
						.getPrevious();
				cursor = startOnNonExceptionalPath;
				if (cursor == null) {
					return false;
				}
			}
			startOnNonExceptionalPath = startOnNonExceptionalPath.getNext();

			final AbstractInsnNode m = cursor;
			next();
			if (cursor.getOpcode() != Opcodes.GOTO) {
				cursor = m;
			}

			output.ignore(startOnNonExceptionalPath, cursor);
			output.ignore(start, end);
			return true;
		}

		private boolean nextIsJavacClose(final JavacPattern p,
				final String ctx) {
			switch (p) {
			case METHOD:
			case FULL:
				// "if (r != null)"
				nextIsVar(Opcodes.ALOAD, "r");
				nextIs(Opcodes.IFNULL);
			}
			switch (p) {
			case METHOD:
			case OPTIMAL:
				nextIsVar(Opcodes.ALOAD, "primaryExc");
				nextIsVar(Opcodes.ALOAD, "r");
				nextIs(Opcodes.INVOKESTATIC);
				if (cursor != null) {
					final MethodInsnNode m = (MethodInsnNode) cursor;
					if ("$closeResource".equals(m.name)
							&& "(Ljava/lang/Throwable;Ljava/lang/AutoCloseable;)V"
									.equals(m.desc)) {
						return true;
					}
					cursor = null;
				}
				return false;
			case FULL:
			case OMITTED_NULL_CHECK:
				nextIsVar(Opcodes.ALOAD, "primaryExc");
				// "if (primaryExc != null)"
				nextIs(Opcodes.IFNULL);
				// "r.close()"
				nextIsClose();
				nextIs(Opcodes.GOTO);
				// "catch (Throwable t)"
				nextIsVar(Opcodes.ASTORE, ctx + "t");
				// "primaryExc.addSuppressed(t)"
				nextIsVar(Opcodes.ALOAD, "primaryExc");
				nextIsVar(Opcodes.ALOAD, ctx + "t");
				nextIsInvoke(Opcodes.INVOKEVIRTUAL, "java/lang/Throwable",
						"addSuppressed", "(Ljava/lang/Throwable;)V");
				nextIs(Opcodes.GOTO);
				// "r.close()"
				nextIsClose();
				return cursor != null;
			default:
				throw new AssertionError();
			}
		}

		private void nextIsClose() {
			nextIsVar(Opcodes.ALOAD, "r");
			next();
			if (cursor == null) {
				return;
			}
			if (cursor.getOpcode() != Opcodes.INVOKEINTERFACE
					&& cursor.getOpcode() != Opcodes.INVOKEVIRTUAL) {
				cursor = null;
				return;
			}
			final MethodInsnNode m = (MethodInsnNode) cursor;
			if (!"close".equals(m.name) || !"()V".equals(m.desc)) {
				cursor = null;
				return;
			}
			final String actual = m.owner;
			if (expectedOwner == null) {
				expectedOwner = actual;
			} else if (!expectedOwner.equals(actual)) {
				cursor = null;
			}
		}

	}

}
