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
package www.pactera.com.coverage.project.component.data;

import www.pactera.com.coverage.project.component.core.analysis.ILine;


public interface ISourceNode extends ICoverageNode {


	int UNKNOWN_LINE = -1;

	int getFirstLine();

	int getLastLine();

	ILine getLine(int nr);

}
