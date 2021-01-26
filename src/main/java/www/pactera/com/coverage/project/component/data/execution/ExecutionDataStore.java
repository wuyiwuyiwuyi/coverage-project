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

import www.pactera.com.coverage.project.component.data.IExecutionDataVisitor;

import java.util.*;

public final class ExecutionDataStore implements IExecutionDataVisitor {

	private final Map<Long, ExecutionData> entries = new HashMap<Long, ExecutionData>();

	private final Set<String> names = new HashSet<String>();


	public void put(final ExecutionData data) throws IllegalStateException {
		final Long id = Long.valueOf(data.getId());
		final ExecutionData entry = entries.get(id);
		if (entry == null) {
			entries.put(id, data);
			names.add(data.getName());
		} else {
			entry.merge(data);
		}
	}

	public void subtract(final ExecutionData data)
			throws IllegalStateException {
		final Long id = Long.valueOf(data.getId());
		final ExecutionData entry = entries.get(id);
		if (entry != null) {
			entry.merge(data, false);
		}
	}

	public void subtract(final ExecutionDataStore store) {
		for (final ExecutionData data : store.getContents()) {
			subtract(data);
		}
	}


	public ExecutionData get(final long id) {
		return entries.get(Long.valueOf(id));
	}


	public boolean contains(final String name) {
		return names.contains(name);
	}


	public ExecutionData get(final Long id, final String name,
			final int probecount) {
		ExecutionData entry = entries.get(id);
		if (entry == null) {
			entry = new ExecutionData(id.longValue(), name, probecount);
			entries.put(id, entry);
			names.add(name);
		} else {
			entry.assertCompatibility(id.longValue(), name, probecount);
		}
		return entry;
	}


	public void reset() {
		for (final ExecutionData executionData : this.entries.values()) {
			executionData.reset();
		}
	}


	public Collection<ExecutionData> getContents() {
		return new ArrayList<ExecutionData>(entries.values());
	}


	public void accept(final IExecutionDataVisitor visitor) {
		for (final ExecutionData data : getContents()) {
			visitor.visitClassExecution(data);
		}
	}

	// === IExecutionDataVisitor ===

	public void visitClassExecution(final ExecutionData data) {
		put(data);
	}
}
