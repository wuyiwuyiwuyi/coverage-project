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
package www.pactera.com.coverage.project.component.core.tools;



import www.pactera.com.coverage.project.component.data.execution.ExecutionDataReader;
import www.pactera.com.coverage.project.component.data.execution.ExecutionDataStore;
import www.pactera.com.coverage.project.component.data.execution.ExecutionDataWriter;
import www.pactera.com.coverage.project.component.data.session.SessionInfoStore;

import java.io.*;

/**
 * Convenience utility for loading *.exec files into a
 * {@link ExecutionDataStore} and a {@link SessionInfoStore}.
 */
public class ExecFileLoader {

	private final SessionInfoStore sessionInfos;
	private final ExecutionDataStore executionData;


	public ExecFileLoader() {
		sessionInfos = new SessionInfoStore();
		executionData = new ExecutionDataStore();
	}


	public void load(final InputStream stream) throws IOException {
		final ExecutionDataReader reader = new ExecutionDataReader(
				new BufferedInputStream(stream));
		reader.setExecutionDataVisitor(executionData);
		reader.setSessionInfoVisitor(sessionInfos);
		reader.read();
	}


	public void load(final File file) throws IOException {
		final InputStream stream = new FileInputStream(file);
		try {
			load(stream);
		} finally {
			stream.close();
		}
	}


	public void save(final OutputStream stream) throws IOException {
		final ExecutionDataWriter dataWriter = new ExecutionDataWriter(stream);
		sessionInfos.accept(dataWriter);
		executionData.accept(dataWriter);
	}


	public void save(final File file, final boolean append) throws IOException {
		final File folder = file.getParentFile();
		if (folder != null) {
			folder.mkdirs();
		}
		final FileOutputStream fileStream = new FileOutputStream(file, append);
		// Avoid concurrent writes from other processes:
		fileStream.getChannel().lock();
		final OutputStream bufferedStream = new BufferedOutputStream(
				fileStream);
		try {
			save(bufferedStream);
		} finally {
			bufferedStream.close();
		}
	}


	public SessionInfoStore getSessionInfoStore() {
		return sessionInfos;
	}


	public ExecutionDataStore getExecutionDataStore() {
		return executionData;
	}

}
