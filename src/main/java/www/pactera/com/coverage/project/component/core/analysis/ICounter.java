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

public interface ICounter {

	enum CounterValue {

		TOTALCOUNT,

		MISSEDCOUNT,

		COVEREDCOUNT,

		MISSEDRATIO,

		COVEREDRATIO
	}

	int EMPTY = 0x00;

	int NOT_COVERED = 0x01;

	int FULLY_COVERED = 0x02;

	int PARTLY_COVERED = NOT_COVERED | FULLY_COVERED;

	double getValue(CounterValue value);

	int getTotalCount();

	int getCoveredCount();

	int getMissedCount();

	double getCoveredRatio();

	double getMissedRatio();

	int getStatus();

}
