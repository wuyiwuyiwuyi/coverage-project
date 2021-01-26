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
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AnalyzerAdapter;
import www.pactera.com.coverage.project.component.core.analysis.IProbeIdGenerator;
import www.pactera.com.coverage.project.component.core.analysis.instr.InstrSupport;


public class ClassProbesAdapter extends ClassVisitor
		implements IProbeIdGenerator {

	private static final MethodProbesVisitor EMPTY_METHOD_PROBES_VISITOR = new MethodProbesVisitor() {
	};

	private final ClassProbesVisitor cv;

	private final boolean trackFrames;

	private int counter = 0;

	private String name;


	public ClassProbesAdapter(final ClassProbesVisitor cv,
                              final boolean trackFrames) {
		super(InstrSupport.ASM_API_VERSION, cv);
		this.cv = cv;
		this.trackFrames = trackFrames;
	}

	@Override
	public void visit(final int version, final int access, final String name,
			final String signature, final String superName,
			final String[] interfaces) {
		this.name = name;
		super.visit(version, access, name, signature, superName, interfaces);
	}

	@Override
	public final MethodVisitor visitMethod(final int access, final String name,
			final String desc, final String signature,
			final String[] exceptions) {
		final MethodProbesVisitor methodProbes;
		final MethodProbesVisitor mv = cv.visitMethod(access, name, desc,
				signature, exceptions);
		if (mv == null) {
			// We need to visit the method in any case, otherwise probe ids
			// are not reproducible
			methodProbes = EMPTY_METHOD_PROBES_VISITOR;
		} else {
			methodProbes = mv;
		}
		return new MethodSanitizer(null, access, name, desc, signature,
				exceptions) {

			@Override
			public void visitEnd() {
				super.visitEnd();
				LabelFlowAnalyzer.markLabels(this);
				final MethodProbesAdapter probesAdapter = new MethodProbesAdapter(
						methodProbes, ClassProbesAdapter.this);
				if (trackFrames) {
					final AnalyzerAdapter analyzer = new AnalyzerAdapter(
							ClassProbesAdapter.this.name, access, name, desc,
							probesAdapter);
					probesAdapter.setAnalyzer(analyzer);
					methodProbes.accept(this, analyzer);
				} else {
					methodProbes.accept(this, probesAdapter);
				}
			}
		};
	}

	@Override
	public void visitEnd() {
		cv.visitTotalProbeCount(counter);
		super.visitEnd();
	}

	// === IProbeIdGenerator ===

	public int nextId() {
		return counter++;
	}

}
