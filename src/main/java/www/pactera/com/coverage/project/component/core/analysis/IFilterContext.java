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

import java.util.Set;

/**
 * Context information provided to filters.
 */
public interface IFilterContext {

	String getClassName();

	String getSuperClassName();

	Set<String> getClassAnnotations();


	Set<String> getClassAttributes();


	String getSourceFileName();


	String getSourceDebugExtension();

}
