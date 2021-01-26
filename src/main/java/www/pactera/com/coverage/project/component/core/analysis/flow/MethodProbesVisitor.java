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


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.MethodNode;
import www.pactera.com.coverage.project.component.core.analysis.IFrame;
import www.pactera.com.coverage.project.component.core.analysis.instr.InstrSupport;


public abstract class MethodProbesVisitor extends MethodVisitor {

	public MethodProbesVisitor() {
		this(null);
	}

	public MethodProbesVisitor(final MethodVisitor mv) {
		super(InstrSupport.ASM_API_VERSION, mv);
	}

	@SuppressWarnings("unused")
	public void visitProbe(final int probeId) {
	}

	@SuppressWarnings("unused")
	public void visitJumpInsnWithProbe(final int opcode, final Label label,
			final int probeId, final IFrame frame) {
	}

	@SuppressWarnings("unused")
	public void visitInsnWithProbe(final int opcode, final int probeId) {
	}

	@SuppressWarnings("unused")
	public void visitTableSwitchInsnWithProbes(final int min, final int max,
			final Label dflt, final Label[] labels, final IFrame frame) {
	}

	@SuppressWarnings("unused")
	public void visitLookupSwitchInsnWithProbes(final Label dflt,
			final int[] keys, final Label[] labels, final IFrame frame) {
	}

	public void accept(final MethodNode methodNode,
			final MethodVisitor methodVisitor) {
		methodNode.accept(methodVisitor);
	}

}
