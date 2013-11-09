package telerik.academy.lecto;

import telerik.academy.lecto.contentprovider.ReminderContentProvider;
import telerik.academy.lecto.database.ReminderTable;
import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ReminderListActivity extends ListActivity implements
		LoaderManager.LoaderCallbacks<Cursor> {

	private SimpleCursorAdapter mAdapter;

	/** Called when the activity is first created. */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_list);
		fillData();

		registerForContextMenu(getListView());
	}

	private void fillData() {

		// Create an array to specify the fields we want (only the TITLE)
		String[] from = new String[] { ReminderTable.KEY_TITLE };
		// and an array of the fields we want to bind in the view
		int[] to = new int[] { R.id.reminder_text };

		getLoaderManager().initLoader(0, null, this);
		mAdapter = new SimpleCursorAdapter(this, R.layout.reminder_row, null,
				from, to, 0);
		// inform the list view where to find its data
		setListAdapter(mAdapter);
	}

	@Override
	protected void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		Intent intent = new Intent(this, ReminderEditActivity.class);

		Uri reminderUri = Uri.parse(ReminderContentProvider.CONTENT_URI + "/" + id);
		// place the ID of the task to be edited into the intent
		intent.putExtra(ReminderContentProvider.CONTENT_ITEM_TYPE, reminderUri);
		startActivity(intent);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, view, menuInfo);

		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.list_menu_item_longpress, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_delete:
			// Delete the lecture
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();

			Uri uri = Uri.parse(ReminderContentProvider.CONTENT_URI + "/"
					+ info.id);
			getContentResolver().delete(uri, null, null);
			fillData();
			return true;
		}

		return super.onContextItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		MenuInflater menuInflater = getMenuInflater();
		// inflate the menu from the XML resource into an actual menu object
		menuInflater.inflate(R.menu.list_menu, menu);

		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_insert:
			createReminder();
			return true;
		}

		return super.onMenuItemSelected(featureId, item);
	}

	private void createReminder() {
		Intent intent = new Intent(this, ReminderEditActivity.class);
		startActivity(intent);
	}

	// creates a new loader after the initLoader () call
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { ReminderTable.KEY_ROWID,
				ReminderTable.KEY_TITLE };
		CursorLoader cursorLoader = new CursorLoader(this,
				ReminderContentProvider.CONTENT_URI, projection, null, null,
				null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// data is not available anymore, delete reference
		mAdapter.swapCursor(null);
	}
}
