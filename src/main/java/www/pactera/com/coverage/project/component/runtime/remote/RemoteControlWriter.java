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
package www.pactera.com.coverage.project.component.runtime.remote;

import www.pactera.com.coverage.project.component.data.execution.ExecutionDataWriter;
import www.pactera.com.coverage.project.component.runtime.IRemoteCommandVisitor;

import java.io.IOException;
import java.io.OutputStream;


public class RemoteControlWriter extends ExecutionDataWriter
		implements IRemoteCommandVisitor {


	public static final byte BLOCK_CMDOK = 0x20;


	public static final byte BLOCK_CMDDUMP = 0x40;

	public RemoteControlWriter(final OutputStream output) throws IOException {
		super(output);
	}

	public void sendCmdOk() throws IOException {
		out.writeByte(RemoteControlWriter.BLOCK_CMDOK);
	}

	public void visitDumpCommand(final boolean dump, final boolean reset)
			throws IOException {
		out.writeByte(RemoteControlWriter.BLOCK_CMDDUMP);
		out.writeBoolean(dump);
		out.writeBoolean(reset);
	}

}
