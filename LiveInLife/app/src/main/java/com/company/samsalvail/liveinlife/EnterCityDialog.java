package com.company.samsalvail.liveinlife;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

public class EnterCityDialog extends DialogFragment implements TextView.OnEditorActionListener {

    private EditText mEditText;

    public EnterCityDialog() {
        // Empty constructor required for DialogFragment
    }

    public interface EnterCityDialogListener {
        void onFinishEditDialog(String inputText);
    }

    public static EnterCityDialog newInstance(String title) {
        EnterCityDialog frag = new EnterCityDialog();
        Bundle args = new Bundle();
        args.putString("Enter the City", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_enter_city, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        mEditText = (EditText) view.findViewById(R.id.edit_city);
        mEditText.setOnEditorActionListener(this);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter the City");
        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field
        mEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text back to activity through the implemented listener
            EnterCityDialogListener listener = (EnterCityDialogListener) getActivity();
            listener.onFinishEditDialog(mEditText.getText().toString());
            // Close the dialog and return back to the parent activity
            dismiss();
            return true;
        }
        return false;
    }
}