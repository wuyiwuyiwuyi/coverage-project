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


import www.pactera.com.coverage.project.component.data.IBundleCoverage;

import java.io.IOException;

public interface IReportGroupVisitor {


	void visitBundle(IBundleCoverage bundle, ISourceFileLocator locator)
			throws IOException;

	IReportGroupVisitor visitGroup(String name) throws IOException;

}
