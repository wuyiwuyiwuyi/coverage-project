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
package www.pactera.com.coverage.project.component.core.analysis;




import www.pactera.com.coverage.project.component.core.analysis.internal.BundleCoverageImpl;
import www.pactera.com.coverage.project.component.core.analysis.internal.SourceFileCoverageImpl;
import www.pactera.com.coverage.project.component.data.IBundleCoverage;
import www.pactera.com.coverage.project.component.data.IClassCoverage;
import www.pactera.com.coverage.project.component.data.ICoverageVisitor;
import www.pactera.com.coverage.project.component.data.ISourceFileCoverage;

import java.util.*;

public class CoverageBuilder implements ICoverageVisitor {

	private final Map<String, IClassCoverage> classes;

	private final Map<String, ISourceFileCoverage> sourcefiles;

	public CoverageBuilder() {
		this.classes = new HashMap<String, IClassCoverage>();
		this.sourcefiles = new HashMap<String, ISourceFileCoverage>();
	}

	public Collection<IClassCoverage> getClasses() {
		return Collections.unmodifiableCollection(classes.values());
	}

	public Collection<ISourceFileCoverage> getSourceFiles() {
		return Collections.unmodifiableCollection(sourcefiles.values());
	}

	public IBundleCoverage getBundle(final String name) {
		return new BundleCoverageImpl(name, classes.values(),
				sourcefiles.values());
	}

	public Collection<IClassCoverage> getNoMatchClasses() {
		final Collection<IClassCoverage> result = new ArrayList<IClassCoverage>();
		for (final IClassCoverage c : classes.values()) {
			if (c.isNoMatch()) {
				result.add(c);
			}
		}
		return result;
	}

	// === ICoverageVisitor ===

	public void visitCoverage(final IClassCoverage coverage) {
		final String name = coverage.getName();
		final IClassCoverage dup = classes.put(name, coverage);
		if (dup != null) {
			if (dup.getId() != coverage.getId()) {
				throw new IllegalStateException(
						"Can't add different class with same name: " + name);
			}
		} else {
			final String source = coverage.getSourceFileName();
			if (source != null) {
				final SourceFileCoverageImpl sourceFile = getSourceFile(source,
						coverage.getPackageName());
				sourceFile.increment(coverage);
			}
		}
	}

	private SourceFileCoverageImpl getSourceFile(final String filename,
												 final String packagename) {
		final String key = packagename + '/' + filename;
		SourceFileCoverageImpl sourcefile = (SourceFileCoverageImpl) sourcefiles
				.get(key);
		if (sourcefile == null) {
			sourcefile = new SourceFileCoverageImpl(filename, packagename);
			sourcefiles.put(key, sourcefile);
		}
		return sourcefile;
	}

}
