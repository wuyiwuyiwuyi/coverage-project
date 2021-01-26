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
package www.pactera.com.coverage.project.component.core.analysis;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;


public class ContentTypeDetector {


	public static final int UNKNOWN = -1;


	public static final int CLASSFILE = 0xcafebabe;


	public static final int ZIPFILE = 0x504b0304;


	public static final int GZFILE = 0x1f8b0000;


	public static final int PACK200FILE = 0xcafed00d;

	private static final int BUFFER_SIZE = 8;

	private final InputStream in;

	private final int type;


	public ContentTypeDetector(final InputStream in) throws IOException {
		if (in.markSupported()) {
			this.in = in;
		} else {
			this.in = new BufferedInputStream(in, BUFFER_SIZE);
		}
		this.in.mark(BUFFER_SIZE);
		this.type = determineType(this.in);
		this.in.reset();
	}

	private static int determineType(final InputStream in) throws IOException {
		final int header = readInt(in);
		switch (header) {
		case ZIPFILE:
			return ZIPFILE;
		case PACK200FILE:
			return PACK200FILE;
		case CLASSFILE:
			final int majorVersion = readInt(in) & 0xFFFF;
			if (majorVersion >= 45) {
				return CLASSFILE;
			}
		}
		if ((header & 0xffff0000) == GZFILE) {
			return GZFILE;
		}
		return UNKNOWN;
	}

	private static int readInt(final InputStream in) throws IOException {
		return in.read() << 24 | in.read() << 16 | in.read() << 8 | in.read();
	}

	public InputStream getInputStream() {
		return in;
	}

	public int getType() {
		return type;
	}

}
