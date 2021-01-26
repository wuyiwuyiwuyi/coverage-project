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



import www.pactera.com.coverage.project.component.data.execution.ExecutionDataReader;
import www.pactera.com.coverage.project.component.runtime.IRemoteCommandVisitor;
import www.pactera.com.coverage.project.component.runtime.remote.RemoteControlWriter;

import java.io.IOException;
import java.io.InputStream;


public class RemoteControlReader extends ExecutionDataReader {

	private IRemoteCommandVisitor remoteCommandVisitor;

	public RemoteControlReader(final InputStream input) throws IOException {
		super(input);
	}

	@Override
	protected boolean readBlock(final byte blockid) throws IOException {
		switch (blockid) {
		case RemoteControlWriter.BLOCK_CMDDUMP:
			readDumpCommand();
			return true;
		case RemoteControlWriter.BLOCK_CMDOK:
			return false;
		default:
			return super.readBlock(blockid);
		}
	}

	public void setRemoteCommandVisitor(final IRemoteCommandVisitor visitor) {
		this.remoteCommandVisitor = visitor;
	}

	private void readDumpCommand() throws IOException {
		if (remoteCommandVisitor == null) {
			throw new IOException("No remote command visitor.");
		}
		final boolean dump = in.readBoolean();
		final boolean reset = in.readBoolean();
		remoteCommandVisitor.visitDumpCommand(dump, reset);
	}

}
