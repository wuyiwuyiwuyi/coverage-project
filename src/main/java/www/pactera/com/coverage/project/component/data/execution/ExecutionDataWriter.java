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



import www.pactera.com.coverage.project.component.data.compact.CompactDataOutput;
import www.pactera.com.coverage.project.component.data.IExecutionDataVisitor;
import www.pactera.com.coverage.project.component.data.ISessionInfoVisitor;
import www.pactera.com.coverage.project.component.data.session.SessionInfo;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class ExecutionDataWriter
		implements ISessionInfoVisitor, IExecutionDataVisitor {


	public static final char FORMAT_VERSION;

	static {
		// Runtime initialize to ensure javac does not inline the value.
		FORMAT_VERSION = 0x1007;
	}


	public static final char MAGIC_NUMBER = 0xC0C0;


	public static final byte BLOCK_HEADER = 0x01;


	public static final byte BLOCK_SESSIONINFO = 0x10;


	public static final byte BLOCK_EXECUTIONDATA = 0x11;


	protected final CompactDataOutput out;

	public ExecutionDataWriter(final OutputStream output) throws IOException {
		this.out = new CompactDataOutput(output);
		writeHeader();
	}


	private void writeHeader() throws IOException {
		out.writeByte(BLOCK_HEADER);
		out.writeChar(MAGIC_NUMBER);
		out.writeChar(FORMAT_VERSION);
	}


	public void flush() throws IOException {
		out.flush();
	}

	public void visitSessionInfo(final SessionInfo info) {
		try {
			out.writeByte(BLOCK_SESSIONINFO);
			out.writeUTF(info.getId());
			out.writeLong(info.getStartTimeStamp());
			out.writeLong(info.getDumpTimeStamp());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void visitClassExecution(final ExecutionData data) {
		if (data.hasHits()) {
			try {
				out.writeByte(BLOCK_EXECUTIONDATA);
				out.writeLong(data.getId());
				out.writeUTF(data.getName());
				out.writeBooleanArray(data.getProbes());
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static final byte[] getFileHeader() {
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		try {
			new ExecutionDataWriter(buffer);
		} catch (final IOException e) {
			// Must not happen with ByteArrayOutputStream
			throw new AssertionError(e);
		}
		return buffer.toByteArray();
	}

}
