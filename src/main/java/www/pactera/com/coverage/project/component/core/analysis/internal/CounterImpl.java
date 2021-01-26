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
package www.pactera.com.coverage.project.component.core.analysis.internal;


import www.pactera.com.coverage.project.component.core.analysis.ICounter;


public abstract class CounterImpl implements ICounter {


	private static final int SINGLETON_LIMIT = 30;

	private static final CounterImpl[][] SINGLETONS = new CounterImpl[SINGLETON_LIMIT
			+ 1][];

	static {
		for (int i = 0; i <= SINGLETON_LIMIT; i++) {
			SINGLETONS[i] = new CounterImpl[SINGLETON_LIMIT + 1];
			for (int j = 0; j <= SINGLETON_LIMIT; j++) {
				SINGLETONS[i][j] = new Fix(i, j);
			}
		}
	}


	public static final CounterImpl COUNTER_0_0 = SINGLETONS[0][0];


	public static final CounterImpl COUNTER_1_0 = SINGLETONS[1][0];


	public static final CounterImpl COUNTER_0_1 = SINGLETONS[0][1];


	private static class Var extends CounterImpl {
		public Var(final int missed, final int covered) {
			super(missed, covered);
		}

		@Override
		public CounterImpl increment(final int missed, final int covered) {
			this.missed += missed;
			this.covered += covered;
			return this;
		}
	}


	private static class Fix extends CounterImpl {
		public Fix(final int missed, final int covered) {
			super(missed, covered);
		}

		@Override
		public CounterImpl increment(final int missed, final int covered) {
			return getInstance(this.missed + missed, this.covered + covered);
		}
	}

	public static CounterImpl getInstance(final int missed, final int covered) {
		if (missed <= SINGLETON_LIMIT && covered <= SINGLETON_LIMIT) {
			return SINGLETONS[missed][covered];
		} else {
			return new Var(missed, covered);
		}
	}


	public static CounterImpl getInstance(final ICounter counter) {
		return getInstance(counter.getMissedCount(), counter.getCoveredCount());
	}

	protected int missed;


	protected int covered;


	protected CounterImpl(final int missed, final int covered) {
		this.missed = missed;
		this.covered = covered;
	}


	public CounterImpl increment(final ICounter counter) {
		return increment(counter.getMissedCount(), counter.getCoveredCount());
	}


	public abstract CounterImpl increment(int missed, int covered);

	// === ICounter implementation ===

	public double getValue(final CounterValue value) {
		switch (value) {
		case TOTALCOUNT:
			return getTotalCount();
		case MISSEDCOUNT:
			return getMissedCount();
		case COVEREDCOUNT:
			return getCoveredCount();
		case MISSEDRATIO:
			return getMissedRatio();
		case COVEREDRATIO:
			return getCoveredRatio();
		default:
			throw new AssertionError(value);
		}
	}

	public int getTotalCount() {
		return missed + covered;
	}

	public int getCoveredCount() {
		return covered;
	}

	public int getMissedCount() {
		return missed;
	}

	public double getCoveredRatio() {
		return (double) covered / (missed + covered);
	}

	public double getMissedRatio() {
		return (double) missed / (missed + covered);
	}

	public int getStatus() {
		int status = covered > 0 ? FULLY_COVERED : EMPTY;
		if (missed > 0) {
			status |= NOT_COVERED;
		}
		return status;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof ICounter) {
			final ICounter that = (ICounter) obj;
			return this.missed == that.getMissedCount()
					&& this.covered == that.getCoveredCount();
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return missed ^ covered * 17;
	}

	@Override
	public String toString() {
		final StringBuilder b = new StringBuilder("Counter["); //$NON-NLS-1$
		b.append(getMissedCount());
		b.append('/').append(getCoveredCount());
		b.append(']');
		return b.toString();
	}

}
