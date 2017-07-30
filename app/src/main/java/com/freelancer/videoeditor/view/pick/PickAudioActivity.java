package com.freelancer.videoeditor.view.pick;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.freelancer.videoeditor.BuildConfig;
import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.config.ShareConstants;
import com.freelancer.videoeditor.util.ExtraUtils;
import com.freelancer.videoeditor.util.FileUtils;
import com.freelancer.videoeditor.util.Util;
import com.freelancer.videoeditor.view.base.BaseActivity;
import com.freelancer.videoeditor.view.base.BasePresenter;
import com.freelancer.videoeditor.vo.Audio;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by ThongLe on 7/29/2017.
 */

public class PickAudioActivity extends BaseActivity implements View.OnClickListener, OnAudioClickListener{
    public static final String KEY_AUDIO_RESULT = "KEY_AUDIO_RESULT";
    private static final int REQUEST_AUDIO = 2323;;
    private ListAudioAdapter mAdapter;
    private ImageView mBtnPickAudio;
    private Button mButtonApply;
    private Audio mCurrentAudio;
    private String mCurrentAudioPath;
    private ImageView mImageBack;
    private ImageView mImageSort;
    private ListView mListAudio;
    private ArrayList<Audio> mListDataAudio;
    private MediaPlayer mMediaPlayer;
    private ImageView mPlayPauseAudio;
    private AlertDialog mSortDialog;
    private TextView mTextAudioDuration;
    private TextView mTextAudioName;
    private TextView mTextAudioTitle;
    private ArrayList<String> pathList = new ArrayList();

    private class GetListAudio extends AsyncTask<Void, Void, String> {
        private GetListAudio() {
        }

        protected void onPreExecute() {
            PickAudioActivity.this.mListDataAudio = new ArrayList();
        }

        protected String doInBackground(Void... params) {
            String[] projection = new String[]{"_id", "artist", ShareConstants.WEB_DIALOG_PARAM_TITLE, "_data", "_display_name", "duration"};
            Uri uri = Media.EXTERNAL_CONTENT_URI;
            Cursor cursor = PickAudioActivity.this.getContentResolver().query(uri, projection, "is_music != 0", null, null);
            if (cursor == null || cursor.getCount() == 0) {
                PickAudioActivity.this.runOnUiThread(() -> Toast.makeText(PickAudioActivity.this, "No audio found", Toast.LENGTH_SHORT).show());
            } else {
                int column_index_path = cursor.getColumnIndexOrThrow("_data");
                int columnIndexTitle = cursor.getColumnIndexOrThrow(ShareConstants.WEB_DIALOG_PARAM_TITLE);
                int columnIndexName = cursor.getColumnIndexOrThrow("_display_name");
                int columnIndexDuration = cursor.getColumnIndexOrThrow("duration");
                int columnIndexArtist = cursor.getColumnIndexOrThrow("artist");
                while (cursor.moveToNext()) {
                    String pathFile = cursor.getString(column_index_path);
                    String name = cursor.getString(columnIndexName);
                    int duration = cursor.getInt(columnIndexDuration);
                    String artist = cursor.getString(columnIndexArtist);
                    File file = new File(pathFile);
                    if (file.exists() && !PickAudioActivity.this.CheckDuplicatedFolder(pathFile, PickAudioActivity.this.pathList)) {
                        if (TextUtils.isEmpty(name)) {
                            name = file.getName();
                        }
                        PickAudioActivity.this.pathList.add(pathFile);
                        StringBuilder append = new StringBuilder().append(BuildConfig.FLAVOR);
                        Audio audio = new Audio(name, pathFile, file.getPath(), append.append(Util.formatDuration((long) duration)).toString(), artist);
                        audio.setSeconds(duration / 1000);
                        PickAudioActivity.this.mListDataAudio.add(audio);
                    }
                }
                cursor.close();
            }
            return BuildConfig.FLAVOR;
        }

        protected void onPostExecute(String result) {
            if (PickAudioActivity.this.mListDataAudio != null && PickAudioActivity.this.mListDataAudio.size() > 0) {
                PickAudioActivity.this.mAdapter = new ListAudioAdapter(PickAudioActivity.this, PickAudioActivity.this.mListDataAudio);
                PickAudioActivity.this.mListAudio.setAdapter(PickAudioActivity.this.mAdapter);
            }
            PickAudioActivity.this.mBtnPickAudio.setVisibility(View.VISIBLE);
        }
    }

    private enum Sort {
        NAME,
        SIZE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_pick_audio);
        init();
//        if (instance.isPermissionAllow(this, PhotoEditorActivity.REQUEST_CODE_CROP, "android.permission.READ_EXTERNAL_STORAGE")) {
//            showListAudio();
//        }

        this.mBtnPickAudio.setVisibility(View.GONE);
        setSquareSize(this.mBtnPickAudio, ExtraUtils.getDisplayInfo(this).widthPixels / 6);
    }

    private void setSquareSize(View view, int size) {
        view.getLayoutParams().width = size;
        view.getLayoutParams().height = size;
    }

    private void init() {
        this.mImageBack = (ImageView) findViewById(R.id.btnAudioBack);
        this.mTextAudioTitle = (TextView) findViewById(R.id.txtTitleAudio);
        this.mImageSort = (ImageView) findViewById(R.id.btnSortAudio);
        this.mListAudio = (ListView) findViewById(R.id.list_view_audio);
        this.mButtonApply = (Button) findViewById(R.id.button_apply_audio);
        this.mPlayPauseAudio = (ImageView) findViewById(R.id.image_play_audio);
        this.mTextAudioName = (TextView) findViewById(R.id.text_audio_name);
        this.mTextAudioDuration = (TextView) findViewById(R.id.text_audio_duration);
        this.mBtnPickAudio = (ImageView) findViewById(R.id.pick_audio_from_file);
        this.mImageBack.setOnClickListener(this);
        this.mImageSort.setOnClickListener(this);
        this.mButtonApply.setOnClickListener(this);
        this.mPlayPauseAudio.setOnClickListener(this);
        this.mBtnPickAudio.setOnClickListener(this);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnAudioBack) {
            setResult(0);
            finish();
        } else if (i == R.id.btnSortAudio) {
            showSortAudio();
        } else if (i == R.id.button_apply_audio) {
            applyAudio();
        } else if (i == R.id.image_play_audio) {
            if (TextUtils.isEmpty(this.mCurrentAudioPath)) {
                Toast.makeText(this, "Please select an audio", Toast.LENGTH_SHORT).show();
            } else {
                togglePlayAudioSelected(BuildConfig.FLAVOR, true);
            }
        } else if (i == R.id.pick_audio_from_file) {
            pickAudioFromIntent();
        }
    }

    private void showSortAudio() {
        CharSequence[] items = new CharSequence[]{" Name ", " Size ", " Date "};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sort albums by");
        builder.setSingleChoiceItems(items, -1, (dialog, item) -> {
            switch (item) {
                case 0 :
                    Collections.sort(PickAudioActivity.this.mListDataAudio, (lhs, rhs) -> lhs.getName().compareToIgnoreCase(rhs.getName()));
                    PickAudioActivity.this.mListAudio.setAdapter(mAdapter);
                    break;
                case 1:
                    Collections.sort(PickAudioActivity.this.mListDataAudio, (lhs, rhs) -> {
                        File fileI = new File(lhs.getPathFolder());
                        File fileJ = new File(rhs.getPathFolder());
                        long totalSizeFileI = fileI.length();
                        long totalSizeFileJ = fileJ.length();
                        if (totalSizeFileI > totalSizeFileJ) {
                            return -1;
                        }
                        if (totalSizeFileI < totalSizeFileJ) {
                            return 1;
                        }
                        return 0;
                    });
                    PickAudioActivity.this.mListAudio.setAdapter(mAdapter);
                    break;
                case 2:
                    for (int i = 0; i < PickAudioActivity.this.mListDataAudio.size(); i++) {
                        Collections.sort(PickAudioActivity.this.mListDataAudio, (lhs, rhs) -> {
                            File fileI = new File(lhs.getPathFolder());
                            File fileJ = new File(rhs.getPathFolder());
                            if (fileI.lastModified() > fileJ.lastModified()) {
                                return -1;
                            }
                            if (fileI.lastModified() < fileJ.lastModified()) {
                                return 1;
                            }
                            return 0;
                        });
                    }
                    PickAudioActivity.this.mListAudio.setAdapter(mAdapter);
                    break;
            }
            PickAudioActivity.this.mSortDialog.dismiss();
        });
        this.mSortDialog = builder.create();
        this.mSortDialog.show();
    }

    private void togglePlayAudioSelected(String newAudioPath, boolean ignore) {
        if (!ignore) {
            if (newAudioPath.equals(this.mCurrentAudioPath)) {
                this.mPlayPauseAudio.setTag("STOP");
            } else {
                this.mCurrentAudioPath = newAudioPath;
                this.mPlayPauseAudio.setTag("PLAY");
            }
        }
        if ((this.mPlayPauseAudio.getTag() == null ? "PLAY" : this.mPlayPauseAudio.getTag().toString()).equals("STOP")) {
            this.mPlayPauseAudio.setImageResource(R.drawable.icon_play);
            this.mPlayPauseAudio.setTag("PLAY");
            stopAudioSound();
            return;
        }
        this.mPlayPauseAudio.setImageResource(R.drawable.icon_stop);
        this.mPlayPauseAudio.setTag("STOP");
        playAudioSound();
    }

    private void playAudioSound() {
        stopAudioSound();
        this.mMediaPlayer = new MediaPlayer();
        try {
            this.mMediaPlayer.setDataSource(this.mCurrentAudioPath);
            this.mMediaPlayer.prepare();
            this.mMediaPlayer.start();
            this.mMediaPlayer.setOnPreparedListener(mp -> {
                int duration = mp.getDuration();
                if (duration != -1) {
                    PickAudioActivity.this.mCurrentAudio.setSeconds(duration / 1000);
                    StringBuilder append = new StringBuilder().append(BuildConfig.FLAVOR);
                    PickAudioActivity.this.mTextAudioDuration.setText(append.append(Util.formatDuration((long) duration)).toString());
                }
            });
            this.mMediaPlayer.setOnCompletionListener(mp -> PickAudioActivity.this.togglePlayAudioSelected(PickAudioActivity.this.mCurrentAudioPath, false));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAudioSound() {
        if (this.mMediaPlayer != null) {
            this.mMediaPlayer.stop();
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
    }

    private void applyAudio() {
        if (this.mCurrentAudio != null) {
            Intent intent = new Intent();
            Bundle data = new Bundle();
            data.putSerializable(KEY_AUDIO_RESULT, this.mCurrentAudio);
            intent.putExtras(data);
            setResult(-1, intent);
        } else {
            setResult(0);
        }
        finish();
    }

    private void showListAudio() {
        new GetListAudio().execute(new Void[0]);
    }

    protected void onDestroy() {
        super.onDestroy();
        stopAudioSound();
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected void initializeInjector() {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    protected void onPause() {
        super.onPause();
        stopAudioSound();
    }

    private boolean CheckDuplicatedFolder(String path, ArrayList<String> list) {
        if (!list.isEmpty() && list.contains(path)) {
            return true;
        }
        return false;
    }

    public void onPlayAudio(Audio audio) {
        if (audio != null) {
            this.mCurrentAudio = audio;
            this.mTextAudioName.setText(audio.getName());
            String audioPath = audio.getPathFile();
            if (!audioPath.equals(this.mCurrentAudioPath)) {
                togglePlayAudioSelected(audioPath, false);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == REQUEST_AUDIO) {
            Uri uri = data.getData();
            if (uri != null) {
                String mimeType = FileUtils.getMimeType(this, uri);
                if (!TextUtils.isEmpty(mimeType) && mimeType.contains("audio")) {
                    playMusic(uri);
                }
            } else {
                Toast.makeText(this, "Can't pick the audio file", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void pickAudioFromIntent() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setDataAndType(Media.EXTERNAL_CONTENT_URI, FileUtils.MIME_TYPE_AUDIO);
        try {
            startActivityForResult(intent, REQUEST_AUDIO);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "The file of this explorer did not find", Toast.LENGTH_SHORT).show();
        }
    }

    private Audio getAudioFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, new String[]{"_id", "artist", ShareConstants.WEB_DIALOG_PARAM_TITLE, "_data", "_display_name", "duration"}, null, null, null);
        if (!(cursor == null || cursor.getCount() == 0)) {
            int columnIndexName = cursor.getColumnIndexOrThrow("_display_name");
            int columnIndexDuration = cursor.getColumnIndexOrThrow("duration");
            int columnIndexArtist = cursor.getColumnIndexOrThrow("artist");
            if (cursor.moveToFirst()) {
                String pathFile = FileUtils.getPath(this, uri);
                String name = cursor.getString(columnIndexName);
                int duration = cursor.getInt(columnIndexDuration);
                String artist = cursor.getString(columnIndexArtist);
                File file = new File(pathFile);
                if (file.exists()) {
                    if (TextUtils.isEmpty(name)) {
                        name = file.getName();
                    }
                    StringBuilder append = new StringBuilder().append(BuildConfig.FLAVOR);
                    Audio audio = new Audio(name, pathFile, file.getPath(), append.append(Util.formatDuration((long) duration)).toString(), artist);
                    audio.setSeconds(duration / 1000);
                    return audio;
                }
            }
            cursor.close();
        }
        return null;
    }

    private void playMusic(Uri uri) {
        Audio audio = getAudioFromUri(uri);
        if (audio != null) {
            if (this.mAdapter != null) {
                this.mAdapter.removeAudioSelected();
            }
            onPlayAudio(audio);
            return;
        }
        Toast.makeText(this, "Can't pick the audio", Toast.LENGTH_SHORT).show();
    }
}
