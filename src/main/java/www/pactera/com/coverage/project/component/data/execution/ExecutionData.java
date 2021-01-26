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
package www.pactera.com.coverage.project.component.data.execution;

import java.util.Arrays;

import static java.lang.String.format;

public final class ExecutionData {

	private final long id;

	private final String name;

	private final boolean[] probes;

	public ExecutionData(final long id, final String name,
                         final boolean[] probes) {
		this.id = id;
		this.name = name;
		this.probes = probes;
	}

	public ExecutionData(final long id, final String name,
                         final int probeCount) {
		this.id = id;
		this.name = name;
		this.probes = new boolean[probeCount];
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean[] getProbes() {
		return probes;
	}


	public void reset() {
		Arrays.fill(probes, false);
	}

	public boolean hasHits() {
		for (final boolean p : probes) {
			if (p) {
				return true;
			}
		}
		return false;
	}

	public void merge(final ExecutionData other) {
		merge(other, true);
	}

	public void merge(final ExecutionData other, final boolean flag) {
		assertCompatibility(other.getId(), other.getName(),
				other.getProbes().length);
		final boolean[] otherData = other.getProbes();
		for (int i = 0; i < probes.length; i++) {
			if (otherData[i]) {
				probes[i] = flag;
			}
		}
	}

	public void assertCompatibility(final long id, final String name,
			final int probecount) throws IllegalStateException {
		if (this.id != id) {
			throw new IllegalStateException(
					format("Different ids (%016x and %016x).",
							Long.valueOf(this.id), Long.valueOf(id)));
		}
		if (!this.name.equals(name)) {
			throw new IllegalStateException(
					format("Different class names %s and %s for id %016x.",
							this.name, name, Long.valueOf(id)));
		}
		if (this.probes.length != probecount) {
			throw new IllegalStateException(format(
					"Incompatible execution data for class %s with id %016x.",
					name, Long.valueOf(id)));
		}
	}

	@Override
	public String toString() {
		return String.format("ExecutionData[name=%s, id=%016x]", name,
				Long.valueOf(id));
	}

}
