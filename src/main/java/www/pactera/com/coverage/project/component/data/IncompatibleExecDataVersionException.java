/*******************************************************************************
 * Copyright (c) 2009, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Marc R. Hoffmann, somechris - initial API and implementation
 *
 *******************************************************************************/
package www.pactera.com.coverage.project.component.data;

import www.pactera.com.coverage.project.component.data.execution.ExecutionDataWriter;

import java.io.IOException;


public class IncompatibleExecDataVersionException extends IOException {

	private static final long serialVersionUID = 1L;

	private final int actualVersion;


	public IncompatibleExecDataVersionException(final int actualVersion) {
		super(String.format("Cannot read execution data version 0x%x. "
				+ "This version of JaCoCo uses execution data version 0x%x.",
				Integer.valueOf(actualVersion),
				Integer.valueOf(ExecutionDataWriter.FORMAT_VERSION)));
		this.actualVersion = actualVersion;
	}

	public int getExpectedVersion() {
		return ExecutionDataWriter.FORMAT_VERSION;
	}

	public int getActualVersion() {
		return actualVersion;
	}

}
