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


import org.objectweb.asm.ClassVisitor;
import www.pactera.com.coverage.project.component.core.analysis.instr.InstrSupport;


public abstract class ClassProbesVisitor extends ClassVisitor {


	public ClassProbesVisitor() {
		this(null);
	}


	public ClassProbesVisitor(final ClassVisitor cv) {
		super(InstrSupport.ASM_API_VERSION, cv);
	}


	@Override
	public abstract MethodProbesVisitor visitMethod(int access, String name,
			String desc, String signature, String[] exceptions);

	public abstract void visitTotalProbeCount(int count);

}
