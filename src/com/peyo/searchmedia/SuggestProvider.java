package com.peyo.searchmedia;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class SuggestProvider extends ContentProvider {
    private static final String TAG = "SearchMedia.Provider";

    private static final String AUTHORITY = "com.peyo.searchmedia";
    private static final int SEARCH_SUGGEST = 1;
    private UriMatcher mUriMatcher;

    private final String[] queryProjection =
            new String[] {
                    BaseColumns._ID,
                    SearchManager.SUGGEST_COLUMN_TEXT_1, // Title
                    SearchManager.SUGGEST_COLUMN_TEXT_2, // Description
                    SearchManager.SUGGEST_COLUMN_RESULT_CARD_IMAGE,
                    SearchManager.SUGGEST_COLUMN_PRODUCTION_YEAR,
                    SearchManager.SUGGEST_COLUMN_DURATION,
                    SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
            };

    @Override
    public boolean onCreate() {
        mUriMatcher = buildUriMatcher();
        return true;
    }

    private UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(
                AUTHORITY, "/search/" + SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        uriMatcher.addURI(
                AUTHORITY,
                "/search/" + SearchManager.SUGGEST_URI_PATH_QUERY + "/*",
                SEARCH_SUGGEST);
        return uriMatcher;
    }

    @Override
    public Cursor query(
            Uri uri,
            String[] projection,
            String selection,
            String[] selectionArgs,
            String sortOrder) {
        Log.d(TAG, uri.toString());

        if (mUriMatcher.match(uri) == SEARCH_SUGGEST && selectionArgs != null)  {
            Log.d(TAG, "selectionArgs[0] : " + selectionArgs[0]);
            return getSuggestions(selectionArgs[0]);
        } else {
            Log.d(TAG, "Unknown uri or no selectionArgs: " + uri);
            throw new IllegalArgumentException("Unknown Uri: " + uri);
        }
    }

    private Cursor getSuggestions(String query) {
        MatrixCursor matrixCursor = new MatrixCursor(queryProjection);
        if (query.toLowerCase().equals("big buck bunny")) {
            matrixCursor.addRow(bbbRow());
        } else if (query.toLowerCase().equals("jurassic park")) {
            matrixCursor.addRow(jurassicRow());
        }
        return matrixCursor;
    }

    private Object[] bbbRow() {
        return new Object[] {
            1, // _ID
            "Big Buck Bunny", //Title
            "Big Buck Bunny tells the story of a giant rabbit with a heart bigger than himself. When one sunny day three rodents rudely harass him, something snaps... and the rabbit ain't no bunny anymore! In the typical cartoon tradition he prepares the nasty rodents a comical revenge.",
            "https://peach.blender.org/wp-content/uploads/poster_bunny_big.jpg?x11217", //CardImage
            2008, //ProductionYear
            (int) (TimeUnit.MINUTES.toMillis(9) + TimeUnit.SECONDS.toMillis(56)), //Duration
             1, // SUGGEST_COLUMN_INTENT_DATA_ID
        };
    }

    private Object[] jurassicRow() {
        return new Object[] {
            2, // _ID
            "Jurassic Park", //Title
            "During a preview tour, a theme park suffers a major power breakdown that allows its cloned dinosaur exhibits to run amok.",
            "https://orange.blender.org/wp-content/themes/orange/images/common/ed_header.jpg?x53801", //CardImage
            1993, //ProductionYear
            (int) (TimeUnit.HOURS.toMillis(2) + TimeUnit.MINUTES.toMillis(7)
                    + TimeUnit.SECONDS.toMillis(15)), //Duration
            2, // SUGGEST_COLUMN_INTENT_DATA_ID
        };
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new UnsupportedOperationException("Insert is not implemented.");
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        throw new UnsupportedOperationException("Delete is not implemented.");
    }

    @Override
    public int update(
            Uri uri,
            ContentValues contentValues,
            String s,
            String[] strings) {
        throw new UnsupportedOperationException("Update is not implemented.");
    }
}
