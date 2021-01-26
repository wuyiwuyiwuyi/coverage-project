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



import www.pactera.com.coverage.project.component.core.analysis.ICounter;

import java.util.BitSet;
import java.util.Collection;

public class Instruction {

	private final int line;

	private int branches;

	private final BitSet coveredBranches;

	private Instruction predecessor;

	private int predecessorBranch;


	public Instruction(final int line) {
		this.line = line;
		this.branches = 0;
		this.coveredBranches = new BitSet();
	}


	public void addBranch(final Instruction target, final int branch) {
		branches++;
		target.predecessor = this;
		target.predecessorBranch = branch;
		if (!target.coveredBranches.isEmpty()) {
			propagateExecutedBranch(this, branch);
		}
	}


	public void addBranch(final boolean executed, final int branch) {
		branches++;
		if (executed) {
			propagateExecutedBranch(this, branch);
		}
	}

	private static void propagateExecutedBranch(Instruction insn, int branch) {
		// No recursion here, as there can be very long chains of instructions
		while (insn != null) {
			if (!insn.coveredBranches.isEmpty()) {
				insn.coveredBranches.set(branch);
				break;
			}
			insn.coveredBranches.set(branch);
			branch = insn.predecessorBranch;
			insn = insn.predecessor;
		}
	}


	public int getLine() {
		return line;
	}


	public Instruction merge(final Instruction other) {
		final Instruction result = new Instruction(this.line);
		result.branches = this.branches;
		result.coveredBranches.or(this.coveredBranches);
		result.coveredBranches.or(other.coveredBranches);
		return result;
	}

	public Instruction replaceBranches(
			final Collection<Instruction> newBranches) {
		final Instruction result = new Instruction(this.line);
		result.branches = newBranches.size();
		int idx = 0;
		for (final Instruction b : newBranches) {
			if (!b.coveredBranches.isEmpty()) {
				result.coveredBranches.set(idx++);
			}
		}
		return result;
	}

	public ICounter getInstructionCounter() {
		return coveredBranches.isEmpty() ? CounterImpl.COUNTER_1_0
				: CounterImpl.COUNTER_0_1;
	}

	public ICounter getBranchCounter() {
		if (branches < 2) {
			return CounterImpl.COUNTER_0_0;
		}
		final int covered = coveredBranches.cardinality();
		return CounterImpl.getInstance(branches - covered, covered);
	}

}
