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
import www.pactera.com.coverage.project.component.core.analysis.internal.Instruction;


public final class LabelInfo {

	public static final int NO_PROBE = -1;

	private boolean target = false;

	private boolean multiTarget = false;

	private boolean successor = false;

	private boolean methodInvocationLine = false;

	private boolean done = false;

	private int probeid = NO_PROBE;

	private Label intermediate = null;

	private Instruction instruction = null;

	// instances are only created within this class
	private LabelInfo() {
	}


	public static void setTarget(final Label label) {
		final LabelInfo info = create(label);
		if (info.target || info.successor) {
			info.multiTarget = true;
		} else {
			info.target = true;
		}
	}


	public static void setSuccessor(final Label label) {
		final LabelInfo info = create(label);
		info.successor = true;
		if (info.target) {
			info.multiTarget = true;
		}
	}


	public static boolean isMultiTarget(final Label label) {
		final LabelInfo info = get(label);
		return info == null ? false : info.multiTarget;
	}

	public static boolean isSuccessor(final Label label) {
		final LabelInfo info = get(label);
		return info == null ? false : info.successor;
	}

	public static void setMethodInvocationLine(final Label label) {
		create(label).methodInvocationLine = true;
	}

	public static boolean isMethodInvocationLine(final Label label) {
		final LabelInfo info = get(label);
		return info == null ? false : info.methodInvocationLine;
	}

	public static boolean needsProbe(final Label label) {
		final LabelInfo info = get(label);
		return info != null && info.successor
				&& (info.multiTarget || info.methodInvocationLine);
	}

	public static void setDone(final Label label) {
		create(label).done = true;
	}


	public static void resetDone(final Label label) {
		final LabelInfo info = get(label);
		if (info != null) {
			info.done = false;
		}
	}


	public static void resetDone(final Label[] labels) {
		for (final Label label : labels) {
			resetDone(label);
		}
	}


	public static boolean isDone(final Label label) {
		final LabelInfo info = get(label);
		return info == null ? false : info.done;
	}

	public static void setProbeId(final Label label, final int id) {
		create(label).probeid = id;
	}


	public static int getProbeId(final Label label) {
		final LabelInfo info = get(label);
		return info == null ? NO_PROBE : info.probeid;
	}


	public static void setIntermediateLabel(final Label label,
			final Label intermediate) {
		create(label).intermediate = intermediate;
	}

	public static Label getIntermediateLabel(final Label label) {
		final LabelInfo info = get(label);
		return info == null ? null : info.intermediate;
	}

	public static void setInstruction(final Label label,
			final Instruction instruction) {
		create(label).instruction = instruction;
	}


	public static Instruction getInstruction(final Label label) {
		final LabelInfo info = get(label);
		return info == null ? null : info.instruction;
	}

	private static LabelInfo get(final Label label) {
		final Object info = label.info;
		return info instanceof LabelInfo ? (LabelInfo) info : null;
	}

	private static LabelInfo create(final Label label) {
		LabelInfo info = get(label);
		if (info == null) {
			info = new LabelInfo();
			label.info = info;
		}
		return info;
	}

}
