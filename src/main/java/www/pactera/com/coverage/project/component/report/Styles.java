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


public final class Styles {


	public static final String BREADCRUMB = "breadcrumb";


	public static final String INFO = "info";


	public static final String FOOTER = "footer";


	public static final String RIGHT = "right";


	public static final String EL_REPORT = "el_report";


	public static final String EL_SESSION = "el_session";


	public static final String EL_GROUP = "el_group";


	public static final String EL_BUNDLE = "el_bundle";


	public static final String EL_PACKAGE = "el_package";


	public static final String EL_SOURCE = "el_source";


	public static final String EL_CLASS = "el_class";


	public static final String EL_METHOD = "el_method";


	public static final String COVERAGETABLE = "coverage";


	public static final String BAR = "bar";


	public static final String CTR1 = "ctr1";


	public static final String CTR2 = "ctr2";


	public static final String SORTABLE = "sortable";


	public static final String UP = "up";


	public static final String DOWN = "down";


	public static final String SOURCE = "source";


	public static final String NR = "nr";


	public static final String NOT_COVERED = "nc";


	public static final String PARTLY_COVERED = "pc";


	public static final String FULLY_COVERED = "fc";


	public static final String BRANCH_NOT_COVERED = "bnc";


	public static final String BRANCH_PARTLY_COVERED = "bpc";


	public static final String BRANCH_FULLY_COVERED = "bfc";

	public static String combine(final String... styles) {
		final StringBuilder sb = new StringBuilder();
		for (final String style : styles) {
			if (style != null) {
				if (sb.length() > 0) {
					sb.append(" ");
				}
				sb.append(style);
			}
		}
		return sb.length() == 0 ? null : sb.toString();
	}

	private Styles() {
	}

}
