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


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;
import www.pactera.com.coverage.project.component.core.analysis.flow.ClassProbesAdapter;
import www.pactera.com.coverage.project.component.core.analysis.instr.InstrSupport;
import www.pactera.com.coverage.project.component.core.analysis.internal.CRC64;
import www.pactera.com.coverage.project.component.core.analysis.internal.ClassAnalyzer;
import www.pactera.com.coverage.project.component.core.analysis.internal.ClassCoverageImpl;
import www.pactera.com.coverage.project.component.core.analysis.internal.StringPool;
import www.pactera.com.coverage.project.component.data.ICoverageVisitor;
import www.pactera.com.coverage.project.component.data.execution.ExecutionData;
import www.pactera.com.coverage.project.component.data.execution.ExecutionDataStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.StringTokenizer;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Analyzer {

	private final ExecutionDataStore executionData;

	private final ICoverageVisitor coverageVisitor;

	private final StringPool stringPool;


	public Analyzer(final ExecutionDataStore executionData,
                    final ICoverageVisitor coverageVisitor) {
		this.executionData = executionData;
		this.coverageVisitor = coverageVisitor;
		this.stringPool = new StringPool();
	}


	private ClassVisitor createAnalyzingVisitor(final long classid,
			final String className) {
		final ExecutionData data = executionData.get(classid);
		final boolean[] probes;
		final boolean noMatch;
		if (data == null) {
			probes = null;
			noMatch = executionData.contains(className);
		} else {
			probes = data.getProbes();
			noMatch = false;
		}
		final ClassCoverageImpl coverage = new ClassCoverageImpl(className,
				classid, noMatch);
		final ClassAnalyzer analyzer = new ClassAnalyzer(coverage, probes,
				stringPool) {
			@Override
			public void visitEnd() {
				super.visitEnd();
				coverageVisitor.visitCoverage(coverage);
			}
		};
		return new ClassProbesAdapter(analyzer, false);
	}

	private void analyzeClass(final byte[] source) {
		final long classId = CRC64.classId(source);
		final ClassReader reader = InstrSupport.classReaderFor(source);
		if ((reader.getAccess() & Opcodes.ACC_MODULE) != 0) {
			return;
		}
		if ((reader.getAccess() & Opcodes.ACC_SYNTHETIC) != 0) {
			return;
		}
		final ClassVisitor visitor = createAnalyzingVisitor(classId,
				reader.getClassName());
		reader.accept(visitor, 0);
	}

	public void analyzeClass(final byte[] buffer, final String location)
			throws IOException {
		try {
			analyzeClass(buffer);
		} catch (final RuntimeException cause) {
			throw analyzerError(location, cause);
		}
	}


	public void analyzeClass(final InputStream input, final String location)
			throws IOException {
		final byte[] buffer;
		try {
			buffer = InputStreams.readFully(input);
		} catch (final IOException e) {
			throw analyzerError(location, e);
		}
		analyzeClass(buffer, location);
	}

	private IOException analyzerError(final String location,
			final Exception cause) {
		final IOException ex = new IOException(
				String.format("Error while analyzing %s.", location));
		ex.initCause(cause);
		return ex;
	}


	public int analyzeAll(final InputStream input, final String location)
			throws IOException {
		final ContentTypeDetector detector;
		try {
			detector = new ContentTypeDetector(input);
		} catch (final IOException e) {
			throw analyzerError(location, e);
		}
		switch (detector.getType()) {
		case ContentTypeDetector.CLASSFILE:
			analyzeClass(detector.getInputStream(), location);
			return 1;
		case ContentTypeDetector.ZIPFILE:
			return analyzeZip(detector.getInputStream(), location);
		case ContentTypeDetector.GZFILE:
			return analyzeGzip(detector.getInputStream(), location);
		case ContentTypeDetector.PACK200FILE:
			return analyzePack200(detector.getInputStream(), location);
		default:
			return 0;
		}
	}


	public int analyzeAll(final File file) throws IOException {
		int count = 0;
		if (file.isDirectory()) {
			for (final File f : file.listFiles()) {
				count += analyzeAll(f);
			}
		} else {
			final InputStream in = new FileInputStream(file);
			try {
				count += analyzeAll(in, file.getPath());
			} finally {
				in.close();
			}
		}
		return count;
	}


	public int analyzeAll(final String path, final File basedir)
			throws IOException {
		int count = 0;
		final StringTokenizer st = new StringTokenizer(path,
				File.pathSeparator);
		while (st.hasMoreTokens()) {
			count += analyzeAll(new File(basedir, st.nextToken()));
		}
		return count;
	}

	private int analyzeZip(final InputStream input, final String location)
			throws IOException {
		final ZipInputStream zip = new ZipInputStream(input);
		ZipEntry entry;
		int count = 0;
		while ((entry = nextEntry(zip, location)) != null) {
			count += analyzeAll(zip, location + "@" + entry.getName());
		}
		return count;
	}

	private ZipEntry nextEntry(final ZipInputStream input,
			final String location) throws IOException {
		try {
			return input.getNextEntry();
		} catch (final IOException e) {
			throw analyzerError(location, e);
		}
	}

	private int analyzeGzip(final InputStream input, final String location)
			throws IOException {
		GZIPInputStream gzipInputStream;
		try {
			gzipInputStream = new GZIPInputStream(input);
		} catch (final IOException e) {
			throw analyzerError(location, e);
		}
		return analyzeAll(gzipInputStream, location);
	}

	private int analyzePack200(final InputStream input, final String location)
			throws IOException {
		InputStream unpackedInput;
		try {
			unpackedInput = Pack200Streams.unpack(input);
		} catch (final IOException e) {
			throw analyzerError(location, e);
		}
		return analyzeAll(unpackedInput, location);
	}

}
