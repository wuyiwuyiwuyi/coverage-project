/*******************************************************************************
 * Copyright (c) 2009, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Brock Janiczak - analysis and concept
 *    Marc R. Hoffmann - initial API and implementation
 *
 *******************************************************************************/
package www.pactera.com.coverage.project.component.core.analysis.internal;

import java.util.HashMap;
import java.util.Map;


public final class StringPool {

	private static final String[] EMPTY_ARRAY = new String[0];

	private final Map<String, String> pool = new HashMap<String, String>(1024);

	public String get(final String s) {
		if (s == null) {
			return null;
		}
		final String norm = pool.get(s);
		if (norm == null) {
			pool.put(s, s);
			return s;
		}
		return norm;
	}


	public String[] get(final String[] arr) {
		if (arr == null) {
			return null;
		}
		if (arr.length == 0) {
			return EMPTY_ARRAY;
		}
		for (int i = 0; i < arr.length; i++) {
			arr[i] = get(arr[i]);
		}
		return arr;
	}

}
