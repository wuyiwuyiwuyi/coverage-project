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


import www.pactera.com.coverage.project.component.data.ISourceFileCoverage;


public class SourceFileCoverageImpl extends SourceNodeImpl
		implements ISourceFileCoverage {

	private final String packagename;


	public SourceFileCoverageImpl(final String name, final String packagename) {
		super(ElementType.SOURCEFILE, name);
		this.packagename = packagename;
	}

	// === ISourceFileCoverage implementation ===

	public String getPackageName() {
		return packagename;
	}

}
