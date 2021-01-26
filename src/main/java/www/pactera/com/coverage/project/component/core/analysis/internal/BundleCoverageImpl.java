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



import www.pactera.com.coverage.project.component.data.IBundleCoverage;
import www.pactera.com.coverage.project.component.data.IClassCoverage;
import www.pactera.com.coverage.project.component.data.IPackageCoverage;
import www.pactera.com.coverage.project.component.data.ISourceFileCoverage;
import www.pactera.com.coverage.project.component.data.coverage.CoverageNodeImpl;

import java.util.*;


public class BundleCoverageImpl extends CoverageNodeImpl
		implements IBundleCoverage {

	private final Collection<IPackageCoverage> packages;


	public BundleCoverageImpl(final String name,
                              final Collection<IPackageCoverage> packages) {
		super(ElementType.BUNDLE, name);
		this.packages = packages;
		increment(packages);
	}


	public BundleCoverageImpl(final String name,
                              final Collection<IClassCoverage> classes,
                              final Collection<ISourceFileCoverage> sourcefiles) {
		this(name, groupByPackage(classes, sourcefiles));
	}

	private static Collection<IPackageCoverage> groupByPackage(
			final Collection<IClassCoverage> classes,
			final Collection<ISourceFileCoverage> sourcefiles) {
		final Map<String, Collection<IClassCoverage>> classesByPackage = new HashMap<String, Collection<IClassCoverage>>();
		for (final IClassCoverage c : classes) {
			addByName(classesByPackage, c.getPackageName(), c);
		}

		final Map<String, Collection<ISourceFileCoverage>> sourceFilesByPackage = new HashMap<String, Collection<ISourceFileCoverage>>();
		for (final ISourceFileCoverage s : sourcefiles) {
			addByName(sourceFilesByPackage, s.getPackageName(), s);
		}

		final Set<String> packageNames = new HashSet<String>();
		packageNames.addAll(classesByPackage.keySet());
		packageNames.addAll(sourceFilesByPackage.keySet());

		final Collection<IPackageCoverage> result = new ArrayList<IPackageCoverage>();
		for (final String name : packageNames) {
			Collection<IClassCoverage> c = classesByPackage.get(name);
			if (c == null) {
				c = Collections.emptyList();
			}
			Collection<ISourceFileCoverage> s = sourceFilesByPackage.get(name);
			if (s == null) {
				s = Collections.emptyList();
			}
			result.add(new PackageCoverageImpl(name, c, s));
		}
		return result;
	}

	private static <T> void addByName(final Map<String, Collection<T>> map,
			final String name, final T value) {
		Collection<T> list = map.get(name);
		if (list == null) {
			list = new ArrayList<T>();
			map.put(name, list);
		}
		list.add(value);
	}

	// === IBundleCoverage implementation ===

	public Collection<IPackageCoverage> getPackages() {
		return packages;
	}

}
