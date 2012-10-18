package com.anibug.smsmanager.activity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.anibug.smsmanager.utils.Session;
import com.anibug.smsmanager.model.Model;

public abstract class ListActivityBase<T extends Model> extends ListActivity {

	private int positionClicked = -1;

	public final int MENU_ITEM_REMOVE = 1;
	public final int MENU_ITEM_EDIT = 2;

	public abstract void updateList();
	protected abstract int getContextMenuOptions();

	protected String getContextMenuTitle(T selected) {
		return selected.toString();
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Session.updateSessionFrom(this);
        getListView().setOnCreateContextMenuListener(this);
    }

    @Override
    public void startActivity(Intent intent) {
        Session.packSessionTo(intent);

        super.startActivity(intent);
    }

    @Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		try {
			// Save the position and recall it when item clicked.
			final AdapterView.AdapterContextMenuInfo info =
					(AdapterView.AdapterContextMenuInfo) menuInfo;
		    positionClicked = info.position;
			@SuppressWarnings("unchecked")
			final T selected = (T) getListAdapter().getItem(positionClicked);
			menu.setHeaderTitle(getContextMenuTitle(selected));
		} catch (final ClassCastException e) {
		    Log.e(getClass().getName(), "bad menuInfo", e);
		    return;
		}

		final int options = getContextMenuOptions();
		if ((options & MENU_ITEM_REMOVE) > 0)
			menu.add(Menu.NONE, MENU_ITEM_REMOVE, Menu.NONE, "Remove");
		if ((options & MENU_ITEM_EDIT) > 0)
			menu.add(Menu.NONE, MENU_ITEM_EDIT, Menu.NONE, "Edit");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		@SuppressWarnings("unchecked")
		final T selected = (T) getListAdapter().getItem(positionClicked);
		switch (item.getItemId()) {
		case MENU_ITEM_REMOVE:
			onItemRemoved(selected);
			updateList();
			return true;
		case MENU_ITEM_EDIT:
			onItemUpdated(selected);
			updateList();
			return true;
		default:
			assert false;
			return true;
		}
	}

	protected void onItemUpdated(T selected) {
	}

	protected void onItemRemoved(T selected) {
	}
}