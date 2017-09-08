package com.freelancer.videoeditor.view.video;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.freelancer.videoeditor.R;
import com.freelancer.videoeditor.util.OnToolListener;
import com.freelancer.videoeditor.view.base.BaseFragment;
import com.freelancer.videoeditor.view.pick.PickAudioActivity;
import com.freelancer.videoeditor.vo.Audio;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MusicFragment extends BaseFragment {
    private static final String TAG = "MusicFragment";
    @BindView(R.id.btnRemoveAudio)
    ImageView imageRemove;
    private String mCurrentPickPath;
    private OnToolListener.OnToolBoxListener mListener;
    private View mRootView;
    @BindView(R.id.text_audio_picked)
    TextView textAddAudio;

    public static MusicFragment newInstance() {
        return newInstance(null);
    }

    public static MusicFragment newInstance(Bundle data) {
        MusicFragment fragment = new MusicFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnToolListener.OnToolBoxListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getClass().getSimpleName() + " must be implemented OnToolBoxListener");
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.mRootView = inflater.inflate(R.layout.fragment_toolbox_music, container, false);
        ButterKnife.bind(this, this.mRootView);
        return this.mRootView;
    }

    @OnClick({R.id.btnRemoveAudio, R.id.text_audio_picked})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRemoveAudio:
                removeAudioText();
                removeAudio();
                return;
            case R.id.text_audio_picked:
                gotoPickAudio();
                return;
            default:
                return;
        }
    }

    public void removeAudioText() {
        setAudioName(getActivity().getString(R.string.text_add_audio));
        Timber.d(TAG, "CLEAR AUDIO TEXT: " + this.textAddAudio.getText().toString());
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1 && requestCode == VideoEditorActivity.REQUEST_PICK_AUDIO) {
            Audio audio = (Audio) data.getExtras().getSerializable(PickAudioActivity.KEY_AUDIO_RESULT);
            if (audio != null) {
                this.mCurrentPickPath = audio.getName();
                setAudioName(this.mCurrentPickPath);
                passToParentActivity(audio);
            }
        }
    }

    private void setAudioName(String name) {
        this.textAddAudio.setText(name);
    }

    private void gotoPickAudio() {
        Intent mIntent = new Intent(getActivity(), PickAudioActivity.class);
        startActivityForResult(mIntent, VideoEditorActivity.REQUEST_PICK_AUDIO);
    }

    private void removeAudio() {
        if (this.mListener != null) {
            this.mListener.onPassData(Action.REMOVE_AUDIO, null);
        }
    }

    private void passToParentActivity(Audio audio) {
        if (this.mListener != null) {
            this.mListener.onPassData(Action.ADD_AUDIO, audio);
        }
    }
}
