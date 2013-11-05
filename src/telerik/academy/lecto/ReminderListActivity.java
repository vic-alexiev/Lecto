package telerik.academy.lecto;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ReminderListActivity extends ListActivity {

	private static final int ACTIVITY_CREATE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reminder_list);

		String[] items = new String[] { "C# I", "C# II", "JavaScript I",
				"Databases" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				R.layout.reminder_row, R.id.reminder_text, items);
		setListAdapter(adapter);

		registerForContextMenu(getListView());
	}

	@Override
	protected void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);

		Intent intent = new Intent(this, ReminderEditActivity.class);
		intent.putExtra("RowId", id);
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
		startActivityForResult(intent, ACTIVITY_CREATE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		// Reload the list here
	}
}
