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
package www.pactera.com.coverage.project.component.report;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class DirectorySourceFileLocator extends InputStreamSourceFileLocator {

	private final File directory;

	public DirectorySourceFileLocator(final File directory,
                                      final String encoding, final int tabWidth) {
		super(encoding, tabWidth);
		this.directory = directory;
	}

	@Override
	protected InputStream getSourceStream(final String path)
			throws IOException {
		final File file = new File(directory, path);
		if (file.isFile()) {
			return new FileInputStream(file);
		} else {
			return null;
		}
	}

}
