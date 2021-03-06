/*******************************************************************************
 * Copyright (c) 2009, 2020 Mountainminds GmbH & Co. KG and Contributors
 * This program and the accompanying materials are made available under
 * the terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    Evgeny Mandrikov - initial API and implementation
 *
 *******************************************************************************/
package www.pactera.com.coverage.project.component.core.analysis;

import org.objectweb.asm.tree.AbstractInsnNode;

import java.util.Set;


public interface IFilterOutput {


	void ignore(AbstractInsnNode fromInclusive, AbstractInsnNode toInclusive);


	void merge(AbstractInsnNode i1, AbstractInsnNode i2);


	void replaceBranches(AbstractInsnNode source,
			Set<AbstractInsnNode> newTargets);

}
