package com.example.gcommerce

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast

private const val DB_NAME = "projectdb"
// USER ACCOUNTS TABLE
private const val TABLE_NAME = "user_accounts"
private const val COL_ID = "user_id"
private const val COL_FNAME = "user_firstname"
private const val COL_LNAME = "user_lastname"
private const val COL_EMAIL = "user_email"
private const val COL_USERNAME = "user_username"
private const val COL_PASSWORD = "user_password"
// ADDED TO CART ITEMS TABLE
private const val CART_TABLE_NAME = "user_cart"
private const val COL_CART_ID = "cart_id"
private const val COL_CART_ITEM = "cart_item"
private const val COL_CART_BUYER = "cart_buyer"
private const val COL_CART_ITEM_PRICE = "cart_item_price"
private const val COL_CART_ITEM_IMAGE = "cart_item_image"
private const val COL_CART_ITEM_QTY = "cart_item_qty"
// PURCHASE HISTORY TABLE
private const val HISTORY_TABLE_NAME = "user_history"
private const val COL_HISTORY_IMAGE = "history_img"
private const val COL_HISTORY_NAME = "history_name"
private const val COL_HISTORY_PRICE = "history_price"
private const val COL_HISTORY_QTY = "history_qty"
private const val COL_HISTORY_BUYER = "history_buyer"

class DBHandler(private val context : Context) : SQLiteOpenHelper(context, DB_NAME, null, 1){
    override fun onCreate(db: SQLiteDatabase?) {
        val createUserAccountTableQuery = "CREATE TABLE $TABLE_NAME (" +
                "$COL_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_FNAME VARCHAR (50), " +
                "$COL_LNAME VARCHAR (50), " +
                "$COL_EMAIL VARCHAR(50), " +
                "$COL_USERNAME VARCHAR(50), " +
                "$COL_PASSWORD VARCHAR(50));"

        val createCartItemsTableQuery = "CREATE TABLE $CART_TABLE_NAME (" +
                "$COL_CART_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COL_CART_ITEM_IMAGE INTEGER, " +
                "$COL_CART_ITEM VARCHAR(50), " +
                "$COL_CART_ITEM_PRICE INTEGER, " +
                "$COL_CART_BUYER VARCHAR(50), " +
                "$COL_CART_ITEM_QTY INTEGER);"

        val createHistoryTableQuery = "CREATE TABLE $HISTORY_TABLE_NAME (" +
                "$COL_HISTORY_IMAGE INTEGER, " +
                "$COL_HISTORY_NAME VARCHAR(50), " +
                "$COL_HISTORY_PRICE INTEGER, " +
                "$COL_HISTORY_QTY INTEGER, " +
                "$COL_HISTORY_BUYER VARCHAR(50));"


        db?.execSQL(createUserAccountTableQuery)
        db?.execSQL(createCartItemsTableQuery)
        db?.execSQL(createHistoryTableQuery)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun insertData(user: User): String{
        val feedback: String
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_FNAME, user.firstName)
        cv.put(COL_LNAME, user.lastName)
        cv.put(COL_EMAIL, user.email)
        cv.put(COL_USERNAME, user.username)
        cv.put(COL_PASSWORD, user.password)

        val result = db.insert(TABLE_NAME, null, cv)
        feedback = if (result == (-1).toLong()){
            "Failed"
        } else {
            "Success"
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
        }

        return isAccountValid
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
    fun insertCartItems(itName: String, itPrice: Int, itBuyer: String, itImage: Int): String {
        val feedback: String
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COL_CART_ITEM, itName)
        cv.put(COL_CART_ITEM_PRICE, itPrice)
        cv.put(COL_CART_BUYER, itBuyer)
        cv.put(COL_CART_ITEM_IMAGE, itImage)
        cv.put(COL_CART_ITEM_QTY, 1)

        val result = db.insert(CART_TABLE_NAME, null, cv)
        feedback = if (result == (-1).toLong()){
            "Failed"
        } else {
            "Success"
        }
        return feedback
    }
    fun updateCartItemQuantity(qty: Int, itemName: String) {
        val db = writableDatabase
        val query = "UPDATE $CART_TABLE_NAME SET $COL_CART_ITEM_QTY = ? WHERE $COL_CART_ITEM = ?"
        try {
            db.execSQL(query, arrayOf(qty, itemName))
        } catch (e: Exception) {
            Log.e("MainActivity", e.message.toString())
        }
    }

    fun getTotalAmount(buyer: String): Int {
        var totalAmt = 0
        val db = readableDatabase
        val query = "SELECT * FROM $CART_TABLE_NAME WHERE $COL_CART_BUYER = '$buyer';"
        val result = db.rawQuery(query, null)

        try {
            result.moveToFirst()
            do {
                val qty = result.getString(result.getColumnIndex(COL_CART_ITEM_QTY)).toInt()
                val price = result.getString(result.getColumnIndex(COL_CART_ITEM_PRICE)).toInt()

                totalAmt += (qty * price)
            } while (result.moveToNext())
        } catch (e: Exception) {
            Log.e("MainActivity", "DBHandler (getTotalAmount)\n${e.message}")
        } finally {
            result.close()
        }

        return totalAmt
    }

    @SuppressLint("Range")
    fun getCartItems (buyer: String) : MutableList<ShopItem> {
        val cartItems = mutableListOf<ShopItem>()
        val db = this.readableDatabase
        val query = "SELECT * FROM $CART_TABLE_NAME WHERE $COL_CART_BUYER = '$buyer';"
        val result: Cursor
        try {
            result = db.rawQuery(query, null)
            result.moveToFirst()
            do {
                val itemName = result.getString(result.getColumnIndex(COL_CART_ITEM))
                val itemPrice = result.getString(result.getColumnIndex(COL_CART_ITEM_PRICE)).toInt()
                val itemImage = result.getString(result.getColumnIndex(COL_CART_ITEM_IMAGE)).toInt()

                val item = ShopItem(itemImage, itemName, itemPrice)
                cartItems.add(item)
            } while (result.moveToNext())

            result.close()
        } catch (e: Exception) {
            Log.i("MainActivity", e.message.toString())
        } finally {
            db.close()
        }

        return cartItems
    }

    fun isAddedToCart(itemName: String, itemBuyer: String): Boolean {
        var isAddedToCart = false
        val db = readableDatabase
        val query = "SELECT $COL_CART_ITEM FROM $CART_TABLE_NAME WHERE $COL_CART_BUYER = '$itemBuyer';"
        try {
            val result = db.rawQuery(query, null)
            result.moveToFirst()

            do {
                val retrievedItem = result.getString(0)
                if (retrievedItem == itemName) {
                    isAddedToCart = true
                    break
                }
            } while (result.moveToNext())
            result.close()
        } catch (e: Exception) {
            Log.e("MainActivity", e.message.toString())
        } finally {
            db.close()
        }
        return isAddedToCart
    }

    fun deleteCartItem(itemName: String) {
        try {
            val db = writableDatabase
            db.delete(CART_TABLE_NAME,"$COL_CART_ITEM = ?",arrayOf(itemName))
            db.close()
        } catch (e: Exception) {
            Log.e("MainActivity", e.message.toString())
        }
    }

    fun getQuantity(buyer: String, item: String): Int? {
        var qty: Int? = null
        try {
            val db = readableDatabase
            val query = "SELECT $COL_CART_ITEM_QTY FROM $CART_TABLE_NAME WHERE $COL_CART_BUYER = '$buyer' AND $COL_CART_ITEM = '$item';"
            val result = db.rawQuery(query, null)
            result.moveToFirst()
            qty = result.getString(0).toInt()

            result.close()
        } catch (e: Exception) {
            Log.e("MainActivity", "DBHandler getQuantity\n${e.message}")
        }

        return qty
    }

    fun deleteAll(buyer: String) {
        val db = writableDatabase
        val query = "DELETE FROM $CART_TABLE_NAME WHERE $COL_CART_BUYER = '$buyer';"

        db.execSQL(query)
        db.close()
    }

    fun onCheckout(buyer: String) {
        val itemsBought = mutableListOf(listOf<String>())
        val db = readableDatabase
        val query = "SELECT * FROM $CART_TABLE_NAME WHERE $COL_CART_BUYER = '$buyer';"
        try {
            val result = db.rawQuery(query, null)
            result.moveToFirst()
            do {
                val col_img = result.getString(1)
                val col_name = result.getString(2)
                val col_price = result.getString(3)
                val col_qty = result.getString(5)

                itemsBought.add(listOf(col_img, col_name, col_price, col_qty))
            } while (result.moveToNext())
            result.close()
        } catch (e: Exception) {
            Log.e("MainActivity", "Before insert into history table\n${e.message}")
        } finally {
            db.close()
        }

        Log.i("MainActivity", "ON CHECKOUT\n$itemsBought")
        insertIntoHistory(itemsBought, buyer)
    }

    private fun insertIntoHistory(list: MutableList<List<String>>, buyer: String) {
        val db = writableDatabase
        val cv = ContentValues()
        try {
            for (i in 1..list.lastIndex) {
                cv.put(COL_HISTORY_IMAGE, list[i][0].toInt())
                cv.put(COL_HISTORY_NAME, list[i][1])
                cv.put(COL_HISTORY_PRICE, list[i][2].toInt())
                cv.put(COL_HISTORY_QTY, list[i][3].toInt())
                cv.put(COL_HISTORY_BUYER, buyer)

                db.insert(HISTORY_TABLE_NAME, null, cv)
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "On insert into history table\n${e.message}")
        }
    }

    fun getHistoryItems(buyer: String): ArrayList<HistoryModel> {
        val names = arrayListOf<HistoryModel>()
        val db = readableDatabase
        val query = "SELECT * FROM $HISTORY_TABLE_NAME WHERE $COL_HISTORY_BUYER = '$buyer';"
        val result = db.rawQuery(query, null)
        try {
            result.moveToFirst()
            do {
                val image = result.getString(0)
                val name = result.getString(1)
                val price = result.getString(2)
                val qty = result.getString(3)
                names.add(HistoryModel(image.toInt(), name, price.toInt(), qty.toInt()))
            } while (result.moveToNext())
        } catch (e: Exception) {
            Log.e("MainActivity", "On select of history items\n${e.message}")
        } finally {
            result.close()
            db.close()
        }

        return names
    }


}