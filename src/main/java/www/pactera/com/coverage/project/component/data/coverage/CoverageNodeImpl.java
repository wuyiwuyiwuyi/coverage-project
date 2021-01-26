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
package www.pactera.com.coverage.project.component.data.coverage;


import www.pactera.com.coverage.project.component.core.analysis.ICounter;
import www.pactera.com.coverage.project.component.core.analysis.internal.CounterImpl;
import www.pactera.com.coverage.project.component.data.ICoverageNode;

import java.util.Collection;


public class CoverageNodeImpl implements ICoverageNode {

	private final ElementType elementType;

	private final String name;


	protected CounterImpl branchCounter;


	protected CounterImpl instructionCounter;


	protected CounterImpl lineCounter;


	protected CounterImpl complexityCounter;


	protected CounterImpl methodCounter;


	protected CounterImpl classCounter;


	public CoverageNodeImpl(final ElementType elementType, final String name) {
		this.elementType = elementType;
		this.name = name;
		this.branchCounter = CounterImpl.COUNTER_0_0;
		this.instructionCounter = CounterImpl.COUNTER_0_0;
		this.complexityCounter = CounterImpl.COUNTER_0_0;
		this.methodCounter = CounterImpl.COUNTER_0_0;
		this.classCounter = CounterImpl.COUNTER_0_0;
		this.lineCounter = CounterImpl.COUNTER_0_0;
	}

	public void increment(final ICoverageNode child) {
		instructionCounter = instructionCounter
				.increment(child.getInstructionCounter());
		branchCounter = branchCounter.increment(child.getBranchCounter());
		lineCounter = lineCounter.increment(child.getLineCounter());
		complexityCounter = complexityCounter
				.increment(child.getComplexityCounter());
		methodCounter = methodCounter.increment(child.getMethodCounter());
		classCounter = classCounter.increment(child.getClassCounter());
	}

	public void increment(final Collection<? extends ICoverageNode> children) {
		for (final ICoverageNode child : children) {
			increment(child);
		}
	}

	// === ICoverageDataNode ===

	public ElementType getElementType() {
		return elementType;
	}

	public String getName() {
		return name;
	}

	public ICounter getInstructionCounter() {
		return instructionCounter;
	}

	public ICounter getBranchCounter() {
		return branchCounter;
	}

	public ICounter getLineCounter() {
		return lineCounter;
	}

	public ICounter getComplexityCounter() {
		return complexityCounter;
	}

	public ICounter getMethodCounter() {
		return methodCounter;
	}

	public ICounter getClassCounter() {
		return classCounter;
	}

	public ICounter getCounter(final CounterEntity entity) {
		switch (entity) {
		case INSTRUCTION:
			return getInstructionCounter();
		case BRANCH:
			return getBranchCounter();
		case LINE:
			return getLineCounter();
		case COMPLEXITY:
			return getComplexityCounter();
		case METHOD:
			return getMethodCounter();
		case CLASS:
			return getClassCounter();
		}
		throw new AssertionError(entity);
	}

	public boolean containsCode() {
		return getInstructionCounter().getTotalCount() != 0;
	}

	public ICoverageNode getPlainCopy() {
		final CoverageNodeImpl copy = new CoverageNodeImpl(elementType, name);
		copy.instructionCounter = CounterImpl.getInstance(instructionCounter);
		copy.branchCounter = CounterImpl.getInstance(branchCounter);
		copy.lineCounter = CounterImpl.getInstance(lineCounter);
		copy.complexityCounter = CounterImpl.getInstance(complexityCounter);
		copy.methodCounter = CounterImpl.getInstance(methodCounter);
		copy.classCounter = CounterImpl.getInstance(classCounter);
		return copy;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(name).append(" [").append(elementType).append("]");
		return sb.toString();
	}

}
