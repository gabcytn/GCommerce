package com.example.gcommerce

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

private const val DB_NAME = "projectdb"
private const val TABLE_NAME = "user_accounts"
private const val COL_ID = "user_id"
private const val COL_FNAME = "user_firstname"
private const val COL_LNAME = "user_lastname"
private const val COL_EMAIL = "user_email"
private const val COL_USERNAME = "user_username"
private const val COL_PASSWORD = "user_password"

class DBHandler(private val context : Context) : SQLiteOpenHelper(context, DB_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_FNAME VARCHAR (50), " +
                "$COL_LNAME VARCHAR (50), " +
                "$COL_EMAIL VARCHAR(50), " +
                "$COL_USERNAME VARCHAR(50), " +
                "$COL_PASSWORD VARCHAR(50));"

        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(user: User): String{
        var feedback = ""
        val db = this.writableDatabase
        var cv = ContentValues()

        cv.put(COL_FNAME, user.firstName)
        cv.put(COL_LNAME, user.lastName)
        cv.put(COL_EMAIL, user.email)
        cv.put(COL_USERNAME, user.username)
        cv.put(COL_PASSWORD, user.password)

        val result = db.insert(TABLE_NAME, null, cv)
        if (result == (-1).toLong()){
            feedback = "Failed"
        } else {
            feedback = "Success"
        }

        return feedback
    }

    fun onLogin(username: String, password: String) : Boolean {
        var isAccountValid = false
        val db = this.readableDatabase
        val query = "SELECT $COL_PASSWORD FROM $TABLE_NAME WHERE $COL_USERNAME = '$username';"

        try {
            val qResult = db.rawQuery(query, null)
            if (qResult.moveToFirst() && password == qResult.getString(0)){
                isAccountValid = true
            }
            qResult.close()
        } catch (e: Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        } finally {
            db.close()
        }

        return isAccountValid
    }

    fun getDisplayName(un: String): String {
        var displayName = ""
        val db = this.readableDatabase
        val query = "SELECT $COL_FNAME FROM $TABLE_NAME WHERE $COL_USERNAME = '$un';"
        try {
            val qResult = db.rawQuery(query, null)
            qResult.moveToFirst()
            displayName = qResult.getString(0)
            qResult.close()
        } catch (e: Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }

        return displayName
    }

    fun getEmail(un: String): String {
        var email = ""
        val db = this.readableDatabase
        val query = "SELECT $COL_EMAIL FROM $TABLE_NAME WHERE $COL_USERNAME = '$un';"
        try {
            val qResult = db.rawQuery(query, null)
            qResult.moveToFirst()
            email = qResult.getString(0)
            qResult.close()
        } catch (e: Exception) {
            Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT).show()
        }

        return email
    }


}