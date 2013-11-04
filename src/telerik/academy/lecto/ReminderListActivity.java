package telerik.academy.lecto;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ReminderListActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reminder_list);
        
        String[] items = new String[] { "C# I", "C# II", "JavaScript I", "Databases" }; 
        ArrayAdapter<String> adapter = 
        		new ArrayAdapter<String>(this, R.layout.reminder_row, R.id.reminder_text, items); 
        setListAdapter(adapter);
        
        registerForContextMenu(getListView());
    }
    
    @Override
    protected void onListItemClick(ListView listView, View view, int position, long id) {
    	super.onListItemClick(listView, view, position, id);
    	
    	Intent intent = new Intent(this, ReminderEditActivity.class);
    	intent.putExtra("RowId", id);
    	startActivity(intent);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, view, menuInfo);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	
    	MenuInflater menuInflater = getMenuInflater();
    	// inflate the menu from the XML resource into an actual menu object
    	menuInflater.inflate(R.menu.list_menu, menu);
    	
    	return true;
    }
}
