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

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.jar.JarInputStream;
import java.util.jar.JarOutputStream;


public final class Pack200Streams {


	@SuppressWarnings("resource")
	public static InputStream unpack(final InputStream input)
			throws IOException {
		final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		final JarOutputStream jar = new JarOutputStream(buffer);
		try {
			final Object unpacker = Class.forName("java.util.jar.Pack200")
					.getMethod("newUnpacker").invoke(null);
			Class.forName("java.util.jar.Pack200$Unpacker")
					.getMethod("unpack", InputStream.class,
							JarOutputStream.class)
					.invoke(unpacker, new NoCloseInput(input), jar);
		} catch (ClassNotFoundException e) {
			throw newIOException(e);
		} catch (NoSuchMethodException e) {
			throw newIOException(e);
		} catch (IllegalAccessException e) {
			throw newIOException(e);
		} catch (InvocationTargetException e) {
			throw newIOException(e.getCause());
		}
		jar.finish();
		return new ByteArrayInputStream(buffer.toByteArray());
	}


	@SuppressWarnings("resource")
	public static void pack(final byte[] source, final OutputStream output)
			throws IOException {
		final JarInputStream jar = new JarInputStream(
				new ByteArrayInputStream(source));
		try {
			final Object packer = Class.forName("java.util.jar.Pack200")
					.getMethod("newPacker").invoke(null);
			Class.forName("java.util.jar.Pack200$Packer")
					.getMethod("pack", JarInputStream.class, OutputStream.class)
					.invoke(packer, jar, output);
		} catch (ClassNotFoundException e) {
			throw newIOException(e);
		} catch (NoSuchMethodException e) {
			throw newIOException(e);
		} catch (IllegalAccessException e) {
			throw newIOException(e);
		} catch (InvocationTargetException e) {
			throw newIOException(e.getCause());
		}
	}

	private static IOException newIOException(final Throwable cause) {
		final IOException exception = new IOException();
		exception.initCause(cause);
		return exception;
	}

	private static class NoCloseInput extends FilterInputStream {
		protected NoCloseInput(final InputStream in) {
			super(in);
		}

		@Override
		public void close() throws IOException {
			// do not close the underlying stream
		}
	}

	private Pack200Streams() {
	}

}
