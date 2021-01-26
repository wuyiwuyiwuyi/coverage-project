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



import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import www.pactera.com.coverage.project.component.core.analysis.flow.LabelInfo;
import www.pactera.com.coverage.project.component.data.ISourceNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class InstructionsBuilder {

	private final boolean[] probes;

	private int currentLine;

	private Instruction currentInsn;

	private final Map<AbstractInsnNode, Instruction> instructions;

	private final List<Label> currentLabel;

	private final List<Jump> jumps;

	InstructionsBuilder(final boolean[] probes) {
		this.probes = probes;
		this.currentLine = ISourceNode.UNKNOWN_LINE;
		this.currentInsn = null;
		this.instructions = new HashMap<AbstractInsnNode, Instruction>();
		this.currentLabel = new ArrayList<Label>(2);
		this.jumps = new ArrayList<Jump>();
	}

	void setCurrentLine(final int line) {
		currentLine = line;
	}

	void addLabel(final Label label) {
		currentLabel.add(label);
		if (!LabelInfo.isSuccessor(label)) {
			noSuccessor();
		}
	}

	void addInstruction(final AbstractInsnNode node) {
		final Instruction insn = new Instruction(currentLine);
		final int labelCount = currentLabel.size();
		if (labelCount > 0) {
			for (int i = labelCount; --i >= 0;) {
				LabelInfo.setInstruction(currentLabel.get(i), insn);
			}
			currentLabel.clear();
		}
		if (currentInsn != null) {
			currentInsn.addBranch(insn, 0);
		}
		currentInsn = insn;
		instructions.put(node, insn);
	}


	void noSuccessor() {
		currentInsn = null;
	}

	void addJump(final Label target, final int branch) {
		jumps.add(new Jump(currentInsn, target, branch));
	}


	void addProbe(final int probeId, final int branch) {
		final boolean executed = probes != null && probes[probeId];
		currentInsn.addBranch(executed, branch);
	}


	Map<AbstractInsnNode, Instruction> getInstructions() {
		// Wire jumps:
		for (final Jump j : jumps) {
			j.wire();
		}

		return instructions;
	}

	private static class Jump {

		private final Instruction source;
		private final Label target;
		private final int branch;

		Jump(final Instruction source, final Label target, final int branch) {
			this.source = source;
			this.target = target;
			this.branch = branch;
		}

		void wire() {
			source.addBranch(LabelInfo.getInstruction(target), branch);
		}

	}

}
