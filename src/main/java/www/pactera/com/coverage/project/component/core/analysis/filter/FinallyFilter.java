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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class FinallyFilter implements IFilter {

	public void filter(final MethodNode methodNode,
					   final IFilterContext context, final IFilterOutput output) {
		for (final TryCatchBlockNode tryCatchBlock : methodNode.tryCatchBlocks) {
			if (tryCatchBlock.type == null) {
				filter(output, methodNode.tryCatchBlocks, tryCatchBlock);
			}
		}
	}

	private static void filter(final IFilterOutput output,
			final List<TryCatchBlockNode> tryCatchBlocks,
			final TryCatchBlockNode catchAnyBlock) {
		final AbstractInsnNode e = next(catchAnyBlock.handler);
		final int size = size(e);
		if (size <= 0) {
			return;
		}

		// Determine instructions inside regions
		final Set<AbstractInsnNode> inside = new HashSet<AbstractInsnNode>();
		for (final TryCatchBlockNode t : tryCatchBlocks) {
			if (t.handler == catchAnyBlock.handler) {
				AbstractInsnNode i = t.start;
				while (i != t.end) {
					inside.add(i);
					i = i.getNext();
				}
			}
		}

		// Find and merge duplicates at exits of regions
		for (final TryCatchBlockNode t : tryCatchBlocks) {
			if (t.handler == catchAnyBlock.handler) {
				boolean continues = false;
				AbstractInsnNode i = t.start;

				while (i != t.end) {
					switch (i.getType()) {
					case AbstractInsnNode.FRAME:
					case AbstractInsnNode.LINE:
					case AbstractInsnNode.LABEL:
						break;
					case AbstractInsnNode.JUMP_INSN:
						final AbstractInsnNode jumpTarget = next(
								((JumpInsnNode) i).label);
						if (!inside.contains(jumpTarget)) {
							merge(output, size, e, jumpTarget);
						}
						continues = i.getOpcode() != Opcodes.GOTO;
						break;
					default:
						switch (i.getOpcode()) {
						case Opcodes.IRETURN:
						case Opcodes.LRETURN:
						case Opcodes.FRETURN:
						case Opcodes.DRETURN:
						case Opcodes.ARETURN:
						case Opcodes.RETURN:
						case Opcodes.ATHROW:
							continues = false;
							break;
						default:
							continues = true;
							break;
						}
						break;
					}
					i = i.getNext();
				}

				i = next(i);
				if (continues && !inside.contains(i)) {
					merge(output, size, e, i);
				}
			}

			if (t != catchAnyBlock && t.start == catchAnyBlock.start
					&& t.end == catchAnyBlock.end) {
				final AbstractInsnNode i = next(next(t.handler));
				if (!inside.contains(i)) {
					// javac's empty catch - merge after ASTORE
					merge(output, size, e, i);
				}
			}
		}
	}

	private static void merge(final IFilterOutput output, final int size,
			AbstractInsnNode e, AbstractInsnNode n) {
		if (!isSame(size, e, n)) {
			return;
		}
		output.ignore(e, e);
		e = next(e);
		for (int i = 0; i < size; i++) {
			output.merge(e, n);
			e = next(e);
			n = next(n);
		}
		output.ignore(e, next(e));

		if (n != null && n.getOpcode() == Opcodes.GOTO) {
			// goto instructions at the end of non-executed duplicates
			// cause partial coverage of last line of finally block,
			// so should be ignored
			output.ignore(n, n);
		}
	}

	private static boolean isSame(final int size, AbstractInsnNode e,
			AbstractInsnNode n) {
		e = next(e);
		for (int i = 0; i < size; i++) {
			if (n == null || e.getOpcode() != n.getOpcode()) {
				return false;
			}
			e = next(e);
			n = next(n);
		}
		return true;
	}

	private static int size(AbstractInsnNode i) {
		if (Opcodes.ASTORE != i.getOpcode()) {
			// when always completes abruptly
			return 0;
		}
		final int var = ((VarInsnNode) i).var;
		int size = -1;
		do {
			size++;
			i = next(i);
			if (i == null) {
				// when always completes abruptly
				return 0;
			}
		} while (!(Opcodes.ALOAD == i.getOpcode()
				&& var == ((VarInsnNode) i).var));
		i = next(i);
		if (Opcodes.ATHROW != i.getOpcode()) {
			return 0;
		}
		return size;
	}

	private static AbstractInsnNode next(AbstractInsnNode i) {
		do {
			i = i.getNext();
		} while (i != null && (AbstractInsnNode.FRAME == i.getType()
				|| AbstractInsnNode.LABEL == i.getType()
				|| AbstractInsnNode.LINE == i.getType()));
		return i;
	}

}
