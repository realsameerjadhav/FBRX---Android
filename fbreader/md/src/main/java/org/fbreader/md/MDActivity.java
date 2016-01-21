/*
 * Copyright (C) 2010-2015 FBReader.ORG Limited <contact@fbreader.org>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.fbreader.md;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;

import org.fbreader.util.Pair;

public abstract class MDActivity extends ActionBarActivity {
	private Toolbar myToolbar;
	private View myProgressIndicator;

	protected abstract int layoutId();

	protected void onPreCreate() {
	}

	@Override
	protected void onCreate(Bundle icicle) {
		onPreCreate();
		super.onCreate(icicle);
		setExceptionHandler();
		setContentView(layoutId());

		myToolbar = (Toolbar)findViewById(R.id.md_toolbar);
		setSupportActionBar(myToolbar);
		myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		myToolbar.setNavigationContentDescription(R.string.desc_back);
		setupToolbarAppearance(myToolbar, true);

		myProgressIndicator = findViewById(R.id.md_progress_indicator);
	}

	@Override
	protected void onResume() {
		setExceptionHandler();
		super.onResume();
	}

	protected void setTitleVisible(boolean visible) {
		if (myToolbar != null) {
			myToolbar.setVisibility(visible ? View.VISIBLE : View.GONE);
		}
	}

	protected final Toolbar getToolbar() {
		return myToolbar;
	}

	public final void setTitleAndSubtitle(Pair<String,String> pair) {
		setTitleAndSubtitle(pair.First, pair.Second);
	}

	public final void setTitleAndSubtitle(String title, String subtitle) {
		setTitle(title);

		if (myToolbar != null) {
			setupToolbarAppearance(myToolbar, subtitle == null);
			myToolbar.setSubtitle(subtitle);
		}
	}

	public final void showProgressIndicator(final boolean show) {
		if (myProgressIndicator != null) {
			myProgressIndicator.post(new Runnable() {
				public void run() {
					myProgressIndicator.setVisibility(show ? View.VISIBLE : View.GONE);
				}
			});
		} 
	}

	public final void setupToolbarAppearance(Toolbar toolbar, boolean single) {
		final TypedValue typedValue = new TypedValue();
		if (single) {
			getTheme().resolveAttribute(R.attr.titleOnlyTextAppearance, typedValue, true);
			myToolbar.setTitleTextAppearance(this, typedValue.resourceId);
		} else {
			getTheme().resolveAttribute(R.attr.titleTextAppearance, typedValue, true);
			myToolbar.setTitleTextAppearance(this, typedValue.resourceId);
			getTheme().resolveAttribute(R.attr.subtitleTextAppearance, typedValue, true);
			myToolbar.setSubtitleTextAppearance(this, typedValue.resourceId);
		}
	}

	private void setExceptionHandler() {
		final Thread.UncaughtExceptionHandler handler = exceptionHandler();
		if (handler != null) {
			Thread.setDefaultUncaughtExceptionHandler(handler);
		}
	}

	protected Thread.UncaughtExceptionHandler exceptionHandler() {
		return null;
	}
}