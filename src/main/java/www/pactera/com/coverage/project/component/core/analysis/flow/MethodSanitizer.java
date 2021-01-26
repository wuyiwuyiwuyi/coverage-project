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
package www.pactera.com.coverage.project.component.core.analysis.flow;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.JSRInlinerAdapter;
import www.pactera.com.coverage.project.component.core.analysis.instr.InstrSupport;

class MethodSanitizer extends JSRInlinerAdapter {

	MethodSanitizer(final MethodVisitor mv, final int access, final String name,
			final String desc, final String signature,
			final String[] exceptions) {
		super(InstrSupport.ASM_API_VERSION, mv, access, name, desc, signature,
				exceptions);
	}

	@Override
	public void visitLocalVariable(final String name, final String desc,
			final String signature, final Label start, final Label end,
			final int index) {
		if (start.info != null && end.info != null) {
			super.visitLocalVariable(name, desc, signature, start, end, index);
		}
	}

	@Override
	public void visitLineNumber(final int line, final Label start) {
		if (start.info != null) {
			super.visitLineNumber(line, start);
		}
	}

}
