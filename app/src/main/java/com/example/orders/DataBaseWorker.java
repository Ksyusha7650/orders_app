package com.example.orders;
import static com.example.orders.R.string.add_order;
import static com.example.orders.R.string.error;
import static com.example.orders.R.string.success_delete;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataBaseWorker extends SQLiteOpenHelper implements Serializable {
    private static final String DATABASE_NAME = "orders.db";
    private static final String TABLE_NAME = "Orders";
    private static final String COL_ID = "id";
    private static final String COL_NUMBER = "number";
    private static final String COL_DATE = "date";
    private static final String COL_STATUS = "status";
    Context context;

    public DataBaseWorker(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE Orders (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NUMBER + " TEXT, " +
                COL_DATE + " DATE, " +
                COL_STATUS + " CHAR)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void insertOrder(String number, String date, Boolean isSigned){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NUMBER,number);
        cv.put(COL_DATE,date);
        cv.put(COL_STATUS, ((isSigned)? "y":"n"));
        long res = db.insert(TABLE_NAME,null, cv);
        if(res == -1){
            Toast.makeText(context, error,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, add_order,
                    Toast.LENGTH_SHORT).show();
        }
    }

    void updateOrder(String number, String date, Boolean isSigned, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_NUMBER,number);
        cv.put(COL_DATE,date);
        cv.put(COL_STATUS, ((isSigned)? "y":"n"));
        long res = db.update(TABLE_NAME,cv, COL_ID+"=?", new String[]{id});
        if(res == -1){
            Toast.makeText(context, error,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, R.string.order_changed,
                    Toast.LENGTH_SHORT).show();
        }
    }
    public List<Order> loadData() {
        String Query = "SELECT *" +
                " FROM " + TABLE_NAME + " ORDER BY " + COL_DATE + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(Query,null);
        }
        List<Order> list= new ArrayList<>();
        while(true){
            assert cursor != null;
            if (!cursor.moveToNext()) break;
            Order order =new Order(
                    cursor.getInt(0),
                cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3).charAt(0) == 'y');
            list.add(order);
        }
        list.add(new Order(0, "","",true));
        cursor.close();
        db.close();
        return list;
    }

    void deleteOrders(boolean isAll, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = (isAll) ?
                db.delete(TABLE_NAME,"",new String[]{}) :
                db.delete(TABLE_NAME, COL_ID+"=?", new String[]{id});
        if(result == -1){
            Toast.makeText(context, error,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, success_delete,
                    Toast.LENGTH_SHORT).show();
        }
    }


}