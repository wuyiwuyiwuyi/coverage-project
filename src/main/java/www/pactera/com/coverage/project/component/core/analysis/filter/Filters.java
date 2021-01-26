/*******************************************************************************
 * Copyright (c) 2009, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Evgeny Mandrikov - initial API and implementation
 *
 *******************************************************************************/
package www.pactera.com.coverage.project.component.core.analysis.filter;

import org.objectweb.asm.tree.MethodNode;
import www.pactera.com.coverage.project.component.core.analysis.IFilter;
import www.pactera.com.coverage.project.component.core.analysis.IFilterContext;
import www.pactera.com.coverage.project.component.core.analysis.IFilterOutput;


public final class Filters implements IFilter {


	public static final IFilter NONE = new Filters();

	private final IFilter[] filters;

	public static IFilter all() {
		return new Filters(new EnumFilter(), new SyntheticFilter(),
				new BridgeFilter(), new SynchronizedFilter(),
				new TryWithResourcesJavac11Filter(),
				new TryWithResourcesJavacFilter(),
				new TryWithResourcesEcjFilter(), new FinallyFilter(),
				new PrivateEmptyNoArgConstructorFilter(),
				new StringSwitchJavacFilter(), new StringSwitchEcjFilter(),
				new EnumEmptyConstructorFilter(), new RecordsFilter(),
				new AnnotationGeneratedFilter(), new KotlinGeneratedFilter(),
				new KotlinLateinitFilter(), new KotlinWhenFilter(),
				new KotlinWhenStringFilter(),
				new KotlinUnsafeCastOperatorFilter(),
				new KotlinNotNullOperatorFilter(),
				new KotlinDefaultArgumentsFilter(), new KotlinInlineFilter(),
				new KotlinCoroutineFilter(), new KotlinDefaultMethodsFilter());
	}

	private Filters(final IFilter... filters) {
		this.filters = filters;
	}

	public void filter(final MethodNode methodNode,
					   final IFilterContext context, final IFilterOutput output) {
		for (final IFilter filter : filters) {
			filter.filter(methodNode, context, output);
		}
	}

}
