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

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.MethodNode;
import www.pactera.com.coverage.project.component.core.analysis.IFilter;
import www.pactera.com.coverage.project.component.core.analysis.IFilterContext;
import www.pactera.com.coverage.project.component.core.analysis.IFilterOutput;

public final class SyntheticFilter implements IFilter {

	private static boolean isScalaClass(final IFilterContext context) {
		return context.getClassAttributes().contains("ScalaSig")
				|| context.getClassAttributes().contains("Scala");
	}

	public void filter(final MethodNode methodNode,
                       final IFilterContext context, final IFilterOutput output) {
		if ((methodNode.access & Opcodes.ACC_SYNTHETIC) == 0) {
			return;
		}

		if (methodNode.name.startsWith("lambda$")) {
			return;
		}

		if (isScalaClass(context)) {
			if (methodNode.name.startsWith("$anonfun$")) {
				return;
			}
		}

		if (KotlinGeneratedFilter.isKotlinClass(context)) {
			if (KotlinDefaultArgumentsFilter
					.isDefaultArgumentsMethod(methodNode)) {
				return;
			}

			if (KotlinDefaultArgumentsFilter
					.isDefaultArgumentsConstructor(methodNode)) {
				return;
			}

			if (KotlinCoroutineFilter.isLastArgumentContinuation(methodNode)) {
				return;
			}
		}

		output.ignore(methodNode.instructions.getFirst(),
				methodNode.instructions.getLast());
	}

}
