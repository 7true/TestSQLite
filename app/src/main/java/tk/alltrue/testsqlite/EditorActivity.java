package tk.alltrue.testsqlite;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.sql.RowId;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import tk.alltrue.data.HotelContract.GuestEntry;
import tk.alltrue.data.HotelDbHelper;

public class EditorActivity extends AppCompatActivity {

    private EditText mNameEditText;
    private EditText mCityEditText;
    private EditText mAgeEditText;

    private Spinner mGenderSpinner;


    private int mGender = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mNameEditText = (EditText) findViewById(R.id.edit_guest_name);
        mCityEditText = (EditText) findViewById(R.id.edit_guest_city);
        mAgeEditText = (EditText) findViewById(R.id.edit_guest_age);

        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        setupSpinner();
    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mGenderSpinner.setAdapter(genderSpinnerAdapter);
        mGenderSpinner.setSelection(2);

        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String selection = (String) parent.getItemAtPosition(i);
                if (!TextUtils.isEmpty(selection)) {
                    mGender = 0;
                } else if (selection.equals(getString(R.string.gender_female))) {
                    mGender = 1;
                } else {
                    mGender = 2;
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = 2;
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_save:
                    insertGuest();
                    finish();
                    return true;
            case R.id.action_update:
                updateGuest();
                finish();
                return true;
            case R.id.action_delete:
                deleteGuest();
                finish();
                return true;
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void insertGuest() {
        String name = mNameEditText.getText().toString().trim();
        String city = mCityEditText.getText().toString().trim();
        String ageString = mAgeEditText.getText().toString().trim();
        int age = Integer.parseInt(ageString);

        HotelDbHelper mDbHelper = new HotelDbHelper(this);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GuestEntry.COLUMN_NAME, name);
        values.put(GuestEntry.COLUMN_CITY, city);
        values.put(GuestEntry.COLUMN_GENDER, mGender);
        values.put(GuestEntry.COLUMN_AGE, age);

        long newRowId = db.insert(GuestEntry.TABLE_NAME, null, values);

        if (newRowId == -1) {
            Toast.makeText(this, "Error to write row to DB", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Guest added to DB by row number: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    private void updateGuest() {
        String name = mNameEditText.getText().toString().trim();
        String city = mCityEditText.getText().toString().trim();
        String ageString = mAgeEditText.getText().toString().trim();

        int age = Integer.parseInt(ageString);

        HotelDbHelper mDbHelper = new HotelDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GuestEntry.COLUMN_NAME, name);
        values.put(GuestEntry.COLUMN_CITY, city);
        values.put(GuestEntry.COLUMN_GENDER, mGender);
        values.put(GuestEntry.COLUMN_AGE, age);
        String[] whereArgs = new String[] {String.valueOf(name)};
        long rowId = db.update(GuestEntry.TABLE_NAME, values,GuestEntry.COLUMN_NAME + "=?",whereArgs);

        if (rowId == -1) {
            Toast.makeText(this, "Error to write row to DB", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Guest updated in DB by row number: " + rowId, Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteGuest() {
        String name = mNameEditText.getText().toString().trim();

        HotelDbHelper mDbHelper = new HotelDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String[] whereArgs = new String[] {String.valueOf(name)};

        long rowId = db.delete(GuestEntry.TABLE_NAME, GuestEntry.COLUMN_NAME + "=?", whereArgs);

        if (rowId == -1) {
            Toast.makeText(this, "Error to write row to DB", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Guest deleted from DB by row number: " + rowId, Toast.LENGTH_SHORT).show();
        }

    }

}
