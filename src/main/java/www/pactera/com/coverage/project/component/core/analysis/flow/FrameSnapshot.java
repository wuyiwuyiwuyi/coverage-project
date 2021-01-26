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

package www.pactera.com.coverage.project.component.core.analysis.flow;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AnalyzerAdapter;
import www.pactera.com.coverage.project.component.core.analysis.IFrame;

import java.util.ArrayList;
import java.util.List;


class FrameSnapshot implements IFrame {

	private static final FrameSnapshot NOP = new FrameSnapshot(null, null);

	private final Object[] locals;
	private final Object[] stack;

	private FrameSnapshot(final Object[] locals, final Object[] stack) {
		this.locals = locals;
		this.stack = stack;
	}


	static IFrame create(final AnalyzerAdapter analyzer, final int popCount) {
		if (analyzer == null || analyzer.locals == null) {
			return NOP;
		}
		final Object[] locals = reduce(analyzer.locals, 0);
		final Object[] stack = reduce(analyzer.stack, popCount);
		return new FrameSnapshot(locals, stack);
	}


	private static Object[] reduce(final List<Object> source,
			final int popCount) {
		final List<Object> copy = new ArrayList<Object>(source);
		final int size = source.size() - popCount;
		copy.subList(size, source.size()).clear();
		for (int i = size; --i >= 0;) {
			final Object type = source.get(i);
			if (type == Opcodes.LONG || type == Opcodes.DOUBLE) {
				copy.remove(i + 1);
			}
		}
		return copy.toArray();
	}

	public void accept(final MethodVisitor mv) {
		if (locals != null) {
			mv.visitFrame(Opcodes.F_NEW, locals.length, locals, stack.length,
					stack);
		}
	}

}
