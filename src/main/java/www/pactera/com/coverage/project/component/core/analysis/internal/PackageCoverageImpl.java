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
import www.pactera.com.coverage.project.component.data.IPackageCoverage;
import www.pactera.com.coverage.project.component.data.ISourceFileCoverage;
import www.pactera.com.coverage.project.component.data.coverage.CoverageNodeImpl;

import java.util.Collection;


public class PackageCoverageImpl extends CoverageNodeImpl
		implements IPackageCoverage {

	private final Collection<IClassCoverage> classes;

	private final Collection<ISourceFileCoverage> sourceFiles;


	public PackageCoverageImpl(final String name,
                               final Collection<IClassCoverage> classes,
                               final Collection<ISourceFileCoverage> sourceFiles) {
		super(ElementType.PACKAGE, name);
		this.classes = classes;
		this.sourceFiles = sourceFiles;
		increment(sourceFiles);
		for (final IClassCoverage c : classes) {
			// We need to add only classes without a source file reference.
			// Classes associated with a source file are already included in the
			// SourceFileCoverage objects.
			if (c.getSourceFileName() == null) {
				increment(c);
			}
		}
	}

	public Collection<IClassCoverage> getClasses() {
		return classes;
	}

	public Collection<ISourceFileCoverage> getSourceFiles() {
		return sourceFiles;
	}

}
