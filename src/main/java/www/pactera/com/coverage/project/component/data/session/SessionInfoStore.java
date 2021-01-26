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

import www.pactera.com.coverage.project.component.data.ISessionInfoVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;


public class SessionInfoStore implements ISessionInfoVisitor {

	private final List<SessionInfo> infos = new ArrayList<SessionInfo>();


	public boolean isEmpty() {
		return infos.isEmpty();
	}


	public List<SessionInfo> getInfos() {
		final List<SessionInfo> copy = new ArrayList<SessionInfo>(infos);
		Collections.sort(copy);
		return copy;
	}

	public SessionInfo getMerged(final String id) {
		if (infos.isEmpty()) {
			return new SessionInfo(id, 0, 0);
		}
		long start = Long.MAX_VALUE;
		long dump = Long.MIN_VALUE;
		for (final SessionInfo i : infos) {
			start = min(start, i.getStartTimeStamp());
			dump = max(dump, i.getDumpTimeStamp());
		}
		return new SessionInfo(id, start, dump);
	}


	public void accept(final ISessionInfoVisitor visitor) {
		for (final SessionInfo i : getInfos()) {
			visitor.visitSessionInfo(i);
		}
	}

	// === ISessionInfoVisitor ===

	public void visitSessionInfo(final SessionInfo info) {
		infos.add(info);
	}

}
