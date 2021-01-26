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
package www.pactera.com.coverage.project.component.data.session;


public class SessionInfo implements Comparable<SessionInfo> {

	private final String id;

	private final long start;

	private final long dump;

	public SessionInfo(final String id, final long start, final long dump) {
		if (id == null) {
			throw new IllegalArgumentException();
		}
		this.id = id;
		this.start = start;
		this.dump = dump;
	}


	public String getId() {
		return id;
	}

	public long getStartTimeStamp() {
		return start;
	}

	public long getDumpTimeStamp() {
		return dump;
	}

	public int compareTo(final SessionInfo other) {
		if (this.dump < other.dump) {
			return -1;
		}
		if (this.dump > other.dump) {
			return +1;
		}
		return 0;
	}

	@Override
	public String toString() {
		return "SessionInfo[" + id + "]";
	}
}
