package com.example.fragments;

import com.example.utils.helpers.MenuHelper;
import com.example.utils.helpers.UserHelper;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class BaseFragment extends Fragment{

	protected static final String ARG_SECTION_NUMBER = "section_number";
	protected int layout;
	protected View currentView;
	protected MenuHelper menuHelper;
	protected UserHelper userHelper;

	public BaseFragment(int layout) {
		this.layout = layout;
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, layout);
		setArguments(args);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		menuHelper = new MenuHelper(getActivity());
		userHelper = new UserHelper(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(layout, container, false);
		currentView = rootView;
		return rootView;
	}
	
	@SuppressWarnings("unchecked")
	protected <T extends View> T getComponentById(int id){
		return (T)getView().findViewById(id);
	}
	
	protected void showError(String text,int idComponent){
		TextView status = (TextView) getView().findViewById(idComponent);
		if(status == null){
			return;
		}
		status.setVisibility(TextView.VISIBLE);
		status.setText(text);
		status.setTextColor(Color.RED);
	}
	
	protected String getTextFromField(int id){
		TextView text = getComponentById(id);
		return text == null ? "" : text.getText().toString();
	}
	
	protected void hideKeyboard() {
		View view = getActivity().getCurrentFocus();
		if (view != null) {  
		    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}
	
}
