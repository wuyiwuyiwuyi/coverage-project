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


import www.pactera.com.coverage.project.component.data.IClassCoverage;
import www.pactera.com.coverage.project.component.data.IMethodCoverage;

import java.util.ArrayList;
import java.util.Collection;


public class ClassCoverageImpl extends SourceNodeImpl
		implements IClassCoverage {

	private final long id;
	private final boolean noMatch;
	private final Collection<IMethodCoverage> methods;
	private String signature;
	private String superName;
	private String[] interfaces;
	private String sourceFileName;


	public ClassCoverageImpl(final String name, final long id,
                             final boolean noMatch) {
		super(ElementType.CLASS, name);
		this.id = id;
		this.noMatch = noMatch;
		this.methods = new ArrayList<IMethodCoverage>();
	}


	public void addMethod(final IMethodCoverage method) {
		this.methods.add(method);
		increment(method);
		// Class is considered as covered when at least one method is covered:
		if (methodCounter.getCoveredCount() > 0) {
			this.classCounter = CounterImpl.COUNTER_0_1;
		} else {
			this.classCounter = CounterImpl.COUNTER_1_0;
		}
	}

	public void setSignature(final String signature) {
		this.signature = signature;
	}


	public void setSuperName(final String superName) {
		this.superName = superName;
	}


	public void setInterfaces(final String[] interfaces) {
		this.interfaces = interfaces;
	}


	public void setSourceFileName(final String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	// === IClassCoverage implementation ===

	public long getId() {
		return id;
	}

	public boolean isNoMatch() {
		return noMatch;
	}

	public String getSignature() {
		return signature;
	}

	public String getSuperName() {
		return superName;
	}

	public String[] getInterfaceNames() {
		return interfaces;
	}

	public String getPackageName() {
		final int pos = getName().lastIndexOf('/');
		return pos == -1 ? "" : getName().substring(0, pos);
	}

	public String getSourceFileName() {
		return sourceFileName;
	}

	public Collection<IMethodCoverage> getMethods() {
		return methods;
	}

}
