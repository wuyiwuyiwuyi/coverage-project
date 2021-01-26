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


public final class EnumEmptyConstructorFilter implements IFilter {

	private static final String CONSTRUCTOR_NAME = "<init>";
	private static final String CONSTRUCTOR_DESC = "(Ljava/lang/String;I)V";

	private static final String ENUM_TYPE = "java/lang/Enum";

	public void filter(final MethodNode methodNode,
					   final IFilterContext context, final IFilterOutput output) {
		if (ENUM_TYPE.equals(context.getSuperClassName())
				&& CONSTRUCTOR_NAME.equals(methodNode.name)
				&& CONSTRUCTOR_DESC.equals(methodNode.desc)
				&& new Matcher().match(methodNode)) {
			output.ignore(methodNode.instructions.getFirst(),
					methodNode.instructions.getLast());
		}
	}

	private static class Matcher extends AbstractMatcher {
		private boolean match(final MethodNode methodNode) {
			firstIsALoad0(methodNode);
			nextIs(Opcodes.ALOAD);
			nextIs(Opcodes.ILOAD);
			nextIsInvoke(Opcodes.INVOKESPECIAL, ENUM_TYPE, CONSTRUCTOR_NAME,
					CONSTRUCTOR_DESC);
			nextIs(Opcodes.RETURN);
			return cursor != null;
		}
	}

}
