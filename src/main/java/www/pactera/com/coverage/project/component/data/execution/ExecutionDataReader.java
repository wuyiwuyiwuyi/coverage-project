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


import www.pactera.com.coverage.project.component.data.*;
import www.pactera.com.coverage.project.component.data.compact.CompactDataInput;
import www.pactera.com.coverage.project.component.data.session.SessionInfo;

import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;


public class ExecutionDataReader {


	protected final CompactDataInput in;

	private ISessionInfoVisitor sessionInfoVisitor = null;

	private IExecutionDataVisitor executionDataVisitor = null;

	private boolean firstBlock = true;


	public ExecutionDataReader(final InputStream input) {
		this.in = new CompactDataInput(input);
	}


	public void setSessionInfoVisitor(final ISessionInfoVisitor visitor) {
		this.sessionInfoVisitor = visitor;
	}


	public void setExecutionDataVisitor(final IExecutionDataVisitor visitor) {
		this.executionDataVisitor = visitor;
	}

	public boolean read()
			throws IOException, IncompatibleExecDataVersionException {
		byte type;
		do {
			int i = in.read();
			if (i == -1) {
				return false; // EOF
			}
			type = (byte) i;
			if (firstBlock && type != ExecutionDataWriter.BLOCK_HEADER) {
				throw new IOException("Invalid execution data file.");
			}
			firstBlock = false;
		} while (readBlock(type));
		return true;
	}


	protected boolean readBlock(final byte blocktype) throws IOException {
		switch (blocktype) {
		case ExecutionDataWriter.BLOCK_HEADER:
			readHeader();
			return true;
		case ExecutionDataWriter.BLOCK_SESSIONINFO:
			readSessionInfo();
			return true;
		case ExecutionDataWriter.BLOCK_EXECUTIONDATA:
			readExecutionData();
			return true;
		default:
			throw new IOException(
					format("Unknown block type %x.", Byte.valueOf(blocktype)));
		}
	}

	private void readHeader() throws IOException {
		if (in.readChar() != ExecutionDataWriter.MAGIC_NUMBER) {
			throw new IOException("Invalid execution data file.");
		}
		final char version = in.readChar();
		if (version != ExecutionDataWriter.FORMAT_VERSION) {
			throw new IncompatibleExecDataVersionException(version);
		}
	}

	private void readSessionInfo() throws IOException {
		if (sessionInfoVisitor == null) {
			throw new IOException("No session info visitor.");
		}
		final String id = in.readUTF();
		final long start = in.readLong();
		final long dump = in.readLong();
		sessionInfoVisitor.visitSessionInfo(new SessionInfo(id, start, dump));
	}

	private void readExecutionData() throws IOException {
		if (executionDataVisitor == null) {
			throw new IOException("No execution data visitor.");
		}
		final long id = in.readLong();
		final String name = in.readUTF();
		final boolean[] probes = in.readBooleanArray();
		executionDataVisitor
				.visitClassExecution(new ExecutionData(id, name, probes));
	}

}
